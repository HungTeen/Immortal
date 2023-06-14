package hungteen.imm.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.imm.client.ClientProxy;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
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
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource source, int packedLight, int packedOverlay) {
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
