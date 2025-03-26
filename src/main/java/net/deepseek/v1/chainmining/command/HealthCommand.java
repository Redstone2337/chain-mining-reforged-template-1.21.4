package net.deepseek.v1.chainmining.command;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class HealthCommand {
    // 异常定义
    private static final SimpleCommandExceptionType INVALID_ENTITY_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.health.invalid_entity"));
    private static final SimpleCommandExceptionType INVALID_AMOUNT_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.health.invalid_amount"));
    private static final SimpleCommandExceptionType INVALID_OPERATION_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.health.invalid_operation"));

    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        // 基础命令结构
        return CommandManager.literal("health")
                .requires(source -> source.hasPermissionLevel(2))
                .then(buildSingleTargetCommand())
                .then(buildMultiTargetCommand())
                .then(buildLimitCommand()
                );
    }

    // 构建单目标命令（add/remove/set）
    private static LiteralArgumentBuilder<ServerCommandSource> buildSingleTargetCommand() {
        return CommandManager.literal("target")
            .then(CommandManager.argument("entity", EntityArgumentType.entity())
                .then(CommandManager.literal("add")
                    .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0.01F))
                        .executes(ctx -> modifySingle(
                            ctx.getSource(),
                            EntityArgumentType.getEntity(ctx, "entity"),
                            FloatArgumentType.getFloat(ctx, "amount"),
                            true
                        )))
                .then(CommandManager.literal("remove")
                    .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0.01F))
                        .executes(ctx -> modifySingle(
                            ctx.getSource(),
                            EntityArgumentType.getEntity(ctx, "entity"),
                            FloatArgumentType.getFloat(ctx, "amount"),
                            false
                        )))
                .then(CommandManager.literal("set")
                    .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0.01F))
                        .executes(ctx -> setSingle(
                            ctx.getSource(),
                            EntityArgumentType.getEntity(ctx, "entity"),
                            FloatArgumentType.getFloat(ctx, "amount")
                        )))
                )))
            );
    }

    // 构建多目标命令（modify）
    private static LiteralArgumentBuilder<ServerCommandSource> buildMultiTargetCommand() {
        return CommandManager.literal("modify")
            .then(CommandManager.argument("targets", EntityArgumentType.entities())
                .then(CommandManager.argument("operation", StringArgumentType.word())
                    .suggests((ctx, builder) -> CommandSource.suggestMatching(new String[]{"add", "remove", "set"}, builder))
                    .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0.01F))
                        .then(CommandManager.literal("distance")
                            .then(CommandManager.argument("center", Vec3ArgumentType.vec3())
                                .then(CommandManager.argument("radius", FloatArgumentType.floatArg(0.1F))
                                    .executes(ctx -> modifyMulti(
                                        ctx.getSource(),
                                        EntityArgumentType.getEntities(ctx, "targets"),
                                        StringArgumentType.getString(ctx, "operation"),
                                        FloatArgumentType.getFloat(ctx, "amount"),
                                        Vec3ArgumentType.getVec3(ctx, "center"),
                                        FloatArgumentType.getFloat(ctx, "radius")
                                    ))))
                        .executes(ctx -> modifyMulti(
                            ctx.getSource(),
                            EntityArgumentType.getEntities(ctx, "targets"),
                            StringArgumentType.getString(ctx, "operation"),
                            FloatArgumentType.getFloat(ctx, "amount"),
                            null,
                            -1.0F
                        )))
                    )
                )
            );
    }

    // 构建最大生命值命令（limit）
    private static LiteralArgumentBuilder<ServerCommandSource> buildLimitCommand() {
        return CommandManager.literal("limit")
            .then(CommandManager.argument("entity", EntityArgumentType.entity())
                .then(CommandManager.argument("operation", StringArgumentType.word())
                    .suggests((ctx, builder) -> CommandSource.suggestMatching(new String[]{"add", "remove", "set"}, builder))
                    .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0.01F))
                        .executes(ctx -> modifyLimit(
                            ctx.getSource(),
                            EntityArgumentType.getEntity(ctx, "entity"),
                            StringArgumentType.getString(ctx, "operation"),
                            FloatArgumentType.getFloat(ctx, "amount")
                        ))
                    )
                )
            );
    }

    // 单目标操作逻辑
    private static int modifySingle(ServerCommandSource source, Entity target, float amount, boolean isAdd) throws CommandSyntaxException {
        LivingEntity living = validateLiving(target);
        float newHealth = isAdd ? 
            Math.min(living.getHealth() + amount, living.getMaxHealth()) : 
            Math.max(living.getHealth() - amount, 0.0F);
        living.setHealth(newHealth);
        source.sendFeedback(() -> Text.translatable(
            isAdd ? "commands.health.add.success" : "commands.health.remove.success",
            String.format("%.1f", amount)
        ), true);
        return 1;
    }

    // 设置单目标生命值
    private static int setSingle(ServerCommandSource source, Entity target, float amount) throws CommandSyntaxException {
        LivingEntity living = validateLiving(target);
        float clamped = Math.min(amount, living.getMaxHealth());
        living.setHealth(clamped);
        source.sendFeedback(() -> Text.translatable("commands.health.set.success", String.format("%.1f", clamped)), true);
        return 1;
    }

    // 多目标操作逻辑
    private static int modifyMulti(ServerCommandSource source, Collection<? extends Entity> targets, String operation, float amount, @Nullable Vec3d center, float radius) throws CommandSyntaxException {
        int affected = 0;
        for (Entity entity : targets) {
            if (!(entity instanceof LivingEntity living)) continue;
            if (center != null && entity.getPos().distanceTo(center) > radius) continue;

            switch (operation) {
                case "add" -> living.setHealth(Math.min(living.getHealth() + amount, living.getMaxHealth()));
                case "remove" -> living.setHealth(Math.max(living.getHealth() - amount, 0.0F));
                case "set" -> living.setHealth(Math.min(amount, living.getMaxHealth()));
                default -> throw INVALID_OPERATION_EXCEPTION.create();
            }
            affected++;
        }
        int finalAffected = affected;
        source.sendFeedback(() -> Text.translatable(
            center != null ? "commands.health.modify.conditional" : "commands.health.modify.default",
                finalAffected, operation
        ), true);
        return affected;
    }

    // 修改最大生命值
    private static int modifyLimit(ServerCommandSource source, Entity target, String operation, float amount) throws CommandSyntaxException {
        LivingEntity living = validateLiving(target);
        EntityAttributeInstance attr = living.getAttributeInstance(EntityAttributes.MAX_HEALTH);
        if (attr == null) throw INVALID_ENTITY_EXCEPTION.create();

        double newValue = switch (operation) {
            case "add" -> attr.getBaseValue() + amount;
            case "remove" -> Math.max(attr.getBaseValue() - amount, 0.01);
            case "set" -> amount;
            default -> throw INVALID_OPERATION_EXCEPTION.create();
        };
        attr.setBaseValue(newValue);
        source.sendFeedback(() -> Text.translatable("commands.health.limit.success", operation, String.format("%.1f", newValue)), true);
        return 1;
    }

    // 验证活体实体
    private static LivingEntity validateLiving(Entity entity) throws CommandSyntaxException {
        if (!(entity instanceof LivingEntity living)) throw INVALID_ENTITY_EXCEPTION.create();
        return living;
    }
}