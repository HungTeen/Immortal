package hungteen.imm.client.gui.screen.meditation;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.client.RenderUtil;
import hungteen.imm.client.gui.overlay.CommonOverlay;
import hungteen.imm.common.RealmManager;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 21:34
 **/
public class RestingScreen extends MeditationScreen {

    private static final ResourceLocation OVERLAY = CommonOverlay.OVERLAY;
    private static final int MANA_BAR_LEN = CommonOverlay.MANA_BAR_LEN;
    private static final int MANA_BAR_HEIGHT = CommonOverlay.MANA_BAR_HEIGHT;
    private static final int SWITCH_BAR_WIDTH = 125;
    private static final int SWITCH_BAR_HEIGHT = 75;
    private static final int SLOT_LEN = 26;

    public RestingScreen() {
        super(MeditationTypes.REST);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        final int x = (this.width << 1) - 91;
        int y = this.height - 32 + 3;
        CommonOverlay.renderSpiritualMana(graphics, this.width, this.height, x, y);
        // Render Break Through Bar.
        if (PlayerUtil.getPlayerRealmStage(ClientUtil.player()).canLevelUp()) {
            y += 10;
            graphics.blit(OVERLAY, x, y + 30, 0, 0, MANA_BAR_LEN, MANA_BAR_HEIGHT);
            final float progress = RealmManager.getBreakThroughProgress(ClientUtil.player());
            final int backManaLen = Mth.floor((MANA_BAR_LEN - 2) * progress);
            graphics.blit(OVERLAY, x + 1, y, 1, 5, backManaLen, MANA_BAR_HEIGHT);
            final float scale = 1;
            final Component text = Component.literal(String.format("%.2f", progress * 100));
            RenderUtil.renderCenterScaledText(graphics.pose(), text, (width >> 1), y - 6, scale, ColorHelper.GOLD.rgb(), ColorHelper.BLACK.rgb());
        }
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

    private int getSlotX(int i) {
        return (this.width - SWITCH_BAR_WIDTH) / 2 + i * 26 + (i + 1) * this.getInterval();
    }

    private int getSlotY() {
        return 20 + 22 + 13;
    }

    private int getInterval() {
        return (SWITCH_BAR_WIDTH - SLOT_LEN * this.getLen()) / (this.getLen() + 1);
    }

}
