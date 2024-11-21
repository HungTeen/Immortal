package hungteen.imm.common.impl.codec;

import hungteen.htlib.api.registry.HTCodecRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.common.codec.ElixirEffect;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/18 14:56
 */
public interface ElixirEffects {

    HTCodecRegistry<ElixirEffect> SETTING = HTRegistryManager.codec(Util.prefix("elixir_effect"), () -> ElixirEffect.CODEC, () -> ElixirEffect.CODEC);

    ResourceKey<ElixirEffect> SPEED_UP = create("speed_up");
    ResourceKey<ElixirEffect> SLOW_DOWN = create("slow_down");

    static void register(BootstrapContext<ElixirEffect> context) {
        context.register(SPEED_UP, new ElixirEffect(MobEffects.MOVEMENT_SPEED, List.of(1)));
        context.register(SLOW_DOWN, new ElixirEffect(MobEffects.MOVEMENT_SLOWDOWN, List.of(2)));
    }

    static HTCodecRegistry<ElixirEffect> registry() {
        return SETTING;
    }

    static ResourceKey<ElixirEffect> create(String name) {
        return registry().createKey(Util.prefix(name));
    }
}
