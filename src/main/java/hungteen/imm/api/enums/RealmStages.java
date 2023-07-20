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
    PRELIMINARY(0F),

    /**
     * 中期。
     */
    MIDTERM(0.35F),

    /**
     * 后期。
     */
    SOPHISTICATION(0.7F),

    /**
     * 小圆满。
     */
    CLOSE_TO_PERFECTION(0.9F),

    /**
     * 大圆满。
     */
    PERFECTION(1F);
    ;

    private final float percent;

    RealmStages(float percent) {
        this.percent = percent;
    }

    public float getPercent() {
        return percent;
    }
}
