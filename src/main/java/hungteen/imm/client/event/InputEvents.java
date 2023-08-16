package hungteen.imm.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.imm.ImmortalMod;
import hungteen.imm.client.*;
import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.util.Constants;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 21:55
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID, value = Dist.CLIENT)
public class InputEvents {

    @SubscribeEvent
    public static void onKeyDown(InputEvent.Key event) {
        if(ClientProxy.MC.isWindowActive() && ClientUtil.player() != null) {
            if(IMMKeyBinds.keyDown(IMMKeyBinds.ACTIVATE_SPELL)){
                SpellManager.pressToActivateSpell(ClientUtil.player());
            }
            if(! ClientHandler.useDefaultCircle() && SpellManager.canUseCircle(ClientUtil.player())) {
                // Switch display of spell circle.
                if(IMMKeyBinds.keyDown(IMMKeyBinds.SPELL_CIRCLE)){
                    ClientHandler.switchSpellCircle();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseDown(InputEvent.MouseButton.Pre event) {
        if(ClientProxy.MC.isWindowActive() && ClientUtil.player() != null) {
            if(IMMKeyBinds.mouseDown(IMMKeyBinds.ACTIVATE_SPELL)){
                SpellManager.pressToActivateSpell(ClientUtil.player());
            }
            if(! ClientHandler.useDefaultCircle() && SpellManager.canUseCircle(ClientUtil.player())){
                // Switch display of spell circle.
                if(IMMKeyBinds.mouseDown(IMMKeyBinds.SPELL_CIRCLE)){
                    ClientHandler.switchSpellCircle();
                    event.setCanceled(true);
                }

                // Right click to activate spell.
                if(ClientDatas.ShowSpellCircle && event.getButton() == InputConstants.MOUSE_BUTTON_RIGHT){
                    SpellManager.selectSpellOnCircle(ClientDatas.lastSelectedPosition);
                    ClientHandler.switchSpellCircle();
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
		double delta = event.getScrollDelta();
        if(! ClientHandler.useDefaultCircle() && SpellManager.canUseCircle(ClientUtil.player())){
            // Scroll to switch select position.
            if(delta != 0.0 && ClientUtil.player() != null && ClientDatas.ShowSpellCircle) {
                ClientDatas.lastSelectedPosition = Mth.clamp(ClientDatas.lastSelectedPosition + delta < 0 ? 1 : -1, 0, Constants.SPELL_CIRCLE_SIZE);
                event.setCanceled(true);
            }
        }
    }

}
