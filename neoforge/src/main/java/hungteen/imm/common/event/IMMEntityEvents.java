package hungteen.imm.common.event;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.common.spell.spells.fire.IgnitionSpell;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.raid.Raider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-22 23:04
 **/
//@EventBusSubscriber(modid = IMMAPI.MOD_ID)
public class IMMEntityEvents {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event){
        if(! event.getLevel().isClientSide()){
            if(event.getEntity() instanceof Raider raider){
                raider.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(raider, Creeper.class, true));
            } else if(event.getEntity() instanceof Creeper creeper){
                creeper.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(creeper, Raider.class, true));
            }
        }
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event){
        if(EntityHelper.isServer(event.getProjectile())){
            IgnitionSpell.checkIgnitionArrow(event.getProjectile(), event.getRayTraceResult());
        }
    }

}
