package hungteen.immortal.data;

import hungteen.immortal.data.tag.BlockTagGen;
import hungteen.immortal.data.tag.EntityTagGen;
import hungteen.immortal.data.tag.ItemTagGen;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:11
 **/
public class DataGenHandler {

    public static void dataGen(GatherDataEvent ev) {
        //for tags.
        final BlockTagGen generator = new BlockTagGen(ev.getGenerator(), ev.getExistingFileHelper());
        ev.getGenerator().addProvider(generator);
        ev.getGenerator().addProvider(new ItemTagGen(ev.getGenerator(), generator, ev.getExistingFileHelper()));
        ev.getGenerator().addProvider(new EntityTagGen(ev.getGenerator(), ev.getExistingFileHelper()));
        //for recipes.
//        ev.getGenerator().addProvider(new RecipeGen(ev.getGenerator()));
        //for loot tables.
//        ev.getGenerator().addProvider(new LootTableGen(ev.getGenerator()));
        //for advancements.
        //for block state.
        ev.getGenerator().addProvider(new BlockStateGen(ev.getGenerator(), ev.getExistingFileHelper()));
        //for model.
        ev.getGenerator().addProvider(new BlockModelGen(ev.getGenerator(), ev.getExistingFileHelper()));
        ev.getGenerator().addProvider(new ItemModelGen(ev.getGenerator(), ev.getExistingFileHelper()));
        //for dimension.
        ev.getGenerator().addProvider(new DimensionGen(ev.getGenerator()));
    }

}
