package hungteen.immortal.client.render.entity;

import hungteen.immortal.client.model.ModelLayers;
import hungteen.immortal.client.model.entity.IronGolemModel;
import hungteen.immortal.common.entity.golem.IronGolem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-24 12:44
 **/
public class IronGolemRender extends ImmortalMobRender<IronGolem> {

    private static final ResourceLocation GOLEM_LOCATION = new ResourceLocation("textures/entity/iron_golem/iron_golem.png");

    public IronGolemRender(EntityRendererProvider.Context rendererManager) {
        super(rendererManager, new IronGolemModel<>(rendererManager.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(IronGolem golem) {
        return GOLEM_LOCATION;
    }
}
