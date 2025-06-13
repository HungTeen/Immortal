package hungteen.imm.common.cultivation;

import hungteen.htlib.util.WeightedList;
import hungteen.htlib.util.helper.JavaHelper;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.api.entity.HasQi;
import hungteen.imm.api.entity.HasRoot;
import hungteen.imm.common.IMMConfigs;
import hungteen.imm.common.entity.IMMAttributes;
import hungteen.imm.common.entity.misc.FlyingItemEntity;
import hungteen.imm.util.Constants;
import hungteen.imm.util.LevelUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理灵气和灵根相关的方法。
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/28 15:08
 **/
public class QiManager {

    public static final int MAX_ROOT_AMOUNT = 5;

    /**
     * 玩家灵根的生成规则： <br>
     * 1. 首先依据概率选择是几个灵根（0 - 5）。 <br>
     * 2. 如果是1个灵根，那么依据权重在普通灵根和异灵根中选择一个。 <br>
     * 3. 否则依据权重在普通五行灵根中选择若干个。 <br>
     *
     * @return 随机生成的灵根。
     */
    public static List<QiRootType> getQiRoots(RandomSource random) {
        return getQiRoots(random, IMMConfigs.getRootChances());
    }

    /**
     * @param rootChances 每个灵根数量的概率，建议数组长度为 5。
     */
    public static List<QiRootType> getQiRoots(RandomSource random, double[] rootChances) {
        double chance = random.nextDouble();
        for (int i = 0; i < Math.max(Constants.MAX_ROOT_AMOUNT, rootChances.length); ++i) {
            if (chance < rootChances[i]) {
                return randomSpawnRoots(random, i);
            }
            chance -= rootChances[i];
        }
        return randomSpawnRoots(random, 5);
    }

    /**
     * 只有灵根数量为 1 时，才需要考虑变异灵根。
     * {@link #getQiRoots(RandomSource)}
     */
    private static List<QiRootType> randomSpawnRoots(RandomSource random, int rootCount) {
        final List<QiRootType> rootChosen = new ArrayList<>();
        if (rootCount == 1) {
            rootChosen.addAll(WeightedList.create(QiRootTypes.registry().getValues().stream().toList()).getRandomItems(random, 1, true));
        } else if (rootCount > 1) {
            rootChosen.addAll(WeightedList.create(QiRootTypes.registry().getValues().stream()
                    .filter(JavaHelper.not(QiRootType::isSpecialRoot))
                    .collect(Collectors.toList())
            ).getRandomItems(random, rootCount, true));
        }
        return rootChosen;
    }

    /**
     * @return 获取实体的灵根。
     */
    public static List<QiRootType> getRoots(Entity entity){
        if(entity instanceof Player player){
            return PlayerUtil.getRoots(player);
        } else if(entity instanceof HasRoot hasRoot){
            return hasRoot.getRoots().stream().toList();
        }
        return List.of();
    }

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

    /**
     * 是否可能有灵根，不可能有灵根就不显示。
     */
    public static boolean mayHaveRoots(Entity entity){
        return entity instanceof LivingEntity;
    }

}
