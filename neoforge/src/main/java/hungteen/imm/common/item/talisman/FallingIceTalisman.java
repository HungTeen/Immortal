package hungteen.imm.common.item.talisman;

import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.common.IMMSounds;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.misc.FallingIceEntity;
import hungteen.imm.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-06-04 16:15
 **/
public class FallingIceTalisman extends DurationTalisman {

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if(! level.isClientSide){
            EntityUtil.playSound(level, entity, IMMSounds.FALLING_ICE_FINISH.get());
            Optional<Vec3> targetOpt = getTargetPosition(entity);
            if(targetOpt.isPresent()){
                Vec3 destination = targetOpt.get().add(0, 7, 0);
                FallingIceEntity iceEntity = new FallingIceEntity(IMMEntities.FALLING_ICE.get(), level);
                iceEntity.setPos(destination);
                iceEntity.setOwner(entity);
                iceEntity.setFloatTicks(30);
                level.addFreshEntity(iceEntity);
            } else {
                EntityUtil.sendTip(entity, NO_TARGET);
                return stack;
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity living) {
        return 20;
    }

    @Override
    public int getCoolDown(ItemStack stack, LivingEntity entity) {
        return 100;
    }

    @Override
    public float getQiCost(ItemStack stack, Entity entity) {
        return 30;
    }

    @Override
    public ArtifactRank getArtifactRealm(ItemStack stack) {
        return ArtifactRank.COMMON;
    }
}
