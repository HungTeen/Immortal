package hungteen.imm.client;

import hungteen.htlib.client.util.ClientHelper;
import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.imm.api.spell.ElementReaction;
import hungteen.imm.client.data.ClientData;
import hungteen.imm.client.gui.screen.SecretManualScreen;
import hungteen.imm.client.gui.screen.meditation.SelectRootScreen;
import hungteen.imm.client.render.level.ReactionRenderer;
import hungteen.imm.client.util.ClientUtil;
import hungteen.imm.common.IMMProxy;
import hungteen.imm.common.cultivation.manual.SecretManual;
import hungteen.imm.common.network.SmithingPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
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

    @Override
    public void openQiRootScreen(Player player) {
        if(ClientHelper.screen() == null){
            mc().setScreen(new SelectRootScreen());
        }
    }

    @Override
    public void openManualScreen(Player player, SecretManual manual, int page, InteractionHand hand) {
        if(ClientHelper.screen() == null){
            mc().setScreen(new SecretManualScreen(manual, page, hand));
        }
    }
}
