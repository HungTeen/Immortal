package hungteen.imm.common.world.feature;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.htlib.util.helper.impl.FeatureHelper;
import hungteen.imm.common.world.feature.configuration.HorizontalStakeConfiguration;
import hungteen.imm.common.world.feature.configuration.WoodStakeConfiguration;
import hungteen.imm.common.world.feature.features.HorizontalStakeFeature;
import hungteen.imm.common.world.feature.features.WoodStakeFeature;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-12 12:38
 **/
public interface IMMFeatures {

//    public static final RegistryObject<ConfiguredFeature<?, ?>> MULBERRY_TREE = CONFIGURED_FEATURES.initialize("mulberry_tree", () ->
//            new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
//                    BlockStateProvider.simple(ImmortalWoods.MULBERRY.getLog()),
//                    new StraightTrunkPlacer(5, 6, 3),
//                    new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(Blocks.AZALEA_LEAVES.defaultBlockState(), 3).add(Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState(), 1)),
//                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1), 4),
//                    new TwoLayersFeatureSize(1, 0, 2)
//            ).build())
//    );

    HTVanillaRegistry<Feature<?>> FEATURES = HTRegistryManager.vanilla(Registries.FEATURE, Util.id());

    HTHolder<WoodStakeFeature> WOOD_STAKE = FEATURES.register("wood_stake", () -> new WoodStakeFeature(WoodStakeConfiguration.CODEC));
    HTHolder<HorizontalStakeFeature> HORIZONTAL_STAKE = FEATURES.register("horizontal_stake", () -> new HorizontalStakeFeature(HorizontalStakeConfiguration.CODEC));

    static void register(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        IMMOreFeatures.register(context);
        IMMVegetationFeatures.register(context);
        IMMTreeFeatures.register(context);
    }

    static void initialize(IEventBus modBus){
        NeoHelper.initRegistry(FEATURES, modBus);
    }

    static ResourceKey<ConfiguredFeature<?, ?>> create(String name){
        return FeatureHelper.config().createKey(Util.prefix(name));
    }


}
