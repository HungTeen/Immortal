package hungteen.imm.common.cultivation.realm;

import hungteen.imm.api.cultivation.RealmStage;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.MutableComponent;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/26 15:17
 **/
public enum RealmStep implements RealmStage {

    LEVEL_1,

    LEVEL_2,

    LEVEL_3,

    LEVEL_4,

    LEVEL_5,

    LEVEL_6,

    LEVEL_7,

    LEVEL_8,

    LEVEL_9,

    LEVEL_10,

    LEVEL_11,

    LEVEL_12,

    LEVEL_13,

    ;

    public static RealmStep[] values(int maxLevel) {
        RealmStep[] levels = new RealmStep[maxLevel];
        System.arraycopy(values(), 0, levels, 0, maxLevel);
        return levels;
    }

    @Override
    public boolean canLevelUp(RealmType type) {
        return ordinal() >= 9;
    }

    @Override
    public boolean hasThreshold(RealmType type) {
        return type == RealmTypes.QI_REFINING.level(13);
    }

    @Override
    public MutableComponent getComponent() {
        return TipUtil.misc("realm_stage." + name().toLowerCase());
    }

    @Override
    public String getModID() {
        return Util.id();
    }
}
