package hungteen.imm.api.interfaces;

import hungteen.imm.api.cultivation.QiRootType;

import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-20 12:43
 **/
public interface IHasRoot {

    /**
     * 生物的灵根种类。
     */
    List<QiRootType> getSpiritualTypes();

}
