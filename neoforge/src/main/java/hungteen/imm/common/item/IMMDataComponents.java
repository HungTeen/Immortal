package hungteen.imm.common.item;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.common.codec.SpellInstance;
import hungteen.imm.common.impl.manuals.SecretManual;
import hungteen.imm.common.impl.manuals.SecretManuals;
import hungteen.imm.util.Util;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/15 10:13
 **/
public interface IMMDataComponents {

    HTVanillaRegistry<DataComponentType<?>> TYPES = HTRegistryManager.vanilla(Registries.DATA_COMPONENT_TYPE, Util.id());

    HTHolder<DataComponentType<ResourceKey<SecretManual>>> SECRET_MANUAL = register("secret_manual", builder -> builder
            .persistent(SecretManuals.resourceKeyCodec()).networkSynchronized(SecretManuals.resourceKeyStreamCodec())
    );
    HTHolder<DataComponentType<Integer>> MANUAL_PAGE = register("manual_page", builder -> builder
            .persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );
    HTHolder<DataComponentType<List<SpellInstance>>> SPELL_INSTANCES = register("spell_instances", builder -> builder
            .persistent(SpellInstance.CODEC_LIST).networkSynchronized(SpellInstance.STREAM_CODEC)
    );
    HTHolder<DataComponentType<Integer>> MAX_SPELL_SLOT = register("max_spell_slot", builder -> builder
            .persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    static <T> HTHolder<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return registry().register(name, () -> ((DataComponentType.Builder) builder.apply(DataComponentType.builder())).build());
    }

    static HTVanillaRegistry<DataComponentType<?>> registry() {
        return TYPES;
    }

    static void initialize(IEventBus event) {
        NeoHelper.initRegistry(registry(), event);
    }
}
