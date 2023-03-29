package hungteen.imm.client.render.entity;

import hungteen.imm.client.model.ModelLayers;
import hungteen.imm.client.model.entity.GrassCarpModel;
import hungteen.imm.client.render.entity.layer.GrassCarpItemLayer;
import hungteen.imm.common.entity.creature.GrassCarp;
import hungteen.imm.util.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 21:52
 **/
public class GrassCarpRender extends ImmortalMobRender<GrassCarp> {

    private static final ResourceLocation RES = Util.prefix("textures/entity/creature/grass_carp.png");

    public GrassCarpRender(EntityRendererProvider.Context context) {
        super(context, new GrassCarpModel<>(context.bakeLayer(ModelLayers.GRASS_CARP)), 0);
        this.addLayer(new GrassCarpItemLayer(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(GrassCarp p_114482_) {
        return RES;
    }

}