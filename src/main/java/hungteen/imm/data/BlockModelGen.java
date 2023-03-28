package hungteen.imm.data;

import hungteen.htlib.data.HTBlockModelGen;
import hungteen.imm.common.impl.registry.ImmortalWoods;
import hungteen.imm.utils.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:10
 **/
public class BlockModelGen extends HTBlockModelGen {

    public BlockModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Util.id(), existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ImmortalWoods.woods().forEach(this::woodIntegration);
    }
}
