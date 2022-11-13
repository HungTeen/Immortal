package hungteen.immortal.common.world.feature;

import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.utils.Util;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-12 13:09
 **/
public class ImmortalPlacedFeatures {

    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Util.id());

    public static final RegistryObject<PlacedFeature> MULBERRY_TREE = PLACED_FEATURES.register("mulberry_tree", () ->
            new PlacedFeature(ImmortalConfiguredFeatures.MULBERRY_TREE.getHolder().get(),
                    List.of(PlacementUtils.filteredByBlockSurvival(ImmortalBlocks.MULBERRY_SAPLING.get())))
    );

    public static void register(IEventBus event) {
        PLACED_FEATURES.register(event);
    }

}
