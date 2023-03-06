package hungteen.immortal.mixin;

import hungteen.immortal.client.ClientDatas;
import hungteen.immortal.client.ClientHandler;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/3 15:27
 */
@Mixin(MouseHandler.class)
public class MixinMouseHandler {

    @Inject(method = "turnPlayer()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void onTurnPlayer(CallbackInfo result, double d0, double d1, double d4, double d5, double d6, double d2, double d3) {
        if(ClientHandler.useDefaultCircle() && ClientDatas.ShowSpellCircle){
            ClientHandler.chooseByVector(d2, d3);
            result.cancel();
        }
    }

}
