package hungteen.imm.api.entity;

import hungteen.imm.api.cultivation.RealmType;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-20 12:57
 **/
public interface Cultivatable extends HasRealm, HasQi, HasRoot, SpellCaster {

    /**
     * 更新生物的境界。
     * @param newRealm New realm type.
     */
    void updateRealm(RealmType newRealm);

}
