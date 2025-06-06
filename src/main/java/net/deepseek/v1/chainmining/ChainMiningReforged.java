package net.deepseek.v1.chainmining;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.deepseek.v1.chainmining.blocks.ModBlocks;
import net.deepseek.v1.chainmining.blocks.entities.ModBlockEntities;
import net.deepseek.v1.chainmining.command.*;
import net.deepseek.v1.chainmining.command.HealthCommand;
import net.deepseek.v1.chainmining.config.ModConfig;
import net.deepseek.v1.chainmining.core.config.ConfigManager;
import net.deepseek.v1.chainmining.core.data.PlayerSelectionData;
import net.deepseek.v1.chainmining.core.entities.ModEnchantmentEffects;
import net.deepseek.v1.chainmining.core.recipes.loaders.BrewingRecipeLoader;
import net.deepseek.v1.chainmining.core.render.SelectionRenderer;
import net.deepseek.v1.chainmining.enchantments.ModEnchantments;
import net.deepseek.v1.chainmining.event.BlockBreakHandler;
import net.deepseek.v1.chainmining.items.ModItems;
import net.deepseek.v1.chainmining.items.groups.ModItemGroups;
import net.deepseek.v1.chainmining.tag.ModBlockTags;
import net.deepseek.v1.chainmining.tag.ModItemTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChainMiningReforged implements ModInitializer {
	public static final String MOD_ID = "cmr";
	public static final int MAX_SELECTION_SIZE = 100_000;
	public static final int MAX_BLOCKS_PER_OPERATION = 100000;
	public static BlockPos startPos = null;
    public static BlockPos endPos = null;
	public static BlockPos teleportPos = null;
	public static boolean confirmed = false;
	public static boolean isRunning = false;
	public static Random random = Random.create();
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ModConfig getConfig() {
		return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// 注册配置
		AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
		ModEnchantments.register();
		ModEnchantmentEffects.register();
		ModItemTags.register();
		ModBlockTags.register();
		ConfigManager.initialize();
		ModItems.register();
		ModItemGroups.register();
		ModBlocks.register();
		ModBlockEntities.register();

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(
				new BrewingRecipeLoader()
		);

		// 注册事件监听
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			BlockBreakHandler handler = new BlockBreakHandler();
			handler.afterBlockBreak(world, player, pos, state, blockEntity);
		});


		CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
//			commandDispatcher.register(ChainMiningCommands.register(commandRegistryAccess));
//			commandDispatcher.register(ReforgedCommand.register(commandRegistryAccess));
			commandDispatcher.register(SetHomeCommand.register());
			commandDispatcher.register(BackCommand.register());
			commandDispatcher.register(HealthCommand.register());
			ChainMiningCommands.register(commandDispatcher,commandRegistryAccess);
			SexCommand.register(commandDispatcher);
			SetCommand.register(commandDispatcher, commandRegistryAccess);
			TestCommand.register(commandDispatcher, commandRegistryAccess);
			RedstoneCommand.register(commandDispatcher);
			VMinerCommand.register(commandDispatcher);
		});

		// 客户端渲染注册
		        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
					SelectionRenderer.register();
				}

				        // 服务器关闭时清理数据
		        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
					PlayerSelectionData.clearAll();
				});

		LOGGER.info("Hello Fabric world!");
	}

	public static Direction getRandomSide() {
		Direction[] sides = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
		return sides[random.nextInt(sides.length)];
	}
}