package hungteen.imm.data.tag;

import hungteen.htlib.data.tag.HTTagsProvider;
import hungteen.htlib.util.helper.impl.StructureHelper;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/28 9:43
 */
public class StructureTagGen extends HTTagsProvider<Structure> {

    public StructureTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper fileHelper) {
        super(output, provider, StructureHelper.get(), Util.id(), fileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
//        this.tag(IMMStructureTags.TELEPORT_RUINS)
//                .add(IMMStructures.TELEPORT_RUIN);
    }

}
