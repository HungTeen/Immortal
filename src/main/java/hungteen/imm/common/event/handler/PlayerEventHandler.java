package hungteen.imm.common.event.handler;

import hungteen.imm.api.EntityBlockResult;
import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.common.entity.misc.SpiritualFlame;
import hungteen.imm.common.event.ImmortalLivingEvents;
import hungteen.imm.common.event.ImmortalPlayerEvents;
import hungteen.imm.common.item.artifacts.FlameGourd;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.utils.PlayerUtil;
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
        SpellManager.checkPassiveSpell(player, SpellTypes.FLY_WITH_ITEM, new EntityBlockResult(player.level, itemEntity));
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
        final HitResult hitResult = PlayerUtil.getHitResult(player);
        switch (hitResult.getType()){
            case BLOCK -> {
                onTraceBlock(player, (BlockHitResult) hitResult);
            }
            case ENTITY -> {
                onTraceEntity(player, (EntityHitResult) hitResult);
            }
        }
    }

}
