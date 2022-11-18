package hungteen.immortal.client.event.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.ClientProxy;
import hungteen.htlib.client.RenderHelper;
import hungteen.htlib.util.Pair;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.immortal.api.registry.ISpell;
import hungteen.immortal.client.ClientDatas;
import hungteen.immortal.client.ClientHandler;
import hungteen.immortal.common.SpellManager;
import hungteen.immortal.common.blockentity.SmithingArtifactBlockEntity;
import hungteen.immortal.common.item.ImmortalToolActions;
import hungteen.immortal.utils.Colors;
import hungteen.immortal.utils.Constants;
import hungteen.immortal.utils.PlayerUtil;
import hungteen.immortal.utils.Util;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-10 10:13
 **/
public class OverlayHandler {

    private static final ResourceLocation SPELL_CIRCLE = Util.prefix("textures/gui/overlay/spell_circle.png");
    private static final ResourceLocation OVERLAY = Util.prefix("textures/gui/overlay/overlay.png");
    private static final int CIRCLE_LEN = 128;
    private static final int CIRCLE_RADIUS = 48;
    private static final int INNER_LEN = 40;
    private static final int SPELL_SLOT_LEN = 20;
    private static final int MANA_BAR_LEN = 182;
    private static final int MANA_BAR_HEIGHT = 5;
    private static final int SMITHING_BAR_LEN = 65;
    private static final int SMITHING_BAR_HEIGHT = 10;
    private static List<Pair<Integer, Integer>> SpellSlots = new ArrayList<>();

    static {
        SpellSlots.clear();
        for (int i = 0; i < Constants.SPELL_NUM_EACH_PAGE; ++i) {
            final double alpha = 2 * Mth.PI / 8 * i;
            final int x = (int) (Math.sin(alpha) * CIRCLE_RADIUS) - SPELL_SLOT_LEN / 2 + 1;
            final int y = (int) (-Math.cos(alpha) * CIRCLE_RADIUS) - SPELL_SLOT_LEN / 2 + 1;
            SpellSlots.add(Pair.of(x, y));
        }
    }

    /**
     *
     */
    public static void renderSpellCircle(PoseStack stack, int height, int width) {
        final int leftPos = (width - CIRCLE_LEN) >> 1;
        final int topPos = (height - CIRCLE_LEN) >> 1;
        final int selectPos = PlayerUtil.getSpellSelectedPosition(ClientProxy.MC.player);
        stack.pushPose();
        RenderSystem.enableBlend();
        RenderHelper.setTexture(SPELL_CIRCLE);
        ClientProxy.MC.gui.blit(stack, leftPos, topPos, 0, 0, CIRCLE_LEN, CIRCLE_LEN);
        // Render Mid Selected White Circle.
        ClientProxy.MC.gui.blit(stack, (width - INNER_LEN) >> 1, (height - INNER_LEN) >> 1, (selectPos % 4) * INNER_LEN, selectPos < 4 ? 160 : 200, INNER_LEN, INNER_LEN);
        // Render Spell Slots.
        for (int i = 0; i < Constants.SPELL_NUM_EACH_PAGE; ++i) {
            final boolean isSelected = i == selectPos;
            // Render the empty spell slot.
            RenderHelper.setTexture(SPELL_CIRCLE);
            final int x = SpellSlots.get(i).getFirst() + width / 2;
            final int y = SpellSlots.get(i).getSecond() + height / 2;
            ClientProxy.MC.gui.blit(stack, x, y, isSelected ? 20 : 0, 128, SPELL_SLOT_LEN, SPELL_SLOT_LEN);
            // Render the spell texture.
            final ISpell spell = PlayerUtil.getSpellAt(ClientProxy.MC.player, i);
            if (spell != null) {
                RenderHelper.setTexture(spell.getSpellTexture());
                GuiComponent.blit(stack, x + 2, y + 2, 0, 0, 16, 16, 16, 16);

                // Render CD.
                final double progress = PlayerUtil.getSpellCDValue(ClientProxy.MC.player, spell);
                if (progress > 0) {
                    RenderHelper.setTexture(SPELL_CIRCLE);
                    RenderSystem.enableBlend();
                    final int CDBarLen = Mth.clamp((int) (progress * SPELL_SLOT_LEN), 1, SPELL_SLOT_LEN);
                    ClientProxy.MC.gui.blit(stack, x, y + SPELL_SLOT_LEN - CDBarLen, 150, 130, SPELL_SLOT_LEN, CDBarLen);
                }

                if (isSelected) {
                    String text = spell.getComponent().getString() + " - " + SpellManager.getCostComponent(spell.getStartMana()).getString();
                    if (progress > 0) {
                        text = text + " - " + SpellManager.getCDComponent((int) (spell.getDuration() * progress)).getString();
                    }
                    RenderHelper.drawCenteredScaledString(stack, ClientProxy.MC.font, text, width >> 1, (height + CIRCLE_LEN + 10) >> 1, ColorHelper.WHITE, 1F);
                }
            }
        }
        stack.popPose();
    }

