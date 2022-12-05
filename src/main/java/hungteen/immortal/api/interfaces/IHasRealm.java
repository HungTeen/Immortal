package hungteen.immortal.api.interfaces;

import hungteen.immortal.api.registry.IRealmType;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 12:57
 **/
public interface IHasRealm {

    /**
     * 获取生物的境界。
     */
    IRealmType getRealm();
}
