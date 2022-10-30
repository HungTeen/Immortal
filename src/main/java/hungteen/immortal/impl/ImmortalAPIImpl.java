package hungteen.immortal.impl;

import hungteen.htlib.interfaces.IRangeData;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.IHasRealm;
import hungteen.immortal.api.registry.*;
import hungteen.immortal.common.capability.player.PlayerDataManager;
import hungteen.immortal.utils.Constants;
import hungteen.immortal.utils.PlayerUtil;
import hungteen.immortal.utils.Util;
import it.unimi.dsi.fastutil.ints.IntComparator;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 22:32
 **/
public class ImmortalAPIImpl implements ImmortalAPI.IImmortalAPI {

    private static final Map<String, ISpell> SPELL_MAP = new HashMap<>();
    private static final Map<String, IRangeData<Integer>> INTEGER_MAP = new HashMap<>();
    private static final Map<String, IRealm> REALM_MAP = new HashMap<>();
    private static final Map<ResourceKey<Biome>, Integer> BIOME_SPIRITUAL_MAP = new HashMap<>();
    private static final Map<ResourceKey<Level>, Float> LEVEL_SPIRITUAL_MAP = new HashMap<>();
    private static final Map<Item, Map<ISpiritualRoot, Integer>> ELIXIR_INGREDIENT_MAP = new HashMap<>();
    private static final Map<String, ISpiritualRoot> SPIRITUAL_ROOT_MAP = new HashMap<>();

    @Override
    public void registerSpiritualRoot(ISpiritualRoot type) {
        SPIRITUAL_ROOT_MAP.put(type.getRegistryName(), type);
    }

    @Override
    public List<ISpiritualRoot> getSpiritualRoots() {
        return SPIRITUAL_ROOT_MAP.values().stream().sorted(Comparator.comparingInt(ISpiritualRoot::getSortPriority)).toList();
    }

    @Override
    public Optional<ISpiritualRoot> getSpiritualRoot(String type) {
        return Optional.ofNullable(SPIRITUAL_ROOT_MAP.getOrDefault(type, null));
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
    public void registerRealm(IRealm type) {
        REALM_MAP.put(type.getRegistryName(), type);
    }

    @Override
    public Collection<IRealm> getRealms() {
        return Collections.unmodifiableCollection(REALM_MAP.values());
    }

    @Override
    public Optional<IRealm> getRealm(String type) {
        return Optional.ofNullable(REALM_MAP.getOrDefault(type, null));
    }

    @Override
    public IRealm getEntityRealm(Entity entity) {
        if(entity instanceof Player){
            PlayerUtil.getManagerResult((Player) entity, PlayerDataManager::getRealm, Realms.MORTALITY);
        }
        return entity instanceof IHasRealm ? ((IHasRealm) entity).getRealm() : Realms.MORTALITY;
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
    public int getSpiritualMana(Player player) {
        return PlayerUtil.getSpiritualMana(player);
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

    @Override
    public void registerElixirIngredient(Item item, Map<ISpiritualRoot, Integer> map) {
        ELIXIR_INGREDIENT_MAP.put(item, map);
    }

    @Override
    public Map<ISpiritualRoot, Integer> getElixirIngredient(Item item) {
        return ELIXIR_INGREDIENT_MAP.getOrDefault(item, Map.of());
    }

    @Override
    public Set<Item> getElixirIngredients() {
        return Collections.unmodifiableSet(ELIXIR_INGREDIENT_MAP.keySet());
    }

}
