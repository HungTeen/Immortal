package hungteen.immortal.data.codec;

import hungteen.htlib.data.HTCodecGen;
import hungteen.immortal.utils.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-18 18:46
 **/
public class SpellBookGen extends HTCodecGen {

    public SpellBookGen(DataGenerator generator) {
        super(generator, Util.id());
    }

    @Override
    public void run(CachedOutput cache) {
//        final SpellBookManager.SpellBook book = new SpellBookManager.SpellBook(List.of(
//                SpellTypes.ITEM_PICKING,
//                SpellTypes.BLOCK_PICKING
//        ));
//        final ResourceLocation location = Util.prefix("test");
//        final Path path = this.createPath(this.path, SpellBookManager.NAME, location);
//        Optional<JsonElement> opt = SpellBookManager.CODEC.encodeStart(this.ops, book).resultOrPartial((s) -> {
//            HTLib.getLogger().error("Couldn't serialize element {}: {}", location, s);
//        });
//        try {
//            DataProvider.saveStable(cache, (JsonElement)opt.get(), path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    protected Path createPath(Path path, String name, ResourceLocation entry) {
        return path.resolve("data").resolve(entry.getNamespace()).resolve(name).resolve(entry.getPath() + ".json");
    }

    @Override
    public String getName() {
        return this.modId + " spell books";
    }
}
