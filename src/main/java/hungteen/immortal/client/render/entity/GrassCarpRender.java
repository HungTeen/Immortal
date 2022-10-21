package hungteen.immortal.client.render.entity;

import hungteen.immortal.client.model.ModelLayers;
import hungteen.immortal.client.model.entity.GrassCarpModel;
import hungteen.immortal.client.render.entity.layer.GrassCarpItemLayer;
import hungteen.immortal.common.entity.creature.GrassCarp;
import hungteen.immortal.utils.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 21:52
 **/
public class GrassCarpRender extends ImmortalMobRender<GrassCarp> {

    private static final ResourceLocation RES = Util.prefix("textures/entity/creature/grass_carp.png");

    public GrassCarpRender(EntityRendererProvider.Context rendererManager) {
        super(rendererManager, new GrassCarpModel<>(rendererManager.bakeLayer(ModelLayers.GRASS_CARP)), 0);
        this.addLayer(new GrassCarpItemLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(GrassCarp p_114482_) {
        return RES;
    }

}
