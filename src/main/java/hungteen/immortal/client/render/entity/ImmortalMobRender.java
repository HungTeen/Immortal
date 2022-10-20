package hungteen.immortal.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3d;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 21:56
 **/
public abstract class ImmortalMobRender<T extends Mob> extends MobRenderer<T, EntityModel<T>> {

    private static final int BREATH_ANIM_CD = 40;

    public ImmortalMobRender(EntityRendererProvider.Context rendererManager, EntityModel<T> entityModelIn, float shadowSizeIn) {
        super(rendererManager, entityModelIn, shadowSizeIn);
    }

    @Override
    protected void scale(T entity, PoseStack matrixStackIn, float partialTickTime) {
        final float sz = getScaleByEntity(entity);
        final Vector3d vec = getTranslateVec(entity);
        matrixStackIn.scale(sz, sz, sz);
        matrixStackIn.translate(vec.x, vec.y, vec.z);
    }

    public float getScaleByEntity(T entity) {
        return entity.getScale();
    }

    public Vector3d getTranslateVec(T entity) {
        return new Vector3d(0, 0, 0);
    }
}
