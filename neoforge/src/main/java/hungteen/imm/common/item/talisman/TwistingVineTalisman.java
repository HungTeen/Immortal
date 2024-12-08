package hungteen.imm.common.item.talisman;

import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.misc.TwistingVines;
import hungteen.imm.util.EntityUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/6 23:40
 **/
public class TwistingVineTalisman extends DurationTalisman {

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if(! level.isClientSide){
            EntityUtil.playSound(level, entity, SoundEvents.BONE_MEAL_USE);
            Vec3 destination = getTargetPosition(entity);
            TwistingVines vines = new TwistingVines(IMMEntities.TWISTING_VINES.get(), level);
            vines.setPos(destination);
            vines.setOwner(entity);
            vines.setVineHealth(500);
            level.addFreshEntity(vines);
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public int getCoolDown(ItemStack stack, LivingEntity entity) {
        return 320;
    }

    @Override
    public float getQiCost(ItemStack stack, Entity entity) {
        return 35;
    }

    @Override
    public ArtifactRank getArtifactRealm(ItemStack stack) {
        return ArtifactRank.COMMON;
    }
}
