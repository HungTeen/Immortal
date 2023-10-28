package hungteen.imm.mixin;

import hungteen.imm.common.entity.creature.spirit.WaterSpirit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/28 13:04
 **/
@Mixin(Mob.class)
public class MixinMob {

    @Inject(method = "getControllingPassenger()Lnet/minecraft/world/entity/LivingEntity;",
            at = @At(
                    value = "RETURN"
            ),
            cancellable = true
    )
    private void getControllingPassenger(CallbackInfoReturnable<LivingEntity> info) {
        if(info.getReturnValue() instanceof WaterSpirit){
            info.setReturnValue(null);
        }
    }
}
