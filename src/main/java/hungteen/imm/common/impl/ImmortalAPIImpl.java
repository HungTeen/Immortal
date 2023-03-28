package hungteen.imm.common.impl;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.api.ImmortalAPI;
import hungteen.imm.api.interfaces.IHasRealm;
import hungteen.imm.api.registry.*;
import hungteen.imm.common.capability.player.PlayerDataManager;
import hungteen.imm.common.impl.registry.InventoryLootTypes;
import hungteen.imm.common.impl.registry.PlayerRangeNumbers;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import hungteen.imm.utils.Constants;
import hungteen.imm.utils.PlayerUtil;
import hungteen.imm.utils.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
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

    private static final HTSimpleRegistry<ISectType> SECT_TYPES = HTRegistryManager.create(Util.prefix("sect"));
    private static final Map<ResourceKey<Biome>, Integer> BIOME_SPIRITUAL_MAP = new HashMap<>();
    private static final Map<ResourceKey<Level>, Float> LEVEL_SPIRITUAL_MAP = new HashMap<>();

    @Override
    public Optional<IHTSimpleRegistry<ISpiritualType>> spiritualRegistry() {
        return Optional.of(SpiritualTypes.registry());
    }

    @Override
    public Optional<IHTSimpleRegistry<ISpellType>> spellRegistry() {
        return Optional.of(SpellTypes.registry());
    }

    @Override
    public Optional<IHTSimpleRegistry<ISectType>> sectRegistry() {
        return Optional.of(SECT_TYPES);
    }

    @Override
    public Optional<IHTSimpleRegistry<IRealmType>> realmRegistry() {
        return Optional.of(RealmTypes.registry());
    }

    @Override
    public Optional<IHTSimpleRegistry<IRangeNumber<Integer>>> integerDataRegistry() {
        return Optional.of(PlayerRangeNumbers.registry());
    }

    @Override
    public Optional<IHTSimpleRegistry<IInventoryLootType>> inventoryLootRegistry() {
        return Optional.of(InventoryLootTypes.registry());
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

}
