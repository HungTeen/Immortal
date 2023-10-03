package hungteen.imm;

import hungteen.imm.api.registry.IElementReaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 21:18
 **/
public class CommonProxy {

    public void onSmithing(BlockPos blockPos, boolean isMainHand){

    }

    public void addReaction(Entity entity, IElementReaction reaction){

    }

    public boolean isDebugMode(){
        return false;
    }

    public Optional<RecipeManager> getRecipeManager(){
        return Optional.empty();
    }

    public Optional<RegistryAccess> registryAccess(){
        return Optional.empty();
    }

    public boolean isShiftKeyDown(){
        return false;
    }
}
