package hungteen.imm.data;

import hungteen.imm.data.advancement.AdvancementGen;
import hungteen.imm.data.recipe.RecipeGen;
import hungteen.imm.data.tag.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

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
        final ExistingFileHelper fileHelper = event.getExistingFileHelper();
        final CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        /* Tags */
        final BlockTagGen generator = new BlockTagGen(output, provider, fileHelper);
        generators.addProvider(event.includeServer(), new BlockTagGen(output, provider, fileHelper));
        generators.addProvider(event.includeServer(), new ItemTagGen(output, generator, provider, fileHelper));
        generators.addProvider(event.includeServer(), new EntityTagGen(output, provider, fileHelper));
        generators.addProvider(event.includeServer(), new BannerPatternTagGen(output, provider, fileHelper));
        generators.addProvider(event.includeServer(), new BiomeTagGen(output, provider, fileHelper));

        /* Recipes */
        generators.addProvider(event.includeServer(), new RecipeGen(output));

        /* Loot Tables */
//        ev.getGenerator().addProvider(new LootTableGen(ev.getGenerator()));

        /* Advancements */
        generators.addProvider(event.includeServer(), new AdvancementGen(output, provider, fileHelper));

        /* Block States */
        generators.addProvider(event.includeClient(), new BlockStateGen(output, fileHelper));

        /* Models */
        generators.addProvider(event.includeClient(), new AtlasGen(output, fileHelper));
        generators.addProvider(event.includeClient(), new BlockModelGen(output, fileHelper));
        generators.addProvider(event.includeClient(), new ItemModelGen(output, fileHelper));

        /* Datapack */
        generators.addProvider(event.includeServer(), new DatapackEntriesGen(output, provider));

        /* Codecs */
    }

}
