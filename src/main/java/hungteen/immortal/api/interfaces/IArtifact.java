package hungteen.immortal.api.interfaces;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-08 09:35
 *
 * 具有灵力的物品需要继承此接口
 **/
public interface IArtifact {

    /**
     * level 0 : 凡间物品。 <br>
     * level 1 - 3 : 法器（低阶、中阶、高阶）。 <br>
     * level 4 - 6 : 法宝（低阶、中阶、高阶）。 <br>
     * level 7 - 9 : 灵宝（低阶、中阶、高阶）。 <br>
     */
    int getArtifactLevel();

    /**
     * 是否为古宝，古代修仙者遗留，不可合成。
     */
    default boolean isAncientArtifact(){
        return false;
    }

}
