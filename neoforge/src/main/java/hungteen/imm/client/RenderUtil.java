package hungteen.imm.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.util.ClientHelper;
import hungteen.htlib.client.util.RenderHelper;
import hungteen.htlib.client.util.TextRenderType;
import hungteen.imm.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.Nullable;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/10 15:47
 */
public class RenderUtil {

    public static final ResourceLocation WIDGETS = Util.get().guiTexture("widgets");
    public static final ResourceLocation EMPTY_ENTITY_TEXTURE = Util.get().entityTexture("empty");

    public static void renderScaledText(PoseStack stack, Component text, float x, float y, float scale, int color, int outlineColor){
        RenderHelper.renderScaledText(stack, ClientHelper.font(), IMMClientProxy.mc().renderBuffers().bufferSource(), text, x, y, scale, color, outlineColor, TextRenderType.NORMAL, LightTexture.FULL_BRIGHT);
    }
    public static void renderCenterScaledText(PoseStack stack, Component text, float x, float y, float scale, int color, int outlineColor){
        RenderHelper.renderCenterScaledText(stack, ClientHelper.font(), IMMClientProxy.mc().renderBuffers().bufferSource(), text, x, y, scale, color, outlineColor, TextRenderType.NORMAL, LightTexture.FULL_BRIGHT);
    }

    public static void renderEntityInInventoryFollowsMouse(GuiGraphics graphics, int posX, int posY, float scale, float deltaX, float deltaY, LivingEntity living) {
        float f = (float)Math.atan(deltaX / 40.0F);
        float f1 = (float)Math.atan(deltaY / 40.0F);
        // Forge: Allow passing in direct angle components instead of mouse position
        renderEntityInInventoryFollowsAngle(graphics, posX, posY, scale, f, f1, living);
    }

    public static void renderEntityInInventoryFollowsAngle(GuiGraphics graphics, int posX, int posY, float scale, float angleXComponent, float angleYComponent, LivingEntity living) {
        float f = angleXComponent;
        float f1 = angleYComponent;
        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float)Math.PI);
        Quaternionf quaternionf1 = (new Quaternionf()).rotateX(f1 * 20.0F * ((float)Math.PI / 180F));
        quaternionf.mul(quaternionf1);
        float f2 = living.yBodyRot;
        float f3 = living.getYRot();
        float f4 = living.getXRot();
        float f5 = living.yHeadRotO;
        float f6 = living.yHeadRot;
        living.yBodyRot = 180.0F + f * 20.0F;
        living.setYRot(180.0F + f * 40.0F);
        living.setXRot(-f1 * 20.0F);
        living.yHeadRot = living.getYRot();
        living.yHeadRotO = living.getYRot();
        renderEntityInInventory(graphics, posX, posY, scale, quaternionf, quaternionf1, living);
        living.yBodyRot = f2;
        living.setYRot(f3);
        living.setXRot(f4);
        living.yHeadRotO = f5;
        living.yHeadRot = f6;
    }

    public static void renderEntityInInventory(GuiGraphics graphics, int posX, int posY, float scale, Quaternionf q1, @Nullable Quaternionf q2, Entity entity) {
        graphics.pose().pushPose();
        graphics.pose().translate(posX, posY, 50.0D);
        graphics.pose().mulPose((new Matrix4f()).scaling(scale, scale, -scale));
        graphics.pose().mulPose(q1);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (q2 != null) {
            q2.conjugate();
            entityrenderdispatcher.overrideCameraOrientation(q2);
        }

        entityrenderdispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, graphics.pose(), graphics.bufferSource(), 15728880);
        });
        graphics.flush();
        entityrenderdispatcher.setRenderShadow(true);
        graphics.pose().popPose();
        Lighting.setupFor3DItems();
    }

    public static void commonTranslate(PoseStack stack, float scale){
        stack.scale(-1.0F, -1.0F, 1.0F);
        stack.scale(scale, scale, scale);
        stack.translate(0.0, -1.501, 0.0);
    }
}
