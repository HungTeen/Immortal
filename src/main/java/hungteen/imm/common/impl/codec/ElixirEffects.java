package hungteen.imm.common.impl.codec;

import hungteen.htlib.api.interfaces.IHTCodecRegistry;
import hungteen.htlib.common.registry.HTCodecRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.imm.common.codec.ElixirEffect;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/18 14:56
 */
public class ElixirEffects {

    private static final HTCodecRegistry<ElixirEffect> SETTING = HTRegistryManager.create(Util.prefix("elixir_effect"), () -> ElixirEffect.CODEC, () -> ElixirEffect.CODEC);

    public static final ResourceKey<ElixirEffect> SPEED_UP = create("speed_up");
    public static final ResourceKey<ElixirEffect> SLOW_DOWN = create("slow_down");

    public static void register(BootstapContext<ElixirEffect> context){
        context.register(SPEED_UP, new ElixirEffect(MobEffects.MOVEMENT_SPEED, List.of(1)));
        context.register(SLOW_DOWN, new ElixirEffect(MobEffects.MOVEMENT_SLOWDOWN, List.of(2)));
    }

    public static IHTCodecRegistry<ElixirEffect> registry(){
        return SETTING;
    }

    public static ResourceKey<ElixirEffect> create(String name){
        return registry().createKey(Util.prefix(name));
    }
}
