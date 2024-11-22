package hungteen.imm.common.world.feature;

import hungteen.htlib.util.helper.impl.FeatureHelper;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-12 13:09
 **/
public interface IMMPlacements {

//    public static final RegistryObject<PlacedFeature> MULBERRY_TREE = PLACED_FEATURES.initialize("mulberry_tree", () ->
//            new PlacedFeature(ImmortalConfiguredFeatures.MULBERRY_TREE.getHolder().get(),
//                    List.of(PlacementUtils.filteredByBlockSurvival(ImmortalBlocks.MULBERRY_SAPLING.get())))
//    );

    static void register(BootstrapContext<PlacedFeature> context) {
        IMMOrePlacements.register(context);
        IMMVegetationPlacements.register(context);
        IMMTreePlacements.register(context);
        IMMStructurePlacements.register(context);
    }

    static ResourceKey<PlacedFeature> create(String name){
        return FeatureHelper.placed().createKey(Util.prefix(name));
    }

}
