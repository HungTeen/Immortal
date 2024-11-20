package hungteen.imm.api.interfaces;

import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.IRealmType;

import java.util.Optional;

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
     * 获取生物当前阶段。
     * @return Stage of entity.
     */
    Optional<RealmStages> getRealmStageOpt();

    /**
     * 获取生物的修行方式。
     * @return Cultivate type of entity.
     */
    default ICultivationType getCultivationType(){
        return getRealm().getCultivationType();
    }

}
