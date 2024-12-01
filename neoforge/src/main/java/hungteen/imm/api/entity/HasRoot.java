package hungteen.imm.api.entity;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.QiRootType;

import java.util.Set;

/**
 * 具有灵根的生物。
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-20 12:43
 **/
public interface HasRoot {

    /**
     * 生物的灵根种类。
     */
    Set<QiRootType> getRoots();

    /**
     * @return 是否具有指定的灵根。
     */
    default boolean hasRoot(QiRootType rootType){
        return getRoots().contains(rootType);
    }

    /**
     * @return 是否具有指定的元素。
     */
    default boolean hasElement(Element element){
        return getRoots().stream().anyMatch(root -> root.containsElement(element));
    }

}
