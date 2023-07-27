package hungteen.imm.api.enums;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/24 9:51
 */
public enum RealmStages {

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
    PERFECTION(1F, true);
    ;

    private final float percent;
    private final boolean canLevelUp;

    RealmStages(float percent, boolean canLevelUp) {

        this.percent = percent;
        this.canLevelUp = canLevelUp;
    }

    public float getPercent() {
        return percent;
    }

    public boolean canLevelUp() {
        return canLevelUp;
    }
}
