package hungteen.immortal.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.immortal.client.model.entity.GrassCarpModel;
import hungteen.immortal.common.entity.creature.GrassCarp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemStack;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 21:54
 **/
public class GrassCarpItemLayer<T extends GrassCarp> extends RenderLayer<T, GrassCarpModel<T>> {

    public GrassCarpItemLayer(RenderLayerParent<T, GrassCarpModel<T>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, T grassCarp, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        poseStack.translate(0, 0.75, 0);

        ItemStack itemstack = grassCarp.getMainHandItem();
        Minecraft.getInstance().getItemInHandRenderer().renderItem(grassCarp, itemstack, ItemTransforms.TransformType.GROUND, false, poseStack, bufferSource, packedLightIn);
        poseStack.popPose();
    }
}
