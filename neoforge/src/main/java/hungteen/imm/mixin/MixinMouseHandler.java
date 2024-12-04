package hungteen.imm.mixin;

import hungteen.imm.client.data.SpellClientData;
import net.minecraft.client.MouseHandler;
import net.neoforged.neoforge.client.event.CalculatePlayerTurnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/3 15:27
 */
@Mixin(MouseHandler.class)
public class MixinMouseHandler {

    @Inject(method = "Lnet/minecraft/client/MouseHandler;turnPlayer(D)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void onTurnPlayer(double p_316356_, CallbackInfo result, CalculatePlayerTurnEvent event, double d2, double d3, double d4, double d0, double d1, int i) {
        if(SpellClientData.useDefaultCircle() && SpellClientData.showSpellCircle){
            SpellClientData.chooseByVector(d0, d1 * i);
            result.cancel();
        }
    }

}
