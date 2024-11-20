package hungteen.imm.client.render.entity.creature.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.CubeModel;
import hungteen.imm.client.render.entity.layer.WoodStakeLayer;
import hungteen.imm.common.entity.creature.monster.SharpStake;
import hungteen.imm.util.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/6 15:14
 */
public class SharpStakeRender extends MobRenderer<SharpStake, CubeModel<SharpStake>> {

    private static final ResourceLocation EYES = Util.get().entityTexture("wood_eyes");

    public SharpStakeRender(EntityRendererProvider.Context context) {
        super(context, new CubeModel<>(context.bakeLayer(IMMModelLayers.SHARP_STAKE)), 0.5F);
        this.addLayer(new WoodStakeLayer(context, this));
    }

    @Override
    protected void setupRotations(SharpStake stake, PoseStack stack, float blob, float yRot, float partialTicks, float scale) {
        super.setupRotations(stake, stack, blob, Direction.fromYRot(yRot).toYRot(), partialTicks, scale);
    }

    @Override
    public ResourceLocation getTextureLocation(SharpStake stake) {
        return EYES;
    }

}
