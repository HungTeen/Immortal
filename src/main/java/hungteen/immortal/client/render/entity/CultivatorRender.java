package hungteen.immortal.client.render.entity;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.immortal.client.ClientProxy;
import hungteen.immortal.client.model.ModelLayers;
import hungteen.immortal.client.model.entity.CultivatorModel;
import hungteen.immortal.client.render.entity.layer.CultivatorArmorLayer;
import hungteen.immortal.common.entity.human.Cultivator;
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
        this.addLayer(new ItemInHandLayer<>(this));
        this.addLayer(new CultivatorArmorLayer<>(
                        this,
                        new HumanoidModel<>(context.bakeLayer(ModelLayers.CULTIVATOR_INNER_ARMOR)),
                        new HumanoidModel<>(context.bakeLayer(ModelLayers.CULTIVATOR_OUTER_ARMOR)),
                        false
                )
        );
        this.addLayer(new CultivatorArmorLayer<>(
                        this,
                        new HumanoidModel<>(context.bakeLayer(ModelLayers.CULTIVATOR_SLIM_INNER_ARMOR)),
                        new HumanoidModel<>(context.bakeLayer(ModelLayers.CULTIVATOR_SLIM_OUTER_ARMOR)),
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
    public ResourceLocation getTextureLocation(Cultivator entity) {
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = ClientProxy.MC.getSkinManager().getInsecureSkinInformation(entity.getCultivatorType().getGameProfile());
        return map.containsKey(MinecraftProfileTexture.Type.SKIN) ? ClientProxy.MC.getSkinManager().registerTexture(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN) : DefaultPlayerSkin.getDefaultSkin();
    }
}
