package hungteen.immortal.common.event.handler;

import hungteen.immortal.common.SpellManager;
import hungteen.immortal.common.capability.player.PlayerDataManager;
import hungteen.immortal.common.entity.ImmortalEntities;
import hungteen.immortal.common.entity.misc.FlyingItemEntity;
import hungteen.immortal.common.entity.misc.SpiritualFlame;
import hungteen.immortal.common.event.ImmortalLivingEvents;
import hungteen.immortal.common.event.ImmortalPlayerEvents;
import hungteen.immortal.common.impl.SpellTypes;
import hungteen.immortal.common.item.artifacts.FlameGourd;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 15:54
 **/
public class PlayerEventHandler {

    /**
     * send packet from server to client to sync player's data.
     * {@link ImmortalPlayerEvents#onPlayerLogin(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent)}
     */
    public static void onPlayerLogin(Player player) {
    }

    /**
     * save card cd.
     * {@link ImmortalPlayerEvents#onPlayerLogout(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent)}
     */
    public static void onPlayerLogout(Player player) {
    }

    /**
     * {@link ImmortalLivingEvents#onLivingDeath(LivingDeathEvent)}
     */
    public static void handlePlayerDeath(LivingDeathEvent ev, Player player) {
    }

    public static void onTossItem(Player player, ItemEntity itemEntity) {
        if(PlayerUtil.isSpellActivated(player, SpellTypes.FLY_WITH_SWORD)){
            FlyingItemEntity flyingItem = ImmortalEntities.FLYING_ITEM.get().create(player.level);
            flyingItem.setDeltaMovement(itemEntity.getDeltaMovement());
            flyingItem.setPos(itemEntity.position());
            flyingItem.setItemStack(itemEntity.getItem());
            itemEntity.discard();
            player.level.addFreshEntity(flyingItem);
        }
    }

    public static void onTraceEntity(Player player, EntityHitResult result) {
        SpellManager.checkSpellAction(player, result);
        /* 火葫芦 收 灵火 */
        if(result.getEntity() instanceof SpiritualFlame && player.getMainHandItem().getItem() instanceof FlameGourd){
            FlameGourd.rightClickFlame(player, player.getMainHandItem());
        }
    }

    public static void onTraceBlock(Player player, BlockHitResult result) {
        SpellManager.checkSpellAction(player, result);
    }

    /**
     * Ray Trace whenever player right click (Server Side Only!).
     * {@link ImmortalPlayerEvents#onPlayerInteractSpec(PlayerInteractEvent.EntityInteractSpecific)}
     * {@link ImmortalPlayerEvents#onPlayerRightClickItem(PlayerInteractEvent.RightClickItem)}
     * {@link ImmortalPlayerEvents#onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock)}
     * {@link ImmortalPlayerEvents#onPlayerRightClickEmpty(PlayerInteractEvent.RightClickEmpty)}
     */
    public static void rayTrace(Player player) {
        final HitResult hitResult = getHitResult(player);
        switch (hitResult.getType()){
            case BLOCK -> {
                onTraceBlock(player, (BlockHitResult) hitResult);
            }
            case ENTITY -> {
                onTraceEntity(player, (EntityHitResult) hitResult);
            }
        }
    }

    public static HitResult getHitResult(Player player) {
        final double range = 20; //TODO 神识决定距离。
        final Vec3 startVec = player.getEyePosition(1.0F);
        final Vec3 lookVec = player.getViewVector(1.0F);
        Vec3 endVec = startVec.add(lookVec.scale(range));
        BlockHitResult blockHitResult = player.level.clip(new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if(blockHitResult.getType() != HitResult.Type.MISS){
            endVec = blockHitResult.getLocation();
        }
        final AABB aabb = player.getBoundingBox().inflate(range);
        final EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(player.level, player, startVec, endVec, aabb, (entity) -> {
            return !entity.isSpectator();
        });
        return entityHitResult != null ? entityHitResult : blockHitResult;
    }

}
