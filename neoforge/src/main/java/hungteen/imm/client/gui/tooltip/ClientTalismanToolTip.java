package hungteen.imm.client.gui.tooltip;

import com.mojang.datafixers.util.Pair;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.client.gui.overlay.ElementOverlay;
import hungteen.imm.common.menu.tooltip.TalismanToolTip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Optional;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-11-13 18:39
 **/
public class ClientTalismanToolTip implements ClientTooltipComponent {

    private static final int TEXT_OFFSET_X = 20;
    private static final int TEXT_OFFSET_Y = 10;
    private static final int ELEMENT_GAP = 11;
    private final TalismanToolTip talismanToolTip;

    public ClientTalismanToolTip(TalismanToolTip talismanToolTip) {
        this.talismanToolTip = talismanToolTip;
    }

    @Override
    public void renderText(Font font, int posX, int posY, Matrix4f matrix4f, MultiBufferSource.BufferSource source) {
        final int offsetX = TEXT_OFFSET_X;
        font.drawInBatch(this.talismanToolTip.getTitle(), (float) posX + offsetX, (float) posY, -1, true, matrix4f, source, Font.DisplayMode.NORMAL, 0, 15728880);
        this.talismanToolTip.getRequirement().ifPresent(c -> {
            font.drawInBatch(c, (float)posX + offsetX, (float)posY + TEXT_OFFSET_Y, -1, true, matrix4f, source, Font.DisplayMode.NORMAL, 0, 15728880);
        });
    }

    @Override
    public void renderImage(Font font, int posX, int posY, GuiGraphics graphics) {
        graphics.blit(this.talismanToolTip.getTexture(), posX, posY + 1, 0, 0, 16, 16, 16, 16);
        Optional<Component> requirement = this.talismanToolTip.getRequirement();
        if(requirement.isPresent()){
            int width = font.width(requirement.get());
            List<QiRootType> qiRootTypes = talismanToolTip.getSpell().requireQiRoots();
            if(! qiRootTypes.isEmpty()){
                for(int i = 0; i < qiRootTypes.size(); ++i) {
                    Pair<Integer, Integer> pair = qiRootTypes.get(i).getTexturePos();
                    graphics.blit(qiRootTypes.get(i).getTexture(), posX + TEXT_OFFSET_X + width + i * ELEMENT_GAP, posY + TEXT_OFFSET_Y, pair.getFirst(), pair.getSecond(), ElementOverlay.ELEMENT_LEN, ElementOverlay.ELEMENT_LEN);
                }
            } else {
                List<Element> elements = talismanToolTip.getSpell().requireElements();
                int offsetX = TEXT_OFFSET_X + width;
                for (int i = 0; i < elements.size(); ++i) {
                    graphics.blit(ElementOverlay.ELEMENTS, posX + offsetX + i * ELEMENT_GAP, posY + TEXT_OFFSET_Y,  ElementOverlay.ELEMENT_INTERVAL * elements.get(i).ordinal(), 0, ElementOverlay.ELEMENT_LEN, ElementOverlay.ELEMENT_LEN);
                }
            }
        }
    }

    @Override
    public int getHeight() {
        return 20;
    }

    @Override
    public int getWidth(Font font) {
        int width = font.width(this.talismanToolTip.getTitle());
        Optional<Component> requirement = this.talismanToolTip.getRequirement();
        if(requirement.isPresent()){
            int requirementWidth = font.width(requirement.get()) + Math.max(talismanToolTip.getSpell().requireElements().size(), talismanToolTip.getSpell().requireQiRoots().size()) * ELEMENT_GAP;
            width = Math.max(width, requirementWidth);
        }
        return TEXT_OFFSET_X + width;
    }
}
