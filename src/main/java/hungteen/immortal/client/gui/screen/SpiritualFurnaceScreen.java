package hungteen.immortal.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.RenderUtil;
import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.htlib.util.MathUtil;
import hungteen.immortal.common.menu.SpiritualFurnaceMenu;
import hungteen.immortal.utils.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 14:48
 **/
public class SpiritualFurnaceScreen extends HTContainerScreen<SpiritualFurnaceMenu> {

    private static final ResourceLocation TEXTURE = Util.prefix("textures/gui/container/spiritual_furnace.png");

    public SpiritualFurnaceScreen(SpiritualFurnaceMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 198;
        this.imageWidth = 198;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);

        if(this.menu.getMaxValue() > 0){
            RenderUtil.setTexture(TEXTURE);
            final int len = MathUtil.getBarLen(this.menu.getFlameValue(), this.menu.getMaxValue(), 9);
            this.blit(stack, this.leftPos + 95, this.topPos + 45 - len, 202, 3 - len, 8, len);

            final int BurnCD = 30;
            final int tick = this.minecraft.player.tickCount % BurnCD;
            final int flameLen = MathUtil.getBarLen(tick, BurnCD, 16);
            this.blit(stack, this.leftPos + 82, this.topPos + 4 - flameLen, 198, 14 - flameLen, 35, flameLen);
        }

        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderUtil.setTexture(TEXTURE);
        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.renderBg(stack, partialTicks, mouseX, mouseY);
    }

}
