package hungteen.imm.client.render.entity.human;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.imm.client.ClientProxy;
import hungteen.imm.client.model.ModelLayers;
import hungteen.imm.client.model.entity.CultivatorModel;
import hungteen.imm.client.render.entity.layer.CultivatorArmorLayer;
import hungteen.imm.common.entity.human.cultivator.Cultivator;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-22 22:21
 **/
public class CultivatorRender extends LivingEntityRenderer<Cultivator, CultivatorModel<Cultivator>> {

    protected CultivatorModel<Cultivator> defaultModel = null;
    protected CultivatorModel<Cultivator> slimModel = null;

    public CultivatorRender(EntityRendererProvider.Context context) {
        super(context, new CultivatorModel<>(context.bakeLayer(ModelLayers.CULTIVATOR), false), 0.5F);
        this.defaultModel = this.model;
        this.slimModel = new CultivatorModel<>(context.bakeLayer(ModelLayers.CULTIVATOR_SLIM), true);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new CultivatorArmorLayer<>(
                        this,
                        new HumanoidModel<>(context.bakeLayer(ModelLayers.CULTIVATOR_INNER_ARMOR)),
                        new HumanoidModel<>(context.bakeLayer(ModelLayers.CULTIVATOR_OUTER_ARMOR)),
                        context.getModelManager(),
                        false
                )
        );
        this.addLayer(new CultivatorArmorLayer<>(
                        this,
                        new HumanoidModel<>(context.bakeLayer(ModelLayers.CULTIVATOR_SLIM_INNER_ARMOR)),
                        new HumanoidModel<>(context.bakeLayer(ModelLayers.CULTIVATOR_SLIM_OUTER_ARMOR)),
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
            if (entity.getCultivatorType().getSkinLocation().isPresent()) {
                return entity.getCultivatorType().getSkinLocation().get();
            } else {
                final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = ClientProxy.MC.getSkinManager().getInsecureSkinInformation(entity.getCultivatorType().getGameProfile().get());
                if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                    final MinecraftProfileTexture texture = map.get(MinecraftProfileTexture.Type.SKIN);
                    final ResourceLocation skinLocation = ClientProxy.MC.getSkinManager().registerTexture(texture, MinecraftProfileTexture.Type.SKIN);
                    String modelValue = texture.getMetadata("model");
                    if (modelValue != null) {
                        entity.getCultivatorType().setSlim(modelValue.equals("slim"));
                    }
                    return skinLocation;
                }
            }
        }
        return DefaultPlayerSkin.getDefaultSkin();
    }

}
