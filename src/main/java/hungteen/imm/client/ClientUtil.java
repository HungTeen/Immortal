package hungteen.imm.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/10 15:51
 */
public class ClientUtil {

    public static Optional<MultiPlayerGameMode> mode(){
        return Optional.ofNullable(ClientProxy.mc().gameMode);
    }

    public static Options option(){
        return ClientProxy.mc().options;
    }

    public static LocalPlayer player(){
        return ClientProxy.mc().player;
    }

    public static Screen screen(){
        return ClientProxy.mc().screen;
    }

    public static ClientLevel level(){
        return ClientProxy.mc().level;
    }

    public static void push(String name, Runnable runnable) {
        push(name);
        runnable.run();
        pop();
    }

    public static void popPush(String name){
        pop();
        push(name);
    }

    public static void push(String name){
        ClientProxy.mc().getProfiler().push(name);
    }

    public static void pop(){
        ClientProxy.mc().getProfiler().pop();
    }

    public static ModelResourceLocation getModelLocation(ResourceLocation location){
        return new ModelResourceLocation(location, "inventory");
    }

    public static InputConstants.Key getKey(int key){
        return InputConstants.getKey(key, 0);
    }

    public static boolean canRenderOverlay() {
        return screen() == null && ! option().hideGui && level() != null && player() != null && ! player().isSpectator();
    }

}
