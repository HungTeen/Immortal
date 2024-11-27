package hungteen.imm.client.util;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.imm.client.IMMClientProxy;
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
 * @create 2023/7/10 15:51
 */
public class ClientUtil {

    public static Minecraft mc(){
        return IMMClientProxy.mc();
    }

    public static Optional<MultiPlayerGameMode> mode(){
        return Optional.ofNullable(mc().gameMode);
    }

    public static Options option(){
        return mc().options;
    }

    public static LocalPlayer player(){
        return mc().player;
    }

    public static Optional<LocalPlayer> playerOpt(){
        return Optional.ofNullable(player());
    }

    public static Screen screen(){
        return mc().screen;
    }

    public static ClientLevel level(){
        return mc().level;
    }

    public static SoundManager soundManager(){
        return mc().getSoundManager();
    }

    public static Camera camera(){
        return mc().gameRenderer.getMainCamera();
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
        mc().getProfiler().push(name);
    }

    public static void pop(){
        mc().getProfiler().pop();
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
