package hungteen.imm.client.render.entity.spirit;

import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.spirit.MetalSpiritModel;
import hungteen.imm.client.render.entity.IMMMobRender;
import hungteen.imm.common.entity.creature.spirit.MetalSpirit;
import hungteen.imm.util.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/26 19:23
 **/
public class MetalSpiritRender extends IMMMobRender<MetalSpirit> {

    private static final ResourceLocation TEXTURE = Util.get().entityTexture("spirit/metal_spirit");

    public MetalSpiritRender(EntityRendererProvider.Context context) {
        super(context, new MetalSpiritModel<>(context.bakeLayer(IMMModelLayers.METAL_SPIRIT)), 0.9F);
    }

    @Override
    public ResourceLocation getTextureLocation(MetalSpirit spirit) {
        return TEXTURE;
    }
}
