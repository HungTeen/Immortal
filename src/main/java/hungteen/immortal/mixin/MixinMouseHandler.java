package hungteen.immortal.mixin;

import hungteen.immortal.client.ClientDatas;
import hungteen.immortal.utils.Constants;
import net.minecraft.client.MouseHandler;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
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
    private void onTurnPlayer(CallbackInfo result, double d2, double d3) {
        if(ClientDatas.ShowSpellCircle){
            if(d2 != 0 && d3 != 0){
                final double delta = Math.atan2(d3, d2);
                ClientDatas.lastSelectedPosition = Mth.clamp((int)(delta * 4 / Math.PI + 4), 0, Constants.SPELL_NUM_EACH_PAGE);
            }
            result.cancel();
        }
    }
}
