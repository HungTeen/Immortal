package hungteen.imm.api.spell;

import hungteen.htlib.api.registry.SimpleEntry;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;

import java.util.Optional;

/**
 * 法术。
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-29 12:30
 **/
public interface SpellType extends SimpleEntry {

    /* Priority Enum */

    int VERY_HIGH = 100;
    int HIGH = 50;
    int MID_HIGH = 25;
    int LITTLE_HIGH = 10;
    int DEFAULT = 0;
    int LITTLE_LOW = -10;
    int MID_LOW = -25;
    int LOW = -50;
    int VERY_LOW = -100;

    /**
     * @param context 施法上下文。
     * @return 返回 true 则表示法术被触发。
     */
    default boolean checkActivate(SpellCastContext context){
        return false;
    }

    /**
     * 此法术有多少层。
     * @return How many levels does the spell have.
     */
    int getMaxLevel();

    /**
     * 施放此法术需要消耗多少灵力。
     * @return How many spiritual qi will cost, only consider the start stage.
     */
    int getConsumeQi();

    /**
     * 此法术的冷却时间。
     * @return Each releasing cool down.
     */
    int getCooldown();

    /**
     * 学习此法术可以获得多少修为。
     * @param level 法术对应的等级
     * @return 增长的修为值。
     */
    float getLearnPoint(int level);

    /**
     * 法术是否能被触发（包括主动和被动）。
     * @return Whether can the spell be triggered.
     */
    boolean canTrigger();

    /**
     * 法术触发时的音效。
     * @return The sound when the spell is triggered.
     */
    default Optional<SoundEvent> getTriggerSound(){
        return Optional.empty();
    }

    /**
     * @return 法术的实战优先级。
     */
    default int getCastingPriority(LivingEntity living){
        return getCategory().getDefaultPriority();
    }

    /**
     * @return 法术的 GUI 显示优先级。
     */
    int getScreenPriority();

    /**
     * @return 法术的模组优先级。
     */
    int getModPriority();

    /**
     * @return 法术的射线检测方块模式。
     */
    default ClipContext.Block getBlockClipMode(int level){
        return ClipContext.Block.COLLIDER;
    }

    /**
     * @return 法术的射线检测流体模式。
     */
    default ClipContext.Fluid getFluidClipMode(int level){
        return ClipContext.Fluid.NONE;
    }

    /**
     * 此法术是否为玩家专属，其他生物不需要学。
     * @return Whether can the spell be learned by non-player entities.
     */
    SpellUsageCategory getCategory();

    /**
     * 控制此法术能够绘制在哪些法器上。
     * @return 绘制类型。
     */
    InscriptionType getInscriptionType(int level);

    /**
     * @return 检查符咒是否可以与触发条件兼容。
     */
    default boolean compatWith(TriggerCondition condition, int level){
        return true;
    }

    /**
     * 获取各个等级的法术描述。
     * @param level 法术等级。
     * @return Get the spell description.
     */
    MutableComponent getSpellDesc(int level);

    /**
     * 获取法术贴图位置、
     * @return Get the texture location for this spell.
     */
    ResourceLocation getSpellTexture();

}
