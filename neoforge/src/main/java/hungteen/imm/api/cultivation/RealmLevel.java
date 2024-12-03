package hungteen.imm.api.cultivation;

import net.minecraft.util.StringRepresentable;

/**
 * 作为统一的大境界参考。
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/2 16:56
 **/
public enum RealmLevel implements StringRepresentable {

    MORTALITY,

    QI_REFINING,

    FOUNDATION,

    CORE_SHAPING,

    GOLDEN_CORE,

    ;

    /**
     * 根据境界值，计算当前的大境界。
     * @return 大境界。
     */
    public static RealmLevel of(int realmValue){
        return realmValue == 0 ? MORTALITY : values()[Math.clamp(realmValue / 100 + 1, 0, values().length - 1)];
    }

    /**
     * 和另一个大境界进行比较。
     * @param level 另一个大境界。
     * @return 该境界大于等于另一个境界。
     */
    public boolean atLeast(RealmLevel level){
        return ordinal() >= level.ordinal();
    }

    /**
     * 和另一个大境界进行比较。
     * @param level 另一个大境界。
     * @return 该境界小于等于另一个境界。
     */
    public boolean atMost(RealmLevel level){
        return ordinal() <= level.ordinal();
    }

    /**
     * @return 精神领域对应的层数。
     */
    public int getRealmRegionLevel(){
        return ordinal() + 1;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
