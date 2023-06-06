package hungteen.imm.api;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.imm.api.registry.*;
import hungteen.imm.common.impl.registry.RealmTypes;
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
 * @create: 2022-09-24 22:26
 **/
public class DummyAPI implements IMMAPI.IImmortalAPI {

    public static final IMMAPI.IImmortalAPI INSTANCE = new DummyAPI();

    @Override
    public Optional<IHTSimpleRegistry<ISpiritualType>> spiritualRegistry() {
        return Optional.empty();
    }

    @Override
    public Optional<IHTSimpleRegistry<ISpellType>> spellRegistry() {
        return Optional.empty();
    }

    @Override
    public Optional<IHTSimpleRegistry<ISectType>> sectRegistry() {
        return Optional.empty();
    }

    @Override
    public Optional<IHTSimpleRegistry<IRealmType>> realmRegistry() {
        return Optional.empty();
    }

    @Override
    public Optional<IHTSimpleRegistry<IRangeNumber<Integer>>> integerDataRegistry() {
        return Optional.empty();
    }

    @Override
    public Optional<IHTSimpleRegistry<IRangeNumber<Float>>> floatDataRegistry() {
        return Optional.empty();
    }

    @Override
    public Optional<IHTSimpleRegistry<IInventoryLootType>> inventoryLootRegistry() {
        return Optional.empty();
    }

    @Override
    public IRealmType getEntityRealm(Entity entity) {
        return RealmTypes.MORTALITY;
    }

    @Override
    public float getSpiritualMana(Player player) {
        return 0;
    }

    @Override
    public void registerLevelRealmSetting(ResourceKey<Level> level, int lowestRealm, int highestRealm) {

    }

    @Override
    public void registerBiomeRealmSetting(ResourceKey<Biome> biome, float minChange, float maxChange) {

    }

    @Override
    public float getSpiritualRate(Level level, BlockPos pos) {
        return 0;
    }

}
