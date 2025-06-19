package hungteen.imm.client.render.entity.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import hungteen.imm.common.entity.misc.talisman.RangeEffectTalismanEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-02 14:45
 **/
public class RangeEffectTalismanRender<T extends RangeEffectTalismanEntity> extends EntityRenderer<T> {

    private final ItemRenderer itemRenderer;

    public RangeEffectTalismanRender(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        ItemStack itemstack = entity.getItem();
        BakedModel bakedmodel = this.itemRenderer.getModel(itemstack, entity.level(), null, entity.getId());
        float yOffset = Mth.sin(((float)entity.getExistTick() + partialTicks) / 10.0F) * 0.2F + 1F;
        final float sz = 0.5F;
        poseStack.scale(sz, sz, sz);
        poseStack.translate(0.0F, yOffset, 0.0F);
        float f3 = ((float)entity.getExistTick() + partialTicks) * Math.max(0.05F, (float) entity.getExistPercent());
        poseStack.mulPose(Axis.YP.rotation(f3));
        this.itemRenderer.render(itemstack, ItemDisplayContext.NONE, false, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T p_114482_) {
        return null;
    }

}
