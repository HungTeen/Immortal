package hungteen.imm.common.event;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.cultivation.QiManager;
import hungteen.imm.common.cultivation.SpellManager;
import hungteen.imm.common.cultivation.TriggerConditions;
import hungteen.imm.common.cultivation.reaction.InhibitionReaction;
import hungteen.imm.common.entity.misc.TwistingVines;
import hungteen.imm.common.event.handler.EntityEventHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
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
            // 自然增加灵力。
            QiManager.increaseQi(event.getEntity());
            // 附加元素。
            ElementManager.tickElements(event.getEntity());
            // 元素反应：寄生。
            InhibitionReaction.parasitism(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event){
        if(! event.getLevel().isClientSide()){
            EntityEventHandler.compatTargetGoal(event.getEntity());
            // 只考虑投掷物的第一次生成。
            if(! event.loadedFromDisk() && event.getEntity() instanceof Projectile projectile && projectile.getOwner() instanceof LivingEntity living){
                SpellManager.activateSpell(living, TriggerConditions.SHOOT, context -> {
                    context.setTarget(event.getEntity());
                });
            }
        }
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event){
        if(EntityHelper.isServer(event.getProjectile())){
            if(event.getProjectile().getOwner() instanceof LivingEntity living){
                SpellManager.activateSpell(living, TriggerConditions.IMPACT, context -> {
                    context.setHitResult(HTHitResult.create(event.getRayTraceResult()));
                });
            }
            EntityEventHandler.attachElementWhenImpact(event.getProjectile(), event.getRayTraceResult());
        }
    }

    @SubscribeEvent
    public static void onEntityMount(EntityMountEvent event){
        if(EntityHelper.isServer(event.getEntity())){
            // 不能主动取消对藤蔓的骑乘。
            if(event.getEntityBeingMounted() instanceof TwistingVines vines && !event.isMounting() && vines.isAlive()){
                event.setCanceled(true);
            }
        }
    }

}
