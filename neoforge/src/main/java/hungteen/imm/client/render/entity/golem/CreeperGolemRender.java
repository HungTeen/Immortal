package hungteen.imm.client.render.entity.golem;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.golem.CreeperGolemModel;
import hungteen.imm.client.render.entity.IMMMobRender;
import hungteen.imm.common.entity.golem.CreeperGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-25 23:24
 **/
public class CreeperGolemRender extends IMMMobRender<CreeperGolem> {

    private static final ResourceLocation LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/creeper/creeper.png");

    public CreeperGolemRender(EntityRendererProvider.Context context) {
        super(context, new CreeperGolemModel(context.bakeLayer(IMMModelLayers.CREEPER_GOLEM)), 0.5F);
    }

    @Override
    protected void scale(CreeperGolem p_114046_, PoseStack stack, float partialTick) {
        float f = p_114046_.getSwelling(partialTick);
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f *= f;
        f *= f;
        final float f2 = (1.0F + f * 0.4F) * f1;
        final float f3 = (1.0F + f * 0.1F) / f1;
        stack.scale(f2, f3, f2);
    }

    @Override
    protected float getWhiteOverlayProgress(CreeperGolem golem, float partialTicks) {
        float f = golem.getSwelling(partialTicks);
        return (int) (f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(CreeperGolem golem) {
        return LOCATION;
    }
}
