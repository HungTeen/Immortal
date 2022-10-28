package hungteen.immortal.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.RenderUtil;
import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.immortal.common.menu.ElixirFurnaceMenu;
import hungteen.immortal.utils.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-28 18:17
 **/
public class ElixirFurnaceScreen extends HTContainerScreen<ElixirFurnaceMenu> {

    private static final ResourceLocation TEXTURE = Util.prefix("textures/gui/container/elixir_furnace.png");

    public ElixirFurnaceScreen(ElixirFurnaceMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 230;
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
