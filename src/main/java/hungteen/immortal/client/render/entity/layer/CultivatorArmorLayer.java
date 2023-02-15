package hungteen.immortal.client.render.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.immortal.common.entity.human.cultivator.Cultivator;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-23 11:10
 **/
public class CultivatorArmorLayer<T extends Cultivator, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends HumanoidArmorLayer<T, M, A> {

    private final boolean isSlim;

    public CultivatorArmorLayer(RenderLayerParent<T, M> layerParent, A innerModel, A outerModel, boolean isSlim) {
        super(layerParent, innerModel, outerModel);
        this.isSlim = isSlim;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, T cultivator, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(cultivator.isSlim() == isSlim){
            super.render(poseStack, bufferSource, packedLightIn, cultivator, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }
}
