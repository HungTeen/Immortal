package hungteen.immortal.api.interfaces;

import hungteen.htlib.interfaces.IComponentEntry;

import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 12:40
 *
 * 境界。
 **/
public interface IRealm extends IComponentEntry {

    /**
     * 达到此境界所需的修为。
     */
    int getCultivation();

    /**
     * 境界用一个数来对应，数越大境界越高。
     */
    int getRealmValue();

    /**
     * 根本的灵力值（不考虑后天加成）。
     */
    int getBaseSpiritualValue();

    /**
     * 此境界有修炼瓶颈，不可自然提升。
     */
    boolean hasThreshold();

    /**
     * 是人类的境界标准。
     */
    boolean forHuman();
}
