package hungteen.imm.common.world.structure;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.common.world.structure.structures.TeleportRuinStructure;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.bus.api.IEventBus;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/6/15 15:42
 */
public class IMMStructurePieces {

    private static final HTVanillaRegistry<StructurePieceType> PIECE_TYPES = HTRegistryManager.vanilla(Registries.STRUCTURE_PIECE, Util.id());

    public static final HTHolder<StructurePieceType.StructureTemplateType> TELEPORT_RUIN = PIECE_TYPES.register("teleport_ruin", () -> TeleportRuinStructure.Piece::new);

    public static void initialize(IEventBus eventBus){
        NeoHelper.initRegistry(PIECE_TYPES, eventBus);
    }

}
