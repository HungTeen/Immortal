package hungteen.imm.client.render.entity.golem;

import hungteen.imm.client.model.ModelLayers;
import hungteen.imm.client.model.entity.golem.IronGolemModel;
import hungteen.imm.client.render.entity.IMMMobRender;
import hungteen.imm.common.entity.golem.IronGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-24 12:44
 **/
public class IronGolemRender extends IMMMobRender<IronGolem> {

    private static final ResourceLocation GOLEM_LOCATION = new ResourceLocation("textures/entity/iron_golem/iron_golem.png");

    public IronGolemRender(EntityRendererProvider.Context rendererManager) {
        super(rendererManager, new IronGolemModel<>(rendererManager.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(IronGolem golem) {
        return GOLEM_LOCATION;
    }
}
