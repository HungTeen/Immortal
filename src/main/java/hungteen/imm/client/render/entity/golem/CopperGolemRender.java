package hungteen.imm.client.render.entity.golem;

import hungteen.imm.client.model.ModelLayers;
import hungteen.imm.client.model.entity.golem.CopperGolemModel;
import hungteen.imm.client.render.entity.IMMMobRender;
import hungteen.imm.common.entity.golem.CopperGolem;
import hungteen.imm.util.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-29 16:20
 **/
public class CopperGolemRender extends IMMMobRender<CopperGolem> {

    private static final ResourceLocation LOCATION = Util.get().entityTexture("golem/copper_golem");

    public CopperGolemRender(EntityRendererProvider.Context rendererManager) {
        super(rendererManager, new CopperGolemModel<>(rendererManager.bakeLayer(ModelLayers.COPPER_GOLEM)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(CopperGolem golem) {
        return LOCATION;
    }
}
