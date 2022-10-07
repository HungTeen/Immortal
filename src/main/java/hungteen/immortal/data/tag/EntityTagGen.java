package hungteen.immortal.data.tag;

import hungteen.htlib.data.tag.HTEntityTagGen;
import hungteen.immortal.utils.Util;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:13
 **/
public class EntityTagGen extends HTEntityTagGen {

    public EntityTagGen(DataGenerator p_126517_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_126517_, Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags() {

    }
}
