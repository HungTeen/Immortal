package hungteen.imm.common.event.handler;

import hungteen.imm.common.cultivation.ElementManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/4 21:40
 **/
public class LivingEventHandler {

    /**
     * 投掷物击中时会传递自身的元素。
     */
    public static void attachElementWhenImpact(LivingEntity living, DamageSource source){
        if(source.getDirectEntity() instanceof Projectile projectile){
            ElementManager.transferElement(projectile, living);
        }
    }
}
