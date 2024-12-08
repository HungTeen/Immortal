package hungteen.imm.client.render.entity.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.htlib.util.HTColor;
import hungteen.imm.client.util.RenderUtil;
import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.misc.ElementCrystalModel;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.entity.misc.ElementCrystal;
import hungteen.imm.util.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-09-18 20:30
 **/
public class ElementCrystalRender extends EntityRenderer<ElementCrystal> {

    private static final ResourceLocation TEXTURE = Util.get().entityTexture("element_crystal");
    private static final RenderType CUTOUT = RenderType.entityCutoutNoCull(TEXTURE);
    private static final RenderType LIGHT = RenderType.eyes(TEXTURE);
    private final ElementCrystalModel solidModel;
    private final ElementCrystalModel lightModel;

    public ElementCrystalRender(EntityRendererProvider.Context context) {
        super(context);
        this.solidModel = new ElementCrystalModel(context.bakeLayer(IMMModelLayers.ELEMENT_CRYSTAL), true);
        this.lightModel = new ElementCrystalModel(context.bakeLayer(IMMModelLayers.ELEMENT_CRYSTAL), false);
    }

    @Override
    public void render(ElementCrystal entityIn, float entityYaw, float partialTicks, PoseStack stack,
                       MultiBufferSource bufferIn, int packedLightIn) {
        final HTColor color = ElementManager.getElementColor(entityIn, false);
        stack.pushPose();
        RenderUtil.commonTranslate(stack, 1F);
        this.solidModel.renderToBuffer(stack, bufferIn.getBuffer(CUTOUT), packedLightIn, OverlayTexture.NO_OVERLAY, color.argb());
        this.lightModel.renderToBuffer(stack, bufferIn.getBuffer(LIGHT), packedLightIn, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(FastColor.as8BitChannel(0.35F), color.rgb()));
        stack.popPose();
        super.render(entityIn, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(ElementCrystal entity) {
        return null;
    }

}
