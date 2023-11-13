package hungteen.imm.common.event;

import hungteen.htlib.common.capability.PlayerCapabilityManager;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.ImmortalMod;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.block.artifacts.SpiritualFurnaceBlock;
import hungteen.imm.common.capability.player.PlayerDataManager;
import hungteen.imm.common.event.handler.PlayerEventHandler;
import hungteen.imm.common.impl.registry.PlayerRangeIntegers;
import hungteen.imm.common.network.EmptyClickPacket;
import hungteen.imm.common.network.NetworkHandler;
import hungteen.imm.common.spell.spells.metal.CriticalHitSpell;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 15:52
 **/
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID)
public class IMMPlayerEvents {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && EntityHelper.isServer(event.player)) {
            if(PlayerUtil.isSitInMeditation(event.player)){
                PlayerUtil.addIntegerData(event.player, PlayerRangeIntegers.MEDITATE_TICK, 1);
            }
            RealmManager.limitEnchantments(event.player);
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
            event.getOriginal().reviveCaps();
            PlayerUtil.getOptManager(event.getOriginal()).ifPresent(r -> {
                PlayerUtil.getOptManager(event.getEntity()).ifPresent(l -> {
                    l.cloneFromExistingPlayerData(r, event.isWasDeath());
                });
            });
            event.getOriginal().invalidateCaps();
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
        NetworkHandler.sendToServer(new EmptyClickPacket(event.getHand()));
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
