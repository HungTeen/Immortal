package hungteen.imm.common.item.talisman;

import hungteen.htlib.util.helper.RandomHelper;
import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.MathUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-06-04 16:15
 **/
public class FireballTalisman extends DurationTalisman {

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if(! level.isClientSide){
            EntityUtil.playSound(level, entity, SoundEvents.BLAZE_SHOOT);
            for(int i = 0; i < 1; ++ i){
                final Vec3 vec = entity.getLookAngle().normalize().scale(2F);
                final LargeFireball fireball = new LargeFireball(level, entity, vec, 2);
                final double forward = RandomHelper.doubleRange(entity.getRandom(), 1);
                final double side = RandomHelper.doubleRange(entity.getRandom(), 2);
                final double dy = RandomHelper.doubleRange(entity.getRandom(), 0.5);
                final double dx = Mth.sin(MathUtil.toRadian(entity.getYRot()));
                final double dz = Mth.cos(MathUtil.toRadian(entity.getYRot()));
                fireball.setPos(entity.position().add(dx * forward + dz * side, dy + entity.getEyeHeight(), dz * forward + dx * side));
                level.addFreshEntity(fireball);
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity living) {
        return 40;
    }

    @Override
    public int getCoolDown(ItemStack stack, LivingEntity entity) {
        return 160;
    }

    @Override
    public float getQiCost(ItemStack stack, Entity entity) {
        return 40;
    }

    @Override
    public ArtifactRank getArtifactRealm(ItemStack stack) {
        return ArtifactRank.COMMON;
    }
}
