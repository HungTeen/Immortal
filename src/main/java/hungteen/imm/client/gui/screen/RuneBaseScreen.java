package hungteen.imm.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.RenderHelper;
import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.common.menu.RuneBaseMenu;
import hungteen.imm.common.rune.RuneCategories;
import hungteen.imm.util.Util;
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
 * @create: 2023-04-17 22:46
 **/
public class RuneBaseScreen<T extends RuneBaseMenu> extends HTContainerScreen<T> {

    private static final ResourceLocation WIDGETS = StringHelper.guiTexture(Util.id(), "widgets");
    private static final int TAB_HEIGHT = 30;
    private static final int TAB_WIDTH = 52;

    public RuneBaseScreen(T screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 189;
        this.imageHeight = 214;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        this.renderBackground(stack);
        RenderHelper.setTexture(WIDGETS);
        final List<RuneCategories> list = this.menu.getRuneCategories();
        for(int i = 0; i < list.size(); i++) {
            final int y = this.menu.getRuneCategory() == list.get(i) ? 32 : 64;
            this.blit(stack, getTabLeftOffset(i), getTabTopOffset(i), 0, y, TAB_WIDTH, TAB_HEIGHT);
        }
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        RenderHelper.drawCenteredScaledString(stack, this.font, this.menu.getRuneCategory().getTitle().getString(), 95, 18, ColorHelper.BLACK, 1F);
        final List<RuneCategories> list = this.menu.getRuneCategories();
        for(int i = 0; i < list.size(); i++) {
            final int x = getTabLeftOffset(i) - this.leftPos + TAB_WIDTH / 2 + 4;
            final int y = getTabTopOffset(i) - this.topPos + 12;
            final int color = list.get(i) == this.menu.getRuneCategory() ? ColorHelper.GREEN : ColorHelper.BLACK;
            RenderHelper.drawCenteredScaledString(stack, this.font, list.get(i).getTabTitle().getString(), x, y, color, 1F);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_99320_) {
        final List<RuneCategories> list = this.menu.getRuneCategories();
        for(int i = 0; i < list.size(); i++) {
            final double dx = mouseX - getTabLeftOffset(i);
            final double dy = mouseY - getTabTopOffset(i);
            final int id = this.menu.getTabOffset(i);
            if(this.menu.getRuneCategory() != list.get(i) && dx >= 0 && dx <= TAB_WIDTH && dy >= 0 && dy <= TAB_HEIGHT && this.menu.clickMenuButton(this.minecraft.player, id)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, id);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, p_99320_);
    }

    private int getTabLeftOffset(int id){
        return this.leftPos - TAB_WIDTH + 11;
    }

    private int getTabTopOffset(int id){
        return this.topPos + 15 + (TAB_HEIGHT + 10) * id;
    }

}
