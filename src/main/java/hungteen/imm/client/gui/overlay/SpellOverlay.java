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
import hungteen.imm.util.MathUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.Util;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/27 15:12
 */
public class SpellOverlay {
    
    public static final IGuiOverlay SPELL_CIRCLE = SpellOverlay::renderSpellCircle;
    public static final IGuiOverlay PREPARE_SPELL = SpellOverlay::renderPreparedSpell;

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
                    renderSpellSlot(graphics, spell, x, y, true);

                    if (isSelected) {
                        final int level = PlayerUtil.getSpellLevel(ClientUtil.player(), spell);
                        MutableComponent text = SpellManager.spellName(spell, level).append(" - ").append(SpellManager.getCostComponent(spell.getConsumeMana()));
                        RenderUtil.renderCenterScaledText(graphics.pose(), text, width >> 1, (height + CIRCLE_LEN + 10) >> 1, 1F, ColorHelper.GREEN.rgb(), ColorHelper.BLACK.rgb());
                    }
                }
            }
            RenderHelper.pop(graphics);
            ClientUtil.pop();
        }
    }

    /**
     * Refer to {@link Gui#renderHotbar(float, GuiGraphics)}.
     */
    public static void renderPreparedSpell(ForgeGui gui, GuiGraphics graphics, float partialTick, int width, int height) {
        final ISpellType spell = PlayerUtil.getPreparingSpell(ClientUtil.player());
        if (ClientUtil.canRenderOverlay() && spell != null) {
            ClientUtil.push("renderPreparedSpell");
            final int x = ((width >> 1) - 91 - 26) - 2;
            final int y = (height - 16 - 3) - 22;
            graphics.blit(TEXTURE, x, y, 20, 128, SPELL_SLOT_LEN, SPELL_SLOT_LEN);
            renderSpellSlot(graphics, spell, x, y, true);
            ClientUtil.pop();
        }
    }

    private static void renderSpellSlot(GuiGraphics graphics, @NotNull ISpellType spell, int x, int y, boolean renderCD){
        graphics.blit(spell.getSpellTexture(), x + 2, y + 2, 0, 0, 16, 16, 16, 16);

        if(renderCD){
            final float progress = PlayerUtil.getSpellCDValue(ClientProxy.MC.player, spell);
            if (progress > 0) {
                final int CDBarLen = MathUtil.getBarLen(progress, SPELL_SLOT_LEN);
                graphics.fill(RenderType.guiOverlay(), x, y + SPELL_SLOT_LEN - CDBarLen, x + SPELL_SLOT_LEN, y + SPELL_SLOT_LEN, Integer.MAX_VALUE);
                graphics.blit(TEXTURE, x, y + SPELL_SLOT_LEN - CDBarLen, 150, 130, SPELL_SLOT_LEN, CDBarLen);
            }
        }
    }

}
