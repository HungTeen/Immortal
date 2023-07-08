package hungteen.imm.client.render.entity.creature;

import hungteen.imm.client.model.ModelLayers;
import hungteen.imm.client.model.entity.SilkWormModel;
import hungteen.imm.client.render.entity.IMMMobRender;
import hungteen.imm.common.entity.creature.SilkWorm;
import hungteen.imm.util.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-21 10:55
 **/
public class SilkWormRender extends IMMMobRender<SilkWorm> {

    private static final ResourceLocation TEXTURE = Util.prefix("textures/entity/creature/silk_worm.png");

    public SilkWormRender(EntityRendererProvider.Context rendererManager) {
        super(rendererManager, new SilkWormModel<>(rendererManager.bakeLayer(ModelLayers.SILK_WORM)), 0.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(SilkWorm worm) {
        return TEXTURE;
    }
}
