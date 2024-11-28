package hungteen.imm.common.cultivation;

import hungteen.imm.api.cultivation.IHasQi;
import hungteen.imm.common.entity.misc.FlyingItemEntity;
import hungteen.imm.util.Constants;
import hungteen.imm.util.LevelUtil;
import net.minecraft.world.entity.Entity;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/28 15:08
 **/
public class QiManager {

    /**
     * 灵气自然增长。
     * @param entity 生物。
     */
    public static void increaseQi(Entity entity) {
        if (entity instanceof IHasQi qiEntity && canManaIncrease(entity)) {
            if(! qiEntity.isQiFull()){
                qiEntity.addQiAmount(LevelUtil.getSpiritualRate(entity.level(), entity.blockPosition()));
            }
        }
    }

    public static boolean canManaIncrease(Entity entity) {
        return !(entity.getVehicle() instanceof FlyingItemEntity)
                && (entity.getId() + entity.level().getGameTime()) % Constants.SPIRITUAL_ABSORB_TIME == 0
                && !ElementManager.isActiveReaction(entity, ElementReactions.PARASITISM);
    }
}
