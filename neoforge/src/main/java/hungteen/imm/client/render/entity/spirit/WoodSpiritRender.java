package hungteen.imm.client.render.entity.spirit;

import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.spirit.MetalSpiritModel;
import hungteen.imm.client.model.entity.spirit.WoodSpiritModel;
import hungteen.imm.client.render.entity.IMMMobRender;
import hungteen.imm.common.entity.creature.spirit.MetalSpirit;
import hungteen.imm.common.entity.creature.spirit.WoodSpirit;
import hungteen.imm.util.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/26 19:23
 **/
public class WoodSpiritRender extends IMMMobRender<WoodSpirit> {

    private static final ResourceLocation TEXTURE = Util.get().entityTexture("spirit/wood_spirit");

    public WoodSpiritRender(EntityRendererProvider.Context context) {
        super(context, new WoodSpiritModel<>(context.bakeLayer(IMMModelLayers.WOOD_SPIRIT)), 0.9F);
    }

    @Override
    public ResourceLocation getTextureLocation(WoodSpirit spirit) {
        return TEXTURE;
    }
}
