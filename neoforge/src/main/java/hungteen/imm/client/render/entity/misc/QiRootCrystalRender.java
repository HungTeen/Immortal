package hungteen.imm.client.render.entity.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.client.util.ModelLayerType;
import hungteen.htlib.util.HTColor;
import hungteen.imm.client.model.entity.misc.CubeModel;
import hungteen.imm.client.render.IMMEntityRenderers;
import hungteen.imm.client.util.RenderUtil;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.entity.misc.QiRootCrystal;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/10 18:25
 **/
public class QiRootCrystalRender extends EntityRenderer<QiRootCrystal> {

    private final CubeModel<QiRootCrystal> cubeModel;

    public QiRootCrystalRender(EntityRendererProvider.Context context) {
        super(context);
        this.cubeModel = new CubeModel<>(IMMEntityRenderers.QI_ROOT_CRYSTAL.getPart(context, ModelLayerType.MAIN));
    }

    @Override
    public void render(QiRootCrystal entity, float entityYaw, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, stack, bufferSource, packedLight);
        final HTColor color = ElementManager.getElementColor(entity, false);
        stack.pushPose();
        RenderUtil.commonTranslate(stack, 1F);
        this.cubeModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.translucent()), packedLight, OverlayTexture.NO_OVERLAY, color.argb());
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(QiRootCrystal entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
