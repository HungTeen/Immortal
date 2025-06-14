package hungteen.imm.api.cultivation;

import hungteen.htlib.api.registry.EnumEntry;
import org.jetbrains.annotations.NotNull;

/**
 * 每个境界会有不同的阶段。
 * @author PangTeen
 * @program Immortal
 * @create 2023/2/24 9:51
 */
public interface RealmStage extends EnumEntry {

    /**
     * @return 此阶段能否升级到下一大境界。
     */
    boolean canLevelUp(RealmType type);

    /**
     * @return 此阶段是否需要突破瓶颈。
     */
    boolean hasThreshold(RealmType type);

    @Override
    @NotNull
    default String getSerializedName() {
        return name().toLowerCase();
    }
}
