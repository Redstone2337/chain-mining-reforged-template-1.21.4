package net.deepseek.v1.chainmining.task;

import net.deepseek.v1.chainmining.config.ClientConfig;
import net.deepseek.v1.chainmining.ChainMiningReforged;

import net.minecraft.entity.ItemEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 掉落物清理任务
 * 基于现实时间的检查机制
 */
public class ItemClearTask {
    private final MinecraftServer server;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledTask;
    private ScheduledFuture<?> countdownTask;
    private int nextClearCountdown;
    private boolean isCountdownRunning = false;

    /* 倒计时相关 */
    private static final int COUNTDOWN_THRESHOLD = 3; // 倒计时阈值（秒）
    private int currentCountdown = 0;

    public ItemClearTask(MinecraftServer server) {
        this.server = server;
        // 倒计时显示为秒，所以需要将游戏刻转换为秒
        this.nextClearCountdown = ClientConfig.getClearTime() / 20;
    }

    /**
     * 启动清理任务
     */
    public void start() {
        if (scheduler != null && !scheduler.isShutdown()) {
            ChainMiningReforged.LOGGER.warn("清理任务已经在运行");
            return;
        }

        this.scheduler = Executors.newScheduledThreadPool(2);

        // 使用游戏刻作为清理间隔，但转换为秒用于调度
        int clearIntervalSeconds = ClientConfig.getClearTime() / 20;

        // 启动定时任务（基于现实时间）
        this.scheduledTask = scheduler.scheduleAtFixedRate(() -> {
            if (server.isRunning()) {
                executeClear();
            }
        }, clearIntervalSeconds, clearIntervalSeconds, TimeUnit.SECONDS);

        // 启动倒计时更新任务（每秒更新一次）
        this.countdownTask = scheduler.scheduleAtFixedRate(this::updateCountdown, 1, 1, TimeUnit.SECONDS);

        ChainMiningReforged.LOGGER.info("清理任务已启动，间隔: {}秒 ({} tick)", clearIntervalSeconds, ClientConfig.getClearTime());
    }

    /**
     * 停止清理任务
     */
    public void stop() {
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            scheduledTask = null;
        }

        if (countdownTask != null) {
            countdownTask.cancel(true);
            countdownTask = null;
        }

        if (scheduler != null) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
            scheduler = null;
        }

        // 重置倒计时状态
        this.nextClearCountdown = ClientConfig.getClearTime() / 20;
        this.isCountdownRunning = false;
        this.currentCountdown = 0;
    }

    /**
     * 更新下一次清理的倒计时
     */
    private void updateCountdown() {
        if (nextClearCountdown > 1) {
            nextClearCountdown--;

            // 检查是否需要开始倒计时提醒
            if (nextClearCountdown <= COUNTDOWN_THRESHOLD && !isCountdownRunning) {
                startCountdown();
            }
        } else {
            nextClearCountdown = ClientConfig.getClearTime() / 20;
            // 重置倒计时状态
            isCountdownRunning = false;
            currentCountdown = 0;
        }
    }

    /**
     * 开始倒计时提醒
     */
    private void startCountdown() {
        isCountdownRunning = true;
        currentCountdown = COUNTDOWN_THRESHOLD;

        // 启动倒计时提醒任务
        scheduler.scheduleAtFixedRate(() -> {
            if (currentCountdown > 0 && server.isRunning()) {
                broadcastCountdownMessage(currentCountdown);
                currentCountdown--;
            } else {
                // 倒计时结束，取消这个任务
                isCountdownRunning = false;
                throw new RuntimeException("Countdown finished"); // 这会终止这个定时任务
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * 广播倒计时消息
     *
     * @param countdown 当前倒计时秒数
     */
    private void broadcastCountdownMessage(int countdown) {
        String countdownText = String.format(ClientConfig.getDisplayCountdownText(), countdown);
        String fullMessage = ClientConfig.getDisplayTextHead() + countdownText;

        server.getPlayerManager().getPlayerList().forEach(player -> {
            player.sendMessage(Text.literal(fullMessage));
        });

        ChainMiningReforged.LOGGER.info("倒计时提醒: {}", fullMessage);
    }

    /**
     * 执行清理操作（仅扫描“所有玩家当前所在区块”及其周围 2 区块）
     */
    private void executeClear() {
        ClearStatistics stats = new ClearStatistics();

        /* 1. 先收集“需要检查的区块”：所有在线玩家所在区块 + 外扩 2 区块 */
        int radius = 2; // 可配置
        Set<ChunkPos> targetChunks = new HashSet<>();
        for (ServerPlayerEntity p : server.getPlayerManager().getPlayerList()) {
            ChunkPos center = new ChunkPos(p.getBlockPos());
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    targetChunks.add(new ChunkPos(center.x + dx, center.z + dz));
                }
            }
        }

        /* 2. 在这些区块里查找 ItemEntity（getEntitiesByClass 只能扫已加载区，刚好符合需求） */
        server.getWorlds().forEach(world -> {
            // 把整个已加载世界当搜索空间，再用 ChunkPos 过滤
            List<ItemEntity> drops = world.getEntitiesByClass(
                    ItemEntity.class,
                    new Box(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE,
                            Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE),
                    e -> e.isAlive() && targetChunks.contains(new ChunkPos(e.getBlockPos()))
            );

            for (ItemEntity item : drops) {
                stats.addItem(item);
                item.discard();
            }
        });

        /* 3. 广播与日志（与你原来完全一致） */
        if (stats.getTotalEntities() == 0) {
            ChainMiningReforged.LOGGER.debug("玩家区块内未发现需要清理的掉落物");
            return;
        }
        broadcastClearMessage(stats);
        logClearStatistics(stats);
    }

    /**
     * 广播清理消息
     * @param stats 清理统计信息
     */
    private void broadcastClearMessage(ClearStatistics stats) {
        String head = ClientConfig.getDisplayTextHead();
        String body = String.format(ClientConfig.getDisplayTextBody(), stats.getTypeCount(), nextClearCountdown);
        String fullMessage = head + body;

        // 向所有在线玩家广播消息
        server.getPlayerManager().getPlayerList().forEach(player -> {
            player.sendMessage(Text.literal(fullMessage));
        });

        ChainMiningReforged.LOGGER.info("广播清理消息: {}", fullMessage);
    }

    /**
     * 记录清理统计信息
     * @param stats 统计信息
     */
    private void logClearStatistics(ClearStatistics stats) {
        ChainMiningReforged.LOGGER.info("清理了 {} 个掉落物实体，涉及 {} 种物品",
                stats.getTotalEntities(), stats.getTypeCount());

        stats.getItemCounts().forEach((itemName, count) -> {
            ChainMiningReforged.LOGGER.info("  - {}: {}", itemName, count);
        });
    }

    /**
     * 清理统计信息类
     */
    private static class ClearStatistics {
        private int totalEntities = 0;
        private final Map<String, Integer> itemCounts = new HashMap<>();

        public void addItem(ItemEntity itemEntity) {
            totalEntities++;
            String itemName = String.valueOf(itemEntity.getItemAge());
            int count = itemEntity.getItemAge();
            itemCounts.put(itemName, itemCounts.getOrDefault(itemName, 0) + count);
        }

        public int getTotalEntities() { return totalEntities; }
        public int getTypeCount() { return itemCounts.size(); }
        public Map<String, Integer> getItemCounts() { return itemCounts; }
    }
}