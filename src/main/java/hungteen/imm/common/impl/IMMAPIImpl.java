package hungteen.imm.common.impl;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.registry.*;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.capability.chunk.ChunkCapability;
import hungteen.imm.common.impl.registry.*;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.common.world.LevelManager;
import hungteen.imm.util.LevelUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 22:32
 **/
public class IMMAPIImpl implements IMMAPI.IImmortalAPI {

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
        return Optional.of(SectTypes.registry());
    }

    @Override
    public Optional<IHTSimpleRegistry<IRealmType>> realmRegistry() {
        return Optional.of(RealmTypes.registry());
    }

    @Override
    public Optional<IHTSimpleRegistry<IRangeNumber<Integer>>> integerDataRegistry() {
        return Optional.of(PlayerRangeIntegers.registry());
    }

    @Override
    public Optional<IHTSimpleRegistry<IRangeNumber<Float>>> floatDataRegistry() {
        return Optional.of(PlayerRangeFloats.registry());
    }

    @Override
    public Optional<IHTSimpleRegistry<IInventoryLootType>> inventoryLootRegistry() {
        return Optional.of(InventoryLootTypes.registry());
    }

    @Override
    public IRealmType getEntityRealm(Entity entity) {
        return RealmManager.getEntityRealm(entity);
    }

    @Override
    public float getSpiritualMana(Player player) {
        return PlayerUtil.getMana(player);
    }

    @Override
    public void registerLevelRealmSetting(ResourceKey<Level> level, int lowestRealm, int highestRealm) {
        LevelManager.registerLevelRealmSetting(level, new LevelManager.LevelRealmSetting(lowestRealm, highestRealm));
    }

    @Override
    public void registerBiomeRealmSetting(ResourceKey<Biome> biome, float minChange, float maxChange) {
        LevelManager.registerBiomeRealmSetting(biome, new LevelManager.BiomeRealmSetting(minChange, maxChange));
    }

    @Override
    public float getSpiritualRate(Level level, BlockPos pos) {
        return LevelUtil.getChunkCapOpt(level, pos).map(ChunkCapability::getSpiritualRate).orElse(0F);
    }

}
