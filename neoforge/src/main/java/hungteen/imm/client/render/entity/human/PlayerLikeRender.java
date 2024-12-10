package hungteen.imm.client.render.entity.human;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.imm.client.model.entity.human.PlayerLikeModel;
import hungteen.imm.common.entity.human.cultivator.PlayerLikeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/10 18:32
 **/
public class PlayerLikeRender<T extends PlayerLikeEntity> extends LivingEntityRenderer<T, PlayerLikeModel<T>> {

    protected final PlayerLikeModel<T> defaultModel;
    protected final PlayerLikeModel<T> slimModel;
    protected PlayerSkin skin;

    public PlayerLikeRender(EntityRendererProvider.Context context, PlayerLikeModel<T> defaultModel, PlayerLikeModel<T> slimModel) {
        super(context, defaultModel, 0.5F);
        this.defaultModel = defaultModel;
        this.slimModel = slimModel;
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn,
                       MultiBufferSource bufferIn, int packedLightIn) {
        this.model = entityIn.isSlim() ? this.slimModel : this.defaultModel;
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (skin == null && entity.getOwner() != null) {
            skin = Minecraft.getInstance().getSkinManager().getInsecureSkin(entity.getOwner().getGameProfile());
            entity.setSlim(skin.model() == PlayerSkin.Model.SLIM);
            return skin.texture();
        }
        return DefaultPlayerSkin.getDefaultTexture();
    }
}
