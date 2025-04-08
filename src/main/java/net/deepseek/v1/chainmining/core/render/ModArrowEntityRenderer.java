package net.deepseek.v1.chainmining.core.render;

import net.deepseek.v1.chainmining.ChainMiningReforged;
import net.deepseek.v1.chainmining.entity.function.BedrockiumArrowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ArrowEntityRenderState;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ModArrowEntityRenderer extends ProjectileEntityRenderer<BedrockiumArrowEntity, ArrowEntityRenderState> {
    public static final Identifier TEXTURE = Identifier.of(ChainMiningReforged.MOD_ID,"textures/entity/projectiles/arrow.png");
    public static final Identifier TIPPED_TEXTURE = Identifier.of(ChainMiningReforged.MOD_ID,"textures/entity/projectiles/tipped_arrow.png");

    public ModArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    protected Identifier getTexture(ArrowEntityRenderState arrowEntityRenderState) {
        return arrowEntityRenderState.tipped ? TIPPED_TEXTURE : TEXTURE;
    }

    public ArrowEntityRenderState createRenderState() {
        return new ArrowEntityRenderState();
    }

    public void updateRenderState(BedrockiumArrowEntity arrowEntity, ArrowEntityRenderState arrowEntityRenderState, float f) {
        super.updateRenderState(arrowEntity, arrowEntityRenderState, f);
        arrowEntityRenderState.tipped = arrowEntity.getColor() > 0;
    }
}
