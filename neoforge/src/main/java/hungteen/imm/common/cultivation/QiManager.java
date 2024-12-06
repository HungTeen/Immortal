package hungteen.imm.common.cultivation;

import hungteen.imm.api.entity.HasQi;
import hungteen.imm.common.entity.IMMAttributes;
import hungteen.imm.common.entity.misc.FlyingItemEntity;
import hungteen.imm.util.Constants;
import hungteen.imm.util.LevelUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/28 15:08
 **/
public class QiManager {

    public static float getQiAmount(Entity entity) {
        return entity instanceof Player player ? PlayerUtil.getQiAmount(player) : entity instanceof HasQi manaEntity ? manaEntity.getQiAmount() : 0;
    }

    public static boolean isQiEmpty(Entity entity) {
        return getQiAmount(entity) > 0;
    }

    public static boolean isQiFull(Entity entity) {
        return getQiAmount(entity) >= getMaxQi(entity);
    }

    public static void addQi(Entity entity, float amount){
        if(entity instanceof Player player){
            PlayerUtil.addQiAmount(player, amount);
        } else if (entity instanceof HasQi qiEntity) {
            if(! qiEntity.isQiFull()){
                qiEntity.addQiAmount(amount);
            }
        }
    }

    public static boolean hasEnoughQi(Entity entity, float amount){
        if(entity instanceof Player player){
            return PlayerUtil.getQiAmount(player) >= amount;
        } else if (entity instanceof HasQi qiEntity) {
            return qiEntity.getQiAmount() >= amount;
        }
        return false;
    }

    /**
     * 灵气自然增长。
     * @param entity 生物。
     */
    public static void increaseQi(Entity entity) {
        if(canManaNaturalIncrease(entity)){
            float rate = LevelUtil.getSpiritualRate(entity.level(), entity.blockPosition());
            addQi(entity, rate);
        }
    }

    public static double getMaxQi(Entity entity){
        if(entity instanceof HasQi hasQi){
            return hasQi.getMaxQiAmount();
        } else if(entity instanceof LivingEntity living){
            return getLivingMaxQi(living);
        }
        return 0;
    }

    public static double getLivingMaxQi(LivingEntity living){
        AttributeInstance attribute = living.getAttribute(IMMAttributes.MAX_QI_AMOUNT.holder());
        return attribute == null ? 0 : attribute.getValue();
    }

    public static boolean canManaNaturalIncrease(Entity entity) {
        return !(entity.getVehicle() instanceof FlyingItemEntity)
                && (entity.getId() + entity.level().getGameTime()) % Constants.SPIRITUAL_ABSORB_TIME == 0
                && !ElementManager.isActiveReaction(entity, ElementReactions.PARASITISM);
    }

}
