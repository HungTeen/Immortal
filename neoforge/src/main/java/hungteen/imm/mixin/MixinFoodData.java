package hungteen.imm.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/2 8:50
 */
@Mixin(FoodData.class)
public class MixinFoodData {

    @Shadow private int tickTimer;

    @Inject(method = "tick(Lnet/minecraft/world/entity/player/Player;)V",
            at = @At(
                    value = "INVOKE",
                    target = "net.minecraft.world.entity.player.Player.heal(F)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void onTick(Player player, CallbackInfo result) {
        if(player.getHealth() > 20){
            this.tickTimer = 0;
            result.cancel();
        }
    }
}
