package hungteen.imm.client.render.entity.human;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.CultivatorModel;
import hungteen.imm.client.render.entity.layer.CultivatorArmorLayer;
import hungteen.imm.common.entity.human.cultivator.Cultivator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-10-22 22:21
 **/
public class CultivatorRender extends LivingEntityRenderer<Cultivator, CultivatorModel<Cultivator>> {

    protected CultivatorModel<Cultivator> defaultModel = null;
    protected CultivatorModel<Cultivator> slimModel = null;

    public CultivatorRender(EntityRendererProvider.Context context) {
        super(context, new CultivatorModel<>(context.bakeLayer(IMMModelLayers.CULTIVATOR), false), 0.5F);
        this.defaultModel = this.model;
        this.slimModel = new CultivatorModel<>(context.bakeLayer(IMMModelLayers.CULTIVATOR_SLIM), true);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new CultivatorArmorLayer<>(
                        this,
                        new HumanoidModel<>(context.bakeLayer(IMMModelLayers.CULTIVATOR_INNER_ARMOR)),
                        new HumanoidModel<>(context.bakeLayer(IMMModelLayers.CULTIVATOR_OUTER_ARMOR)),
                        context.getModelManager(),
                        false
                )
        );
        this.addLayer(new CultivatorArmorLayer<>(
                        this,
                        new HumanoidModel<>(context.bakeLayer(IMMModelLayers.CULTIVATOR_SLIM_INNER_ARMOR)),
                        new HumanoidModel<>(context.bakeLayer(IMMModelLayers.CULTIVATOR_SLIM_OUTER_ARMOR)),
                        context.getModelManager(),
                        true
                )
        );
    }

    @Override
    public void render(Cultivator entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn,
                       MultiBufferSource bufferIn, int packedLightIn) {
        this.model = entityIn.isSlim() ? this.slimModel : this.defaultModel;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected boolean shouldShowName(Cultivator cultivator) {
        return super.shouldShowName(cultivator) && (cultivator.shouldShowName() || cultivator.hasCustomName() && cultivator == this.entityRenderDispatcher.crosshairPickEntity);
    }

    @Override
    public ResourceLocation getTextureLocation(Cultivator entity) {
        if (entity.getCultivatorType().getSkinLocation().isPresent()) {
            return entity.getCultivatorType().getSkinLocation().get();
        } else if (entity.getCultivatorType().getGameProfile().isPresent()) {
            PlayerSkin insecureSkin = Minecraft.getInstance().getSkinManager().getInsecureSkin(entity.getCultivatorType().getGameProfile().get());
            entity.getCultivatorType().setSlim(insecureSkin.model() == PlayerSkin.Model.SLIM);
            return insecureSkin.texture();
        }
        return DefaultPlayerSkin.getDefaultTexture();
    }

}
