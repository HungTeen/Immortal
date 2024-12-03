package hungteen.imm.api.cultivation;

import hungteen.htlib.api.registry.SimpleEntry;
import hungteen.imm.api.IMMAPI;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Map;

/**
 * 跨境界体系的参考标准。
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-29 12:40
 **/
public interface RealmType extends SimpleEntry {

    ResourceLocation HEALTH_MODIFIER = ResourceLocation.fromNamespaceAndPath(IMMAPI.id(), "realm_health_modifier");
    ResourceLocation QI_MODIFIER = ResourceLocation.fromNamespaceAndPath(IMMAPI.id(), "realm_qi_modifier");
    ResourceLocation ARMOR_MODIFIER = ResourceLocation.fromNamespaceAndPath(IMMAPI.id(), "realm_armor_modifier");
//    ResourceLocation HEALTH_MODIFIER = ResourceLocation.fromNamespaceAndPath(IMMAPI.id(), "realm_health_modifier");

    /**
     * 此境界的最大修为。
     * @return how many xp needed for level up.
     */
    int maxCultivation();

    /**
     * 境界用一个数来对应，数越大境界越高。注意：这表示的是大圆满时的值！ <br>
     * @return the bigger the number is, the higher realm it has. Warning: the realm value represent the Perfection Stage.
     */
    int getRealmValue();

    /**
     * 根据境界值，计算当前的大境界。
     * @return 大境界的等级。
     */
    default RealmLevel getRealmLevel(){
        return RealmLevel.of(getRealmValue());
    }

    /**
     * @return 精神领域对应的层数。
     */
    default int getRealmRegionLevel(){
        return getRealmLevel().getRealmRegionLevel();
    }

    /**
     * 到达该境界时，更新该生物的属性。
     * @return 更新的属性。
     */
    Map<Holder<Attribute>, AttributeModifier> getAttributeModifiers();

    /**
     * @return 是否需要突破瓶颈。
     */
    default boolean hasThreshold(){
        return getStage().hasThreshold(this);
    }

    /**
     * @return 是否可以升级到下一个大境界。
     */
    default boolean canLevelUp() {
        return getStage().canLevelUp(this);
    }

    /**
     * @return 当前境界的阶段。
     */
    RealmStage getStage();

    /**
     * 修炼的类型。
     * @return Which way does living go.
     */
    CultivationType getCultivationType();

}
