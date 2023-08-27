package hungteen.imm.common.event;

import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.ImmortalMod;
import hungteen.imm.common.spell.spells.IgnitionSpell;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.raid.Raider;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-22 23:04
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID)
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
