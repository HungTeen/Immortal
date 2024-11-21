package hungteen.imm.api.registry;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.api.registry.SimpleEntry;
import hungteen.imm.api.cultivation.Element;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;

import java.util.Set;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 16:03
 **/
public interface ISpiritualType extends SimpleEntry, WeightedEntry {

    /**
     * 此灵根包含哪些基本元素。
     * @return basic elements.
     */
    Set<Element> getElements();

    /**
     * 默认只有只含一种五行元素的灵根为普通灵根。
     */
    default boolean isCommonRoot(){
        return getElements().size() == 1 && ! getElements().contains(Element.SPIRIT);
    }

    /**
     * 灵根产生的权重。
     * the spawn weight of spiritual roots.
     */
    @Override
    Weight getWeight();

    /**
     * 对应的灵气是什么颜色。
     */
    int getSpiritualColor();

    /**
     * 元素图标的位置。
     */
    Pair<Integer, Integer> getTexturePos();

    /**
     * 图片的地址
     */
    ResourceLocation getTexture();

//    /**
//     * 炼丹炉中的排序。
//     */
//    int getSortPriority();

}
