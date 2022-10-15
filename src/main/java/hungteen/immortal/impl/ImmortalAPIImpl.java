package hungteen.immortal.impl;

import hungteen.htlib.interfaces.IRangeData;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.IEffectRune;
import hungteen.immortal.api.interfaces.IGetterRune;
import hungteen.immortal.api.interfaces.ISpell;
import hungteen.immortal.api.interfaces.ISpiritualRoot;
import hungteen.immortal.utils.Constants;
import hungteen.immortal.utils.PlayerUtil;
import hungteen.immortal.utils.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 22:32
 **/
public class ImmortalAPIImpl implements ImmortalAPI.IImmortalAPI {

    private static final List<ISpiritualRoot> SPIRITUAL_ROOTS = new ArrayList<>();
    private static final HashMap<String, ISpell> SPELL_MAP = new HashMap<>();
    private static final List<IEffectRune> EFFECT_RUNES = new ArrayList<>();
    private static final List<IGetterRune> GETTER_RUNES = new ArrayList<>();
    private static final HashMap<String, IRangeData<Integer>> INTEGER_MAP = new HashMap<>();
    private static final HashMap<ResourceKey<Biome>, Integer> BIOME_SPIRITUAL_MAP = new HashMap<>();
    private static final HashMap<ResourceKey<Level>, Float> LEVEL_SPIRITUAL_MAP = new HashMap<>();


    @Override
    public void registerSpiritualRoot(ISpiritualRoot type) {
        if(! SPIRITUAL_ROOTS.contains(type)){
            SPIRITUAL_ROOTS.add(type);
        } else{
            Util.warn("Spiritual Root Register : Duplicate Type !");
        }
    }

    @Override
    public List<ISpiritualRoot> getSpiritualRoots() {
        return Collections.unmodifiableList(SPIRITUAL_ROOTS);
    }

    @Override
    public void registerSpell(ISpell type) {
        if(! SPELL_MAP.containsKey(type)){
            SPELL_MAP.put(type.getRegistryName(), type);
        } else{
            Util.warn("Spell Register : Duplicate Type !");
        }
    }

    @Override
    public Collection<ISpell> getSpells() {
        return Collections.unmodifiableCollection(SPELL_MAP.values());
    }

    @Override
    public Optional<ISpell> getSpell(String type) {
        return Optional.ofNullable(SPELL_MAP.get(type));
    }

    @Override
    public void registerEffectRune(IEffectRune type) {
        if(! EFFECT_RUNES.contains(type)){
            EFFECT_RUNES.add(type);
        } else{
            Util.warn("Effect Rune Register : Duplicate Type !");
        }
    }

    @Override
    public List<IEffectRune> getEffectRunes() {
        return Collections.unmodifiableList(EFFECT_RUNES);
    }

    @Override
    public void registerIntegerData(IRangeData<Integer> type) {
        if(! INTEGER_MAP.containsKey(type)){
            INTEGER_MAP.put(type.getRegistryName(), type);
        } else{
            Util.warn("Integer Data Register : Duplicate Type !");
        }
    }

    @Override
    public Collection<IRangeData<Integer>> getIntegerCollection() {
        return Collections.unmodifiableCollection(INTEGER_MAP.values());
    }

    @Override
    public Optional<IRangeData<Integer>> getIntegerData(String type) {
        return Optional.ofNullable(INTEGER_MAP.get(type));
    }

    @Override
    public void registerGetterRune(IGetterRune type) {
        if(! GETTER_RUNES.contains(type)){
            GETTER_RUNES.add(type);
        } else{
            Util.warn("Getter Rune Register : Duplicate Type !");
        }
    }

    @Override
    public List<IGetterRune> getGetterRunes() {
        return Collections.unmodifiableList(GETTER_RUNES);
    }

    @Override
    public int getSpiritualMana(Player player) {
        return PlayerUtil.getIntegerData(player, PlayerDatas.SPIRITUAL_MANA);
    }

    @Override
    public void registerBiomeSpiritualValue(ResourceKey<Biome> biomeResourceKey, int spiritualValue) {
        BIOME_SPIRITUAL_MAP.put(biomeResourceKey, spiritualValue);
    }

    @Override
    public void registerLevelSpiritualRatio(ResourceKey<Level> levelResourceKey, float spiritualRatio) {
        LEVEL_SPIRITUAL_MAP.put(levelResourceKey, spiritualRatio);
    }

    @Override
    public int getSpiritualValue(Level level, BlockPos pos) {
        Optional<ResourceKey<Biome>> opt = level.getBiome(pos).unwrapKey();
        if(opt.isPresent()){
            final int value = BIOME_SPIRITUAL_MAP.getOrDefault(opt.get(), Constants.DEFAULT_BIOME_SPIRITUAL_VALUE);
            final float ratio = LEVEL_SPIRITUAL_MAP.getOrDefault(level.dimension(), Constants.DEFAULT_LEVEL_SPIRITUAL_RATIO);
            return Mth.floor(value * ratio);
        }
        return 0;
    }

}
