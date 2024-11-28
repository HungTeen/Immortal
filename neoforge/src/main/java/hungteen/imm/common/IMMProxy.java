package hungteen.imm.common;

import hungteen.htlib.platform.HTLibPlatformAPI;
import hungteen.imm.api.spell.ElementReaction;
import hungteen.imm.client.IMMClientProxy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-16 21:18
 **/
public class IMMProxy {

    private static volatile IMMProxy instance;

    /**
     * @return 根据客户端服务端环境获取代理对象。
     */
    public static IMMProxy get() {
        if (instance == null) {
            synchronized (IMMProxy.class) {
                if (instance == null) {
                    if(HTLibPlatformAPI.get().isPhysicalClient()){
                        instance = new IMMClientProxy();
                    } else {
                        instance = new IMMProxy();
                    }
                }
            }
        }
        return instance;
    }

    public void onSmithing(BlockPos blockPos, boolean isMainHand){

    }

    public void addReaction(Entity entity, ElementReaction reaction){

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
