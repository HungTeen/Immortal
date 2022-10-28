package hungteen.immortal.api.registry;

import hungteen.htlib.interfaces.IComponentEntry;
import net.minecraft.world.item.Rarity;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 12:18
 **/
public interface IElixirType extends IComponentEntry {

    /**
     * 丹药的品级。
     * @return
     */
    Rarity getElixirRarity();

    /**
     * 检查食用者的境界。<br>
     * 返回empty时表示丹药食用无效。<br>
     * 返回false时有极大概率爆体而亡。<br>
     * 返回true时才是正确的食用。
     * @return
     */
    Function<IRealm, Optional<Boolean>> realmChecker();

    /**
     * 辅药的灵气含量标准。
     */
    Map<ISpiritualRoot, Integer> requireSpiritualMap();

//    /**
//     * 主药配方
//     */
//    Collection<Supplier<Item>> getRecipeItems();

    /**
     * 服用丹药的时间。
     */
    default int getEatDuration(){
        return 30;
    }

}
