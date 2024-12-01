package hungteen.imm.api.entity;

import hungteen.imm.api.cultivation.CultivationType;
import hungteen.imm.api.cultivation.RealmStage;
import hungteen.imm.api.cultivation.RealmType;

/**
 * 有境界的。
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/30 12:55
 **/
public interface HasRealm {

    /**
     * 获取生物的境界
     * @return Realm type of entity.
     */
    RealmType getRealm();

    /**
     * 获取生物的修行方式。
     * @return Cultivate type of entity.
     */
    default CultivationType getCultivationType(){
        return getRealm().getCultivationType();
    }

    /**
     * 获取生物的境界阶段。
     * @return Realm stage of entity.
     */
    default RealmStage getRealmStage(){
        return getRealm().getStage();
    }
}
