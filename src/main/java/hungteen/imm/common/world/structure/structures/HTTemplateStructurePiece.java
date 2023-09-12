package hungteen.imm.common.world.structure.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.function.Function;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/15 15:49
 */
public abstract class HTTemplateStructurePiece extends TemplateStructurePiece {

    public HTTemplateStructurePiece(StructurePieceType type, int genDepth, StructureTemplateManager manager, ResourceLocation location, String templateName, StructurePlaceSettings settings, BlockPos pos) {
        super(type, genDepth, manager, location, templateName, settings, pos);
    }

    public HTTemplateStructurePiece(StructurePieceType type, StructureTemplateManager manager, CompoundTag tag, Function<ResourceLocation, StructurePlaceSettings> function) {
        super(type, tag, manager, function);
    }
}
