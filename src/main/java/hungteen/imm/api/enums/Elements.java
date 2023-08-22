package hungteen.imm.api.enums;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

/**
 * 五行元素 & 业元素。
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/25 10:30
 */
public enum Elements implements StringRepresentable {

    /**
     * 金元素。
     */
    METAL(true, 0.5F, 0.25F),

    /**
     * 木元素。
     */
    WOOD(true, 0.6F, 0.15F),

    /**
     * 水元素。
     */
    WATER(true, 0.25F, 0.04F),

    /**
     * 火元素。
     */
    FIRE(true, 1.5F, 0.05F),

    /**
     * 土元素。
     */
    EARTH(true, 1F, 0.1F),

    /**
     * 业元素。
     */
    SPIRIT(false, 1F, 0.1F);

    public static final Codec<Elements> CODEC = StringRepresentable.fromEnum(Elements::values);
    private final boolean isPhysical;
    private final float attachMultiple;
    private final float decaySpeed;

    Elements(boolean isPhysical, float attachChance, float decaySpeed) {
        this.isPhysical = isPhysical;
        this.attachMultiple = attachChance;
        this.decaySpeed = decaySpeed;
    }

    public boolean isPhysical() {
        return isPhysical;
    }

    public float getAttachMultiple() {
        return attachMultiple;
    }

    public float getDecaySpeed() {
        return decaySpeed;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
