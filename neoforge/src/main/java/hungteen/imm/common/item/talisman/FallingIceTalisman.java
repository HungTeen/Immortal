package hungteen.imm.common.item.talisman;

import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.misc.FallingIceEntity;
import hungteen.imm.common.IMMSounds;
import hungteen.imm.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

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
            HitResult hitResult = EntityUtil.getHitResult(entity, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY);
            Vec3 destination = entity.position().add(0, 7, 0);
            if(hitResult instanceof EntityHitResult entityHitResult){
                destination = entityHitResult.getEntity().position().add(0, 7, 0);
            } else if(hitResult instanceof BlockHitResult blockHitResult){
                destination = blockHitResult.getLocation().add(0, 7, 0);
            }
            FallingIceEntity iceEntity = new FallingIceEntity(IMMEntities.FALLING_ICE.get(), level);
            iceEntity.setPos(destination);
            iceEntity.setOwner(entity);
            iceEntity.setFloatTicks(30);
            level.addFreshEntity(iceEntity);
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
