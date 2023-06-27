package hungteen.imm.api.enums;

/**
 * 五行元素 & 业元素。
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/25 10:30
 */
public enum Elements {

    /**
     * 金元素。
     */
    METAL(true),

    /**
     * 木元素。
     */
    WOOD(true),

    /**
     * 水元素。
     */
    WATER(true),

    /**
     * 火元素。
     */
    FIRE(true),

    /**
     * 土元素。
     */
    EARTH(true),

    /**
     * 业元素。
     */
    SPIRIT(false);

    private final boolean isPhysical;

    Elements(boolean isPhysical) {
        this.isPhysical = isPhysical;
    }

    public boolean isPhysical() {
        return isPhysical;
    }
}
