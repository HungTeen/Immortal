package hungteen.imm.data.tag;

import hungteen.htlib.data.tag.HTTagsProvider;
import hungteen.htlib.util.helper.impl.StructureHelper;
import hungteen.imm.common.tag.IMMStructureTags;
import hungteen.imm.common.world.structure.IMMStructures;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.concurrent.CompletableFuture;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/28 9:43
 */
public class StructureTagGen extends HTTagsProvider<Structure> {

    public StructureTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, StructureHelper.get());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(IMMStructureTags.TELEPORT_RUINS)
                .add(IMMStructures.TELEPORT_RUIN);
    }

}
