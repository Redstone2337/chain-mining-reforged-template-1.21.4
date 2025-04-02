package net.deepseek.v1.chainmining.mixin;

import net.deepseek.v1.chainmining.core.config.ConfigManager;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractRedstoneGateBlock.class)
public abstract class AbstractRedstoneGateBlockMixin {

//   @ModifyVariable(method = "getOutputLevel(Lnet/minecraft/block/AbstractRedstoneGateBlock;getOutputLevel(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)I", at = @At("RETURN"), argsOnly = true)
//   private int getOutputLevel(BlockView world, BlockPos pos, BlockState state) {
//       // Check if the block is a MagmaBlock
//       if (state.getBlock() instanceof MagmaBlock) {
//           // Set the output level to 0 for MagmaBlock
//           return 0;
//       }
//       return ConfigManager.getCurrentPowerDistance();
//   }
//    @Shadow
//    protected int getOutputLevel(BlockView world, BlockPos pos, BlockState state) {
//        return 15;
//    }

    @Inject(method = "getOutputLevel", at = @At("RETURN"), cancellable = true)
    private void onGetOutputLevel(BlockView world, BlockPos pos, BlockState state, CallbackInfoReturnable<Integer> cir) {
//        if (state.getBlock() instanceof MagmaBlock) {
//            cir.setReturnValue(0);
//        } else {
//            cir.setReturnValue(ConfigManager.getCurrentPowerDistance());
//        }
        cir.setReturnValue(ConfigManager.getCurrentPowerDistance());
    }
}
