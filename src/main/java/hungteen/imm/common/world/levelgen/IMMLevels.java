package hungteen.imm.common.world.levelgen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.helper.registry.LevelHelper;
import hungteen.imm.common.world.levelgen.dimension.EastWorldDimension;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-31 18:30
 **/
public class IMMLevels {

    public static final ResourceKey<Level> EAST_WORLD = create("east_world");

    public static final MultiNoiseBiomeSource.Preset EAST_WORLD_PRESET = new MultiNoiseBiomeSource.Preset(
            Util.prefix("east_world"),
            biomes -> {
                ImmutableList.Builder<Pair<Climate.ParameterPoint, Holder<Biome>>> builder = ImmutableList.builder();
                EastWorldDimension.addBiomes(resourceKeyPair -> builder.add(resourceKeyPair.mapSecond(biomes::getOrThrow)));
                return new Climate.ParameterList<>(builder.build());
            }
    );

    /**
     * 留着用来初始化。
     */
    public static void register(){

    }

    private static ResourceKey<Level> create(String name) {
        return LevelHelper.get().createKey(Util.prefix(name));
    }

}
