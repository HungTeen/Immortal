package hungteen.imm.api.cultivation;

import hungteen.htlib.api.registry.SimpleEntry;

/**
 * 跨境界体系的参考标准。
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-29 12:40
 **/
public interface RealmType extends SimpleEntry {

    /**
     * 此境界的最大修为。
     * @return how many xp needed for level up.
     */
    int maxCultivation();

    /**
     * 境界用一个数来对应，数越大境界越高。注意：这表示的是大圆满时的值！ <br>
     * @return the bigger the number is, the higher realm it has. Warning: the realm value represent the Perfection Stage.
     */
    int getRealmValue();

//    /**
//     * 根本的灵力值（不考虑后天加成）。
//     * @return the base spiritual value.
//     */
//    int getSpiritualValue();

    RealmStage getStage();

    /**
     * 修炼的类型。
     * @return Which way does living go.
     */
    CultivationType getCultivationType();

}
