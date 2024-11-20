package hungteen.imm.data;

import hungteen.imm.data.loot.LootTableGen;
import hungteen.imm.data.recipe.RecipeGen;
import hungteen.imm.data.tag.BannerPatternTagGen;
import hungteen.imm.data.tag.BlockTagGen;
import hungteen.imm.data.tag.EntityTagGen;
import hungteen.imm.data.tag.ItemTagGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:11
 **/
public class DataGenHandler {

    public static void dataGen(GatherDataEvent event) {
        final DataGenerator generators = event.getGenerator();
        final PackOutput output = event.getGenerator().getPackOutput();
        final CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        final ExistingFileHelper fileHelper = event.getExistingFileHelper();

        /* Tags */
        final BlockTagGen generator = new BlockTagGen(output, provider);
        generators.addProvider(event.includeServer(), generator);
        generators.addProvider(event.includeServer(), new ItemTagGen(output, provider, generator.contentsGetter()));
        generators.addProvider(event.includeServer(), new EntityTagGen(output, provider));
        generators.addProvider(event.includeServer(), new BannerPatternTagGen(output, provider));

        /* Recipes */
        generators.addProvider(event.includeServer(), new RecipeGen(output, provider));

        /* Loot Tables */
        generators.addProvider(event.includeServer(), new LootTableGen(output, provider));

        /* Advancements */
        generators.addProvider(event.includeServer(), new AdvancementGen(output, provider, fileHelper));

        /* Block States */
        generators.addProvider(event.includeClient(), new BlockStateGen(output, fileHelper));

        /* Models */
        generators.addProvider(event.includeClient(), new AtlasGen(output, provider, fileHelper));
        generators.addProvider(event.includeClient(), new BlockModelGen(output, fileHelper));
        generators.addProvider(event.includeClient(), new ItemModelGen(output, fileHelper));

        /* Datapack */
        DatapackEntriesGen.addProviders(event.includeServer(), generators, output, provider, fileHelper);
    }

}
