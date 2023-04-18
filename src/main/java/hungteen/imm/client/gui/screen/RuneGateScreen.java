package hungteen.imm.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.RenderHelper;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.common.menu.RuneGateMenu;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-18 21:47
 **/
public class RuneGateScreen extends RuneBaseScreen<RuneGateMenu> {

    private static final ResourceLocation TEXTURE = StringHelper.containerTexture(Util.id(), "rune_gate_op_table");

    public RuneGateScreen(RuneGateMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(stack, partialTicks, mouseX, mouseY);
        RenderHelper.setTexture(TEXTURE);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

//        final List<Pair<ICraftableRune, ItemStack>> list = this.menu.getRecipes();
//        for (int i = 0; i < SIZE; ++i) {
//            final int id = this.startIndex + i;
//            if (id < list.size()) {
//                final int x = this.leftPos + CORNER_OFFSET_X + i % COLS * 18;
//                final int y = this.topPos + CORNER_OFFSET_Y + i / COLS * 18;
//                int startX = 0;
//                if (i == this.menu.getSelectedRecipeIndex()) {
//                    startX += 16;
//                } else if(MathHelper.isInArea(mouseX, mouseY, x, y, 16, 16)){
//                    startX += 32;
//                }
//
//                RenderHelper.setTexture(TEXTURE);
//                this.blit(stack, x, y, startX, 240, 16, 16);
//
//                this.minecraft.getItemRenderer().renderAndDecorateItem(list.get(id).getSecond(), x, y);
//            }
//        }
    }

    @Override
    protected void renderTooltip(PoseStack stack, int mouseX, int mouseY) {
        super.renderTooltip(stack, mouseX, mouseY);
//        if (this.displayRecipes) {
//            final List<Pair<ICraftableRune, ItemStack>> list = this.menu.getRecipes();
//
//            for (int i = 0; i < SIZE; ++i) {
//                final int id = this.startIndex + i;
//                if (id < list.size()) {
//                    final int x = this.leftPos + CORNER_OFFSET_X + i % COLS * 18;
//                    final int y = this.topPos + CORNER_OFFSET_Y + i / COLS * 18;
//                    if(MathHelper.isInArea(mouseX, mouseY, x, y, 16, 16)){
//                        this.renderTooltip(stack, list.get(id).getSecond(), mouseX, mouseY);
//                    }
//                }
//            }
//        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_99320_) {
//        if (this.displayRecipes) {
//            for (int i = 0; i < SIZE; ++i) {
//                final int id = this.startIndex + i;
//                final double dx = mouseX - (this.leftPos + CORNER_OFFSET_X + i % COLS * 18);
//                final double dy = mouseY - (this.topPos + CORNER_OFFSET_Y + i / COLS * 18);
//                if (dx >= 0 && dx < 16 && dy >= 0 && dy < 16 && this.menu.clickMenuButton(this.minecraft.player, id)) {
//                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
//                    this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, id);
//                    return true;
//                }
//            }
//
////            i = this.leftPos + 119;
////            j = this.topPos + 9;
////            if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
////                this.scrolling = true;
////            }
//        }
        return super.mouseClicked(mouseX, mouseY, p_99320_);
    }

}
