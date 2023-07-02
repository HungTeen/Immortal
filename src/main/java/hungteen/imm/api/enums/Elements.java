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
    METAL(true, 0.05F, 0.25F),

    /**
     * 木元素。
     */
    WOOD(true, 0.06F, 0.15F),

    /**
     * 水元素。
     */
    WATER(true, 0.15F, 0.04F),

    /**
     * 火元素。
     */
    FIRE(true, 0.2F, 0.05F),

    /**
     * 土元素。
     */
    EARTH(true, 0.1F, 0.1F),

    /**
     * 业元素。
     */
    SPIRIT(false, 0.1F, 0.1F);

    private final boolean isPhysical;
    private final float attachChance;
    private final float decaySpeed;

    Elements(boolean isPhysical, float attachChance, float decaySpeed) {
        this.isPhysical = isPhysical;
        this.attachChance = attachChance;
        this.decaySpeed = decaySpeed;
    }

    public boolean isPhysical() {
        return isPhysical;
    }

    public float getAttachChance() {
        return attachChance;
    }

    public float getDecaySpeed() {
        return decaySpeed;
    }
}
