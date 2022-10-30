package hungteen.immortal.api;

import hungteen.htlib.interfaces.IRangeData;
import hungteen.immortal.api.registry.*;
import hungteen.immortal.impl.Realms;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 22:26
 **/
public class DummyAPI implements ImmortalAPI.IImmortalAPI {

    public static final ImmortalAPI.IImmortalAPI INSTANCE = new DummyAPI();

    @Override
    public void registerSpiritualRoot(ISpiritualRoot type) {
    }

    @Override
    public List<ISpiritualRoot> getSpiritualRoots() {
        return List.of();
    }

    @Override
    public Optional<ISpiritualRoot> getSpiritualRoot(String type) {
        return Optional.empty();
    }

    @Override
    public void registerSpell(ISpell type) {

    }

    @Override
    public Collection<ISpell> getSpells() {
        return List.of();
    }

    @Override
    public Optional<ISpell> getSpell(String type) {
        return Optional.empty();
    }

    @Override
    public void registerRealm(IRealm type) {

    }

    @Override
    public Collection<IRealm> getRealms() {
        return List.of();
    }

    @Override
    public Optional<IRealm> getRealm(String type) {
        return Optional.empty();
    }

    @Override
    public IRealm getEntityRealm(Entity entity) {
        return Realms.MORTALITY;
    }

    @Override
    public void registerIntegerData(IRangeData<Integer> type) {

    }

    @Override
    public Collection<IRangeData<Integer>> getIntegerCollection() {
        return List.of();
    }

    @Override
    public Optional<IRangeData<Integer>> getIntegerData(String type) {
        return Optional.empty();
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
    public void registerElixirIngredient(Item item, Map<ISpiritualRoot, Integer> map) {

    }

    @Override
    public Map<ISpiritualRoot, Integer> getElixirIngredient(Item item) {
        return Map.of();
    }

    @Override
    public Set<Item> getElixirIngredients() {
        return Set.of();
    }
}
