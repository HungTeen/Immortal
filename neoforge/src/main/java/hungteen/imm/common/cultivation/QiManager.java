package hungteen.imm.common.cultivation;

import hungteen.imm.api.cultivation.IHasQi;
import hungteen.imm.common.entity.misc.FlyingItemEntity;
import hungteen.imm.util.Constants;
import hungteen.imm.util.LevelUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/28 15:08
 **/
public class QiManager {

    public static void addQi(Entity entity, float amount){
        if(entity instanceof Player player){
            PlayerUtil.addQiAmount(player, amount);
        } else if (entity instanceof IHasQi qiEntity) {
            if(! qiEntity.isQiFull()){
                qiEntity.addQiAmount(amount);
            }
        }
    }

    /**
     * 灵气自然增长。
     * @param entity 生物。
     */
    public static void increaseQi(Entity entity) {
        if(canManaIncrease(entity)){
            float rate = LevelUtil.getSpiritualRate(entity.level(), entity.blockPosition());
            addQi(entity, rate);
        }
    }

    public static boolean canManaIncrease(Entity entity) {
        return !(entity.getVehicle() instanceof FlyingItemEntity)
                && (entity.getId() + entity.level().getGameTime()) % Constants.SPIRITUAL_ABSORB_TIME == 0
                && !ElementManager.isActiveReaction(entity, ElementReactions.PARASITISM);
    }
}
