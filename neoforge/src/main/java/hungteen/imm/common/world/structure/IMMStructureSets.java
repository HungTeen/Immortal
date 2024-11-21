package hungteen.imm.common.world.structure;

import hungteen.imm.util.Util;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/28 20:17
 */
public interface IMMStructureSets {

    ResourceKey<StructureSet> TELEPORT_RUINS = create("teleport_ruins");
    ResourceKey<StructureSet> PLAINS_TRADING_MARKET_SET = create("plains_trading_markets");
    ResourceKey<StructureSet> SPIRITUAL_FLAME_ALTAR_SET = create("spiritual_flame_altars");
    ResourceKey<StructureSet> SPIRIT_LAB_SET = create("spirit_labs");

    static void register(BootstrapContext<StructureSet> context) {
        final HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
        context.register(TELEPORT_RUINS, new StructureSet(
                structures.getOrThrow(IMMStructures.TELEPORT_RUIN),
                new RandomSpreadStructurePlacement(28, 12, RandomSpreadType.LINEAR, 957627271)
        ));
        context.register(PLAINS_TRADING_MARKET_SET, new StructureSet(
                structures.getOrThrow(IMMStructures.PLAINS_TRADING_MARKET),
                new RandomSpreadStructurePlacement(
                        34,
                        8,
                        RandomSpreadType.LINEAR,
                        1080133475
                )
        ));
        context.register(SPIRITUAL_FLAME_ALTAR_SET, new StructureSet(
                structures.getOrThrow(IMMStructures.SPIRITUAL_FLAME_ALTAR),
                new RandomSpreadStructurePlacement(
                        32,
                        8,
                        RandomSpreadType.TRIANGULAR,
                        1692262025
                )
        ));
        context.register(SPIRIT_LAB_SET, new StructureSet(
                structures.getOrThrow(IMMStructures.SPIRIT_LAB),
                new RandomSpreadStructurePlacement(
                        32,
                        8,
                        RandomSpreadType.LINEAR,
                        1420587049
                )
        ));
    }

    static ResourceKey<StructureSet> create(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, Util.prefix(name));
    }

}
