package hungteen.imm.api.cultivation;

import hungteen.htlib.api.registry.EnumEntry;
import org.jetbrains.annotations.NotNull;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/2/24 9:51
 */
public interface RealmStage extends EnumEntry {

    float getPercent();

    boolean canLevelUp();

    default boolean hasThreshold() {
        return !canLevelUp();
    }

    @Override
    @NotNull
    default String getSerializedName() {
        return name().toLowerCase();
    }
}