    public static void renderSpiritualMana(PoseStack poseStack, int screenHeight, int screenWidth) {
        ClientProxy.MC.getProfiler().push("spiritualManaBar");
        RenderHelper.setTexture(OVERLAY);
        final int x = screenWidth / 2 - 91;
        final int y = screenHeight - 32 + 3;
        final int currentMana = PlayerUtil.getSpiritualMana(ClientProxy.MC.player);
        final int maxMana = PlayerUtil.getFullSpiritualMana(ClientProxy.MC.player);
        final int cultivation = PlayerUtil.getCultivation(ClientProxy.MC.player);
        ClientProxy.MC.gui.blit(poseStack, x, y, 0, 0, MANA_BAR_LEN, MANA_BAR_HEIGHT);
        if(maxMana > 0){
            final int backManaLen = MathHelper.getBarLen(currentMana, maxMana, MANA_BAR_LEN - 2);
            ClientProxy.MC.gui.blit(poseStack, x + 1, y, 1, 5, backManaLen, MANA_BAR_HEIGHT);
            if(currentMana > maxMana && cultivation > maxMana){
                final int barLen = MathHelper.getBarLen(currentMana - maxMana, cultivation - maxMana, MANA_BAR_LEN - 2);
                ClientProxy.MC.gui.blit(poseStack, x + 1, y + 1, 1, 16, barLen, MANA_BAR_HEIGHT - 2);
                if(ClientDatas.ManaWarningTick == 0){
                    ClientDatas.ManaWarningTick = Constants.MANA_WARNING_CD;
                } else{
                    -- ClientDatas.ManaWarningTick;
                }
                if(ClientDatas.ManaWarningTick > (Constants.MANA_WARNING_CD >> 1)){
                    ClientProxy.MC.gui.blit(poseStack, x, y, 0, 10, MANA_BAR_LEN, MANA_BAR_HEIGHT);
                }
            }
        }

        ClientProxy.MC.getProfiler().pop();

        ClientProxy.MC.getProfiler().push("spiritualValue");
        final float scale = 1;
        final String text = currentMana + " / " + maxMana;
        RenderHelper.drawCenteredScaledString(poseStack, ClientProxy.MC.font, text, (screenWidth >> 1), y - 6 - 1, ColorHelper.BLACK, scale);
        RenderHelper.drawCenteredScaledString(poseStack, ClientProxy.MC.font, text, (screenWidth >> 1), y - 6 + 1, ColorHelper.BLACK, scale);
        RenderHelper.drawCenteredScaledString(poseStack, ClientProxy.MC.font, text, (screenWidth >> 1) + 1, y - 6, ColorHelper.BLACK, scale);
        RenderHelper.drawCenteredScaledString(poseStack, ClientProxy.MC.font, text, (screenWidth >> 1) - 1, y - 6, ColorHelper.BLACK, scale);
        RenderHelper.drawCenteredScaledString(poseStack, ClientProxy.MC.font, text, (screenWidth >> 1), y - 6, Colors.SPIRITUAL_MANA, scale);
        ClientProxy.MC.getProfiler().pop();
    }

    public static void renderSmithingBar(PoseStack poseStack, int screenHeight, int screenWidth) {
        boolean quit = true;
        ItemStack stack = ItemStack.EMPTY;
        if(Objects.requireNonNull(ClientProxy.MC.player).getMainHandItem().canPerformAction(ImmortalToolActions.ARTIFACT_SMITHING)){
            stack = ClientProxy.MC.player.getMainHandItem();
        } else if(Objects.requireNonNull(ClientProxy.MC.player).getOffhandItem().canPerformAction(ImmortalToolActions.ARTIFACT_SMITHING)){
            stack = ClientProxy.MC.player.getOffhandItem();
        }
        if(!stack.isEmpty() && ! PlayerUtil.isInCD(ClientProxy.MC.player, stack.getItem()) && ClientProxy.MC.hitResult instanceof BlockHitResult && ((BlockHitResult) ClientProxy.MC.hitResult).getDirection() == Direction.UP){
            final BlockPos pos = ((BlockHitResult) ClientProxy.MC.hitResult).getBlockPos();
            final BlockEntity blockEntity = ClientProxy.MC.level.getBlockEntity(pos);
            if(blockEntity instanceof final SmithingArtifactBlockEntity entity){
                if(entity.canSmithing()){
                    quit = false;
                    if(! ClientDatas.StartSmithing){
                        ClientHandler.startSmithing(stack, entity);
                    }
                    ClientProxy.MC.getProfiler().push("smithingBar");
                    final int x = (screenWidth - SMITHING_BAR_LEN) >> 1;
                    final int y = (screenHeight >> 1) + 10;
                    final int len = MathHelper.getBarLen(entity.getCurrentSmithingValue(), entity.getRequireSmithingValue(), 61);
                    final int bestPos = MathHelper.getBarLen(entity.getBestSmithingPoint(), SmithingArtifactBlockEntity.MAX_PROGRESS_VALUE, 61);
                    final int currentPos = MathHelper.getBarLen((int)ClientDatas.SmithingProgress, SmithingArtifactBlockEntity.MAX_PROGRESS_VALUE, 61);
                    RenderHelper.setTexture(OVERLAY);
                    ClientProxy.MC.gui.blit(poseStack, x, y, 0, 20, SMITHING_BAR_LEN, SMITHING_BAR_HEIGHT);
                    ClientProxy.MC.gui.blit(poseStack, x + 2, y + 1, 2, 32, len, 2);
                    if(ClientDatas.BestPointDisplayTick <= Constants.DISPLAY_BEST_SMITHING_POINT_CD){
                        ClientProxy.MC.gui.blit(poseStack, x + 1 + bestPos, y, 69, 20, 3, 4);
                    }
                    ClientProxy.MC.gui.blit(poseStack, x + 1 + currentPos, y, 66, 20, 3, 4);
                    ClientProxy.MC.getProfiler().pop();
                }
            }
        }
        if(quit){
            ClientHandler.quitSmithing();
        }
    }

    public static boolean canRenderManaBar() {
        return canRenderOverlay() && (PlayerUtil.getSpiritualMana(ClientProxy.MC.player) > 0 || ClientDatas.ShowSpellCircle);
    }

    public static boolean canRenderOverlay(){
        return ClientProxy.MC.screen == null && ! ClientProxy.MC.options.hideGui && ClientProxy.MC.level != null && ClientProxy.MC.player != null && ! ClientProxy.MC.player.isSpectator();
    }

}
