package hungteen.immortal.client;

import hungteen.immortal.CommonProxy;
import hungteen.immortal.common.network.NetworkHandler;
import hungteen.immortal.common.network.SmithingPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Objects;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 21:18
 **/
public class ClientProxy extends CommonProxy {

    public static final Minecraft MC = Minecraft.getInstance();

    @Override
    public void openRestingScreen() {

    }

    @Override
    public void onSmithing(BlockPos blockPos, boolean isMainHand) {
        NetworkHandler.sendToServer(new SmithingPacket(blockPos, ClientDatas.SmithingProgress, isMainHand));
    }

    @Override
    public Optional<RecipeManager> getRecipeManager() {
        return Optional.of(Objects.requireNonNull(MC.level).getRecipeManager());
    }
}
