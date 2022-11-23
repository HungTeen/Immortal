package hungteen.immortal.client;

import hungteen.immortal.CommonProxy;
import hungteen.immortal.common.entity.formation.Formation;
import hungteen.immortal.common.network.NetworkHandler;
import hungteen.immortal.common.network.SmithingPacket;
import hungteen.immortal.utils.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.List;
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
    public List<Formation> getFormations(Level level) {
        return ClientDatas.FormationMap.getOrDefault(Util.toString(level.dimension()), new HashSet<>()).stream()
                .map(level::getEntity)
                .filter(Formation.class::isInstance)
                .map(Formation.class::cast)
                .toList();
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
