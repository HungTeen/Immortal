package hungteen.imm.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/10 15:51
 */
public class ClientUtil {

    public static Optional<Minecraft> mc(){
        return Optional.ofNullable(IMMClientProxy.mc());
    }

    public static Optional<MultiPlayerGameMode> mode(){
        return Optional.ofNullable(IMMClientProxy.mc().gameMode);
    }

    public static Options option(){
        return IMMClientProxy.mc().options;
    }

    public static LocalPlayer player(){
        return IMMClientProxy.mc().player;
    }

    public static Screen screen(){
        return IMMClientProxy.mc().screen;
    }

    public static ClientLevel level(){
        return IMMClientProxy.mc().level;
    }

    public static SoundManager soundManager(){
        return IMMClientProxy.mc().getSoundManager();
    }

    public static Camera camera(){
        return IMMClientProxy.mc().gameRenderer.getMainCamera();
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
        IMMClientProxy.mc().getProfiler().push(name);
    }

    public static void pop(){
        IMMClientProxy.mc().getProfiler().pop();
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
