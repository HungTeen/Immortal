package hungteen.imm.data;

import hungteen.imm.util.Util;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-29 23:22
 **/
public class AtlasGen extends SpriteSourceProvider {

    public AtlasGen(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper, Util.id());
    }

    @Override
    protected void addSources() {
        this.atlas(BLOCKS_ATLAS).addSource(
                new DirectoryLister("entity/bed", "entity/bed/")
        );
    }
}
