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
    METAL(true, 0.5F),

    /**
     * 木元素。
     */
    WOOD(true, 0.6F),

    /**
     * 水元素。
     */
    WATER(true, 0.25F),

    /**
     * 火元素。
     */
    FIRE(true, 1.5F),

    /**
     * 土元素。
     */
    EARTH(true, 1F),

    /**
     * 业元素。
     */
    SPIRIT(false, 1F);

    public static final Codec<Elements> CODEC = StringRepresentable.fromEnum(Elements::values);
    private final boolean isPhysical;
    private final float attachFactor;

    Elements(boolean isPhysical, float attachFactor) {
        this.isPhysical = isPhysical;
        this.attachFactor = attachFactor;
    }

    public boolean isPhysical() {
        return isPhysical;
    }

    public float getAttachFactor() {
        return attachFactor;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
