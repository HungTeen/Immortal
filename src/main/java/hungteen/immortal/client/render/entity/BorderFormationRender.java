package hungteen.immortal.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import hungteen.immortal.common.entity.formation.BorderFormation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-18 22:56
 **/
public class BorderFormationRender<T extends BorderFormation> extends EntityRenderer<T> {

    private static final ResourceLocation FORCEFIELD_LOCATION = new ResourceLocation("textures/misc/forcefield.png");

    public BorderFormationRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn,
                       MultiBufferSource bufferIn, int packedLightIn) {

    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return FORCEFIELD_LOCATION;
    }
}
