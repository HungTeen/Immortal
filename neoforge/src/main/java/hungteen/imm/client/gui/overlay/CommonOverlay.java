package hungteen.imm.client.gui.overlay;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.client.data.SpellClientData;
import hungteen.imm.client.util.ClientUtil;
import hungteen.imm.client.util.RenderUtil;
import hungteen.imm.util.Colors;
import hungteen.imm.util.MathUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/27 15:31
 */
public class CommonOverlay {

    public static final ResourceLocation OVERLAY = Util.get().overlayTexture("overlay");
    public static final int MANA_BAR_LEN = 182;
    public static final int MANA_BAR_HEIGHT = 5;
    private static final int SMITHING_BAR_LEN = 65;
    private static final int SMITHING_BAR_HEIGHT = 10;

    public static void renderQiBar(GuiGraphics graphics, DeltaTracker tracker) {
        ClientUtil.playerOpt().ifPresent(player -> {
            if (canRenderManaBar(player)) {
                ClientUtil.push("renderSpiritualMana");
                renderSpiritualMana(graphics, player, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth() / 2 - 91, graphics.guiHeight() - 32 + 3);
                ClientUtil.pop();
            }
        });
    }

    public static void renderSpiritualMana(GuiGraphics graphics, Player player, int screenWidth, int screenHeight, int x, int y) {
        final float currentMana = PlayerUtil.getQiAmount(player);
        final float maxMana = PlayerUtil.getMaxQi(player);
        graphics.blit(OVERLAY, x, y, 0, 0, MANA_BAR_LEN, MANA_BAR_HEIGHT);
        if (maxMana > 0) {
            final int backManaLen = MathUtil.getBarLen(currentMana, maxMana, MANA_BAR_LEN - 2);
            graphics.blit(OVERLAY, x + 1, y, 1, 5, backManaLen, MANA_BAR_HEIGHT);
        }
        final float scale = 0.75F;
        final Component text = Component.literal(String.format("%.1f/%.1f", currentMana, maxMana));
        RenderUtil.renderCenterScaledText(graphics.pose(), text, (screenWidth >> 1), y - 6, scale, Colors.SPIRITUAL_MANA, ColorHelper.BLACK.rgb());
    }

    public static boolean canRenderManaBar(Player player) {
        return ClientUtil.canRenderOverlay() && (PlayerUtil.getMaxQi(player) > 0 || SpellClientData.showSpellCircle);
    }

}
