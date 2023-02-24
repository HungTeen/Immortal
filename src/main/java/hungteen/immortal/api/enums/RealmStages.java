package hungteen.immortal.api.enums;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/24 9:51
 */
public enum RealmStages {

    /**
     * Pre Stage, 前期
     */
    PRELIMINARY(0.2F),

    /**
     * Mid Stage, 中期
     */
    MIDTERM(0.45F),

    /**
     * Post Stage, 后期
     */
    SOPHISTICATION(0.75F),

    /**
     * Complete Stage, 圆满
     */
    PERFECTION(0.9F),
    ;

    private final float percent;

    RealmStages(float percent) {
        this.percent = percent;
    }

    public float getPercent() {
        return percent;
    }
}
