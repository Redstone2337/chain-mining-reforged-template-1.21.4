package net.deepseek.v1.chainmining.core.render;

import net.deepseek.v1.chainmining.core.data.PlayerSelectionData;
import net.deepseek.v1.chainmining.util.SelectionArea;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class SelectionRenderer {
    private static final int OUTLINE_COLOR = 0xFF0000; // 红色
    private static final float PARTICLE_SIZE = 0.2f;

    public static void register() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            ClientWorld world = MinecraftClient.getInstance().world;
            if (world == null) return;

            PlayerSelectionData data = null;
            if (MinecraftClient.getInstance().player != null) {
                data = PlayerSelectionData.get(MinecraftClient.getInstance().player);
            }
            if (data == null) return;

            SelectionArea area = data.getSelection();
            if (area == null || !area.isValid()) return;

            Camera camera = context.camera();
            Vec3d cameraPos = camera.getPos();

            for (BlockPos pos : area.getEdgeBlocks()) {
                // 只渲染可见的边
                if (camera.getFocusedEntity().equals(pos)) continue;

                double x = pos.getX() + 0.5 - cameraPos.x;
                double y = pos.getY() + 0.5 - cameraPos.y;
                double z = pos.getZ() + 0.5 - cameraPos.z;

                // 添加粒子效果
                world.addParticle(
                        (ParticleEffect) ParticleTypes.DUST,
                        x, y, z,
                        OUTLINE_COLOR, PARTICLE_SIZE, 0
                );
            }
        });
    }
}
