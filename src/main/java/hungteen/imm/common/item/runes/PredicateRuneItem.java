package hungteen.imm.common.item.runes;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-02 22:25
 **/
public class PredicateRuneItem<T> extends Item {
    public PredicateRuneItem(Properties properties) {
        super(properties);
    }

    Optional<Predicate<T>> getInfo(ItemStack stack){
        return Optional.empty();
    }
}
