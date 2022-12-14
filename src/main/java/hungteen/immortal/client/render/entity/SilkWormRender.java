package hungteen.immortal.client.render.entity;

import hungteen.immortal.client.model.ModelLayers;
import hungteen.immortal.client.model.entity.SilkWormModel;
import hungteen.immortal.common.entity.creature.SilkWorm;
import hungteen.immortal.utils.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-21 10:55
 **/
public class SilkWormRender extends ImmortalMobRender<SilkWorm> {

    private static final ResourceLocation TEXTURE = Util.prefix("textures/entity/creature/silk_worm.png");

    public SilkWormRender(EntityRendererProvider.Context rendererManager) {
        super(rendererManager, new SilkWormModel<>(rendererManager.bakeLayer(ModelLayers.SILK_WORM)), 0.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(SilkWorm worm) {
        return TEXTURE;
    }
}
