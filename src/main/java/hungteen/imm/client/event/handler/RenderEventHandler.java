package hungteen.imm.client.event.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import hungteen.htlib.client.util.RenderHelper;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.client.ClientDatas;
import hungteen.imm.client.ClientProxy;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.client.RenderUtil;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.util.*;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-10 10:13
 **/
public class RenderEventHandler {

    private static final ResourceLocation SPELL_CIRCLE = Util.prefix("textures/gui/overlay/spell_circle.png");
    private static final ResourceLocation OVERLAY = Util.prefix("textures/gui/overlay/overlay.png");
    private static final ResourceLocation ELEMENTS = Util.get().guiTexture("elements");
    private static final RenderType ELEMENTS_RENDER_TYPE = RenderType.energySwirl(ELEMENTS, 0, 0);;

    private static final int CIRCLE_LEN = 128;
    private static final int CIRCLE_RADIUS = 48;
    private static final int INNER_LEN = 40;
    private static final int SPELL_SLOT_LEN = 20;
    private static final int MANA_BAR_LEN = 182;
    private static final int MANA_BAR_HEIGHT = 5;
    private static final int ELEMENT_LEN = 9;
    private static final int ELEMENT_GUI_INTERVAL = 3;
    private static final float ELEMENT_INTERVAL = 0.2F;
    private static final int SMITHING_BAR_LEN = 65;
    private static final int SMITHING_BAR_HEIGHT = 10;
    private static List<Pair<Integer, Integer>> SpellSlots = new ArrayList<>();

    static {
        for (int i = 0; i < Constants.SPELL_CIRCLE_SIZE; ++i) {
            final double alpha = 2 * Mth.PI / 8 * i;
            final int x = (int) (Math.sin(alpha) * CIRCLE_RADIUS) - SPELL_SLOT_LEN / 2 + 1;
            final int y = (int) (-Math.cos(alpha) * CIRCLE_RADIUS) - SPELL_SLOT_LEN / 2 + 1;
            SpellSlots.add(Pair.of(x, y));
        }
    }

