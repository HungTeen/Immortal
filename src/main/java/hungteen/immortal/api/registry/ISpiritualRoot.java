package hungteen.immortal.api.registry;

import hungteen.htlib.interfaces.IComponentEntry;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 16:03
 *
 * 灵根。
 **/
public interface ISpiritualRoot extends IComponentEntry {

    /**
     * 只有金木水火土五种灵根是普通灵根，值为false即为异灵根。
     * Only 5 common roots (metal, wood, water, fire, earth), the others are all special roots.
     */
    boolean isCommonRoot();

    /**
     * 灵根产生的权重。
     * the spawn weight of spiritual roots.
     */
    int getWeight();


}
