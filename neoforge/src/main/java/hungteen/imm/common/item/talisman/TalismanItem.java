package hungteen.imm.common.item.talisman;

import hungteen.imm.api.artifact.ArtifactItem;
import hungteen.imm.util.BehaviorUtil;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/6/3 15:58
 */
public abstract class TalismanItem extends Item implements ArtifactItem {

    public static final ResourceLocation ACTIVATED = Util.prefix("activated");
    public static final ResourceLocation ACTIVATING = Util.prefix("activating");
    public static final MutableComponent NO_TARGET = TipUtil.info("talisman.no_target");

    public TalismanItem(Properties properties) {
        super(properties);
    }

    public boolean canUseTalisman(ItemStack stack, LivingEntity living){
        return true;
    }

    public Optional<Vec3> getTargetPosition(Entity entity){
        if(entity instanceof Player){
            HitResult hitResult = EntityUtil.getHitResult(entity, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY);
            if(hitResult instanceof EntityHitResult entityHitResult){
                return Optional.of(entityHitResult.getEntity().position());
            } else if(hitResult instanceof BlockHitResult blockHitResult){
                return Optional.of(blockHitResult.getLocation());
            }
        } else if(entity instanceof LivingEntity living){
            Optional<LivingEntity> target = BehaviorUtil.getAttackTarget(living);
            if(target.isPresent()){
                return Optional.of(target.get().position());
            } else if(living instanceof Mob mob){
                if(mob.getTarget() != null){
                    return Optional.of(mob.getTarget().position());
                }
            }
        }
        return Optional.empty();
    }

    public static Component noEnoughQiTip(float qiCost) {
        return TipUtil.info("talisman.no_enough_qi", String.format("%.1f", qiCost));
    }

}
