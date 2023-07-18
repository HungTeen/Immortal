package hungteen.imm.common.world;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.helper.registry.EffectHelper;
import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.common.impl.codec.ElixirEffects;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.item.elixirs.CustomElixirItem;
import hungteen.imm.util.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ClampedNormalInt;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-16 12:17
 **/
public class ElixirManager extends SavedData {

    public static final int BITS = 7;
    public static final int CLAMPED_BITS = 3;
    public static final int EXPLODE_BIT = 0;
    private final ServerLevel level;
    private final Map<ResourceLocation, BitSet> elixirValue = new HashMap<>();
    private final Map<ResourceLocation, BitSet> elixirDirection = new HashMap<>();

    public ElixirManager(ServerLevel level) {
        this.level = level;
        this.setDirty();
    }

    /**
     * @return empty means will explode.
     */
    public static Optional<ItemStack> getElixirResult(ServerLevel level, SimpleContainer container){
        final ElixirManager manager = get(level);
        final Map<Integer, Integer> bins = new HashMap<>();
        for(int i = 0; i < container.getContainerSize(); ++ i){
            if(! container.getItem(i).isEmpty()){
                final ResourceLocation key = ItemHelper.get().getKey(container.getItem(i).getItem());
                final BitSet value = manager.getElixirValue(key);
                final BitSet direction = manager.getElixirDirection(key);
                for(int j = 0; j < BITS; ++ j){
                    if(value.get(j)){
                        final int v = bins.getOrDefault(j, 0);
                        bins.put(j, v + (direction.get(j) ? 1 : -1));
                    }
                }
            }
        }
        if(bins.getOrDefault(EXPLODE_BIT, 0) <= 0){
            final ItemStack stack = new ItemStack(IMMItems.CUSTOM_ELIXIR.get());
            CustomElixirItem.setEffects(stack, ElixirEffects.registry().getValues(level).stream()
                    .map(effect -> Pair.of(effect.effect(), getEffectLevel(effect.maxMatchCnt(bins))))
                    .filter(p -> p.getSecond() > 0)
                    .map(pair -> EffectHelper.effect(pair.getFirst(), 200, pair.getSecond() - 1))
                    .toList()
            );
            return Optional.of(stack);
        }
        return Optional.empty();
    }

    public static int getEffectLevel(int level){
        int ans = 0;
        while(level > 0){
            level >>= 1;
            ++ ans;
        }
        return Math.max(0, ans - 1);
    }

    /**
     * used in item model gen.
     */
    public static ResourceLocation getOuterLayer(Rarity rarity){
        return rarity == Rarity.RARE ? Util.prefix("item/elixir_heaven_layer") :
                rarity == Rarity.UNCOMMON ? Util.prefix("item/elixir_earth_layer") :
                        Util.prefix("item/elixir_human_layer");
    }

    public static BitSet getElixirValue(ServerLevel level, Item item) {
        return get(level).getElixirValue(ItemHelper.get().getKey(item));
    }

    private void setElixirValue(ResourceLocation location, byte[] value) {
        this.elixirValue.put(location, BitSet.valueOf(value));
        this.setDirty();
    }

    private BitSet getElixirValue(ResourceLocation location) {
        return this.elixirValue.computeIfAbsent(location, l -> {
            this.setDirty();
            return getRandomElixirValue();
        });
    }

    private void setElixirDirection(ResourceLocation location, byte[] value) {
        this.elixirDirection.put(location, BitSet.valueOf(value));
        this.setDirty();
    }

    private BitSet getElixirDirection(ResourceLocation location) {
        return this.elixirDirection.computeIfAbsent(location, l -> {
            this.setDirty();
            return getRandomElixirDirection();
        });
    }

    private BitSet getRandomElixirDirection(){
        final RandomSource random = level.getRandom();
        final BitSet bitSet = new BitSet(BITS);
        for(int i = 0; i < BITS; ++ i){
            if(random.nextFloat() < 0.5F) bitSet.set(i);
        }
        return bitSet;
    }

    private BitSet getRandomElixirValue(){
        final RandomSource random = level.getRandom();
        final int bitCnt = 1 + Math.abs(ClampedNormalInt.of(0, 1, - CLAMPED_BITS, CLAMPED_BITS).sample(random));
        final BitSet bitSet = new BitSet(BITS);
        // Just try set bit, not exactly.
        for(int i = 0; i < bitCnt; ++ i){
            final int bit = random.nextInt(BITS);
            bitSet.set(bit);
        }
        return bitSet;
    }

    public static ElixirManager get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent((compound) -> load(level, compound), () -> new ElixirManager(level), "elixir_manager");
    }

    private static ElixirManager load(ServerLevel level, CompoundTag tag) {
        ElixirManager manager = new ElixirManager(level);
        if (tag.contains("ElixirValues")) {
            final ListTag list = tag.getList("ElixirValues", Tag.TAG_COMPOUND);
            list.stream().filter(CompoundTag.class::isInstance).map(CompoundTag.class::cast).forEach(nbt -> {
               if(nbt.contains("Name")){
                   final ResourceLocation key = new ResourceLocation(nbt.getString("Name"));
                   if(nbt.contains("Value")){
                       manager.setElixirValue(key, nbt.getByteArray("Value"));
                   }
                   if(nbt.contains("Direction")){
                       manager.setElixirDirection(key, nbt.getByteArray("Direction"));
                   }
               }
            });
        }
        return manager;
    }

    public CompoundTag save(CompoundTag tag) {
        {
            final ListTag list = new ListTag();
            this.elixirValue.keySet().forEach(key -> {
                final CompoundTag nbt = new CompoundTag();
                nbt.putString("Name", key.toString());
                nbt.putByteArray("Value", this.elixirValue.get(key).toByteArray());
                nbt.putByteArray("Direction", this.getElixirDirection(key).toByteArray());
                list.add(nbt);
            });
            tag.put("ElixirValues", list);
        }
        return tag;
    }

}
