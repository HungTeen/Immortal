package hungteen.imm.data;

import hungteen.imm.util.Util;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

import java.util.concurrent.CompletableFuture;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-29 23:22
 **/
public class AtlasGen extends SpriteSourceProvider {

    public AtlasGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper fileHelper) {
        super(output, provider, Util.id(), fileHelper);
    }

    @Override
    protected void gather() {
        this.atlas(BLOCKS_ATLAS).addSource(
                new DirectoryLister("entity/bed", "entity/bed/")
        );
    }

}
