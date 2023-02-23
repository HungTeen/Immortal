package hungteen.immortal.api;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.immortal.api.registry.IRealmType;
import hungteen.immortal.api.registry.ISectType;
import hungteen.immortal.api.registry.ISpellType;
import hungteen.immortal.api.registry.ISpiritualType;
import hungteen.immortal.common.impl.RealmTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 22:26
 **/
public class DummyAPI implements ImmortalAPI.IImmortalAPI {

    public static final ImmortalAPI.IImmortalAPI INSTANCE = new DummyAPI();

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
    public IRealmType getEntityRealm(Entity entity) {
        return RealmTypes.MORTALITY;
    }

    @Override
    public int getSpiritualMana(Player player) {
        return 0;
    }

    @Override
    public void registerBiomeSpiritualValue(ResourceKey<Biome> biomeResourceKey, int spiritualValue) {

    }

    @Override
    public void registerLevelSpiritualRatio(ResourceKey<Level> levelResourceKey, float spiritualRatio) {

    }

    @Override
    public int getSpiritualValue(Level level, BlockPos pos) {
        return 0;
    }

    @Override
    public void registerElixirIngredient(Item item, Map<ISpiritualType, Integer> map) {

    }

    @Override
    public Map<ISpiritualType, Integer> getElixirIngredient(Item item) {
        return Map.of();
    }

    @Override
    public Set<Item> getElixirIngredients() {
        return Set.of();
    }
}
