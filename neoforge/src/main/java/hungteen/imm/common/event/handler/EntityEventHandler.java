package hungteen.imm.common.event.handler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/4 21:40
 **/
public class EntityEventHandler {

    /**
     * 增加世界观相关的锁敌。
     */
    public static void compatTargetGoal(Entity entity){
        if(entity instanceof Raider raider){
            raider.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(raider, Creeper.class, true));
        } else if(entity instanceof Creeper creeper){
            creeper.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(creeper, Raider.class, true));
        }
    }

    public static void attachElementWhenImpact(Projectile projectile, HitResult hitResult){
        if(hitResult instanceof BlockHitResult blockHitResult){
            // TODO 弹射物与元素方块的交互。
        }
    }
}
