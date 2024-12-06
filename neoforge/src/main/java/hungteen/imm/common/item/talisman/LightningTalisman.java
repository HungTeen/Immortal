package hungteen.imm.common.item.talisman;

import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.util.EntityUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
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
public class LightningTalisman extends DurationTalisman {

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if(! level.isClientSide){
            HitResult hitResult = EntityUtil.getHitResult(entity, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY);
            Vec3 destination = hitResult.getLocation();
            if(hitResult instanceof EntityHitResult entityHitResult){
                destination = entityHitResult.getEntity().position();
            } else if(hitResult instanceof BlockHitResult blockHitResult){
                destination = blockHitResult.getLocation();
            }
            LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
            bolt.setPos(destination);
            if(entity instanceof ServerPlayer serverPlayer){
                bolt.setCause(serverPlayer);
            }
            bolt.setDamage(6);
            level.addFreshEntity(bolt);
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity living) {
        return 30;
    }

    @Override
    public int getCoolDown(ItemStack stack, LivingEntity entity) {
        return 200;
    }

    @Override
    public float getQiCost(ItemStack stack, Entity entity) {
        return 50;
    }

    @Override
    public ArtifactRank getArtifactRealm(ItemStack stack) {
        return ArtifactRank.COMMON;
    }
}
