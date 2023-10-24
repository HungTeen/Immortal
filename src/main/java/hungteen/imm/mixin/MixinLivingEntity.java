package hungteen.imm.mixin;

import hungteen.imm.common.entity.creature.spirit.WaterSpirit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/22 10:09
 **/
@Mixin(LivingEntity.class)
public class MixinLivingEntity{

    @Inject(method = "increaseAirSupply(I)I",
            at = @At(
                    value = "RETURN"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void increaseAirSupply(int p_21307_, CallbackInfoReturnable<Integer> info) {
        if(WaterSpirit.isWaterSpiritRiding((Entity) (Object) this)){
            info.setReturnValue(p_21307_);
        }
    }

}
