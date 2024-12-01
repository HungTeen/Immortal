package hungteen.imm.common.cultivation.realm;

import hungteen.imm.api.cultivation.RealmStage;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.MutableComponent;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/26 15:03
 **/
public enum RealmRank implements RealmStage {

    /**
     * 前期。
     */
    LOW(false),

    /**
     * 中期。
     */
    MID(false),

    /**
     * 后期。
     */
    HIGH(true),

    ;

    private final boolean canLevelUp;

    RealmRank(boolean canLevelUp) {
        this.canLevelUp = canLevelUp;
    }

    @Override
    public boolean canLevelUp(RealmType type) {
        return canLevelUp;
    }

    @Override
    public boolean hasThreshold(RealmType type) {
        return true;
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
