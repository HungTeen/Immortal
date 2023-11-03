package hungteen.imm.common.item.talismans;

import hungteen.htlib.util.helper.RandomHelper;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.MathUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-04 16:15
 **/
public class FireballTalismanItem extends DurationTalismanItem{

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        EntityUtil.playSound(level, entity, SoundEvents.BLAZE_SHOOT);
        if(! level.isClientSide){
            for(int i = 0; i < 1; ++ i){
                final LargeFireball fireball = new LargeFireball(level, entity, 0, 0, 0, 2);
                final double forward = RandomHelper.doubleRange(entity.getRandom(), 1);
                final double side = RandomHelper.doubleRange(entity.getRandom(), 2);
                final double dy = RandomHelper.doubleRange(entity.getRandom(), 0.5);
                final double dx = Mth.sin(MathUtil.toRadian(entity.getYRot()));
                final double dz = Mth.cos(MathUtil.toRadian(entity.getYRot()));
                fireball.setPos(entity.position().add(dx * forward + dz * side, dy + entity.getEyeHeight(), dz * forward + dx * side));
                fireball.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, 1.5F, 1.0F);
                level.addFreshEntity(fireball);
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public IRealmType getArtifactRealm(ItemStack stack) {
        return RealmTypes.MODERATE_ARTIFACT;
    }
}
