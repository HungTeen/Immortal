package hungteen.imm.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.RenderHelper;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.common.menu.RuneGateMenu;
import hungteen.imm.common.rune.filter.IFilterRuneType;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(stack, partialTicks, mouseX, mouseY);
        RenderHelper.setTexture(TEXTURE);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        for (int i = 0; i < Math.min(this.menu.getGateNums(), LEN); ++i) {
            final int x = getLeftOffset(i);
            final int y = getTopOffset(i);
            final int tx = (i == this.menu.getSelectedGateIndex()) ? 0 : 32;
            this.blit(stack, x, y, tx, 240, 16, 16);
        }

        if(this.menu.getValidStatus() > 0){
            this.blit(stack, this.leftPos + 109, this.topPos + 100, 1, 231, 8, 8);
        }
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        super.renderLabels(stack, mouseX, mouseY);
        for (int i = 0; i < Math.min(this.menu.getGateNums(), LEN); ++i) {
            final int x = getLeftOffset(i) + 8 - this.leftPos;
            final int y = getTopOffset(i) + 5 - this.topPos;
            final IFilterRuneType<?> type = this.menu.getGateTypes().get(i);
            RenderHelper.drawCenteredScaledString(stack, this.font, type.getComponent().getString(), x, y, ColorHelper.BLACK, 0.75F);
        }
        if(this.menu.getValidStatus() > 0){
            final int status = this.menu.getValidStatus();
            final Component component = status == 1 ? TipUtil.gui("rune_conflict") :
                    status == 2 ? TipUtil.gui("need_more_rune") :
                    TipUtil.gui("need_less_rune");
            RenderHelper.drawCenteredScaledString(stack, this.font, component.getString(), 115, 85, ColorHelper.DARK_RED, 0.8F);
        }
    }

    @Override
    protected void renderTooltip(PoseStack stack, int mouseX, int mouseY) {
        super.renderTooltip(stack, mouseX, mouseY);
        for (int i = 0; i < Math.min(this.menu.getGateNums(), LEN); ++i) {
            final int x = getLeftOffset(i);
            final int y = getTopOffset(i);
            if (MathHelper.isInArea(mouseX, mouseY, x, y, 16, 16)) {
                final IFilterRuneType<?> type = this.menu.getGateTypes().get(i);
                this.renderComponentTooltip(stack, List.of(
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
