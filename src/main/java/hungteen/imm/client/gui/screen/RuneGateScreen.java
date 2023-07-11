package hungteen.imm.client.gui.screen;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.client.RenderUtil;
import hungteen.imm.common.menu.RuneGateMenu;
import hungteen.imm.common.rune.filter.IFilterRuneType;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-18 21:47
 **/
public class RuneGateScreen extends RuneBaseScreen<RuneGateMenu> {

    private static final ResourceLocation TEXTURE = StringHelper.containerTexture(Util.id(), "rune_gate_op_table");
    private static final int ROWS = 2;
    private static final int COLS = 5;
    private static final int LEN = ROWS * COLS;

    public RuneGateScreen(RuneGateMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(graphics, partialTicks, mouseX, mouseY);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        for (int i = 0; i < Math.min(this.menu.getGateNums(), LEN); ++i) {
            final int x = getLeftOffset(i);
            final int y = getTopOffset(i);
            final int tx = (i == this.menu.getSelectedGateIndex()) ? 0 : 32;
            graphics.blit(TEXTURE, x, y, tx, 240, 16, 16);
        }

        if(this.menu.getValidStatus() > 0){
            graphics.blit(TEXTURE, this.leftPos + 109, this.topPos + 100, 1, 231, 8, 8);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        for (int i = 0; i < Math.min(this.menu.getGateNums(), LEN); ++i) {
            final int x = getLeftOffset(i) + 8 - this.leftPos;
            final int y = getTopOffset(i) + 5 - this.topPos;
            final IFilterRuneType<?> type = this.menu.getGateTypes().get(i);
            RenderUtil.renderCenterScaledText(graphics.pose(), type.getComponent(), x, y, 0.75F, ColorHelper.BLACK.rgb(), ColorHelper.BLACK.rgb());
        }
        if(this.menu.getValidStatus() > 0){
            final int status = this.menu.getValidStatus();
            final Component component = status == 1 ? TipUtil.gui("rune_conflict") :
                    status == 2 ? TipUtil.gui("need_more_rune") :
                    status == 3 ? TipUtil.gui("need_less_rune") :
                    TipUtil.gui("type_not_fit");
            RenderUtil.renderCenterScaledText(graphics.pose(), component, 115, 85, 0.8F, ColorHelper.DARK_RED.rgb(), ColorHelper.BLACK.rgb());
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);
        for (int i = 0; i < Math.min(this.menu.getGateNums(), LEN); ++i) {
            final int x = getLeftOffset(i);
            final int y = getTopOffset(i);
            if (MathHelper.isInArea(mouseX, mouseY, x, y, 16, 16)) {
                final IFilterRuneType<?> type = this.menu.getGateTypes().get(i);
                graphics.renderComponentTooltip(this.font, List.of(
                        type.getComponent(), type.getDesc().withStyle(ChatFormatting.GREEN)
                ), mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_99320_) {
        for (int i = 0; i < Math.min(this.menu.getGateNums(), LEN); ++i) {
            final double dx = mouseX - getLeftOffset(i);
            final double dy = mouseY - getTopOffset(i);
            if (dx >= 0 && dy >= 0 && dx < 16 && dy < 16 && this.menu.clickMenuButton(this.minecraft.player, i)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, i);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, p_99320_);
    }

    private int getLeftOffset(int id){
        return this.leftPos + 51 + id % COLS * 18;
    }

    private int getTopOffset(int id){
        return this.topPos + 36 + id / COLS * 18;
    }

}
