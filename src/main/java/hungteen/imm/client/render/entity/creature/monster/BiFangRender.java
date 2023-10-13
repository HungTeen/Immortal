package hungteen.imm.client.render.entity.creature.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.BiFangModel;
import hungteen.imm.client.model.entity.SilkWormModel;
import hungteen.imm.client.render.entity.IMMMobRender;
import hungteen.imm.common.entity.creature.SilkWorm;
import hungteen.imm.common.entity.creature.monster.BiFang;
import hungteen.imm.util.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Chicken;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/13 20:25
 **/
public class BiFangRender extends IMMMobRender<BiFang> {

    private static final ResourceLocation TEXTURE = Util.get().entityTexture("creature/bi_fang");

    public BiFangRender(EntityRendererProvider.Context rendererManager) {
        super(rendererManager, new BiFangModel(rendererManager.bakeLayer(IMMModelLayers.BI_FANG)), 0.8F);
    }

    @Override
    public float getScaleByEntity(BiFang entity) {
        return super.getScaleByEntity(entity) * 2.5F;
    }

    @Override
    protected float getBob(BiFang biFang, float partialTicks) {
        float f = Mth.lerp(partialTicks, biFang.oFlap, biFang.flap);
        float f1 = Mth.lerp(partialTicks, biFang.oFlapSpeed, biFang.flapSpeed);
        return (Mth.sin(f) + 1.0F) * f1;
    }

    @Override
    public ResourceLocation getTextureLocation(BiFang worm) {
        return TEXTURE;
    }
}
