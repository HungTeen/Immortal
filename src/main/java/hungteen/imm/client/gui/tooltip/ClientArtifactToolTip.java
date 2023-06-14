package hungteen.imm.client.gui.tooltip;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.RenderHelper;
import hungteen.imm.common.menu.tooltip.ArtifactToolTip;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-13 18:39
 **/
public class ClientArtifactToolTip  implements ClientTooltipComponent {

    public static final ResourceLocation TEXTURE_LOCATION = Util.mcPrefix("textures/gui/container/bundle.png");
    private final ArtifactToolTip artifactToolTip;

    public ClientArtifactToolTip(ArtifactToolTip artifactToolTip) {
        this.artifactToolTip = artifactToolTip;
    }

    @Override
    public void renderImage(Font font, int posX, int posY, PoseStack stack, ItemRenderer itemRenderer) {
        stack.pushPose();
        RenderHelper.setTexture(TEXTURE_LOCATION);
        GuiComponent.blit(stack, posX, posY, 0, 0, getHeight(), getWidth(font), 128, 128);
        itemRenderer.renderAndDecorateItem(stack, this.artifactToolTip.getArtifact(), posX + 1, posY + 1, 0);
        itemRenderer.renderGuiItemDecorations(stack, font, this.artifactToolTip.getArtifact(), posX + 1, posY + 1);
        stack.popPose();
    }

    @Override
    public int getHeight() {
        return this.artifactToolTip.getHeight();
    }

    @Override
    public int getWidth(Font font) {
        return this.artifactToolTip.getWidth();
    }
}
