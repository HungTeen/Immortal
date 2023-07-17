package hungteen.imm.common.impl.manuals;

import hungteen.htlib.api.interfaces.IHTCodecRegistry;
import hungteen.htlib.common.registry.HTCodecRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.imm.common.codec.SecretManual;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/24 15:09
 */
public class SecretManuals {

    private static final HTCodecRegistry<SecretManual> TUTORIALS = HTRegistryManager.create(Util.prefix("secret_manual"), () -> SecretManual.CODEC, () -> SecretManual.CODEC);

    public static void register(BootstapContext<SecretManual> context){

    }

    public static IHTCodecRegistry<SecretManual> registry(){
        return TUTORIALS;
    }

    public static ResourceKey<SecretManual> create(String name) {
        return registry().createKey(Util.prefix(name));
    }
}
