package hungteen.immortal.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hungteen.immortal.client.ClientProxy;
import hungteen.immortal.common.item.ImmortalItems;
import hungteen.immortal.common.item.artifacts.RawArtifact;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-06 12:30
 **/
public class RawArtifactItemRender extends BlockEntityWithoutLevelRenderer {

    public RawArtifactItemRender() {
        super(ClientProxy.MC.getBlockEntityRenderDispatcher(), ClientProxy.MC.getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource source, int packedLight, int packedOverlay) {
//        if(stack.is(ImmortalItems.RAW_ARTIFACT.get())){
//            ItemStack artifact = RawArtifact.getArtifactItem(stack);
//            BakedModel bakedmodel = ClientProxy.MC.getItemRenderer().getModel(artifact, ClientProxy.MC.level, (LivingEntity)null, 0);
//            RenderType rendertype = ItemBlockRenderTypes.getRenderType(artifact, false);
//            VertexConsumer consumer = ItemRenderer.getFoilBuffer(source, rendertype, true, artifact.hasFoil());
////            ClientProxy.MC.getItemRenderer().render(artifact, transformType, false, poseStack, source, packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
//            ClientProxy.MC.getItemRenderer().renderModelLists(bakedmodel, artifact, packedLight, packedOverlay, poseStack, consumer);
//        }
    }

}
