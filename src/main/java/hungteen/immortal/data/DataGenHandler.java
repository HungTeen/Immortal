package hungteen.immortal.data;

import hungteen.immortal.data.codec.RegistryGen;
import hungteen.immortal.data.recipe.RecipeGen;
import hungteen.immortal.data.tag.*;
import net.minecraftforge.data.event.GatherDataEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:11
 **/
public class DataGenHandler {

    public static void dataGen(GatherDataEvent event) {
        /* Tags */
        final BlockTagGen generator = new BlockTagGen(event.getGenerator(), event.getExistingFileHelper());
        event.getGenerator().addProvider(event.includeServer(), generator);
        event.getGenerator().addProvider(event.includeServer(), new ItemTagGen(event.getGenerator(), generator, event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeServer(), new EntityTagGen(event.getGenerator(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeServer(), new BannerPatternTagGen(event.getGenerator(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeServer(), new BiomeTagGen(event.getGenerator(), event.getExistingFileHelper()));
        /* Recipes */
        event.getGenerator().addProvider(event.includeServer(), new RecipeGen(event.getGenerator()));

        /* Loot Tables */
//        ev.getGenerator().addProvider(new LootTableGen(ev.getGenerator()));

        /* Advancements */

        /* Block States */
        event.getGenerator().addProvider(event.includeClient(), new BlockStateGen(event.getGenerator(), event.getExistingFileHelper()));

        /* Models */
        event.getGenerator().addProvider(event.includeClient(), new BlockModelGen(event.getGenerator(), event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeClient(), new ItemModelGen(event.getGenerator(), event.getExistingFileHelper()));

        /* Codecs */
        event.getGenerator().addProvider(event.includeServer(), new RegistryGen(event.getGenerator()));
//        event.getGenerator().addProvider(event.includeServer(), new BiomeGen(event.getGenerator()));
//        event.getGenerator().addProvider(event.includeServer(), new DimensionGen(event.getGenerator()));
//        event.getGenerator().addProvider(event.includeServer(), new SpellBookGen(event.getGenerator()));
//        event.getGenerator().addProvider(event.includeServer(), new StructureGen(event.getGenerator()));
    }

}
