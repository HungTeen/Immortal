package hungteen.immortal.api.interfaces;

import hungteen.htlib.interfaces.IComponentEntry;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 12:30
 *
 * 法术。
 **/
public interface ISpell extends IComponentEntry {

    /**
     * 学习此法术需要什么灵根。
     * How many types of roots required to learn this spell.
     */
    List<ISpiritualRoot> requireSpiritualRoots();

    /**
     * 学习此法术需要的最低境界。
     * The lowest realm required to learn this spell.
     */
    int requireRealm();

    /**
     * 学习此法术需要的前置法术。
     * The pre spells required to learn this spell.
     */
    List<ISpell> requirePreSpells();

    /**
     * 施放此法术需要消耗多少灵力。
     * how many spiritual energy will cost.
     */
    int getCostEnergy();

    /**
     * 此法术的持续时间。
     * Duration of this spell.
     */
    int getDuration();


}
