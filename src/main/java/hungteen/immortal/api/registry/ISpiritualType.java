package hungteen.immortal.api.registry;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.api.interfaces.ISimpleEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 16:03
 **/
public interface ISpiritualType extends ISimpleEntry, WeightedEntry {

    /**
     * 只有金木水火土五种灵根是普通灵根，值为false即为异灵根。
     * Only 5 common roots (metal, wood, water, fire, earth), the others are all special roots.
     */
    boolean isCommonRoot();

    /**
     * 灵根产生的权重。
     * the spawn weight of spiritual roots.
     */
    Weight getWeight();

    /**
     * 对应的灵气是什么颜色。
     */
    int getSpiritualColor();

    /**
     * 元素图标的位置。
     */
    Pair<Integer, Integer> getTexturePosition();

    /**
     * 图片的地址
     */
    ResourceLocation getResourceLocation();

    /**
     * 炼丹炉中的排序。
     */
    int getSortPriority();


}
