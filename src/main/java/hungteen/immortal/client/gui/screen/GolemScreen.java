package hungteen.immortal.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.RenderUtil;
import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.immortal.common.menu.GolemMenu;
import hungteen.immortal.utils.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-24 12:34
 **/
public class GolemScreen extends HTContainerScreen<GolemMenu> {

    private static final ResourceLocation TEXTURE = Util.prefix("textures/gui/container/golem_inventory.png");

    public GolemScreen(GolemMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 176;
        this.imageWidth = 166;
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderUtil.setTexture(TEXTURE);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageHeight, this.imageWidth);
        super.renderBg(stack, partialTicks, mouseX, mouseY);
        this.renderTooltip(stack, mouseX, mouseY);
    }

}
