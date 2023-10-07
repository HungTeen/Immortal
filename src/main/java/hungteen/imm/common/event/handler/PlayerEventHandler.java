package hungteen.imm.common.event.handler;

import hungteen.imm.common.event.IMMPlayerEvents;
import hungteen.imm.common.item.artifacts.FlameGourd;
import hungteen.imm.util.EntityUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 15:54
 **/
public class PlayerEventHandler {

    public static void onTossItem(Player player, ItemEntity itemEntity) {
    }

    public static InteractionResult onTraceEntity(Player player, InteractionHand hand, EntityHitResult hitResult) {
        InteractionResult result = InteractionResult.PASS;
        result = FlameGourd.collectSpiritualFlame(player, hand, hitResult.getEntity());
        return result;
    }

    public static InteractionResult onTraceBlock(Player player, InteractionHand hand, BlockHitResult result) {
        return InteractionResult.PASS;
    }

    /**
     * {@link IMMPlayerEvents#onPlayerInteractSpec(PlayerInteractEvent.EntityInteractSpecific)}
     * {@link IMMPlayerEvents#onPlayerRightClickItem(PlayerInteractEvent.RightClickItem)}
     * {@link IMMPlayerEvents#onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock)}
     * {@link IMMPlayerEvents#onPlayerRightClickEmpty(PlayerInteractEvent.RightClickEmpty)}
     */
    public static InteractionResult rayTrace(Player player, InteractionHand hand) {
        final HitResult hitResult = EntityUtil.getHitResult(player, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY);
        InteractionResult result = InteractionResult.PASS;
        switch (hitResult.getType()){
            case BLOCK -> {
                result = onTraceBlock(player, hand, (BlockHitResult) hitResult);
            }
            case ENTITY -> {
                result = onTraceEntity(player, hand, (EntityHitResult) hitResult);
            }
        }
        return result;
    }

}
