package hungteen.imm.client.gui.screen;

import hungteen.htlib.client.gui.screen.HTContainerScreen;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.imm.common.menu.SpiritualFurnaceMenu;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 14:48
 **/
public class SpiritualFurnaceScreen extends HTContainerScreen<SpiritualFurnaceMenu> {

    private static final ResourceLocation TEXTURE = Util.get().containerTexture("spiritual_furnace");

    public SpiritualFurnaceScreen(SpiritualFurnaceMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 198;
        this.imageWidth = 198;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

        if (this.menu.getMaxValue() > 0) {
            final int len = MathHelper.getBarLen(this.menu.getFlameValue(), this.menu.getMaxValue(), 9);
            graphics.blit(TEXTURE, this.leftPos + 95, this.topPos + 45 + 9 - len, 202, 3 + 9 - len, 8, len);
        }

        if (this.menu.triggered() && this.minecraft.player != null) {
            final int BurnCD = 30;
            final int tick = this.minecraft.player.tickCount % BurnCD;
            final int len = MathHelper.getBarLen(tick, BurnCD, 16) + 1;
            graphics.blit(TEXTURE, this.leftPos + 82, this.topPos + 4 + 16 - len, 198, 14 + 16 - len, 35, len);
        }

        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.renderBg(graphics, partialTicks, mouseX, mouseY);
    }

}
