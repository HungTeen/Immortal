package hungteen.imm.client.render.entity.undead;

import hungteen.htlib.client.util.ModelLayerType;
import hungteen.imm.client.model.entity.undead.QiSkeletonModel;
import hungteen.imm.client.render.IMMEntityRenderers;
import hungteen.imm.common.entity.undead.QiSkeleton;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-20 22:06
 **/
public class QiSkeletonRender extends HumanoidMobRenderer<QiSkeleton, QiSkeletonModel<QiSkeleton>> {

    private static final ResourceLocation SKELETON_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/skeleton/skeleton.png");

    public QiSkeletonRender(EntityRendererProvider.Context context) {
        super(context, new QiSkeletonModel<>(IMMEntityRenderers.QI_SKELETON.getPart(context, ModelLayerType.MAIN)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(
                        this,
                        new QiSkeletonModel<>(IMMEntityRenderers.QI_SKELETON.getPart(context, ModelLayerType.INNER_ARMOR)),
                        new QiSkeletonModel<>(IMMEntityRenderers.QI_SKELETON.getPart(context, ModelLayerType.OUTER_ARMOR)),
                        context.getModelManager()
                )
        );
    }

    @Override
    public ResourceLocation getTextureLocation(QiSkeleton entity) {
        return SKELETON_LOCATION;
    }
}
