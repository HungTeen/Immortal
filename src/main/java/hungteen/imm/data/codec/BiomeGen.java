package hungteen.imm.data.codec;

import com.mojang.serialization.Lifecycle;
import hungteen.htlib.data.HTCodecGen;
import hungteen.imm.common.world.biome.ImmortalBiomes;
import hungteen.imm.utils.Util;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-18 13:26
 **/
public class BiomeGen {

//    public BiomeGen(DataGenerator generator) {
//        super(generator, Util.id());
//    }
//
//    @Override
//    public void run(CachedOutput cache) {
//        WritableRegistry<Biome> biomeRegistry = new MappedRegistry<>(Registry.BIOME_REGISTRY, Lifecycle.experimental(), null);
//
//        this.getBiomes().forEach((rl, biome) -> {
//            biomeRegistry.register(ResourceKey.create(Registry.BIOME_REGISTRY, rl), biome, Lifecycle.experimental());
//        });
//
//        register(cache, Registry.BIOME_REGISTRY, biomeRegistry, Biome.DIRECT_CODEC);
//    }
//
//    private Map<ResourceLocation, Biome> getBiomes() {
//        return ImmortalBiomes.biomes().entrySet().stream()
//                .collect(Collectors.toUnmodifiableMap(entry -> entry.getKey().location(), Map.Entry::getValue));
//    }
//
//    @Override
//    public String getName() {
//        return this.modId +" biomes";
//    }

}
