package hungteen.imm.client.render.entity.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import hungteen.imm.common.entity.misc.FlyingItemEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-02 14:45
 **/
public class FlyingItemEntityRender extends EntityRenderer<FlyingItemEntity> {

    private final ItemRenderer itemRenderer;

    public FlyingItemEntityRender(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(FlyingItemEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn,
                       MultiBufferSource bufferIn, int packedLightIn) {
        final ItemStack stack = entityIn.getItemStack();
        final boolean isBlock = stack.getItem() instanceof BlockItem;
        matrixStackIn.pushPose();
        BakedModel bakedmodel = this.itemRenderer.getModel(stack, entityIn.level(), (LivingEntity) null, entityIn.getId());
        final float sz = 1F;
        matrixStackIn.scale(sz, sz, sz);
        matrixStackIn.translate(0, isBlock ? 0.5 : 0.1, 0);
        final float direction = entityIn.getControllingPassenger() == null ? 0 : entityIn.getControllingPassenger().getYRot();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - direction + (isBlock ? 0 : 45)));
        matrixStackIn.mulPose(Axis.XN.rotationDegrees(isBlock ? 0 : 90));

        matrixStackIn.pushPose();
        this.itemRenderer.render(stack, ItemDisplayContext.NONE, false, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, bakedmodel);
        matrixStackIn.popPose();

        matrixStackIn.popPose();

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(FlyingItemEntity p_114482_) {
        return null;
    }

}
