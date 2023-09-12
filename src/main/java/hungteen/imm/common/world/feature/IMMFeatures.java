package hungteen.imm.common.world.feature;

import hungteen.imm.common.world.feature.configuration.HorizontalStakeConfiguration;
import hungteen.imm.common.world.feature.configuration.WoodStakeConfiguration;
import hungteen.imm.common.world.feature.features.HorizontalStakeFeature;
import hungteen.imm.common.world.feature.features.WoodStakeFeature;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-12 12:38
 **/
public interface IMMFeatures {

//    public static final RegistryObject<ConfiguredFeature<?, ?>> MULBERRY_TREE = CONFIGURED_FEATURES.register("mulberry_tree", () ->
//            new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
//                    BlockStateProvider.simple(ImmortalWoods.MULBERRY.getLog()),
//                    new StraightTrunkPlacer(5, 6, 3),
//                    new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(Blocks.AZALEA_LEAVES.defaultBlockState(), 3).add(Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState(), 1)),
//                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 4),
//                    new TwoLayersFeatureSize(1, 0, 2)
//            ).build())
//    );

    DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Util.id());

    RegistryObject<WoodStakeFeature> WOOD_STAKE = FEATURES.register("wood_stake", () -> new WoodStakeFeature(WoodStakeConfiguration.CODEC));
    RegistryObject<HorizontalStakeFeature> HORIZONTAL_STAKE = FEATURES.register("horizontal_stake", () -> new HorizontalStakeFeature(HorizontalStakeConfiguration.CODEC));

    static void register(BootstapContext<ConfiguredFeature<?, ?>> context) {
        IMMOreFeatures.register(context);
        IMMVegetationFeatures.register(context);
        IMMTreeFeatures.register(context);
    }

    static void register(IEventBus modBus){
        FEATURES.register(modBus);
    }

    static ResourceKey<ConfiguredFeature<?, ?>> create(String name){
        return FeatureUtils.createKey(Util.prefixName(name));
    }


}
