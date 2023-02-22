package hungteen.immortal.data;

import hungteen.htlib.common.WoodIntegrations;
import hungteen.htlib.data.HTBlockModelGen;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.impl.ImmortalWoods;
import hungteen.immortal.utils.Util;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:10
 **/
public class BlockModelGen extends HTBlockModelGen {

    public BlockModelGen(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Util.id(), existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ImmortalWoods.woods().forEach(this::woodIntegration);
    }
}
