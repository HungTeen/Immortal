package hungteen.imm.api.cultivation;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.api.registry.SimpleEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;

import java.util.Optional;
import java.util.Set;

/**
 * 灵根类型。
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-24 16:03
 **/
public interface QiRootType extends SimpleEntry, WeightedEntry {

    /**
     * 此灵根包含哪些基本元素。
     * @return basic elements.
     */
    Set<Element> getElements();

    /**
     * 是否包含某个元素。
     * @param element the element.
     * @return true if this root contains the element.
     */
    default boolean containsElement(Element element){
        return getElements().contains(element);
    }

    /**
     * 五行灵根为普通灵根。
     * @return true if this is a common root.
     */
    boolean isCommonRoot();

    /**
     * 获取变异灵根的概率。
     * @return 变异灵根的概率，范围在0-1之间。如果为空则说明不是变异灵根。
     */
    Optional<Float> getSpecialRootChance();

    /**
     * 是否为特殊变异灵根，此种灵根不能和任何灵根同时存在。
     * @return true if this is a special root.
     */
    default boolean isSpecialRoot(){
        return getSpecialRootChance().isPresent();
    }

    /**
     * 灵根产生的权重。
     * @return the spawn weight of spiritual roots.
     */
    @Override
    Weight getWeight();

    /**
     * @return 对应的灵气是什么颜色。
     */
    int getSpiritualColor();

    /**
     * @return 元素图标的位置。
     */
    Pair<Integer, Integer> getTexturePos();

    /**
     * @return 灵根图片的地址
     */
    ResourceLocation getTexture();

    /**
     * @return 排序优先级。
     */
    int getSortPriority();

}
