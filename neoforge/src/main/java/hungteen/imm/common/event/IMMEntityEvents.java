package hungteen.imm.common.event;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.cultivation.ElementReactions;
import hungteen.imm.common.cultivation.QiManager;
import hungteen.imm.common.cultivation.spell.fire.IgnitionSpell;
import hungteen.imm.common.misc.damage.IMMDamageSources;
import hungteen.imm.util.EntityUtil;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.raid.Raider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-22 23:04
 **/
@EventBusSubscriber(modid = IMMAPI.MOD_ID)
public class IMMEntityEvents {

    @SubscribeEvent
    public static void tick(EntityTickEvent.Post event) {
        if(EntityHelper.isServer(event.getEntity())){
            // 增加灵力。
            QiManager.increaseQi(event.getEntity());
            // 附加元素。
            ElementManager.tickElements(event.getEntity());
            // 元素反应：寄生。
            ElementManager.ifActiveReaction(event.getEntity(), ElementReactions.PARASITISM, (reaction, scale) -> {
                if(EntityUtil.hasMana(event.getEntity())){
                    EntityUtil.addMana(event.getEntity(), - scale * 2.5F);
                } else {
                    if(event.getEntity().getRandom().nextFloat() < 0.2F){
                        event.getEntity().hurt(IMMDamageSources.elementReaction(event.getEntity()), scale * 2F);
                    }
                }
            });
        }
    }

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
