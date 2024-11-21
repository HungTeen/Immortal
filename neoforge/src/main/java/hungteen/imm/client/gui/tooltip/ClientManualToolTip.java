package hungteen.imm.client.gui.tooltip;

import hungteen.htlib.client.util.RenderHelper;
import hungteen.imm.common.menu.tooltip.ManualToolTip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-13 18:39
 **/
public class ClientManualToolTip implements ClientTooltipComponent {

    private static final int TEXT_OFFSET_X = 20;
    private static final int TEXT_OFFSET_Y = 10;
    private final ManualToolTip manualToolTip;

    public ClientManualToolTip(ManualToolTip manualToolTip) {
        this.manualToolTip = manualToolTip;
    }

    @Override
    public void renderText(Font font, int posX, int posY, Matrix4f matrix4f, MultiBufferSource.BufferSource source) {
        final int offsetX = (this.manualToolTip.getTexture().isPresent() ? TEXT_OFFSET_X : 0);
        font.drawInBatch(this.manualToolTip.getManualTitle(), (float)posX + offsetX, (float)posY, -1, true, matrix4f, source, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(this.manualToolTip.getContentInfo(), (float)posX + offsetX, (float)posY + TEXT_OFFSET_Y, -1, true, matrix4f, source, Font.DisplayMode.NORMAL, 0, 15728880);
    }

    @Override
    public void renderImage(Font font, int posX, int posY, GuiGraphics graphics) {
        this.manualToolTip.getTexture().ifPresent(tex -> {
            graphics.blit(tex, posX, posY + 1, 0, 0, 16, 16, 16, 16);
        });
    }

    @Override
    public int getHeight() {
        return 20;
    }

    @Override
    public int getWidth(Font font) {
        return (this.manualToolTip.getTexture().isPresent() ? TEXT_OFFSET_X : 0) + Math.max(font.width(this.manualToolTip.getManualTitle()), font.width(this.manualToolTip.getContentInfo()));
    }
}
