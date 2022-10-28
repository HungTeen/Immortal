package hungteen.immortal.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.RenderUtil;
import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.immortal.common.menu.SpiritualStoveMenu;
import hungteen.immortal.utils.Util;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.FurnaceMenu;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 14:48
 **/
public class SpiritualStoveScreen extends HTContainerScreen<SpiritualStoveMenu> {

    private static final ResourceLocation TEXTURE = Util.prefix("textures/gui/container/spiritual_stove.png");

    public SpiritualStoveScreen(SpiritualStoveMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 198;
        this.imageWidth = 198;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderUtil.setTexture(TEXTURE);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.renderBg(stack, partialTicks, mouseX, mouseY);
    }

}
