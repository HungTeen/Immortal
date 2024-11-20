package hungteen.imm.common.event;

import hungteen.htlib.platform.HTLibPlatformAPI;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.block.artifacts.SpiritualFurnaceBlock;
import hungteen.imm.common.capability.player.PlayerDataManager;
import hungteen.imm.common.event.handler.PlayerEventHandler;
import hungteen.imm.common.impl.registry.PlayerRangeIntegers;
import hungteen.imm.common.network.EmptyClickPacket;
import hungteen.imm.common.spell.spells.metal.CriticalHitSpell;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 15:52
 **/
//@EventBusSubscriber(modid = IMMAPI.MOD_ID)
public class IMMPlayerEvents {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (EntityHelper.isServer(event.getEntity())) {
            if(PlayerUtil.isSitInMeditation(event.getEntity())){
                PlayerUtil.addIntegerData(event.getEntity(), PlayerRangeIntegers.MEDITATE_TICK, 1);
            }
            RealmManager.limitEnchantments(event.getEntity());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (EntityHelper.isServer(event.getEntity())) {
            PlayerUtil.getOptManager(event.getEntity()).ifPresent(PlayerDataManager::initialize);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (EntityHelper.isServer(event.getEntity())) {
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(EntityHelper.isServer(event.getEntity())) {
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (EntityHelper.isServer(event.getEntity())) {
            // TODO 玩家 Cap clone。
//            event.getOriginal().reviveCaps();
//            PlayerUtil.getOptManager(event.getOriginal()).ifPresent(r -> {
//                PlayerUtil.getOptManager(event.getEntity()).ifPresent(l -> {
//                    l.cloneFromExistingPlayerData(r, event.isWasDeath());
//                });
//            });
//            event.getOriginal().invalidateCaps();
        }
    }

    @SubscribeEvent
    public static void onPlayerPickupXp(PlayerXpEvent.PickupXp event) {
        if(RealmManager.getRealm(event.getEntity()).getCultivationType().isSpiritual()){
            EntityUtil.addMana(event.getEntity(), event.getOrb().getValue() * 0.4F);
            event.getOrb().discard();
            event.setCanceled(true);
        }
    }

//    @SubscribeEvent
//    public static void onPlayerActivateSpell(PlayerSpellEvent.ActivateSpellEvent.Post event) {
//        SpellManager.checkSpellAction(event.getEntity(), event.getSpell(), event.getRealmValue());
//    }

    @SubscribeEvent
    public static void onPlayerInteractSpec(PlayerInteractEvent.EntityInteractSpecific event) {
        if (EntityHelper.isServer(event.getEntity())) {
            PlayerEventHandler.rayTrace(event.getEntity(), event.getHand());
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        InteractionResult result = InteractionResult.PASS;
        result = PlayerEventHandler.rayTrace(event.getEntity(), event.getHand());
        if(result.consumesAction()){
            event.setCanceled(true);
        }
        event.setCancellationResult(result);
    }

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult result = InteractionResult.PASS;
        if(event.getLevel().getBlockState(event.getPos()).is(IMMBlockTags.FURNACE_BLOCKS)){
            result = SpiritualFurnaceBlock.use(event.getLevel(), event.getEntity(), event.getLevel().getBlockState(event.getPos()), event.getPos());
        }
        if (! result.consumesAction()) {
            result = PlayerEventHandler.rayTrace(event.getEntity(), event.getHand());
        }
        if(result.consumesAction()){
            event.setCanceled(true);
        }
        event.setCancellationResult(result);
//        HammerItem.smithing(event.getEntity(), event.getHand(), event.getFace(), event.getPos());
    }

    /**
     * Only Client side !
     */
    @SubscribeEvent
    public static void onPlayerRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        HTLibPlatformAPI.get().sendToServer(new EmptyClickPacket(event.getHand()));
    }

    @SubscribeEvent
    public static void onPlayerTossItem(ItemTossEvent event) {
        if (EntityHelper.isServer(event.getEntity())) {
            PlayerEventHandler.onTossItem(event.getPlayer(), event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerCriticalHit(CriticalHitEvent event) {
        if (EntityHelper.isServer(event.getEntity())) {
            CriticalHitSpell.checkCriticalHit(event.getEntity(), event);
        }
    }

}
