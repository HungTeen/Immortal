package hungteen.imm.data;

import hungteen.htlib.data.HTBlockModelGen;
import hungteen.imm.common.impl.registry.IMMWoods;
import hungteen.imm.util.Util;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-07 12:10
 **/
public class BlockModelGen extends HTBlockModelGen {

    public BlockModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Util.id(), existingFileHelper);
    }

    @Override
    protected void registerModels() {
        IMMWoods.woods().forEach(this::woodSuitGen);
    }

    @Override
    public @NotNull String getName() {
        return this.modid + " block models";
    }
}
