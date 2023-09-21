package hungteen.imm.client.gui.tooltip;

import hungteen.imm.common.menu.tooltip.ArtifactToolTip;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-13 18:39
 **/
public class ClientArtifactToolTip  implements ClientTooltipComponent {

    public static final ResourceLocation TEXTURE_LOCATION = Util.mc().containerTexture("bundle");
    private final ArtifactToolTip artifactToolTip;

    public ClientArtifactToolTip(ArtifactToolTip artifactToolTip) {
        this.artifactToolTip = artifactToolTip;
    }

    @Override
    public void renderImage(Font font, int posX, int posY, GuiGraphics graphics) {
        graphics.blit(TEXTURE_LOCATION, posX, posY, 0, 0, getHeight(), getWidth(font), 128, 128);
        graphics.renderItem(this.artifactToolTip.getArtifact(), posX + 1, posY + 1, 0);
        graphics.renderItemDecorations(font, this.artifactToolTip.getArtifact(), posX + 1, posY + 1);
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
