package hungteen.immortal.data.tag;

import hungteen.htlib.data.tag.HTTagsProvider;
import hungteen.htlib.util.helper.registry.BannerPatternHelper;
import hungteen.htlib.util.helper.registry.BiomeHelper;
import hungteen.immortal.common.tag.ImmortalBiomeTags;
import hungteen.immortal.utils.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-03-24 23:16
 **/
public class BiomeTagGen extends HTTagsProvider<Biome> {

    public BiomeTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, BiomeHelper.get(), Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ImmortalBiomeTags.HAS_OVERWORLD_TRADING_MARKET)
                .add(Biomes.DRIPSTONE_CAVES, Biomes.LUSH_CAVES);
    }

}
