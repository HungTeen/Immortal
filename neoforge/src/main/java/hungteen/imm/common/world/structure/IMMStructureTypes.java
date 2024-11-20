package hungteen.imm.common.world.structure;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.common.world.structure.structures.TeleportRuinStructure;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.bus.api.IEventBus;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/15 15:08
 */
public class IMMStructureTypes {

    private static final HTVanillaRegistry<StructureType<?>> STRUCTURE_TYPE = HTRegistryManager.vanilla(Registries.STRUCTURE_TYPE, Util.id());

    public static final HTHolder<StructureType<TeleportRuinStructure>> TELEPORT_RUIN = STRUCTURE_TYPE.register("teleport_ruin", () -> () -> TeleportRuinStructure.CODEC);

    public static void initialize(IEventBus eventBus){
        NeoHelper.initRegistry(STRUCTURE_TYPE, eventBus);
    }
}
