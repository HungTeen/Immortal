package hungteen.imm.client.gui.screen.meditation;

import net.minecraft.client.gui.GuiGraphics;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 21:34
 **/
public class RestingScreen extends MeditationScreen {

    private static final int SWITCH_BAR_WIDTH = 125;
    private static final int SWITCH_BAR_HEIGHT = 75;
    private static final int SLOT_LEN = 26;

    public RestingScreen(){
        super(MeditationTypes.REST);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int key) {
        final double dx = mouseX - (this.leftPos + 82);
        final double dy = mouseY - (this.topPos + 108);
//        if (dx >= 0 && dy >= 0 && dx < FULL_FLAME_LEN && dy < FULL_FLAME_HEIGHT && this.menu.clickMenuButton(this.minecraft.player, 1)) {
//            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
//            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 1);
//            return true;
//        }
//        for (MeditationTypes type : MeditationTypes.values()) {
//            final boolean selected = type.ordinal() == this.selectedIndex;
//            graphics.blit(SLOTS, this.getSlotX(type.ordinal()), this.getSlotY(),  selected ? 26 : 0, 75, SLOT_LEN, SLOT_LEN);
//        }
        return super.mouseClicked(mouseX, mouseY, key);
    }

    private int getSlotX(int i){
        return (this.width - SWITCH_BAR_WIDTH) / 2 + i * 26 + (i + 1) * this.getInterval();
    }

    private int getSlotY(){
        return 20 + 22 + 13;
    }

    private int getInterval(){
        return (SWITCH_BAR_WIDTH - SLOT_LEN * this.getLen()) / (this.getLen() + 1);
    }

}
