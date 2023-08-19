package hungteen.imm.common.event;

import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.ImmortalMod;
import hungteen.imm.api.interfaces.IHasMana;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.world.entity.trial.BreakThroughTrial;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.LevelUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 16:32
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID)
public class IMMLivingEvents {

    public static void tick(LivingEvent.LivingTickEvent event) {
        if(EntityHelper.isServer(event.getEntity())){
            if(event.getEntity() instanceof IHasMana entity && EntityUtil.canManaIncrease(event.getEntity())) {
                if(! entity.isManaFull()){
                    entity.addMana(LevelUtil.getSpiritualRate(event.getEntity().level(), event.getEntity().blockPosition()));
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onLivingDeath(LivingDeathEvent ev) {
        if(ev.getEntity() instanceof ServerPlayer player){
            if(BreakThroughTrial.checkTrialFail(player)){
                ev.setCanceled(true);
                return;
            }
        }
        // Cause by player.
        if(ev.getSource().getEntity() instanceof ServerPlayer player) {
            RealmManager.onPlayerKillLiving(player, ev.getEntity());
        }
    }

}
