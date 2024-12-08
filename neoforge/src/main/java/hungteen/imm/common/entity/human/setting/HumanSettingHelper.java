package hungteen.imm.common.entity.human.setting;

import hungteen.htlib.util.SimpleWeightedList;
import hungteen.htlib.util.WeightedList;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.common.cultivation.realm.MultiRealm;
import hungteen.imm.common.entity.human.HumanLikeEntity;
import hungteen.imm.common.entity.human.HumanSettings;
import hungteen.imm.common.entity.human.setting.loot.ItemEntry;
import hungteen.imm.common.entity.human.setting.loot.SlotSetting;
import hungteen.imm.common.entity.human.setting.trade.TradeEntry;
import hungteen.imm.common.item.SecretManualItem;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/8 13:27
 **/
public abstract class HumanSettingHelper {

    /**
     * 每次匹配太慢了，根据实体类型缓存，提高性能。
     */
    private static final Map<EntityType<?>, List<HumanSetting>> SETTING_CACHE = new HashMap<>();

    public static Optional<HumanSetting> getRandomSetting(Level level, HumanLikeEntity human) {
        return WeightedList.create(getMatchSettings(level, human)).getRandomItem(human.getRandom());
    }

    public static List<HumanSetting> getMatchSettings(Level level, HumanLikeEntity human) {
        return getSettings(level, human.getType()).stream().filter(setting -> setting.predicateSetting().match(human)).toList();
    }

    public static List<HumanSetting> getSettings(Level level, EntityType<?> entityType) {
        if(! SETTING_CACHE.containsKey(entityType)){
            SETTING_CACHE.put(entityType, HumanSettings.registry().getValues(level).stream().filter(setting -> setting.predicateSetting().type().equals(entityType)).toList());
        }
        return SETTING_CACHE.get(entityType);
    }

    public static TradeEntry emeraldTrade(int emerald, SpellType spell, int level){
        return new TradeEntry(
                List.of(new ItemStack(Items.EMERALD, emerald)),
                List.of(SecretManualItem.create(spell, level)),
                constant(1)
        );
    }

    public static TradeEntry trade(Item item, int count, Item result, int resultCount){
        return new TradeEntry(
                List.of(stack(item, count)),
                List.of(stack(result, resultCount))
        );
    }

    public static TradeEntry trade(Item item, int count, Item result, int resultCount, IntProvider tradeCount){
        return new TradeEntry(
                List.of(stack(item, count)),
                List.of(stack(result, resultCount)),
                tradeCount
        );
    }

    public static TradeEntry trade(Item item, int count, Item result, int resultCount, IntProvider tradeCount, FloatProvider xp){
        return new TradeEntry(
                List.of(stack(item, count)),
                List.of(stack(result, resultCount)),
                tradeCount,
                xp
        );
    }

    public static ItemStack stack(ItemLike item) {
        return new ItemStack(item);
    }

    public static ItemStack stack(ItemLike item, int count) {
        return new ItemStack(item, count);
    }

    public static IntProvider constant(int value) {
        return ConstantInt.of(value);
    }

    public static IntProvider range(int from, int to) {
        return UniformInt.of(from, to);
    }

    public static HumanPredicateSetting predicate(EntityType<?> entityType){
        return new HumanPredicateSetting(entityType, List.of(), List.of());
    }

    public static HumanPredicateSetting predicate(EntityType<?> entityType, MultiRealm multiRealm, QiRootType... roots){
        return new HumanPredicateSetting(entityType, List.of(multiRealm.getRealms()), List.of(roots));
    }

    public static HumanPredicateSetting predicate(EntityType<?> entityType, RealmType type, QiRootType... roots){
        return new HumanPredicateSetting(entityType, List.of(type), List.of(roots));
    }

    public static SlotSetting single(ItemEntry entry) {
        return new SlotSetting(SimpleWeightedList.single(entry));
    }

    public static ItemEntry single(Item item, int minPoint, int maxPoint) {
        return new ItemEntry(stack(item), constant(1), range(minPoint, maxPoint));
    }

    public static ItemEntry multi(Item item, int minCount, int maxCount) {
        return new ItemEntry(stack(item), range(minCount, maxCount), constant(0));
    }

    public static SlotSetting pair(ItemEntry entry1, ItemEntry entry2) {
        return new SlotSetting(SimpleWeightedList.pair(entry1, entry2));
    }

    public static SlotSetting pair(Item item1, Item item2, int minPoint, int maxPoint) {
        return pair(single(item1, minPoint, maxPoint), single(item2, minPoint, maxPoint));
    }
}
