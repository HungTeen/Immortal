package hungteen.immortal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 21:18
 **/
public class CommonProxy {

    public void openRestingScreen(){
    }

    public void onSmithing(BlockPos blockPos, boolean isMainHand){

    }

    public Optional<RecipeManager> getRecipeManager(){
        return Optional.empty();
    }
}
