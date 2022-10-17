package hungteen.immortal.event.handler;

import hungteen.immortal.SpellManager;
import hungteen.immortal.capability.player.PlayerDataManager;
import hungteen.immortal.entity.FlyingItemEntity;
import hungteen.immortal.entity.ImmortalEntities;
import hungteen.immortal.entity.SpiritualFlame;
import hungteen.immortal.event.ImmortalLivingEvents;
import hungteen.immortal.event.ImmortalPlayerEvents;
import hungteen.immortal.impl.Spells;
import hungteen.immortal.impl.SpiritualRoots;
import hungteen.immortal.item.artifacts.FlameGourd;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
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
        PlayerUtil.getOptManager(player).ifPresent(PlayerDataManager::init);

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

    /**
     * {@link ImmortalPlayerEvents#onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone)}
     */
    public static void clonePlayerData(Player oldPlayer, Player newPlayer, boolean died) {
        PlayerUtil.getOptManager(oldPlayer).ifPresent(oldOne -> {
            PlayerUtil.getOptManager(newPlayer).ifPresent(newOne -> {
                newOne.cloneFromExistingPlayerData(oldOne, died);
            });
        });
    }

    public static void onTossItem(Player player, ItemEntity itemEntity) {
        if(PlayerUtil.isSpellActivated(player, Spells.FLY_WITH_SWORD)){
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
