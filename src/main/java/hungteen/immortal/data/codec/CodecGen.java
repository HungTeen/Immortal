package hungteen.immortal.data.codec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import hungteen.immortal.utils.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-17 23:02
 *
 * Copy some code from https://github.com/TeamTwilight/twilightforest/blob/1.18.x/src/main/java/twilightforest/data/WorldGenerator.java
 **/
public abstract class CodecGen implements DataProvider {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected final DataGenerator generator;
    protected final Path path;
    protected final DynamicOps<JsonElement> ops;
    protected final String modId;

    public CodecGen(DataGenerator generator, String modId) {
        this.generator = generator;
        this.path = this.generator.getOutputFolder();
        this.ops = RegistryOps.create(JsonOps.INSTANCE, access());
        this.modId = modId;
    }

    @Override
    public void run(HashCache cache){

    }

    protected  <T> void registerCap(HashCache cache, RegistryAccess.RegistryData<T> data) {
        register(cache, data.key(), access().ownedRegistryOrThrow(data.key()), data.codec());
    }

    protected <E, T extends Registry<E>> void register(HashCache cache, ResourceKey<? extends T> key, T registry, Encoder<E> encoder) {
        registry.entrySet().forEach(entry -> {
            final ResourceLocation location = entry.getKey().location();
            if (location.getNamespace().equals(Util.id())) {
                Util.info(location.getPath());
                Path otherPath = createPath(path, key.location(), location);
                register(createPath(path, key.location(), location), cache, encoder, entry.getValue());
            }
        });
    }

    protected <E> void register(Path location, HashCache cache, Encoder<E> encoder, E entry) {
        try {
            Optional<JsonElement> opt = encoder.encodeStart(ops, entry).resultOrPartial((s) -> {
                Util.error("Couldn't serialize element {}: {}", location, s);
            });
            if (opt.isPresent()) {
                if (opt.get().isJsonObject()) {
                    JsonObject obj = opt.get().getAsJsonObject();
                    if (obj.has("generator") && obj.get("generator").isJsonObject()) {
                        JsonObject generator = obj.getAsJsonObject("generator");
                        if (generator.has("use_overworld_seed")) {
                            generator.remove("use_overworld_seed");
                            generator.addProperty("use_overworld_seed", true);
                        }
                        if (generator.has("wrapped_generator")) {
                            JsonObject wrapped_generator = generator.getAsJsonObject("wrapped_generator");
                            if (wrapped_generator.has("biome_source")){
                                wrapped_generator.getAsJsonObject("biome_source").remove("seed");
                            }
                        }
                    }
                }
                DataProvider.save(GSON, cache, opt.get(), location);
            }
        } catch (IOException ioexception) {
            Util.error("Couldn't save element {}", location, ioexception);
        }
    }

    /**
     * create data gen path for specific entry.
     */
    protected Path createPath(Path path, ResourceLocation registry, ResourceLocation entry) {
        return path.resolve("data").resolve(entry.getNamespace()).resolve(registry.getPath()).resolve(entry.getPath() + ".json");
    }

    public RegistryAccess access(){
        return BuiltinRegistries.ACCESS;
    }

}
