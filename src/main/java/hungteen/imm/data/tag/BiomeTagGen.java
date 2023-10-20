package hungteen.imm.data.tag;

import hungteen.htlib.data.tag.HTTagsProvider;
import hungteen.htlib.util.helper.registry.BiomeHelper;
import hungteen.imm.common.tag.IMMBiomeTags;
import hungteen.imm.common.world.biome.IMMBiomes;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;
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
        /* Forge */
        this.tag(IMMBiomeTags.HAS_BIRCH_TREE).add(Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, IMMBiomes.CUT_BIRCH_FOREST);
        this.tag(IMMBiomeTags.HAS_JUNGLE_TREE).add(Biomes.JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.SPARSE_JUNGLE);
        this.tag(IMMBiomeTags.HAS_DARK_OAK_TREE).add(Biomes.DARK_FOREST, IMMBiomes.CUT_DARK_FOREST);
        this.tag(IMMBiomeTags.HAS_ACACIA_TREE).add(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA);
        this.tag(IMMBiomeTags.HAS_SPRUCE_TREE).add(Biomes.SNOWY_TAIGA, Biomes.TAIGA);
        this.tag(IMMBiomeTags.HAS_CHERRY_TREE).add(Biomes.CHERRY_GROVE);

        bind(IMMBiomes.PLAINS, Tags.Biomes.IS_PLAINS);
        bind(IMMBiomes.DESERT, Tags.Biomes.IS_DESERT, Tags.Biomes.IS_HOT, Tags.Biomes.IS_DRY, Tags.Biomes.IS_SANDY);
        bind(IMMBiomes.SAVANNA, BiomeTags.IS_SAVANNA, Tags.Biomes.IS_HOT, Tags.Biomes.IS_SPARSE);
        bind(IMMBiomes.BAMBOO_JUNGLE, BiomeTags.IS_JUNGLE, Tags.Biomes.IS_HOT, Tags.Biomes.IS_WET);

        /* IMM */
        this.tag(IMMBiomeTags.HAS_TELEPORT_RUIN)
                .add(Biomes.PLAINS, Biomes.SAVANNA_PLATEAU);
        this.tag(IMMBiomeTags.HAS_PLAINS_TRADING_MARKET)
                .add(IMMBiomes.PLAINS);
        this.tag(IMMBiomeTags.HAS_SPIRITUAL_FLAME_ALTAR)
                .add(IMMBiomes.SAVANNA);
    }

    @SafeVarargs
    private void bind(ResourceKey<Biome> biome, TagKey<Biome>... tags) {
        for (TagKey<Biome> key : tags) {
            tag(key).add(biome);
        }
    }

}
