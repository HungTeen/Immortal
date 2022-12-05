package hungteen.immortal.api.interfaces;

import hungteen.immortal.api.registry.ISpiritualType;

import java.util.Collection;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 12:43
 **/
public interface IHasRoot {

    /**
     * 生物的灵根种类。
     */
    Collection<ISpiritualType> getSpiritualTypes();

}
