package hungteen.imm.client.gui.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.RenderHelper;
import hungteen.htlib.client.gui.screen.HTScreen;
import hungteen.imm.client.ImmortalKeyBinds;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 21:34
 **/
public class RestingScreen extends HTScreen {

    private static final ResourceLocation SPELL_CIRCLE = Util.prefix("textures/gui/overlay/spell_circle.png");
    private static final List<SpellSlot> SLOTS = new ArrayList<>();

    public RestingScreen(){
        this.imageHeight = 128;
        this.imageWidth = 128;
    }

    @Override
    protected void init() {
        super.init();
        final int ox = this.height / 2;
        final int oy = this.width / 2;
        final int r = 50;
        SLOTS.clear();
        for(int i = 0; i < 8; ++ i){
            final double alpha = 2 * Mth.PI / 8 * i;
            final int x = (int)(Math.sin(alpha) * r) + ox;
            final int y = (int)(Math.cos(alpha) * r)  + oy;
            SLOTS.add(new SpellSlot(i, y, x));
            System.out.println(x + " " + y);
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (!this.checkToClose()) {
            poseStack.pushPose();
            RenderSystem.enableBlend();
            RenderHelper.setTexture(SPELL_CIRCLE);
            blit(poseStack, this.leftPos, this.topPos, 0, 0, 128, 128);
            super.render(poseStack, mouseX, mouseY, partialTicks);
            poseStack.popPose();

            for(SpellSlot spellSlot : SLOTS){
                spellSlot.render(poseStack, mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
        return super.mouseScrolled(p_94686_, p_94687_, p_94688_);
    }

    @Override
    public boolean keyPressed(int keyId, int p_96553_, int p_96554_) {
        return super.keyPressed(keyId, p_96553_, p_96554_);
    }

    private boolean checkToClose() {
        if (!InputConstants.isKeyDown(this.minecraft.getWindow().getWindow(), ImmortalKeyBinds.getKeyValue(ImmortalKeyBinds.SPELL_CIRCLE))) {
            this.minecraft.setScreen(null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static class SpellSlot extends AbstractWidget {

        private final int spellId;
        private boolean isSelected;

        public SpellSlot(int spellId, int posX, int posY) {
            super(posX - 10, posY - 10, 20, 20, Component.empty());
            this.spellId = spellId;
        }

        @Override
        public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
            RenderHelper.setTexture(SPELL_CIRCLE);
            blit(poseStack, this.getX(), this.getY(), this.isSelected ? 20 : 0, 128, this.height, this.width);
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput p_259858_) {

        }
    }
}
