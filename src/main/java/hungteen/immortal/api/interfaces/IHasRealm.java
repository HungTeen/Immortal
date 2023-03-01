package hungteen.immortal.api.interfaces;

import hungteen.immortal.api.registry.ICultivationType;
import hungteen.immortal.api.registry.IRealmType;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 12:57
 **/
public interface IHasRealm {

    /**
     * 获取生物的境界
     * @return Realm type of entity.
     */
    IRealmType getRealm();

    /**
     * 获取生物的修行方式
     * @return Cultivate type of entity
     */
    ICultivationType getCultivationType();
}
