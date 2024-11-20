package hungteen.imm.client.render.entity.golem;

import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.golem.SnowGolemModel;
import hungteen.imm.client.render.entity.IMMMobRender;
import hungteen.imm.common.entity.golem.SnowGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-23 21:58
 **/
public class SnowGolemRender extends IMMMobRender<SnowGolem> {

    private static final ResourceLocation GOLEM_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/snow_golem.png");

    public SnowGolemRender(EntityRendererProvider.Context rendererManager) {
        super(rendererManager, new SnowGolemModel<>(rendererManager.bakeLayer(IMMModelLayers.SNOW_GOLEM)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(SnowGolem golem) {
        return GOLEM_LOCATION;
    }
}
