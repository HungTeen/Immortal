package hungteen.imm.client.render.entity.human;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.util.ModelLayerType;
import hungteen.imm.client.model.entity.human.ChillagerModel;
import hungteen.imm.client.render.IMMEntityRenderers;
import hungteen.imm.common.entity.human.pillager.Chillager;
import hungteen.imm.util.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/8 20:33
 **/
public class ChillagerRender extends VillagerLikeRender<Chillager, ChillagerModel<Chillager>> {

    private static final ResourceLocation TEXTURE = Util.get().entityTexture("pillager/chillager");

    public ChillagerRender(EntityRendererProvider.Context context) {
        super(context, new ChillagerModel<>(IMMEntityRenderers.CHILLAGER.getPart(context, ModelLayerType.MAIN)));
    }

    @Override
    public void render(Chillager entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        entity.targetMovement = entity.targetMovement + (targetCapRot(entity) - entity.targetMovement) * 0.1F * partialTicks;
        entity.capeRotate = Mth.lerp(partialTicks, entity.capeRotate, entity.targetMovement);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private float targetCapRot(Chillager entity){
        return (float) Mth.clamp(entity.getDeltaMovement().length() * 3, 0, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(Chillager entity) {
        return TEXTURE;
    }
}
