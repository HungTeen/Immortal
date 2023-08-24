package hungteen.imm.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.imm.ImmortalMod;
import hungteen.imm.client.*;
import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.util.Constants;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
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
            if(event.getKey() == InputConstants.KEY_H && Screen.hasAltDown()){
                ClientData.displayReactionInfo = ! ClientData.displayReactionInfo;
                ClientUtil.player().sendSystemMessage(TipUtil.info("display_reaction." + (ClientData.displayReactionInfo ? "open" : "close")).withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD));
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
                if(ClientData.ShowSpellCircle && event.getButton() == InputConstants.MOUSE_BUTTON_RIGHT){
                    SpellManager.selectSpellOnCircle(ClientData.lastSelectedPosition);
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
            if(delta != 0.0 && ClientUtil.player() != null && ClientData.ShowSpellCircle) {
                ClientData.lastSelectedPosition = (ClientData.lastSelectedPosition + (delta < 0 ? 1 : -1) + Constants.SPELL_CIRCLE_SIZE) % Constants.SPELL_CIRCLE_SIZE;
                event.setCanceled(true);
            }
        }
    }

}
