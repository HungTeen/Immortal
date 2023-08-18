package hungteen.imm.client.gui.overlay;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.client.ClientDatas;
import hungteen.imm.client.ClientProxy;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.client.RenderUtil;
import hungteen.imm.util.Colors;
import hungteen.imm.util.MathUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/27 15:31
 */
public class CommonOverlay {

    public static final IGuiOverlay SPIRITUAL_MANA = CommonOverlay::renderSpiritualMana;
    public static final ResourceLocation OVERLAY = Util.get().overlayTexture("overlay");
    public static final int MANA_BAR_LEN = 182;
    public static final int MANA_BAR_HEIGHT = 5;
    private static final int SMITHING_BAR_LEN = 65;
    private static final int SMITHING_BAR_HEIGHT = 10;

    private static void renderSpiritualMana(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if(canRenderManaBar()){
            ClientUtil.push("renderSpiritualMana");
            renderSpiritualMana(graphics, screenWidth, screenHeight, screenWidth / 2 - 91, screenHeight - 32 + 3);
            ClientUtil.pop();
        }
    }

    public static void renderSpiritualMana(GuiGraphics graphics, int screenWidth, int screenHeight, int x, int y) {
        final float currentMana = PlayerUtil.getMana(ClientProxy.MC.player);
        final float maxMana = PlayerUtil.getMaxMana(ClientProxy.MC.player);
        graphics.blit(OVERLAY, x, y, 0, 0, MANA_BAR_LEN, MANA_BAR_HEIGHT);
        if (maxMana > 0) {
            final int backManaLen = MathUtil.getBarLen(currentMana, maxMana, MANA_BAR_LEN - 2);
            graphics.blit(OVERLAY, x + 1, y, 1, 5, backManaLen, MANA_BAR_HEIGHT);
        }
        final float scale = 1;
        final Component text = Component.literal(currentMana + " / " + maxMana);
        RenderUtil.renderCenterScaledText(graphics.pose(), text, (screenWidth >> 1), y - 6, scale, Colors.SPIRITUAL_MANA, ColorHelper.BLACK.rgb());
    }

    public static boolean canRenderManaBar() {
        return ClientUtil.canRenderOverlay() && (PlayerUtil.getMaxMana(ClientUtil.player()) > 0 || ClientDatas.ShowSpellCircle);
    }

}
