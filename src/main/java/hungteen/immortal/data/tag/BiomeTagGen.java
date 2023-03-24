package hungteen.immortal.data.tag;

import hungteen.htlib.data.tag.HTTagsProvider;
import hungteen.htlib.util.helper.registry.BiomeHelper;
import hungteen.immortal.common.tag.ImmortalBiomeTags;
import hungteen.immortal.utils.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-24 23:16
 **/
public class BiomeTagGen extends HTTagsProvider<Biome> {

    public BiomeTagGen(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, BiomeHelper.get(), Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ImmortalBiomeTags.HAS_OVERWORLD_TRADING_MARKET)
                .add(Biomes.DRIPSTONE_CAVES, Biomes.LUSH_CAVES);
    }

}
