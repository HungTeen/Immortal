package hungteen.immortal.api.interfaces;

import hungteen.htlib.interfaces.IComponentEntry;

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
}
