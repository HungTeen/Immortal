package hungteen.imm.common.event;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.common.block.artifacts.SpiritualFurnaceBlock;
import hungteen.imm.common.capability.player.IMMPlayerData;
import hungteen.imm.common.cultivation.spell.metal.CriticalHitSpell;
import hungteen.imm.common.event.handler.PlayerEventHandler;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-25 15:52
 **/
@EventBusSubscriber(modid = IMMAPI.MOD_ID)
public class IMMPlayerEvents {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if(! event.getEntity().level().isClientSide()){
            PlayerUtil.setData(event.getEntity(), IMMPlayerData::tick);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (EntityHelper.isServer(event.getEntity())) {
            PlayerUtil.setData(event.getEntity(), IMMPlayerData::initialize);
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
            PlayerUtil.setData(event.getEntity(), IMMPlayerData::syncToClient);
        }
    }

    @SubscribeEvent
    public static void onPlayerPickupXp(PlayerXpEvent.PickupXp event) {
//        if(CultivationManager.getRealm(event.getEntity()).getCultivationType().isSpiritual()){
//            EntityUtil.addQiAmount(event.getEntity(), event.getOrb().getValue() * 0.4F);
//            event.getOrb().discard();
//            event.setCanceled(true);
//        }
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
//        HTLibPlatformAPI.get().sendToServer(new EmptyClickPacket(event.getHand()));
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
