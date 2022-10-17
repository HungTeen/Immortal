package hungteen.immortal.api.interfaces;

import hungteen.htlib.interfaces.INameEntry;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 12:18
 **/
public interface IElixirType extends INameEntry {

    /**
     * 丹药的品级。
     */
    IElixirRank getElixirRank();

    /**
     * 所需的境界。
     */
    IRealm getLimitRealm();

    /**
     * 丹药灵气含量。
     */
    int getSpiritualValue();

    /**
     * 服用丹药的时间。
     */
    default int getEatDuration(){
        return 30;
    }

}
