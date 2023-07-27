package hungteen.imm.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import hungteen.htlib.client.util.RenderHelper;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.client.ClientDatas;
import hungteen.imm.client.ClientProxy;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.client.RenderUtil;
import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.util.Constants;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/27 15:12
 */
public class SpellOverlay {
    
    public static final IGuiOverlay SPELL_CIRCLE = SpellOverlay::renderSpellCircle;
    public static final IGuiOverlay PREPARE_BAR = SpellOverlay::renderPrepareBar;

    public static final ResourceLocation TEXTURE = Util.get().overlayTexture("spell_circle");
    public static final int CIRCLE_LEN = 128;
    public static final int SPELL_SLOT_LEN = 20;
    private static final List<Pair<Integer, Integer>> SPELL_SLOTS = new ArrayList<>();
    private static final int CIRCLE_RADIUS = 48;
    private static final int INNER_LEN = 40;

    static {
        for (int i = 0; i < Constants.SPELL_CIRCLE_SIZE; ++i) {
            final double alpha = 2 * Mth.PI / 8 * i;
            final int x = (int) (Math.sin(alpha) * CIRCLE_RADIUS) - SPELL_SLOT_LEN / 2 + 1;
            final int y = (int) (-Math.cos(alpha) * CIRCLE_RADIUS) - SPELL_SLOT_LEN / 2 + 1;
            SPELL_SLOTS.add(Pair.of(x, y));
        }
    }

    public static int getSlotX(int centerX, int slotId){
        return SPELL_SLOTS.get(slotId).getFirst() + centerX;
    }

    public static int getSlotY(int centerY, int slotId){
        return SPELL_SLOTS.get(slotId).getSecond() + centerY;
    }
    
    public static void renderSpellCircle(ForgeGui gui, GuiGraphics graphics, float partialTick, int width, int height) {
        if(ClientUtil.canRenderOverlay() && ClientDatas.ShowSpellCircle){
            ClientUtil.push("renderSpellCircle");
            final int leftPos = (width - CIRCLE_LEN) >> 1;
            final int topPos = (height - CIRCLE_LEN) >> 1;
            final int selectPos = ClientDatas.lastSelectedPosition;
            RenderHelper.push(graphics);
            RenderSystem.enableBlend();
            graphics.blit(TEXTURE, leftPos, topPos, 0, 0, CIRCLE_LEN, CIRCLE_LEN);
            // Render Mid Selected White Circle.
            if (selectPos != -1) {
                graphics.blit(TEXTURE, (width - INNER_LEN) >> 1, (height - INNER_LEN) >> 1, (selectPos % 4) * INNER_LEN, selectPos < 4 ? 160 : 200, INNER_LEN, INNER_LEN);
            }
            // Render Spell Slots.
            for (int i = 0; i < Constants.SPELL_CIRCLE_SIZE; ++i) {
                final boolean isSelected = i == selectPos;
                // Render the empty spell slot.
                final int x = getSlotX(width / 2, i);
                final int y = getSlotY(height / 2, i);
                graphics.blit(TEXTURE, x, y, isSelected ? 20 : 0, 128, SPELL_SLOT_LEN, SPELL_SLOT_LEN);
                // Render the spell texture.
                final ISpellType spell = PlayerUtil.getSpellAt(ClientProxy.MC.player, i);
                if (spell != null) {
                    graphics.blit(spell.getSpellTexture(), x + 2, y + 2, 0, 0, 16, 16, 16, 16);

                    // Render CD.
                    final double progress = PlayerUtil.getSpellCDValue(ClientProxy.MC.player, spell);
                    if (progress > 0) {
                        RenderSystem.enableBlend();
                        final int CDBarLen = Mth.clamp((int) (progress * SPELL_SLOT_LEN), 1, SPELL_SLOT_LEN);
                        graphics.blit(TEXTURE, x, y + SPELL_SLOT_LEN - CDBarLen, 150, 130, SPELL_SLOT_LEN, CDBarLen);
                    }

                    if (isSelected) {
                        MutableComponent text = spell.getComponent().append("-").append(SpellManager.getCostComponent(spell.getConsumeMana()));
                        if (progress > 0) {
                            text = text.append("-").append(SpellManager.getCDComponent((int) (spell.getCooldown() * progress)));
                        }
                        RenderUtil.renderCenterScaledText(graphics.pose(), text, width >> 1, (height + CIRCLE_LEN + 10) >> 1, 1F, ColorHelper.WHITE.rgb(), ColorHelper.BLACK.rgb());
                    }
                }
            }
            RenderHelper.pop(graphics);
            ClientUtil.pop();
        }
    }

    public static void renderPrepareBar(ForgeGui gui, GuiGraphics graphics, float partialTick, int width, int height) {
        if (ClientUtil.canRenderOverlay() && SpellManager.isPreparing(ClientUtil.player())) {
            ClientUtil.push("renderPrepareBar");
            final int leftPos = (width - CIRCLE_LEN) >> 1;
            final int topPos = (height - CIRCLE_LEN) >> 1;
            ClientUtil.pop();
        }
    }

}
