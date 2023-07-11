package hungteen.imm.client.gui.screen;

import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.common.menu.GolemInventoryMenu;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-24 12:34
 **/
public class GolemInventoryScreen extends HTContainerScreen<GolemInventoryMenu> {

    private static final ResourceLocation TEXTURE = StringHelper.containerTexture(Util.id(), "golem_inventory");
    private static final int TOP_HEIGHT = 25;
    private static final int BOTTOM_HEIGHT = 95;
    private static final int SLOT_HEIGHT = 18;
    private static final int WIDTH = 189;
    private final int rows;

    public GolemInventoryScreen(GolemInventoryMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = this.getTotalHeight();
        this.rows = this.getMenu().getRuneRows();
        this.imageWidth = WIDTH;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        //Dynamically render background.
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, TOP_HEIGHT);
        for(int i = 0; i < this.rows; ++ i){
            graphics.blit(TEXTURE, this.leftPos, this.topPos + TOP_HEIGHT + SLOT_HEIGHT * i, 0, 131, this.imageWidth, SLOT_HEIGHT);
        }
        graphics.blit(TEXTURE, this.leftPos, this.topPos + TOP_HEIGHT + SLOT_HEIGHT * this.rows, 0, 119, this.imageWidth, BOTTOM_HEIGHT);
        super.renderBg(graphics, partialTicks, mouseX, mouseY);
    }

    private int getTotalHeight(){
        return TOP_HEIGHT + BOTTOM_HEIGHT + SLOT_HEIGHT * this.menu.getRuneRows();
    }

}
