package hungteen.imm.common.cultivation.realm;

import hungteen.imm.api.cultivation.RealmStage;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.util.Util;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/26 15:17
 **/
public enum DummyPeriod implements RealmStage {

    DUMMY,

    COMMON,

    ;

    @Override
    public boolean canLevelUp(RealmType type) {
        return this == COMMON;
    }

    @Override
    public boolean hasThreshold(RealmType type) {
        return true;
    }

    @Override
    public String getModID() {
        return Util.id();
    }
}
