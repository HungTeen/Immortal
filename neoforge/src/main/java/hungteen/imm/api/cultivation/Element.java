package hungteen.imm.api.cultivation;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

import java.util.stream.Stream;

/**
 * 五行元素 & 业元素。
 * @author PangTeen
 * @program Immortal
 * @create 2023/2/25 10:30
 */
public enum Element implements StringRepresentable {

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

    public static final Codec<Element> CODEC = StringRepresentable.fromEnum(Element::values);
    private final boolean isPhysical;
    private final float attachFactor;

    /**
     * @param isPhysical 是否是物质元素，只有业元素师精神元素。
     * @param attachFactor 附着系数，决定了元素衰减的快慢。
     */
    Element(boolean isPhysical, float attachFactor) {
        this.isPhysical = isPhysical;
        this.attachFactor = attachFactor;
    }

    public boolean isPhysical() {
        return isPhysical;
    }

    public boolean isSpiritual() {
        return !isPhysical;
    }

    public static Stream<Element> getPhysicalElements() {
        return Stream.of(values()).filter(Element::isPhysical);
    }

    public static Stream<Element> getSpiritualElements() {
        return Stream.of(values()).filter(Element::isSpiritual);
    }

    public float getAttachFactor() {
        return attachFactor;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
