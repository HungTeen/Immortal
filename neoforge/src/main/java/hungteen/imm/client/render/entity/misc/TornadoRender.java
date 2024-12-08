package hungteen.imm.client.render.entity.misc;

import hungteen.htlib.client.render.entity.HTEntityRender;
import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.misc.TornadoModel;
import hungteen.imm.common.entity.misc.Tornado;
import hungteen.imm.util.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/11/1 18:30
 **/
public class TornadoRender extends HTEntityRender<Tornado> {

    private static final ResourceLocation TEXTURE = Util.get().entityTexture("tornado/tornado");
    private static final ResourceLocation FIRE = Util.get().entityTexture("tornado/fire_tornado");

    public TornadoRender(EntityRendererProvider.Context context) {
        super(context, new TornadoModel<>(context.bakeLayer(IMMModelLayers.TORNADO)));
    }

    @Override
    protected float getScaleByEntity(Tornado tornado) {
        return tornado.getScale();
    }

    @Override
    public ResourceLocation getTextureLocation(Tornado tornado) {
        if(tornado.isFireTornado()) return FIRE;
        return TEXTURE;
    }

}
