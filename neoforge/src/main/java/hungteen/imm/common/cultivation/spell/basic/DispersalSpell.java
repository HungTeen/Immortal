package hungteen.imm.common.cultivation.spell.basic;

import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import net.minecraft.server.level.ServerPlayer;

/**
 * TODO 神识。
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/18 14:25
 */
public class DispersalSpell extends SpellTypeImpl {

    public DispersalSpell() {
        super("dispersal", property().maxLevel(1).qi(5).cd(100).notTrigger());
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
//        // 精神世界不能用神识。
//        if(! SpiritWorldDimension.isSpiritWorld(context.level())){
//            if(context.owner() instanceof ServerPlayer player) {
//                // 召唤一个玩家替身在原处。
//                EntityUtil.create(player.serverLevel(), IMMEntities.REALITY_PLAYER.get(), player.blockPosition(), MobSpawnType.EVENT).ifPresent(realityPlayer -> {
//                    realityPlayer.setOwner(player);
//                    realityPlayer.setSpirit(false);
//                    player.serverLevel().addFreshEntity(realityPlayer);
//                });
//                turnPlayerSpirit(player);
//            }
//            return true;
//        } else {
//            return false;
//        }
        return false;
    }

    public static void turnPlayerSpirit(ServerPlayer player) {
//        player.getAbilities().mayfly = false;
//        player.setNoGravity(false);
    }

}
