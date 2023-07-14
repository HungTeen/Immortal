package hungteen.imm.client;

import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/10 15:51
 */
public class ClientUtil {

    public static Optional<MultiPlayerGameMode> getMode(){
        return Optional.ofNullable(ClientProxy.mc().gameMode);
    }

    public static Options getOption(){
        return ClientProxy.mc().options;
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

}
