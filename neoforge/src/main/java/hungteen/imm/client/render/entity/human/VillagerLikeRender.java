package hungteen.imm.client.render.entity.human;

import hungteen.imm.common.entity.human.VillagerLikeEntity;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-22 22:21
 **/
public abstract class VillagerLikeRender<T extends VillagerLikeEntity, M extends EntityModel<T> & ArmedModel> extends LivingEntityRenderer<T, M> {

    public VillagerLikeRender(EntityRendererProvider.Context context, M model) {
        super(context, model, 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    protected boolean shouldShowName(T villagerLike) {
        return super.shouldShowName(villagerLike) && (villagerLike.shouldShowName() || villagerLike.hasCustomName() && villagerLike == this.entityRenderDispatcher.crosshairPickEntity);
    }

}
