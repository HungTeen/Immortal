package hungteen.imm.common.impl;

import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.cultivation.SpellTypes;

import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-24 22:32
 **/
public class IMMAPIImpl implements IMMAPI {

    @Override
    public int apiVersion() {
        return 1;
    }

    @Override
    public Optional<HTCustomRegistry<QiRootType>> qiRootRegistry() {
        return Optional.of(QiRootTypes.registry());
    }

    @Override
    public Optional<HTCustomRegistry<RealmType>> realmRegistry() {
        return Optional.of(RealmTypes.registry());
    }

    @Override
    public Optional<HTCustomRegistry<SpellType>> spellRegistry() {
        return Optional.of(SpellTypes.registry());
    }

//    @Override
//    public Optional<IHTSimpleRegistry<ISectType>> sectRegistry() {
//        return Optional.of(SectTypes.registry());
//    }
//
//    @Override
//    public Optional<IHTSimpleRegistry<IRangeNumber<Integer>>> integerDataRegistry() {
//        return Optional.of(PlayerRangeIntegers.registry());
//    }
//
//    @Override
//    public Optional<IHTSimpleRegistry<IRangeNumber<Float>>> floatDataRegistry() {
//        return Optional.of(PlayerRangeFloats.registry());
//    }

//    @Override
//    public IRealmType getEntityRealm(Entity entity) {
//        return RealmManager.getRealm(entity);
//    }
//
//    @Override
//    public float getSpiritualMana(Player player) {
//        return PlayerUtil.getQiAmount(player);
//    }
//
//    @Override
//    public void registerLevelRealmSetting(ResourceKey<Level> level, int lowestRealm, int highestRealm) {
//        LevelManager.registerLevelRealmSetting(level, new LevelManager.LevelRealmSetting(lowestRealm, highestRealm));
//    }
//
//    @Override
//    public void registerBiomeRealmSetting(ResourceKey<Biome> biome, float minChange, float maxChange) {
//        LevelManager.registerBiomeRealmSetting(biome, new LevelManager.BiomeRealmSetting(minChange, maxChange));
//    }
//
//    @Override
//    public float getSpiritualRate(Level level, BlockPos pos) {
//        return LevelUtil.getSpiritualRate(level, pos);
//    }

}
