package hungteen.imm.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 21:56
 **/
public abstract class IMMMobRender<T extends Mob> extends MobRenderer<T, EntityModel<T>> {

    private static final int BREATH_ANIM_CD = 40;

    public IMMMobRender(EntityRendererProvider.Context context, EntityModel<T> model, float shadowSize) {
        super(context, model, shadowSize);
    }

    @Override
    protected void scale(T entity, PoseStack stack, float partialTick) {
        final Vec3 vec = getTranslateVec(entity, partialTick);
        getScaleByEntity(entity, partialTick).ifLeft(f -> {
            stack.scale(f, f, f);
        }).ifRight(size -> {
            stack.scale((float) size.x, (float) size.y, (float) size.z);
        });
        stack.translate(vec.x, vec.y, vec.z);
    }

    public Either<Float, Vec3> getScaleByEntity(T entity, float partialTick) {
        return Either.left(1F);
    }

    public Vec3 getTranslateVec(T entity, float partialTick) {
        return new Vec3(0, 0, 0);
    }
}
