package hungteen.imm.common.world.feature;

import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-12 13:09
 **/
public interface IMMPlacements {

//    public static final RegistryObject<PlacedFeature> MULBERRY_TREE = PLACED_FEATURES.register("mulberry_tree", () ->
//            new PlacedFeature(ImmortalConfiguredFeatures.MULBERRY_TREE.getHolder().get(),
//                    List.of(PlacementUtils.filteredByBlockSurvival(ImmortalBlocks.MULBERRY_SAPLING.get())))
//    );

    static void register(BootstapContext<PlacedFeature> context) {
        IMMOrePlacements.register(context);
        IMMVegetationPlacements.register(context);
        IMMTreePlacements.register(context);
        IMMStructurePlacements.register(context);
    }

    static ResourceKey<PlacedFeature> create(String name){
        return PlacementUtils.createKey(Util.prefixName(name));
    }

}
