package hungteen.imm.api.registry;

import hungteen.htlib.api.interfaces.ISimpleEntry;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.enums.SpellUsageCategories;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;

/**
 * 法术。
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 12:30
 **/
public interface ISpellType extends ISimpleEntry {

    /**
     * 法术被触发。
     * @param owner who activated this spell.
     * @param result target.
     * @param level spell level.
     * @return activate successfully.
     */
    default boolean checkActivate(LivingEntity owner, HTHitResult result, int level){
        return false;
    }

    /**
     * 此法术有多少层。
     * @return How many levels does the spell have.
     */
    int getMaxLevel();

    /**
     * 施放此法术需要消耗多少灵力。
     * @return How many spiritual mana will cost, only consider the start stage.
     */
    int getConsumeMana();

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
     * 法术是否能被放置在法术轮盘上。
     * @return Whether can the spell be place on circle.
     */
    boolean canPlaceOnCircle();

    int getPriority();

    int getModPriority();

    default ClipContext.Block getBlockClipMode(int level){
        return ClipContext.Block.COLLIDER;
    }

    default ClipContext.Fluid getFluidClipMode(int level){
        return ClipContext.Fluid.NONE;
    }

    /**
     * 此法术是否为玩家专属，其他生物不需要学。
     * @return Whether can the spell be learned by non-player entities.
     */
    SpellUsageCategories getCategory();

    MutableComponent getSpellDesc(int level);

    /**
     * 获取法术贴图位置、
     * @return Get the texture location for this spell.
     */
    ResourceLocation getSpellTexture();

}
