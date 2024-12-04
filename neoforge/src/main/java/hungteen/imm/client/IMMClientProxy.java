package hungteen.imm.client;

import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.imm.client.data.ClientData;
import hungteen.imm.client.util.ClientUtil;
import hungteen.imm.common.IMMProxy;
import hungteen.imm.api.spell.ElementReaction;
import hungteen.imm.client.render.level.ReactionRenderer;
import hungteen.imm.common.network.SmithingPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Objects;
import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-16 21:18
 **/
public class IMMClientProxy extends IMMProxy {

    public static final Minecraft MC = Minecraft.getInstance();

    public static Minecraft mc(){
        return MC;
    }

    @Override
    public void onSmithing(BlockPos blockPos, boolean isMainHand) {
        NetworkHelper.sendToServer(new SmithingPacket(blockPos, ClientData.SmithingProgress, isMainHand));
    }

    @Override
    public void addReaction(Entity entity, ElementReaction reaction) {
        ReactionRenderer.addReaction(entity, reaction);
    }

    @Override
    public boolean isDebugMode() {
        return ClientData.isDebugMode;
    }

    @Override
    public Optional<RecipeManager> getRecipeManager() {
        return Optional.of(Objects.requireNonNull(MC.level).getRecipeManager());
    }

    @Override
    public Optional<RegistryAccess> registryAccess() {
        return Optional.of(ClientUtil.level().registryAccess());
    }

    @Override
    public boolean isShiftKeyDown(){
        return Screen.hasShiftDown();
    }
}
