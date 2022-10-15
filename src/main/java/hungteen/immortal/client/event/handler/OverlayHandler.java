package hungteen.immortal.client.event.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.ClientProxy;
import hungteen.htlib.client.RenderUtil;
import hungteen.htlib.client.gui.RecipeRenderManager;
import hungteen.htlib.util.ColorUtil;
import hungteen.htlib.util.MathUtil;
import hungteen.htlib.util.Pair;
import hungteen.immortal.SpellManager;
import hungteen.immortal.api.interfaces.ISpell;
import hungteen.immortal.client.event.OverlayEvents;
import hungteen.immortal.utils.Constants;
import hungteen.immortal.utils.PlayerUtil;
import hungteen.immortal.utils.Util;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-10 10:13
 **/
public class OverlayHandler {

    private static final ResourceLocation SPELL_CIRCLE = Util.prefix("textures/gui/overlay/spell_circle.png");
    private static final int CIRCLE_LEN = 128;
    private static final int CIRCLE_RADIUS = 48;
    private static final int INNER_LEN = 40;
    private static final int SPELL_SLOT_LEN = 20;
    private static List<Pair<Integer, Integer>> SpellSlots = new ArrayList<>();

    static {
        SpellSlots.clear();
        for(int i = 0; i < Constants.SPELL_NUM_EACH_PAGE; ++ i) {
            final double alpha = 2 * Mth.PI / 8 * i;
            final int x = (int) (Math.sin(alpha) * CIRCLE_RADIUS)  - SPELL_SLOT_LEN / 2 + 1;
            final int y = (int) (-Math.cos(alpha) * CIRCLE_RADIUS) - SPELL_SLOT_LEN / 2 + 1;
            SpellSlots.add(Pair.of(x, y));
        }
    }

    /**
     * {@link OverlayEvents#onPostRenderOverlay(net.minecraftforge.client.event.RenderGameOverlayEvent.Post)}
     */
    public static void renderSpellCircle(PoseStack stack, int width, int height) {
        final int leftPos = (width - CIRCLE_LEN) >> 1;
        final int topPos = (height - CIRCLE_LEN) >> 1;
        final int selectPos = PlayerUtil.getSpellSelectedPosition(ClientProxy.MC.player);
        stack.pushPose();
        RenderSystem.enableBlend();
        RenderUtil.setTexture(SPELL_CIRCLE);
        ClientProxy.MC.gui.blit(stack, leftPos, topPos, 0, 0, CIRCLE_LEN, CIRCLE_LEN);
        // Render Mid Selected White Circle.
        ClientProxy.MC.gui.blit(stack, (width - INNER_LEN) >> 1, (height - INNER_LEN) >> 1, (selectPos % 4) * INNER_LEN, selectPos < 4 ? 160 : 200, INNER_LEN, INNER_LEN);
        // Render Spell Slots.
        for(int i = 0; i < Constants.SPELL_NUM_EACH_PAGE; ++ i){
            final boolean isSelected = i == selectPos;
            // Render the empty spell slot.
            RenderUtil.setTexture(SPELL_CIRCLE);
            final int x = SpellSlots.get(i).getFirst() + width / 2;
            final int y = SpellSlots.get(i).getSecond() + height / 2;
            ClientProxy.MC.gui.blit(stack, x, y, isSelected ? 20 : 0, 128, SPELL_SLOT_LEN, SPELL_SLOT_LEN);
            // Render the spell texture.
            final ISpell spell = PlayerUtil.getSpellAt(ClientProxy.MC.player, i);
            if (spell != null) {
                RenderUtil.setTexture(spell.getSpellTexture());
                ClientProxy.MC.gui.blit(stack, x + 2, y + 2, 0, 0, 16, 16, 16, 16);

                // Render CD.
                final double progress = PlayerUtil.getSpellCDValue(ClientProxy.MC.player, spell);
                if(progress > 0){
                    RenderUtil.setTexture(SPELL_CIRCLE);
                    RenderSystem.enableBlend();
                    final int CDBarLen = Mth.clamp((int) (progress * SPELL_SLOT_LEN), 1, SPELL_SLOT_LEN);
                    ClientProxy.MC.gui.blit(stack, x, y + SPELL_SLOT_LEN - CDBarLen, 150, 130, SPELL_SLOT_LEN, CDBarLen);
                }

                if(isSelected){
                    String text = spell.getComponent().getString() + " - " + SpellManager.getCostComponent(spell.getStartMana()).getString();
                    if(progress > 0){
                        text = text + " - " + SpellManager.getCDComponent((int) (spell.getDuration() * progress)).getString();
                    }
                    RenderUtil.drawCenteredScaledString(stack, ClientProxy.MC.font, text, width >> 1, (height + CIRCLE_LEN + 10) >> 1  , ColorUtil.WHITE, 1F);
                }
            }
        }
        stack.popPose();
    }

}
