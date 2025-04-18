package net.deepseek.v1.chainmining.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SexCommand {
    private static final SimpleCommandExceptionType INVALID_DURATION = new SimpleCommandExceptionType(
            Text.translatable("command.sex.error.invalid_duration")
    );
    private static final DynamicCommandExceptionType INVALID_TARGET = new DynamicCommandExceptionType(
            target -> Text.translatable("command.sex.error.invalid_target", target)
    );

    private static final String TRANSLATION_KEY_PREFIX = "command.sex.";
    private static final String SUCCESS_MULTIPLE = TRANSLATION_KEY_PREFIX + "success.multiple";
    private static final String SUCCESS_SINGLE = TRANSLATION_KEY_PREFIX + "success.single";
    private static final String NOT_EXECUTED = TRANSLATION_KEY_PREFIX + "not_executed";
    private static final String DURATION_TICKS = TRANSLATION_KEY_PREFIX + "duration_ticks";
    private static final String CONJUNCTION = TRANSLATION_KEY_PREFIX + "conjunction";

    private static final List<HeartParticleEffect> activeEffects = new ArrayList<>();
    private static final int MAX_NESTING_DEPTH = 50; // 防止无限递归

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> sexCommand = CommandManager.literal("sex")
                .requires(source -> source.hasPermissionLevel(2))
                .then(buildBaseCommandChain());

        dispatcher.register(sexCommand);
    }

    private static ArgumentBuilder<ServerCommandSource, ?> buildBaseCommandChain() {
        return CommandManager.literal("sexas")
                .then(CommandManager.argument("target", EntityArgumentType.entities())
                        .then(CommandManager.argument("duration", IntegerArgumentType.integer(-1))
                                .then(CommandManager.argument("execute", BoolArgumentType.bool())
                                        .executes(SexCommand::executeSex)
                                        .then(buildNestedCommands(1))
                                )
                        )
                );
    }

    private static ArgumentBuilder<ServerCommandSource, ?> buildNestedCommands(int currentDepth) {
        if (currentDepth >= MAX_NESTING_DEPTH) {
            return CommandManager.literal(""); // 达到最大深度时停止
        }

        return CommandManager.literal("sexas")
                .then(CommandManager.argument("nextTarget", EntityArgumentType.entities())
                        .then(buildNestedCommands(currentDepth + 1)))
                .then(CommandManager.literal("sexat")
                        .then(CommandManager.argument("nextTarget", EntityArgumentType.entities())
                                .then(buildNestedCommands(currentDepth + 1))));
    }

    private static int executeSex(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            Collection<? extends Entity> targets = validateTargets(context, "target");
            int duration = validateDuration(context);
            boolean execute = BoolArgumentType.getBool(context, "execute");

            List<Entity> allTargets = new ArrayList<>(targets);
            collectNestedTargets(context, allTargets, 1);

            if (!execute) {
                context.getSource().sendFeedback(() -> Text.translatable(NOT_EXECUTED), false);
                return 0;
            }

            createParticleEffects(context.getSource().getWorld(), allTargets, duration);

            sendSuccessFeedback(context, allTargets, duration);
            return 1;
        } catch (CommandSyntaxException e) {
            throw e;
        } catch (Exception e) {
            throw new SimpleCommandExceptionType(Text.literal("Internal command error: " + e.getMessage())).create();
        }
    }

    private static Collection<? extends Entity> validateTargets(CommandContext<ServerCommandSource> context, String argName) throws CommandSyntaxException {
        try {
            Collection<? extends Entity> targets = EntityArgumentType.getEntities(context, argName);
            if (targets.isEmpty()) {
                throw INVALID_TARGET.create(argName);
            }
            return targets;
        } catch (CommandSyntaxException e) {
            context.getSource().sendError(Text.translatable("argument.entity.invalid"));
            throw e;
        }
    }

    private static int validateDuration(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int duration = IntegerArgumentType.getInteger(context, "duration");
        if (duration < -1) {
            throw INVALID_DURATION.create();
        }
        return duration;
    }

    private static void collectNestedTargets(CommandContext<ServerCommandSource> context, List<Entity> targetList, int depth) throws CommandSyntaxException {
        if (depth >= MAX_NESTING_DEPTH) return;

        if (context.getNodes().stream().anyMatch(node -> node.getNode().getName().equals("sexas"))) {
            Collection<? extends Entity> additionalTargets = validateTargets(context, "nextTarget");
            targetList.addAll(additionalTargets);
            collectNestedTargets(context, targetList, depth + 1);
        }

        if (context.getNodes().stream().anyMatch(node -> node.getNode().getName().equals("sexat"))) {
            Collection<? extends Entity> additionalTargets = validateTargets(context, "nextTarget");
            targetList.addAll(additionalTargets);
            collectNestedTargets(context, targetList, depth + 1);
        }
    }

    private static void createParticleEffects(ServerWorld world, List<Entity> targets, int duration) {
        for (Entity target : targets) {
            HeartParticleEffect effect = new HeartParticleEffect(target, world, duration);
            activeEffects.add(effect);
        }
    }

    private static void sendSuccessFeedback(CommandContext<ServerCommandSource> context, List<Entity> targets, int duration) {
        Text feedbackMessage;
        if (targets.size() == 1) {
            feedbackMessage = Text.translatable(SUCCESS_SINGLE, targets.getFirst().getDisplayName());
        } else {
            Text[] entityNames = targets.stream()
                    .map(Entity::getDisplayName)
                    .toArray(Text[]::new);
            feedbackMessage = Text.translatable(SUCCESS_MULTIPLE, (Object[])entityNames);
        }

        if (duration > 0) {
            feedbackMessage = Text.translatable(DURATION_TICKS, feedbackMessage, duration);
        } else if (duration == -1) {
            feedbackMessage = Text.translatable(DURATION_TICKS, feedbackMessage, "∞");
        }

        Text finalFeedbackMessage = feedbackMessage;
        context.getSource().sendFeedback(() -> finalFeedbackMessage, true);
    }

    public static void tickActiveEffects() {
        activeEffects.removeIf(effect -> {
            effect.tick();
            return effect.isFinished();
        });
    }

    private static class HeartParticleEffect {
        private final Entity target;
        private final ServerWorld world;
        private int remainingTicks;

        public HeartParticleEffect(Entity target, ServerWorld world, int duration) {
            this.target = target;
            this.world = world;
            this.remainingTicks = duration;
        }

        public void tick() {
            if (remainingTicks != 0) { // -1表示无限，>0表示有限
                spawnHeartParticles();
                if (remainingTicks > 0) {
                    remainingTicks--;
                }
            }
        }

        public boolean isFinished() {
            return remainingTicks == 0;
        }

        private void spawnHeartParticles() {
            if (target.isRemoved()) return;

            Vec3d pos = target.getPos();
            double radius = 1.0;
            int particleCount = 10;

            for (int i = 0; i < particleCount; i++) {
                double angle = 2 * Math.PI * i / particleCount;
                double x = pos.x + radius * Math.cos(angle);
                double y = pos.y + 1.5;
                double z = pos.z + radius * Math.sin(angle);

                world.spawnParticles(
                        ParticleTypes.HEART,
                        x, y, z,
                        1, 0, 0, 0, 0
                );
            }
        }
    }
}
