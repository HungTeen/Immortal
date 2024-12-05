package hungteen.imm.client.render.level;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.client.gui.overlay.ElementOverlay;
import hungteen.imm.client.render.IMMRenderType;
import hungteen.imm.client.util.ClientUtil;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Map;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-10 10:13
 **/
public class ElementRenderer {

    private static final int DISAPPEAR_WARN_CD = 50;
    private static final int DISAPPEAR_WARN_AMOUNT = 5;
    private static final int DISAPPEAR_CD = 5;
    private static final int DISPLAY_ROBUST_CD = 10;
    private static final ResourceLocation ELEMENTS = ElementOverlay.ELEMENTS;
    private static final RenderType ELEMENTS_RENDER_TYPE = IMMRenderType.elementIcon(ELEMENTS, 0, 0);;
    private static final int ELEMENT_LEN = ElementOverlay.ELEMENT_LEN;
    private static final float ELEMENT_INTERVAL = 0.2F;

    public static void renderEntityElements(Entity entity, EntityRenderer<?> renderer, PoseStack stack, MultiBufferSource bufferSource, int packedLight) {
        if(! ElementManager.displayElementAboveHead(entity)){
            return;
        }
        final double distance = renderer.entityRenderDispatcher.distanceToSqr(entity);
        final Player player = PlayerHelper.getClientPlayer().get();
        if (player != entity && canSeeElements(player, entity, distance)) {
            final float scale = ELEMENT_LEN * 1F / 16 / 2F;
            final Map<Element, Float> elements = ElementManager.getElements(entity);
            final List<Element> list = PlayerUtil.filterElements(ClientUtil.player(), elements.keySet().stream().toList());
            final int cnt = list.size();
            final float barWidth = cnt + (cnt - 1) * ELEMENT_INTERVAL;
            stack.pushPose();
            stack.translate(0, entity.getBbHeight() + 0.6F, 0.0F);
            stack.mulPose(renderer.entityRenderDispatcher.cameraOrientation());
            stack.scale(-scale, -scale, scale);
            RenderSystem.enableBlend();
            if(! list.isEmpty()){
                int tmp = 0;
                for (Element element : list) {
                    if(! elements.containsKey(element)) {
                        continue;
                    }
                    final float amount = elements.get(element);
                    final boolean robust = (elements.get(element) > 0);
                    final boolean warn = needWarn(entity, element, robust, Math.abs(amount));
                    stack.pushPose();
                    stack.translate(- barWidth / 2 + 0.5F + (1 + ELEMENT_INTERVAL) * tmp, 0, 0);
                    if(! warn || notDisappear(entity)){
                        final int offsetY = (robust && displayRobust(entity)) ? 10 : 0;
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
        final PoseStack.Pose pose = stack.last();
        final Matrix4f matrix4f = pose.pose();

//        final VertexConsumer builder = bufferSource.getBuffer(ELEMENTS_RENDER_TYPE);
        final VertexConsumer builder = bufferSource.getBuffer(ELEMENTS_RENDER_TYPE);
        vertex(builder, matrix4f, pose, -0.5F, -0.5F, 0, 1F, 1F, 1F, alpha, sx, sy, packedLight);
        vertex(builder, matrix4f, pose, -0.5F, 0.5F, 0, 1F, 1F, 1F, alpha, sx, dy, packedLight);
        vertex(builder, matrix4f, pose, 0.5F, 0.5F, 0, 1F, 1F, 1F, alpha, dx, dy, packedLight);
        vertex(builder, matrix4f, pose, 0.5F, -0.5F, 0, 1F, 1F, 1F, alpha, dx, sy, packedLight);

        stack.popPose();
    }

    private static void vertex(VertexConsumer consumer, Matrix4f matrix4f, PoseStack.Pose pose, float x, float y, float z, float r, float g, float b, float a, float u, float v, int packedLight) {
        consumer.addVertex(matrix4f, x, y, z)
                .setColor(r, g, b, a)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0.0F, 0.5F, 0.0F)
        ;
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

    public static boolean displayRobust(Entity entity) {
        return (entity.tickCount % (DISPLAY_ROBUST_CD << 1)) < DISPLAY_ROBUST_CD;
    }

    public static boolean notDisappear(Entity entity) {
        return (entity.tickCount % (DISAPPEAR_CD << 1)) < DISAPPEAR_CD;
    }

    /**
     * Amount need below threshold and last time is less than threshold.
     */
    public static boolean needWarn(Entity entity, Element element, boolean robust, float amount) {
        return (amount < DISAPPEAR_WARN_AMOUNT) && (amount / ElementManager.getDecayAmount(entity, element, robust) < DISAPPEAR_WARN_CD);
    }

    public static boolean canSeeElements(Player player, Entity entity, double distanceSqr) {
        return distanceSqr < 1000;
    }

}
