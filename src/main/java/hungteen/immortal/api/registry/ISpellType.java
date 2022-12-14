package hungteen.immortal.api.registry;

import hungteen.htlib.api.interfaces.ISimpleEntry;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 12:30
 *
 * 法术。
 **/
public interface ISpellType extends ISimpleEntry {

    /**
     * 此法术有多少层。
     * How many levels does the spell have.
     */
    int getMaxLevel();

    /**
     * 施放此法术需要消耗多少灵力，只考虑开始阶段。
     * how many spiritual energy will cost, only consider the start stage.
     */
    int getStartMana();

    /**
     * 施放此法术需要消耗多少灵力，中间阶段。
     * how many spiritual energy will cost, only consider the using stage.
     */
    int getContinueMana();

    /**
     * 此法术的持续时间。
     * Duration of this spell.
     */
    int getDuration();

    /**
     * 学习此法术需要的最低境界。
     * The lowest realm required to learn this spell.
     * @return
     */
    IRealmType requireRealm(int level);

    /**
     * 学习此法术需要什么灵根。
     * How many types of roots required to learn this spell.
     */
    List<ISpiritualType> requireSpiritualRoots();

    /**
     * 学习此法术需要的前置法术。
     * The pre spells required to learn this spell.
     */
    List<ISpellType> requirePreSpells();

    /**
     * 是否有功法秘籍的物品。
     * Weather the specific spell has tutorial book.
     */
    boolean hasTutorialBook();

    /**
     * 获取法术贴图位置、
     * Get the assess method for this spell.
     * @return
     */
    ResourceLocation getSpellTexture();

}
