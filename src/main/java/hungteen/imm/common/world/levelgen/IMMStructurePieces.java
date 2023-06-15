package hungteen.imm.common.world.levelgen;

import hungteen.imm.common.world.levelgen.structure.TeleportRuinStructure;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/15 15:42
 */
public class IMMStructurePieces {

    private static final DeferredRegister<StructurePieceType> PIECE_TYPES = DeferredRegister.create(BuiltInRegistries.STRUCTURE_PIECE.key(), Util.id());

    public static final RegistryObject<StructurePieceType.StructureTemplateType> TELEPORT_RUIN = PIECE_TYPES.register("teleport_ruin", () -> TeleportRuinStructure.Piece::new);

    public static void register(IEventBus eventBus){
        PIECE_TYPES.register(eventBus);
    }

}
