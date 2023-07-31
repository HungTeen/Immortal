package hungteen.imm.client.gui.screen.meditation;

import hungteen.imm.client.ClientUtil;
import hungteen.imm.client.RenderUtil;
import net.minecraft.client.gui.GuiGraphics;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/27 14:42
 */
public class CultivationScreen extends MeditationScreen {

    private static final int LIVING_WIDTH = 46;
    private static final int LIVING_HEIGHT = 66;

    public CultivationScreen() {
        super(MeditationTypes.CULTIVATION);
        this.imageWidth = 200;
        this.imageHeight = 129;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        final int x = this.leftPos + 21 + (LIVING_WIDTH >> 1);
        final int y = this.topPos + 14 + LIVING_HEIGHT + 10;
        RenderUtil.renderEntityInInventoryFollowsMouse(graphics, x, y, 30, x - mouseX, y - mouseY, ClientUtil.player());
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks) {
        super.renderBg(graphics, partialTicks);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

}
