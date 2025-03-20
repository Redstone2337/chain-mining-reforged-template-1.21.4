package net.deepseek.v1.chainmining.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandRegistryAccess;
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
    private static final SimpleCommandExceptionType INVALID_ENTITY_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.health.invalid_entity"));
    private static final SimpleCommandExceptionType INVALID_AMOUNT_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.health.invalid_amount"));
    private static final SimpleCommandExceptionType INVALID_LIMIT_OPERATION = new SimpleCommandExceptionType(Text.translatable("commands.health.invalid_limit_operation"));
    private static final SimpleCommandExceptionType INVALID_MODIFY_OPERATION = new SimpleCommandExceptionType(Text.translatable("commands.health.invalid_modify_operation"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
            CommandManager.literal("health")
                .requires(source -> source.hasPermissionLevel(2))
                // 基础生命值操作
                .then(CommandManager.argument("target", EntityArgumentType.entity())
                    // add 命令
                    .then(CommandManager.literal("add")
                        .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0.01F))
                            .executes(context -> modifyHealth(
                                context.getSource(),
                                EntityArgumentType.getEntity(context, "target"),
                                FloatArgumentType.getFloat(context, "amount"),
                                true
                            ))
                        )
                    )
                    // remove 命令
                    .then(CommandManager.literal("remove")
                        .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0.01F))
                            .executes(context -> modifyHealth(
                                context.getSource(),
                                EntityArgumentType.getEntity(context, "target"),
                                FloatArgumentType.getFloat(context, "amount"),
                                false
                            ))
                        )
                    )
                    // set 命令
                    .then(CommandManager.literal("set")
                        .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0.01F))
                            .executes(context -> setHealth(
                                context.getSource(),
                                EntityArgumentType.getEntity(context, "target"),
                                FloatArgumentType.getFloat(context, "amount")
                            ))
                        )
                    )
                    // limit 命令
                    .then(CommandManager.literal("limit")
                        .then(CommandManager.argument("operation", StringArgumentType.word())
                            .suggests((context, builder) -> CommandSource.suggestMatching(new String[]{"add", "remove", "set"}, builder))
                            .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0.01F))
                                .executes(context -> modifyMaxHealth(
                                    context.getSource(),
                                    EntityArgumentType.getEntity(context, "target"),
                                    StringArgumentType.getString(context, "operation"),
                                    FloatArgumentType.getFloat(context, "amount")
                                ))
                        )
                    )
                )
                // modify 命令（支持多目标和条件筛选）
                .then(CommandManager.literal("modify")
                    .then(CommandManager.argument("targets", EntityArgumentType.entities())
                        .then(CommandManager.argument("operation", StringArgumentType.word())
                            .suggests((context, builder) -> CommandSource.suggestMatching(new String[]{"add", "remove", "set"}, builder))
                            .then(CommandManager.argument("amount", FloatArgumentType.floatArg(0.01F))
                                // 添加距离限制
                                .then(CommandManager.literal("distance")
                                    .then(CommandManager.argument("center", Vec3ArgumentType.vec3())
                                        .then(CommandManager.argument("radius", FloatArgumentType.floatArg(0.1F))
                                            .executes(context -> modifyWithCondition(
                                                context.getSource(),
                                                EntityArgumentType.getEntities(context, "targets"),
                                                StringArgumentType.getString(context, "operation"),
                                                FloatArgumentType.getFloat(context, "amount"),
                                                Vec3ArgumentType.getVec3(context, "center"),
                                                FloatArgumentType.getFloat(context, "radius")
                                            ))
                                    )
                                )
                                // 无距离限制版本
                                .executes(context -> modifyWithCondition(
                                    context.getSource(),
                                    EntityArgumentType.getEntities(context, "targets"),
                                    StringArgumentType.getString(context, "operation"),
                                    FloatArgumentType.getFloat(context, "amount"),
                                    null,
                                    -1.0F
                                ))
                            )
                        )
                    )
                )))
        );
    }

    // 修改当前生命值（增加/减少）
    private static int modifyHealth(ServerCommandSource source, Entity target, float amount, boolean isAdd) throws CommandSyntaxException {
        if (!(target instanceof LivingEntity living)) throw INVALID_ENTITY_EXCEPTION.create();
        if (amount <= 0) throw INVALID_AMOUNT_EXCEPTION.create();
        
        float newHealth = isAdd 
            ? Math.min(living.getHealth() + amount, living.getMaxHealth())
            : Math.max(living.getHealth() - amount, 0.0F);
        
        living.setHealth(newHealth);
        source.sendFeedback(() -> Text.translatable(
            isAdd ? "commands.health.add.success" : "commands.health.remove.success",
            String.format("%.1f", amount)
        ), true);
        return 1;
    }

    // 设置当前生命值
    private static int setHealth(ServerCommandSource source, Entity target, float amount) throws CommandSyntaxException {
        if (!(target instanceof LivingEntity living)) throw INVALID_ENTITY_EXCEPTION.create();
        if (amount < 0) throw INVALID_AMOUNT_EXCEPTION.create();
        
        float clamped = Math.min(amount, living.getMaxHealth());
        living.setHealth(clamped);
        source.sendFeedback(() -> Text.translatable("commands.health.set.success", String.format("%.1f", clamped)), true);
        return 1;
    }

    // 修改最大生命值
    private static int modifyMaxHealth(ServerCommandSource source, Entity target, String operation, float amount) throws CommandSyntaxException {
        if (!(target instanceof LivingEntity living)) throw INVALID_ENTITY_EXCEPTION.create();
        if (amount <= 0) throw INVALID_AMOUNT_EXCEPTION.create();
        
        EntityAttributeInstance attr = living.getAttributeInstance(EntityAttributes.MAX_HEALTH);
        if (attr == null) throw INVALID_ENTITY_EXCEPTION.create();
        
        double newValue = switch (operation) {
            case "add" -> attr.getBaseValue() + amount;
            case "remove" -> Math.max(attr.getBaseValue() - amount, 0.01);
            case "set" -> amount;
            default -> throw INVALID_LIMIT_OPERATION.create();
        };
        
        attr.setBaseValue(newValue);
        source.sendFeedback(() -> Text.translatable(
            "commands.health.limit.success",
            operation,
            String.format("%.1f", newValue)
        ), true);
        return 1;
    }

    // 带条件修改方法
    private static int modifyWithCondition(
        ServerCommandSource source,
        Collection<? extends Entity> targets,
        String operation,
        float amount,
        @Nullable Vec3d center,
        float radius
    ) throws CommandSyntaxException {
        int affected = 0;
        for (Entity entity : targets) {
            if (!(entity instanceof LivingEntity living)) continue;
            
            // 距离条件检查
            if (center != null && radius > 0) {
                double distance = entity.getPos().distanceTo(center);
                if (distance > radius) continue;
            }

            // 执行操作
            switch (operation) {
                case "add" -> {
                    float newHealth = Math.min(living.getHealth() + amount, living.getMaxHealth());
                    living.setHealth(newHealth);
                    affected++;
                }
                case "remove" -> {
                    float newHealth = Math.max(living.getHealth() - amount, 0.0F);
                    living.setHealth(newHealth);
                    affected++;
                }
                case "set" -> {
                    float clamped = Math.min(amount, living.getMaxHealth());
                    living.setHealth(clamped);
                    affected++;
                }
                default -> throw INVALID_MODIFY_OPERATION.create();
            }
        }

        // 反馈信息
        String feedbackKey = center != null ? 
            "commands.health.modify.conditional" : 
            "commands.health.modify.default";
        int finalAffected = affected;
        source.sendFeedback(() -> Text.translatable(feedbackKey, finalAffected, operation), true);
        return affected;
    }
}