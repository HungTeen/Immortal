package hungteen.imm.client.gui.screen;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.client.util.RenderHelper;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.common.menu.RuneCraftingMenu;
import hungteen.imm.common.rune.ICraftableRune;
import hungteen.imm.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-10 21:45
 **/
public class RuneCraftScreen extends RuneBaseScreen<RuneCraftingMenu> {

    private static final ResourceLocation TEXTURE = StringHelper.containerTexture(Util.id(), "rune_craft_table");
    private static final int ROWS = 5;
    private static final int COLS = 5;
    private static final int SIZE = ROWS * COLS;
    private static final int BAR_HEIGHT = 88;
    private static final int CORNER_OFFSET_X = 47;
    private static final int CORNER_OFFSET_Y = 32;
    private boolean displayRecipes;
    private boolean scrolling;
    private double scrollPercent;
    private int startIndex;

    public RuneCraftScreen(RuneCraftingMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        screenContainer.setClientSlotUpdateListener(this::slotChanged);
    }

    private void slotChanged() {
        this.displayRecipes = this.menu.hasRecipes();
        if (!this.displayRecipes) {
            this.scrollPercent = 0F;
            this.startIndex = 0;
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(graphics, partialTicks, mouseX, mouseY);
        RenderHelper.setTexture(TEXTURE);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        final List<Pair<ICraftableRune, ItemStack>> list = this.menu.getRecipes();
        for (int i = 0; i < SIZE; ++i) {
            final int id = this.startIndex + i;
            if (id < list.size()) {
                final int x = this.leftPos + CORNER_OFFSET_X + i % COLS * 18;
                final int y = this.topPos + CORNER_OFFSET_Y + i / COLS * 18;
                int startX = 0;
                if (i == this.menu.getSelectedRecipeIndex()) {
                    startX += 16;
                } else if(MathHelper.isInArea(mouseX, mouseY, x, y, 16, 16)){
                    startX += 32;
                }

                RenderHelper.setTexture(TEXTURE);
                graphics.blit(TEXTURE, x, y, startX, 240, 16, 16);

                graphics.renderItem(list.get(id).getSecond(), x, y);
            }
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);
        if (this.displayRecipes) {
            final List<Pair<ICraftableRune, ItemStack>> list = this.menu.getRecipes();

            for (int i = 0; i < SIZE; ++i) {
                final int id = this.startIndex + i;
                if (id < list.size()) {
                    final int x = this.leftPos + CORNER_OFFSET_X + i % COLS * 18;
                    final int y = this.topPos + CORNER_OFFSET_Y + i / COLS * 18;
                    if(MathHelper.isInArea(mouseX, mouseY, x, y, 16, 16)){
                        graphics.renderTooltip(this.font, list.get(id).getSecond(), mouseX, mouseY);
                    }
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_99320_) {
        this.scrolling = false;
        if (this.displayRecipes) {
            for (int i = 0; i < SIZE; ++i) {
                final int id = this.startIndex + i;
                final double dx = mouseX - (this.leftPos + CORNER_OFFSET_X + i % COLS * 18);
                final double dy = mouseY - (this.topPos + CORNER_OFFSET_Y + i / COLS * 18);
                if (dx >= 0 && dx < 16 && dy >= 0 && dy < 16 && this.menu.clickMenuButton(this.minecraft.player, id)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, id);
                    return true;
                }
            }

//            i = this.leftPos + 119;
//            j = this.topPos + 9;
//            if (mouseX >= (double)i && mouseX < (double)(i + 12) && mouseY >= (double)j && mouseY < (double)(j + 54)) {
//                this.scrolling = true;
//            }
        }
        return super.mouseClicked(mouseX, mouseY, p_99320_);
    }

    //    @Override
//    public boolean mouseDragged(double p_97752_, double p_97753_, int p_97754_, double p_97755_, double p_97756_) {
//        if (this.scrolling && this.canScroll()) {
//            int i = this.topPos + 14;
//            int j = i + 54;
//            this.scrollOffs = ((float)p_99323_ - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
//            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
//            this.startIndex = (int)((double)(this.scrollOffs * (float)this.getOffscreenRows()) + 0.5D) * 4;
//            return true;
//        }
//        return super.mouseDragged(p_97752_, p_97753_, p_97754_, p_97755_, p_97756_);
//    }

    @Override
    public boolean mouseScrolled(double p_94686_, double p_94687_, double deltaX, double deltaY) {
        if (this.canScroll()) {
            final int rows = this.getHiddenRows();
            final double f = deltaX / rows;
            this.scrollPercent = Mth.clamp(this.scrollPercent - f, 0.0F, 1.0F);
            this.startIndex = Math.round((float) (this.scrollPercent * rows)) * COLS;
        }

        return true;
    }

    private boolean canScroll() {
        return this.displayRecipes && this.menu.getNumRecipes() > SIZE;
    }

    protected int getHiddenRows() {
        return (this.menu.getNumRecipes() + COLS - 1) / COLS - ROWS;
    }

}
