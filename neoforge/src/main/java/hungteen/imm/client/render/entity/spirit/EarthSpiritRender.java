package hungteen.imm.client.render.entity.spirit;

import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.spirit.EarthSpiritModel;
import hungteen.imm.client.model.entity.spirit.WoodSpiritModel;
import hungteen.imm.client.render.entity.IMMMobRender;
import hungteen.imm.common.entity.creature.spirit.EarthSpirit;
import hungteen.imm.common.entity.creature.spirit.WoodSpirit;
import hungteen.imm.util.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/26 19:23
 **/
public class EarthSpiritRender extends IMMMobRender<EarthSpirit> {

    private static final ResourceLocation TEXTURE = Util.get().entityTexture("spirit/earth_spirit");

    public EarthSpiritRender(EntityRendererProvider.Context context) {
        super(context, new EarthSpiritModel<>(context.bakeLayer(IMMModelLayers.EARTH_SPIRIT)), 0.9F);
    }

    @Override
    public ResourceLocation getTextureLocation(EarthSpirit spirit) {
        return TEXTURE;
    }
}