    public static void renderSpellCircle(GuiGraphics graphics, int height, int width) {
        ClientUtil.push("renderSpellCircle");
        final int leftPos = (width - CIRCLE_LEN) >> 1;
        final int topPos = (height - CIRCLE_LEN) >> 1;
        final int selectPos = ClientDatas.lastSelectedPosition;
        RenderHelper.push(graphics);
        RenderSystem.enableBlend();
        graphics.blit(SPELL_CIRCLE, leftPos, topPos, 0, 0, CIRCLE_LEN, CIRCLE_LEN);
        // Render Mid Selected White Circle.
        if (selectPos != -1) {
            graphics.blit(SPELL_CIRCLE, (width - INNER_LEN) >> 1, (height - INNER_LEN) >> 1, (selectPos % 4) * INNER_LEN, selectPos < 4 ? 160 : 200, INNER_LEN, INNER_LEN);
        }
        // Render Spell Slots.
        for (int i = 0; i < Constants.SPELL_CIRCLE_SIZE; ++i) {
            final boolean isSelected = i == selectPos;
            // Render the empty spell slot.
            final int x = SpellSlots.get(i).getFirst() + width / 2;
            final int y = SpellSlots.get(i).getSecond() + height / 2;
            graphics.blit(SPELL_CIRCLE, x, y, isSelected ? 20 : 0, 128, SPELL_SLOT_LEN, SPELL_SLOT_LEN);
            // Render the spell texture.
            final ISpellType spell = PlayerUtil.getSpellAt(ClientProxy.MC.player, i);
            if (spell != null) {
                graphics.blit(spell.getSpellTexture(), x + 2, y + 2, 0, 0, 16, 16, 16, 16);

                // Render CD.
                final double progress = PlayerUtil.getSpellCDValue(ClientProxy.MC.player, spell);
                if (progress > 0) {
                    RenderSystem.enableBlend();
                    final int CDBarLen = Mth.clamp((int) (progress * SPELL_SLOT_LEN), 1, SPELL_SLOT_LEN);
                    graphics.blit(SPELL_CIRCLE, x, y + SPELL_SLOT_LEN - CDBarLen, 150, 130, SPELL_SLOT_LEN, CDBarLen);
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

    public static void renderSpiritualMana(GuiGraphics graphics, int screenHeight, int screenWidth) {
        ClientUtil.push("spiritualManaBar");
        final int x = screenWidth / 2 - 91;
        final int y = screenHeight - 32 + 3;
        final float currentMana = PlayerUtil.getMana(ClientProxy.MC.player);
        final float maxMana = PlayerUtil.getFullMana(ClientProxy.MC.player);
        final float limitMana = PlayerUtil.getLimitMana(ClientProxy.MC.player);
        graphics.blit(OVERLAY, x, y, 0, 0, MANA_BAR_LEN, MANA_BAR_HEIGHT);
        if (maxMana > 0) {
            final int backManaLen = MathUtil.getBarLen(currentMana, maxMana, MANA_BAR_LEN - 2);
            graphics.blit(OVERLAY, x + 1, y, 1, 5, backManaLen, MANA_BAR_HEIGHT);
            if (currentMana > maxMana && limitMana > maxMana) {
                final int barLen = MathUtil.getBarLen(currentMana - maxMana, limitMana - maxMana, MANA_BAR_LEN - 2);
                graphics.blit(OVERLAY, x + 1, y + 1, 1, 16, barLen, MANA_BAR_HEIGHT - 2);
                if (ClientDatas.ManaWarningTick == 0) {
                    ClientDatas.ManaWarningTick = Constants.MANA_WARNING_CD;
                } else {
                    --ClientDatas.ManaWarningTick;
                }
                if (ClientDatas.ManaWarningTick > (Constants.MANA_WARNING_CD >> 1)) {
                    graphics.blit(OVERLAY, x, y, 0, 10, MANA_BAR_LEN, MANA_BAR_HEIGHT);
                }
            }
        }

        ClientUtil.pop();

        ClientUtil.push("spiritualValue");
        final float scale = 1;
        final Component text = Component.literal(currentMana + " / " + maxMana);
//        RenderHelper.renderCenterScaledText(poseStack, ClientProxy.MC.font, text, (screenWidth >> 1), y - 6 - 1, ColorHelper.BLACK, scale);
//        RenderHelper.renderCenterScaledText(poseStack, ClientProxy.MC.font, text, (screenWidth >> 1), y - 6 + 1, ColorHelper.BLACK, scale);
//        RenderHelper.renderCenterScaledText(poseStack, ClientProxy.MC.font, text, (screenWidth >> 1) + 1, y - 6, ColorHelper.BLACK, scale);
//        RenderHelper.renderCenterScaledText(poseStack, ClientProxy.MC.font, text, (screenWidth >> 1) - 1, y - 6, ColorHelper.BLACK, scale);
//        RenderHelper.renderCenterScaledText(poseStack, ClientProxy.MC.font, text, (screenWidth >> 1), y - 6, Colors.SPIRITUAL_MANA, scale);
        RenderUtil.renderCenterScaledText(graphics.pose(), text, (screenWidth >> 1), y - 6, scale, Colors.SPIRITUAL_MANA, ColorHelper.BLACK.rgb());
        ClientUtil.pop();
    }

    /**
     * Above {@link Gui#renderSelectedItemName(GuiGraphics)}.
     */
    public static void renderElements(GuiGraphics graphics, int height, int width) {
        ClientUtil.push("renderElements");
        int topPos = height - 59 - 12;
        if (! ClientUtil.getMode().map(MultiPlayerGameMode::canHurtPlayer).orElse(false)) {
            topPos += 14;
        }
        final Entity entity = PlayerHelper.getClientPlayer().get();
        final Map<Elements, Float> elements = ElementManager.getElements(entity);
        final int cnt = elements.size();
        final int barWidth = cnt * ELEMENT_LEN + (cnt - 1) * ELEMENT_GUI_INTERVAL;
        int startX = (width - barWidth) >> 1;
        if (!elements.isEmpty()) {
            RenderHelper.push(graphics);
            RenderSystem.enableBlend();
            for (Elements element : Elements.values()) {
                if (!elements.containsKey(element)) continue;
                final float amount = elements.get(element);
                final boolean robust = (elements.get(element) > 0);
                final boolean warn = ElementManager.needWarn(entity, element, robust, Math.abs(amount));
                if (!warn || ElementManager.notDisappear(entity)) {
                    graphics.blit(ELEMENTS, startX, topPos, 10 * element.ordinal(), 0, ELEMENT_LEN, ELEMENT_LEN);
                    if (robust && ElementManager.displayRobust(entity)) {
                        graphics.blit(ELEMENTS, startX, topPos, 10 * element.ordinal(), 10, ELEMENT_LEN, ELEMENT_LEN);
                    }
                }
                startX += ELEMENT_LEN + ELEMENT_GUI_INTERVAL;
            }
            RenderHelper.pop(graphics);
        }
        ClientUtil.pop();
    }

    public static void renderEntityElements(Entity entity, EntityRenderer<?> renderer, PoseStack stack, MultiBufferSource bufferSource, int packedLight) {
        final double distance = renderer.entityRenderDispatcher.distanceToSqr(entity);
        final Player player = PlayerHelper.getClientPlayer().get();
        if (player != entity && ElementManager.canSeeElements(player, entity, distance)) {
            final float scale = ELEMENT_LEN * 1F / 16 / 2F;
            final Map<Elements, Float> elements = ElementManager.getElements(entity);
            final int cnt = elements.size();
            final float barWidth = cnt + (cnt - 1) * ELEMENT_INTERVAL;
            stack.pushPose();
            stack.translate(0, entity.getBbHeight() + 0.6F, 0.0F);
            stack.mulPose(renderer.entityRenderDispatcher.cameraOrientation());
            stack.scale(-scale, -scale, scale);
            RenderSystem.enableBlend();
            if(! elements.isEmpty()){
                int tmp = 0;
                for (Elements element : Elements.values()) {
                    if(! elements.containsKey(element)) continue;
                    final float amount = elements.get(element);
                    final boolean robust = (elements.get(element) > 0);
                    final boolean warn = ElementManager.needWarn(entity, element, robust, Math.abs(amount));
                    stack.pushPose();
                    stack.translate(- barWidth / 2 + 0.5F + (1 + ELEMENT_INTERVAL) * tmp, 0, 0);
                    if(! warn || ElementManager.notDisappear(entity)){
                        final int offsetY = (robust && ElementManager.displayRobust(entity)) ? 10 : 0;
                        float alpha = (float) Math.sin(entity.tickCount * 0.02 % Math.PI);
                        alpha = 1F;
                        renderIcon(stack, bufferSource, 10 * element.ordinal(), offsetY, packedLight, alpha);
                    }
                    stack.popPose();
                    ++ tmp;
                }
            }
            stack.popPose();
        }
    }

    private static void renderIcon(PoseStack stack, MultiBufferSource bufferSource, int offsetX, int offsetY, int packedLight, float alpha) {
        stack.pushPose();
        final float sx = offsetX / 256F;
        final float sy = offsetY / 256F;
        final float dx = (offsetX + ELEMENT_LEN) / 256F;
        final float dy = (offsetY + ELEMENT_LEN) / 256F;
        final PoseStack.Pose posestack$pose = stack.last();
        final Matrix4f matrix4f = posestack$pose.pose();
        final Matrix3f matrix3f = posestack$pose.normal();
//        RenderType  type = RenderType.itemEntityTranslucentCull(ELEMENTS);
//        final VertexConsumer vertexconsumer = bufferSource.getBuffer(type);
        final VertexConsumer vertexconsumer = bufferSource.getBuffer(ELEMENTS_RENDER_TYPE);
        vertex(vertexconsumer, matrix4f, matrix3f, -0.5F, -0.5F, 0, 1F, 1F, 1F, alpha, sx, sy, packedLight);
        vertex(vertexconsumer, matrix4f, matrix3f, -0.5F, 0.5F, 0, 1F, 1F, 1F, alpha, sx, dy, packedLight);
        vertex(vertexconsumer, matrix4f, matrix3f, 0.5F, 0.5F, 0, 1F, 1F, 1F, alpha, dx, dy, packedLight);
        vertex(vertexconsumer, matrix4f, matrix3f, 0.5F, -0.5F, 0, 1F, 1F, 1F, alpha, dx, sy, packedLight);
        stack.popPose();
    }

    private static void vertex(VertexConsumer consumer, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, float z, float r, float g, float b, float a, float u, float v, int packedLight) {
        consumer.vertex(matrix4f, x, y, z).color(r, g, b, a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static void renderSmithingBar(GuiGraphics graphics, int screenHeight, int screenWidth) {
//        boolean quit = true;
//        ItemStack stack = ItemStack.EMPTY;
//        if(Objects.requireNonNull(ClientProxy.MC.player).getMainHandItem().canPerformAction(ImmortalToolActions.ARTIFACT_SMITHING)){
//            stack = ClientProxy.MC.player.getMainHandItem();
//        } else if(Objects.requireNonNull(ClientProxy.MC.player).getOffhandItem().canPerformAction(ImmortalToolActions.ARTIFACT_SMITHING)){
//            stack = ClientProxy.MC.player.getOffhandItem();
//        }
//        if(!stack.isEmpty() && ! PlayerUtil.isInCD(ClientProxy.MC.player, stack.getItem()) && ClientProxy.MC.hitResult instanceof BlockHitResult && ((BlockHitResult) ClientProxy.MC.hitResult).getDirection() == Direction.UP){
//            final BlockPos pos = ((BlockHitResult) ClientProxy.MC.hitResult).getBlockPos();
//            final BlockEntity blockEntity = ClientProxy.MC.level.getBlockEntity(pos);
//            if(blockEntity instanceof final SmithingArtifactBlockEntity entity){
//                if(entity.canSmithing()){
//                    quit = false;
//                    if(! ClientDatas.StartSmithing){
//                        ClientHandler.startSmithing(stack, entity);
//                    }
//                    ClientProxy.MC.getProfiler().push("smithingBar");
//                    final int x = (screenWidth - SMITHING_BAR_LEN) >> 1;
//                    final int y = (screenHeight >> 1) + 10;
//                    final int len = MathHelper.getBarLen(entity.getCurrentSmithingValue(), entity.getRequireSmithingValue(), 61);
//                    final int bestPos = MathHelper.getBarLen(entity.getBestSmithingPoint(), SmithingArtifactBlockEntity.MAX_PROGRESS_VALUE, 61);
//                    final int currentPos = MathHelper.getBarLen((int)ClientDatas.SmithingProgress, SmithingArtifactBlockEntity.MAX_PROGRESS_VALUE, 61);
//                    RenderHelper.setTexture(OVERLAY);
//                    GuiComponent.blit(poseStack, x, y, 0, 20, SMITHING_BAR_LEN, SMITHING_BAR_HEIGHT);
//                    GuiComponent.blit(poseStack, x + 2, y + 1, 2, 32, len, 2);
//                    if(ClientDatas.BestPointDisplayTick <= Constants.DISPLAY_BEST_SMITHING_POINT_CD){
//                        GuiComponent.blit(poseStack, x + 1 + bestPos, y, 69, 20, 3, 4);
//                    }
//                    GuiComponent.blit(poseStack, x + 1 + currentPos, y, 66, 20, 3, 4);
//                    ClientProxy.MC.getProfiler().pop();
//                }
//            }
//        }
//        if(quit){
//            ClientHandler.quitSmithing();
//        }
    }

    public static boolean canRenderManaBar() {
        return canRenderOverlay() && (PlayerUtil.getMana(ClientProxy.MC.player) > 0 || ClientDatas.ShowSpellCircle);
    }

    public static boolean canRenderOverlay() {
        return ClientProxy.MC.screen == null && !ClientProxy.MC.options.hideGui && ClientProxy.MC.level != null && ClientProxy.MC.player != null && !ClientProxy.MC.player.isSpectator();
    }

}
