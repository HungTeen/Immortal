package hungteen.imm.client.gui.tooltip;

import com.mojang.datafixers.util.Either;
import hungteen.imm.common.codec.SpellInstance;
import hungteen.imm.common.cultivation.SpellManager;
import hungteen.imm.common.menu.tooltip.ArtifactToolTip;
import hungteen.imm.util.TipUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

import java.util.List;

/**
 * 每次显示一个。
 *
 * @author HungTeen
 * @program Immortal
 * @create 2022-11-13 18:39
 **/
public class ClientArtifactToolTip implements ClientTooltipComponent {

    private static final int TEXT_OFFSET_X = 20;
    private static final int TEXT_OFFSET_Y = 10;
    private final ArtifactToolTip artifactToolTip;

    public ClientArtifactToolTip(ArtifactToolTip artifactToolTip) {
        this.artifactToolTip = artifactToolTip;
    }

    public static void handleArtifactSpell(ItemStack stack, List<Either<FormattedText, TooltipComponent>> components) {
        int maxSlots = SpellManager.getMaxSpellSlot(stack);
        if (maxSlots > 0) {
            List<SpellInstance> instances = SpellManager.getSpellsInItem(stack);
            int start = 1;
            components.add(start, Either.left(TipUtil.tooltip("artifact_slots", "%d/%d".formatted(instances.size(), maxSlots))));
            for(int i = 0; i < instances.size(); ++ i){
                components.add(start + i + 1, Either.right(new ArtifactToolTip(instances.get(i))));
            }
        }
    }

    @Override
    public void renderText(Font font, int posX, int posY, Matrix4f matrix4f, MultiBufferSource.BufferSource source) {
        final int offsetX = TEXT_OFFSET_X;
        font.drawInBatch(this.artifactToolTip.getSpellTitle(), (float) posX + offsetX, (float) posY, -1, true, matrix4f, source, Font.DisplayMode.NORMAL, 0, 15728880);
        font.drawInBatch(this.artifactToolTip.getConditionTitle(), (float) posX + offsetX, (float) posY + TEXT_OFFSET_Y, -1, true, matrix4f, source, Font.DisplayMode.NORMAL, 0, 15728880);
    }

    @Override
    public void renderImage(Font font, int posX, int posY, GuiGraphics graphics) {
        graphics.blit(this.artifactToolTip.getSpellTexture(), posX, posY + 1, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public int getHeight() {
        return 20;
    }

    @Override
    public int getWidth(Font font) {
        return TEXT_OFFSET_X + Math.max(font.width(this.artifactToolTip.getSpellTitle()), font.width(this.artifactToolTip.getConditionTitle()));
    }
}
