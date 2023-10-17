package hungteen.imm.client.render.entity.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.records.HTColor;
import hungteen.imm.client.RenderUtil;
import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.ElementAmethystModel;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.entity.misc.ElementAmethyst;
import hungteen.imm.util.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-09-18 20:30
 **/
public class ElementAmethystRender extends EntityRenderer<ElementAmethyst> {

    private static final ResourceLocation TEXTURE = Util.get().entityTexture("element_amethyst");
    private static final RenderType CUTOUT = RenderType.entityCutoutNoCull(TEXTURE);
    private static final RenderType LIGHT = RenderType.eyes(TEXTURE);
    private final ElementAmethystModel solidModel;
    private final ElementAmethystModel lightModel;

    public ElementAmethystRender(EntityRendererProvider.Context context) {
        super(context);
        this.solidModel = new ElementAmethystModel(context.bakeLayer(IMMModelLayers.ELEMENT_AMETHYST), true);
        this.lightModel = new ElementAmethystModel(context.bakeLayer(IMMModelLayers.ELEMENT_AMETHYST), false);
    }

    @Override
    public void render(ElementAmethyst entityIn, float entityYaw, float partialTicks, PoseStack stack,
                       MultiBufferSource bufferIn, int packedLightIn) {
        final HTColor color = ElementManager.getElementColor(entityIn, false);
        final float red = ColorHelper.to(color.red());
        final float green = ColorHelper.to(color.green());
        final float blue = ColorHelper.to(color.blue());
        stack.pushPose();
        RenderUtil.commonTranslate(stack, 1F);
        this.solidModel.renderToBuffer(stack, bufferIn.getBuffer(CUTOUT), packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1F);
        this.lightModel.renderToBuffer(stack, bufferIn.getBuffer(LIGHT), packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 0.5F);
        stack.popPose();
        super.render(entityIn, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(ElementAmethyst entity) {
        return null;
    }

}