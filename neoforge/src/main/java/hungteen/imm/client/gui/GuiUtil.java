package hungteen.imm.client.gui;

import hungteen.imm.client.util.ClientUtil;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/19 14:51
 */
public class GuiUtil {

    public static void playSound(SoundEvent event) {
        ClientUtil.soundManager().play(SimpleSoundInstance.forUI(event, 1.0F));
    }

    public static void playSound(Holder<SoundEvent> event) {
        ClientUtil.soundManager().play(SimpleSoundInstance.forUI(event, 1.0F));
    }

    public static void playDownSound() {
        playSound(SoundEvents.UI_BUTTON_CLICK);
    }

    public static void playTurnPageSound() {
        playSound(SoundEvents.BOOK_PAGE_TURN);
    }

}
