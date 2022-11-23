package hungteen.immortal;

import hungteen.immortal.common.entity.formation.Formation;
import hungteen.immortal.common.world.data.Formations;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import java.util.List;
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

    public List<Formation> getFormations(Level level){
        if(level instanceof ServerLevel){
            return Formations.getFormations((ServerLevel)level);
        }
        return List.of();
    }

    public Optional<RecipeManager> getRecipeManager(){
        return Optional.empty();
    }
}
