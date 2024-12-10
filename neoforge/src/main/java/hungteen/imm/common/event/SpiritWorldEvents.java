package hungteen.imm.common.event;

import hungteen.imm.api.IMMAPI;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.world.levelgen.IMMDimensionTypes;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/10 16:36
 **/
@EventBusSubscriber(modid = IMMAPI.MOD_ID)
public class SpiritWorldEvents {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void cancelDeath(LivingDeathEvent event){
        if(isInSpiritWorld(event.getEntity().level()) && event.getEntity() instanceof ServerPlayer serverPlayer){
            CultivationManager.breakThroughFail(serverPlayer);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void cancelBreakBlock(BlockEvent.BreakEvent event){
        if(isInSpiritWorld(event.getLevel()) && ! canModify(event.getPlayer())){
            EntityUtil.sendTip(event.getPlayer(), TipUtil.info("spirit_world.cannot_break_block"));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void cancelPlaceBlock(BlockEvent.EntityPlaceEvent event){
        if(isInSpiritWorld(event.getLevel()) && ! canModify(event.getEntity())){
            EntityUtil.sendTip(event.getEntity(), TipUtil.info("spirit_world.cannot_place_block"));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void cancelFluidPlace(BlockEvent.FluidPlaceBlockEvent event){
        if(isInSpiritWorld(event.getLevel())){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void cancelMultiPlace(BlockEvent.EntityMultiPlaceEvent event){
        if(isInSpiritWorld(event.getLevel()) && ! canModify(event.getEntity())){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void cancelMultiPlace(ExplosionEvent.Start event){
        if(isInSpiritWorld(event.getLevel())){
            event.setCanceled(true);
        }
    }

    public static boolean canModify(Entity entity){
        return entity instanceof Player player && PlayerUtil.isCreativeOrSpectator(player);
    }

    public static boolean isInSpiritWorld(LevelAccessor levelAccessor){
        Optional<HolderLookup.RegistryLookup<DimensionType>> lookup = levelAccessor.registryAccess().lookup(Registries.DIMENSION_TYPE);
        if(lookup.isPresent()){
            Holder.Reference<DimensionType> holder = lookup.get().getOrThrow(IMMDimensionTypes.SPIRIT_WORLD);
            return levelAccessor.dimensionType().equals(holder.value());
        }
        return false;
    }
}
