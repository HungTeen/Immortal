package hungteen.immortal.data.tag;

import hungteen.htlib.data.tag.HTBlockTagGen;
import hungteen.immortal.utils.Util;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:12
 **/
public class BlockTagGen extends HTBlockTagGen {
    public BlockTagGen(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags() {

    }
}
