package hungteen.imm.client.gui.tooltip;

import hungteen.imm.common.menu.tooltip.ElementToolTip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-27 21:14
 **/
public class ClientElementToolTip implements ClientTooltipComponent {

    private final ElementToolTip elementToolTip;

    public ClientElementToolTip(ElementToolTip elementToolTip) {
        this.elementToolTip = elementToolTip;
    }

    @Override
    public void renderText(Font font, int posX, int posY, Matrix4f matrix4f, MultiBufferSource.BufferSource source) {
//        int width = 0;
//        for (Pair<ISpiritualType, Integer> pair : this.elementToolTip.getIngredients()) {
//            final float scale = 0.5F;
//            matrix4f.mul(Matrix4f.createScaleMatrix(scale, scale, scale));
//            final String text = pair.getSecond() + "";
//            final int textWidth = font.width(text);
//            font.drawInBatch(text, (posX + width + 10 - textWidth / 2) / scale, (posY + ElementToolTip.ICON_WIDTH + 1) / scale, -1, true, matrix4f, source, false, 0, 15728880);
//            matrix4f.multiply(Matrix4f.createScaleMatrix(1 / scale, 1 / scale, 1 / scale));
//            width += ElementToolTip.SINGLE_WIDTH;
//        }
    }

    @Override
    public void renderImage(Font font, int posX, int posY, GuiGraphics graphics) {
//        stack.pushPose();
//        int width = 0;
//        for (Pair<ISpiritualType, Integer> pair : this.elementToolTip.getIngredients()) {
//            RenderHelper.setTexture(pair.getFirst().getTexture());
//            GuiComponent.blit(stack, posX + width + 2, posY, pair.getFirst().getTexturePos().getFirst(), pair.getFirst().getTexturePos().getSecond(), 10, 10, 256, 256);
//            width += ElementToolTip.SINGLE_WIDTH;
//        }
//        stack.popPose();
    }

    @Override
    public int getHeight() {
        return elementToolTip.getHeight();
    }

    @Override
    public int getWidth(Font font) {
        return elementToolTip.getWidth();
    }
}
