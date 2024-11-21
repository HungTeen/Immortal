package hungteen.imm.client.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import hungteen.htlib.client.util.ClientHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-07-31 16:50
 **/
public class HTButton extends Button {

    private final ResourceLocation textureLocation;

    protected HTButton(Builder builder, ResourceLocation textureLocation) {
        super(builder);
        this.textureLocation = textureLocation;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int x, int y, float partialTicks) {
        if(this.active != this.isActive()){
            this.active = this.isActive();
        }
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        graphics.blit(this.textureLocation, this.getX(), this.getY(), this.getTextureX(), this.getTextureY(), this.getWidth(), this.getHeight());
        int i = getFGColor();
        this.renderString(graphics, ClientHelper.font(), i | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    @Override
    public int getFGColor() {
        if (packedFGColor != UNSET_FG_COLOR) return packedFGColor;
        return getColor(this.active);
    }

    protected int getColor(boolean active){
        return active ? 16777215 : 10526880; // White : Light Grey
    }

    protected int getTextureX() {
        return 0;
    }

    protected int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 2;
        }
        return 46 + i * 20;
    }
}
