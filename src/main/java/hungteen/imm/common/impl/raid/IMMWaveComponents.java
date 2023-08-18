package hungteen.imm.common.impl.raid;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.api.interfaces.raid.ISpawnComponent;
import hungteen.htlib.api.interfaces.raid.IWaveComponent;
import hungteen.htlib.common.impl.spawn.HTSpawnComponents;
import hungteen.htlib.common.impl.wave.CommonWave;
import hungteen.htlib.common.impl.wave.HTWaveComponents;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 17:25
 **/
public interface IMMWaveComponents {

    ResourceKey<IWaveComponent> MORTALITY_WAVE_1 = create("mortality_wave_1");
    ResourceKey<IWaveComponent> MORTALITY_WAVE_2 = create("mortality_wave_2");
    ResourceKey<IWaveComponent> MORTALITY_WAVE_3 = create("mortality_wave_3");

    static void register(BootstapContext<IWaveComponent> context) {
        final HolderGetter<ISpawnComponent> spawns = context.lookup(HTSpawnComponents.registry().getRegistryKey());
        final Holder<ISpawnComponent> once3Zombies = spawns.getOrThrow(IMMSpawnComponents.ONCE_3_ZOMBIES);
        final Holder<ISpawnComponent> duration10Zombies = spawns.getOrThrow(IMMSpawnComponents.DURATION_10_ZOMBIES);
        final Holder<ISpawnComponent> once1Skeleton = spawns.getOrThrow(IMMSpawnComponents.ONCE_1_SKELETON);
        final Holder<ISpawnComponent> duration5Skeletons = spawns.getOrThrow(IMMSpawnComponents.DURATION_5_SKELETONS);
        context.register(MORTALITY_WAVE_1, new CommonWave(
                trial(600).build(),
                List.of(Pair.of(ConstantInt.of(20), once3Zombies))
        ));
        context.register(MORTALITY_WAVE_2, new CommonWave(
                trial(1200).build(),
                List.of(
                        Pair.of(ConstantInt.of(20), once1Skeleton),
                        Pair.of(ConstantInt.of(100), duration10Zombies)
                )
        ));
        context.register(MORTALITY_WAVE_3, new CommonWave(
                trial(1200).build(),
                List.of(
                        Pair.of(ConstantInt.of(20), duration10Zombies),
                        Pair.of(ConstantInt.of(70), duration5Skeletons)
                )
        ));
    }

    static HTWaveComponents.WaveSettingBuilder trial(int duration) {
        return HTWaveComponents.builder()
                .skip(true)
                .wave(duration)
                ;
    }

    static ResourceKey<IWaveComponent> create(String name) {
        return HTWaveComponents.registry().createKey(Util.prefix(name));
    }
}
