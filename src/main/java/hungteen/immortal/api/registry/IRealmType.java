package hungteen.immortal.api.registry;

import hungteen.htlib.api.interfaces.ISimpleEntry;

/**
 * 跨境界体系的参考标准。
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 12:40
 **/
public interface IRealmType extends ISimpleEntry {

    /**
     * 达到此境界所需的修为。
     * @return how many xp needed for level up.
     */
    int requireCultivation();

    /**
     * 境界用一个数来对应，数越大境界越高。
     * @return the bigger the number is, the higher realm it has.
     */
    int getRealmValue();

    /**
     * 根本的灵力值（不考虑后天加成）。
     * @return the base spiritual value.
     */
    int getBaseSpiritualValue();

    /**
     * 能承载的极限灵气值。
     * @return the boundary value entity can take.
     */
    int getSpiritualValueLimit();

    /**
     * 有突破门槛。
     * @return whether can living naturally level up.
     */
    boolean hasThreshold();

    /**
     * 修炼的类型。
     * @return Which way does living go.
     */
    ICultivationType getCultivationType();

}
