package hungteen.immortal.data.codec;

import com.mojang.serialization.Lifecycle;
import hungteen.immortal.utils.Util;
import hungteen.immortal.common.world.biome.ImmortalBiomes;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-18 13:26
 **/
public class BiomeGen extends CodecGen{

    public BiomeGen(DataGenerator generator) {
        super(generator, Util.id());
    }

    @Override
    public void run(HashCache cache) {
        WritableRegistry<Biome> biomeRegistry = new MappedRegistry<>(Registry.BIOME_REGISTRY, Lifecycle.experimental(), null);

        this.getBiomes().forEach((rl, biome) -> {
            biomeRegistry.register(ResourceKey.create(Registry.BIOME_REGISTRY, rl), biome, Lifecycle.experimental());
        });

        StreamSupport.stream(RegistryAccess.knownRegistries().spliterator(), false)
                .filter(r -> access().ownedRegistry(r.key()).isPresent() && !r.key().equals(Registry.BIOME_REGISTRY))
                .forEach(data -> registerCap(cache, data));

        register(cache, Registry.BIOME_REGISTRY, biomeRegistry, Biome.DIRECT_CODEC);
    }

    private Map<ResourceLocation, Biome> getBiomes() {
        return ImmortalBiomes.biomes().entrySet().stream()
                .peek(biomeEntry -> biomeEntry.getValue().setRegistryName(biomeEntry.getKey().location()))
                .collect(Collectors.toUnmodifiableMap(entry -> entry.getKey().location(), Map.Entry::getValue));
    }

    @Override
    public String getName() {
        return this.modId +" biomes";
    }

}
