package hungteen.imm.common.world;

import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.impl.registry.RealmTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-15 19:13
 **/
public class LevelManager {

    private static final Map<ResourceKey<Level>, LevelRealmSetting> LEVEL_REALM_SETTING_MAP = new ConcurrentHashMap<>();
    private static final Map<ResourceKey<Biome>, BiomeRealmSetting> BIOME_REALM_SETTING_MAP = new ConcurrentHashMap<>();

    static {
        registerLevelRealmSetting(Level.END, new LevelRealmSetting(RealmTypes.MORTALITY, RealmTypes.SPIRITUAL_LEVEL_2));
        registerBiomeRealmSetting(Biomes.LUSH_CAVES, new BiomeRealmSetting(1F, 2F));
    }

    public static void registerLevelRealmSetting(ResourceKey<Level> levelResourceKey, LevelRealmSetting setting) {
        LEVEL_REALM_SETTING_MAP.put(levelResourceKey, setting);
    }

    public static void registerBiomeRealmSetting(ResourceKey<Biome> biomeResourceKey, BiomeRealmSetting setting) {
        BIOME_REALM_SETTING_MAP.put(biomeResourceKey, setting);
    }

    public static LevelRealmSetting getLevelRealmSetting(ResourceKey<Level> levelResourceKey) {
        return LEVEL_REALM_SETTING_MAP.getOrDefault(levelResourceKey, LevelRealmSetting.DEFAULT);
    }

    public static BiomeRealmSetting getBiomeRealmSetting(ResourceKey<Biome> biomeResourceKey) {
        return BIOME_REALM_SETTING_MAP.getOrDefault(biomeResourceKey, BiomeRealmSetting.DEFAULT);
    }

    public static Optional<Float> getChunkSpiritualRate(LevelChunk chunk){
        if(! chunk.getLevel().isClientSide()){
            final Optional<ResourceKey<Biome>> biomeOpt = chunk.getLevel().getBiome(chunk.getPos().getWorldPosition()).unwrapKey();
            final BiomeRealmSetting setting = biomeOpt.isPresent() ? getBiomeRealmSetting(biomeOpt.get()) : BiomeRealmSetting.DEFAULT;
            return Optional.of(setting.getChangeRate(chunk.getLevel().getRandom()));
        }
        return Optional.empty();
    }

    public record LevelRealmSetting(float lowestRealm, float highestRealm){

        public static final LevelRealmSetting DEFAULT = new LevelRealmSetting(RealmTypes.MORTALITY, RealmTypes.SPIRITUAL_LEVEL_1);

        public LevelRealmSetting(IRealmType lowestRealmType, IRealmType highestRealmType){
            this(lowestRealmType.getRealmValue(), highestRealmType.getRealmValue());
        }

    }

    public record BiomeRealmSetting(float minChange, float maxChange){

        public static final BiomeRealmSetting DEFAULT = new BiomeRealmSetting(0.08F, 0.12F);

        public float getChangeRate(RandomSource source){
            return Mth.clamp(Mth.normal(source, (minChange() + maxChange()) / 2, (maxChange() - minChange()) / 6), minChange(), maxChange());
        }
    }

}
