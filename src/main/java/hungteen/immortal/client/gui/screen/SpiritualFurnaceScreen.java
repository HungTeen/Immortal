package hungteen.immortal.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.RenderHelper;
import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.htlib.util.helper.MathHelper;
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
public class SpiritualFurnaceScreen {
//        extends HTContainerScreen<SpiritualFurnaceMenu> {
//
//    private static final ResourceLocation TEXTURE = Util.prefix("textures/gui/container/spiritual_furnace.png");
//
//    public SpiritualFurnaceScreen(SpiritualFurnaceMenu screenContainer, Inventory inv, Component titleIn) {
//        super(screenContainer, inv, titleIn);
//        this.imageHeight = 198;
//        this.imageWidth = 198;
//    }
//
//    @Override
//    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
//        super.render(stack, mouseX, mouseY, partialTicks);
//
//        if(this.menu.getMaxValue() > 0){
//            RenderHelper.setTexture(TEXTURE);
//            final int len = MathHelper.getBarLen(this.menu.getFlameValue(), this.menu.getMaxValue(), 9);
//            this.blit(stack, this.leftPos + 95, this.topPos + 45 + 9 - len, 202, 3 + 9 - len, 8, len);
//
//            }
//
//        if(this.menu.triggered() && this.minecraft.player != null){
//            RenderHelper.setTexture(TEXTURE);
//            final int BurnCD = 30;
//            final int tick = this.minecraft.player.tickCount % BurnCD;
//            final int len = MathHelper.getBarLen(tick, BurnCD, 16) + 1;
//            this.blit(stack, this.leftPos + 82, this.topPos + 4 + 16 - len, 198, 14 + 16 - len, 35, len);
//        }
//
//        this.renderTooltip(stack, mouseX, mouseY);
//    }
//
//    @Override
//    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
//        RenderHelper.setTexture(TEXTURE);
//        this.blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
//        super.renderBg(stack, partialTicks, mouseX, mouseY);
//    }

}
