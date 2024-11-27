package hungteen.imm.common.cultivation.realm;

import hungteen.imm.api.cultivation.RealmStage;
import hungteen.imm.util.Util;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/26 15:17
 **/
public enum RealmLevel implements RealmStage {

    LEVEL_1,

    LEVEL_2,

    LEVEL_3,

    LEVEL_4,
    ;

    @Override
    public float getPercent() {
        return 0;
    }

    @Override
    public boolean canLevelUp() {
        return false;
    }

    @Override
    public String getModID() {
        return Util.id();
    }
}
