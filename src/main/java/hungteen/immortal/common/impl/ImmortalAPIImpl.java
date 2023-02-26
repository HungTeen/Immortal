package hungteen.immortal.common.impl;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.IHasRealm;
import hungteen.immortal.api.registry.IRealmType;
import hungteen.immortal.api.registry.ISectType;
import hungteen.immortal.api.registry.ISpellType;
import hungteen.immortal.api.registry.ISpiritualType;
import hungteen.immortal.common.capability.player.PlayerDataManager;
import hungteen.immortal.utils.Constants;
import hungteen.immortal.utils.PlayerUtil;
import hungteen.immortal.utils.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 22:32
 **/
public class ImmortalAPIImpl implements ImmortalAPI.IImmortalAPI {

    private static final HTSimpleRegistry<ISectType> SECT_TYPES = HTRegistryManager.create(Util.prefix("sect"));
    private static final HTSimpleRegistry<IRealmType> REALM_TYPES = HTRegistryManager.create(Util.prefix("realm"));
    private static final HTSimpleRegistry<IRangeNumber<Integer>> INTEGER_DATA_TYPES = HTRegistryManager.create(Util.prefix("integer_data"));
    private static final Map<ResourceKey<Biome>, Integer> BIOME_SPIRITUAL_MAP = new HashMap<>();
    private static final Map<ResourceKey<Level>, Float> LEVEL_SPIRITUAL_MAP = new HashMap<>();
    private static final Map<Item, Map<ISpiritualType, Integer>> ELIXIR_INGREDIENT_MAP = new HashMap<>();

    @Override
    public Optional<IHTSimpleRegistry<ISpiritualType>> spiritualRegistry() {
        return Optional.of(SpiritualTypes.spiritualRegistry());
    }

    @Override
    public Optional<IHTSimpleRegistry<ISpellType>> spellRegistry() {
        return Optional.of(SpellTypes.spellRegistry());
    }

    @Override
    public Optional<IHTSimpleRegistry<ISectType>> sectRegistry() {
        return Optional.of(SECT_TYPES);
    }

    @Override
    public Optional<IHTSimpleRegistry<IRealmType>> realmRegistry() {
        return Optional.of(REALM_TYPES);
    }

    @Override
    public Optional<IHTSimpleRegistry<IRangeNumber<Integer>>> integerDataRegistry() {
        return Optional.of(INTEGER_DATA_TYPES);
    }

    @Override
    public IRealmType getEntityRealm(Entity entity) {
        if(entity instanceof Player){
            PlayerUtil.getManagerResult((Player) entity, PlayerDataManager::getRealmType, RealmTypes.MORTALITY);
        }
        //TODO 境界
        return entity instanceof IHasRealm ? ((IHasRealm) entity).getRealm() : RealmTypes.MORTALITY;
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
    public void registerElixirIngredient(Item item, Map<ISpiritualType, Integer> map) {
        ELIXIR_INGREDIENT_MAP.put(item, map);
    }

    @Override
    public Map<ISpiritualType, Integer> getElixirIngredient(Item item) {
        return ELIXIR_INGREDIENT_MAP.getOrDefault(item, Map.of());
    }

    @Override
    public Set<Item> getElixirIngredients() {
        return Collections.unmodifiableSet(ELIXIR_INGREDIENT_MAP.keySet());
    }

}
