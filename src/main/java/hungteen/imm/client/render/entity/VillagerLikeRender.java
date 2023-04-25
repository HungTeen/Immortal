package hungteen.imm.client.render.entity;

import hungteen.imm.client.model.ModelLayers;
import hungteen.imm.client.model.entity.PillagerModel;
import hungteen.imm.common.entity.human.VillagerLikeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-22 22:21
 **/
public class VillagerLikeRender extends LivingEntityRenderer<VillagerLikeEntity, PillagerModel<VillagerLikeEntity>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/illager/pillager.png");

    public VillagerLikeRender(EntityRendererProvider.Context context) {
        super(context, new PillagerModel<>(context.bakeLayer(ModelLayers.VILLAGER)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    protected boolean shouldShowName(VillagerLikeEntity villagerLike) {
        return super.shouldShowName(villagerLike) && (villagerLike.shouldShowName() || villagerLike.hasCustomName() && villagerLike == this.entityRenderDispatcher.crosshairPickEntity);
    }

    @Override
    public ResourceLocation getTextureLocation(VillagerLikeEntity entity) {
        return TEXTURE;
    }
}
