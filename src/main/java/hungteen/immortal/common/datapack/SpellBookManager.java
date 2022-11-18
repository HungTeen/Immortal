package hungteen.immortal.common.datapack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.ISpell;
import hungteen.immortal.impl.Spells;
import hungteen.immortal.utils.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-18 16:53
 **/
public class SpellBookManager extends SimpleJsonResourceReloadListener {

    public static final Codec<SpellBook> CODEC = RecordCodecBuilder.create(spellBookInstance -> spellBookInstance.group(
            new SpellCodec().listOf().fieldOf("spells").forGetter(SpellBook::spells)
    ).apply(spellBookInstance, SpellBook::new));
    private static final Gson GSON = (new GsonBuilder()).create();
    public static final String NAME= "spell_books";
    private static final Map<ResourceLocation, SpellBook> SPELL_BOOKS = new HashMap<>();

    public SpellBookManager() {
        super(GSON, NAME);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profilerFiller) {
        SPELL_BOOKS.clear();
        map.forEach((resourceLocation, jsonElement) -> {
            CODEC.parse(JsonOps.INSTANCE, jsonElement)
                    .resultOrPartial(Util::error)
                    .ifPresent(spellBook -> SPELL_BOOKS.put(resourceLocation, spellBook));
        });
        Util.info("Loaded {} spell books", SPELL_BOOKS.size());
    }

    public static Optional<SpellBook> get(ResourceLocation name){
        return Optional.ofNullable(SPELL_BOOKS.getOrDefault(name, null));
    }

    public static Collection<ResourceLocation> getBooks(){
        return Collections.unmodifiableCollection(SPELL_BOOKS.keySet());
    }

    public record SpellBook(List<ISpell> spells) {

    }

    public static final class SpellCodec implements Codec<ISpell> {

        @Override
        public <T> DataResult<Pair<ISpell, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input).map(string -> {
                Optional<ISpell> opt = ImmortalAPI.get().getSpell(string);
                return opt.map(iSpell -> Pair.of(iSpell, ops.empty())).orElseGet(() -> Pair.of(Spells.RELEASING, ops.empty()));
            });
        }

        @Override
        public <T> DataResult<T> encode(ISpell input, DynamicOps<T> ops, T prefix) {
            return ops.mergeToPrimitive(prefix, ops.createString(input.getRegistryName()));
        }
    }

}
