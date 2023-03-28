package hungteen.immortal.data;

import hungteen.immortal.data.codec.RegistryGen;
import hungteen.immortal.data.recipe.RecipeGen;
import hungteen.immortal.data.tag.*;
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

        /* Block States */
        generators.addProvider(event.includeClient(), new BlockStateGen(output, fileHelper));

        /* Models */
        generators.addProvider(event.includeClient(), new BlockModelGen(output, fileHelper));
        generators.addProvider(event.includeClient(), new ItemModelGen(output, fileHelper));

        /* Codecs */
//        generators.addProvider(event.includeServer(), new RegistryGen(event.getGenerator()));
//        generators.addProvider(event.includeServer(), new BiomeGen(event.getGenerator()));
//        generators.addProvider(event.includeServer(), new DimensionGen(event.getGenerator()));
//        generators.addProvider(event.includeServer(), new SpellBookGen(event.getGenerator()));
//        generators.addProvider(event.includeServer(), new StructureGen(event.getGenerator()));
    }

}
