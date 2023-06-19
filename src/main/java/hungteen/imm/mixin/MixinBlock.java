package hungteen.imm.mixin;

import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.tag.IMMBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/14 16:35
 */
@Mixin(Block.class)
public class MixinBlock {

    @Inject(method = "animateTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V",
            at = @At(value = "TAIL"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand, CallbackInfo result) {
        if(state.is(IMMBlockTags.SPIRITUAL_ORES) && level.getRandom().nextFloat() < 0.1F){
            for(int i = 0; i < 4; ++ i){
                ParticleHelper.spawnRandomSpeedParticle(level, IMMParticles.SPIRIT.get(), new Vec3(pos.getX() + rand.nextDouble(), pos.getY() + 1.1, pos.getZ() + rand.nextDouble()),0.1F, 0.05F);
            }
        }
    }
}
