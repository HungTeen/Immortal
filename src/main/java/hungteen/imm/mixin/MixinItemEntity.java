package hungteen.imm.mixin;

import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.common.tag.IMMItemTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/14 15:29
 */
@Mixin(ItemEntity.class)
public abstract class MixinItemEntity {

    @Shadow public abstract ItemStack getItem();

    @Inject(method = "tick()V",
            at = @At(value = "TAIL"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void tick(CallbackInfo result) {
        final Level level = ((ItemEntity)(Object)this).level;
        final Vec3 pos = ((ItemEntity)(Object)this).position();
        if(level.isClientSide && this.getItem().is(IMMItemTags.SPIRITUAL_STONES) && level.getRandom().nextFloat() < 0.1F){
            for(int i = 0; i < 2; ++ i){
                ParticleHelper.spawnRandomSpeedParticle(level, ParticleTypes.GLOW, pos,0.1F, 0.1F);
            }
        }
    }

}
