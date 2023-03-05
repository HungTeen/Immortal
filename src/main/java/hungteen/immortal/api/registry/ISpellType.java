package hungteen.immortal.api.registry;

import hungteen.htlib.api.interfaces.ISimpleEntry;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * 法术。
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 12:30
 **/
public interface ISpellType extends ISimpleEntry {

//    void start(LivingEntity owner, int level);

    /**
     * 此法术有多少层。
     * @return How many levels does the spell have.
     */
    int getMaxLevel();

    /**
     * 施放此法术需要消耗多少灵力，只考虑开始阶段。
     * @return How many spiritual mana will cost, only consider the start stage.
     */
    int getStartMana();

    /**
     * 施放此法术需要消耗多少灵力，中间阶段。
     * @return How many spiritual mana will cost, only consider each using stage.
     */
    int getContinueMana();

    /**
     * 此法术的持续时间。
     * @return Active duration of this spell.
     */
    int getDuration();

    /**
     * 此法术的冷却时间。
     * @return each releasing cool down.
     */
    int getCooldown();

    /**
     * 学习此法术需要什么灵根。
     * @return How many types of roots required to learn this spell.
     */
    List<ISpiritualType> requireSpiritualRoots();

    /**
     * 获取法术贴图位置、
     * @return Get the texture location for this spell.
     */
    ResourceLocation getSpellTexture();

}
