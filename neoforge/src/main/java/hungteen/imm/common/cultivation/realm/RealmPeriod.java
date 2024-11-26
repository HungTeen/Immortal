package hungteen.imm.common.cultivation.realm;

import hungteen.imm.api.cultivation.RealmStage;
import hungteen.imm.util.Util;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/26 15:03
 **/
public enum RealmPeriod implements RealmStage {

    /**
     * 前期。
     */
    PRELIMINARY(0F, false),

    /**
     * 中期。
     */
    MIDTERM(0.35F, false),

    /**
     * 后期。
     */
    SOPHISTICATION(0.7F, false),

    /**
     * 小圆满。
     */
    CLOSE_TO_PERFECTION(0.9F, true),

    /**
     * 大圆满。
     */
    PERFECTION(1F, true),

    ;

    private final float percent;
    private final boolean canLevelUp;

    RealmPeriod(float percent, boolean canLevelUp) {
        this.percent = percent;
        this.canLevelUp = canLevelUp;
    }

    @Override
    public float getPercent() {
        return percent;
    }

    @Override
    public boolean canLevelUp() {
        return canLevelUp;
    }

    @Override
    public boolean hasNextStage() {
        return this != PERFECTION;
    }

//    public static RealmStage next(RealmStage stage) {
//        return RealmStage.values()[Math.min(stage.ordinal() + 1, RealmStage.values().length - 1)];
//    }

    @Override
    public String getModID() {
        return Util.id();
    }
}
