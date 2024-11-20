package hungteen.imm.api.enums;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/24 9:51
 */
public enum RealmStages implements StringRepresentable {

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

    public static final Codec<RealmStages> CODEC = StringRepresentable.fromEnum(RealmStages::values);
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

    public boolean hasThreshold() {
        return ! canLevelUp();
    }

    public boolean hasNextStage(){
        return this != PERFECTION;
    }

    public static RealmStages next(RealmStages stage){
        return RealmStages.values()[Math.min(stage.ordinal() + 1, RealmStages.values().length - 1)];
    }

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }
}
