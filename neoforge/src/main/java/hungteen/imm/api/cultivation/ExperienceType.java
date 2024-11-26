package hungteen.imm.api.cultivation;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/20 15:48
 */
public enum ExperienceType implements StringRepresentable {

    /**
     * Eat elixir to gain experience.
     */
    ELIXIR,

    /**
     * Fight with mobs to gain experience.
     */
    FIGHTING,

    /**
     * Learn spells to gain experience.
     */
    SPELL,

//    /**
//     * Complete missions to gain experience.
//     */
//    MISSION,

    /**
     * Trade with others.
     */
    PERSONALITY
    ;

    public static final Codec<ExperienceType> CODEC = StringRepresentable.fromEnum(ExperienceType::values);

    @Override
    public String getSerializedName() {
        return name();
    }
}
