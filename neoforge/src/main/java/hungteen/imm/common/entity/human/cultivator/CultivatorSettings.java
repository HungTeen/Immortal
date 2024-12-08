package hungteen.imm.common.entity.human.cultivator;

import hungteen.htlib.util.SimpleWeightedList;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.human.HumanSettings;
import hungteen.imm.common.entity.human.setting.HumanSetting;
import hungteen.imm.common.entity.human.setting.HumanSettingHelper;
import hungteen.imm.common.entity.human.setting.trade.TradeEntry;
import hungteen.imm.common.entity.human.setting.trade.TradeSetting;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/8 13:28
 **/
public class CultivatorSettings extends HumanSettingHelper {

    public static void register(BootstrapContext<HumanSetting> context) {
        context.register(HumanSettings.POOR_MORTALITY, new HumanSetting(
                predicate(IMMEntities.WANDERING_CULTIVATOR.get(), RealmTypes.MORTALITY),
                100,
                List.of(
                        single(multi(Items.APPLE, 5, 15)),
                        single(multi(Items.ROTTEN_FLESH, 32, 64)),
                        single(multi(Items.ARROW, 8, 16)),
                        single(multi(Items.ENDER_PEARL, 0, 1)),
                        single(single(Items.SHIELD, 0, 1)),
                        single(single(Items.BOW, 1, 4)),
                        pair(Items.WOODEN_SWORD, Items.STONE_SWORD, 0, 8),
                        pair(Items.LEATHER_HELMET, Items.CHAINMAIL_HELMET, 0, 8),
                        pair(Items.LEATHER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, 0, 8),
                        pair(Items.LEATHER_LEGGINGS, Items.CHAINMAIL_LEGGINGS, 0, 8),
                        pair(Items.LEATHER_BOOTS, Items.CHAINMAIL_BOOTS, 0, 8)
                ),
                Optional.empty(),
                List.of()
        ));
        context.register(HumanSettings.COMMON_MORTALITY, new HumanSetting(
                predicate(IMMEntities.WANDERING_CULTIVATOR.get(), RealmTypes.MORTALITY),
                200,
                List.of(
                        single(multi(Items.BREAD, 15, 25)),
                        single(multi(Items.ROTTEN_FLESH, 32, 64)),
                        single(multi(Items.GOLDEN_APPLE, 1, 3)),
                        single(multi(Items.ARROW, 16, 32)),
                        single(multi(Items.ENDER_PEARL, 1, 2)),
                        single(single(Items.SHIELD, 1, 1)),
                        single(single(Items.BOW, 0, 15)),
                        single(single(Items.IRON_SWORD, 0, 15)),
                        single(single(Items.IRON_HELMET, 0, 15)),
                        single(single(Items.IRON_CHESTPLATE, 0, 15)),
                        single(single(Items.IRON_LEGGINGS, 0, 15)),
                        single(single(Items.IRON_BOOTS, 0, 15))
                ),
                Optional.of(new TradeSetting(range(2, 3), true, SimpleWeightedList.<TradeEntry>builder()
                        .add(trade(Items.DIAMOND, 1, Items.MELON_SEEDS, 3, range(1, 3)), 10)
                        .add(trade(Items.DIAMOND, 1, Items.PUMPKIN_SEEDS, 3, range(1, 3)), 10)
                        .add(trade(Items.DIAMOND, 1, Items.BEETROOT_SEEDS, 3, range(1, 3)), 10)
                        .build()
                )),
                List.of()
        ));
        context.register(HumanSettings.STRONG_MORTALITY, new HumanSetting(
                predicate(IMMEntities.WANDERING_CULTIVATOR.get(), RealmTypes.MORTALITY),
                100,
                List.of(
                        single(multi(Items.PORKCHOP, 5, 15)),
                        single(multi(Items.GOLDEN_APPLE, 1, 3)),
                        single(multi(Items.ARROW, 32, 64)),
                        single(multi(Items.ENDER_PEARL, 2, 4)),
                        single(single(Items.SHIELD, 1, 1)),
                        single(single(Items.BOW, 0, 25)),
                        pair(Items.IRON_SWORD, Items.DIAMOND_SWORD, 0, 20),
                        pair(Items.IRON_HELMET, Items.DIAMOND_HELMET, 0, 20),
                        pair(Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE, 0, 20),
                        pair(Items.IRON_LEGGINGS, Items.DIAMOND_LEGGINGS, 0, 20),
                        pair(Items.IRON_BOOTS, Items.DIAMOND_BOOTS, 0, 20)
                ),
                Optional.of(new TradeSetting(range(2, 4), true, SimpleWeightedList.<TradeEntry>builder()
                        .add(trade(Items.DIAMOND, 1, Items.EMERALD, 8, range(1, 2)), 10)
                        .add(trade(Items.DIAMOND, 1, Items.MELON_SEEDS, 3, range(1, 3)), 10)
                        .add(trade(Items.DIAMOND, 1, Items.PUMPKIN_SEEDS, 3, range(1, 3)), 10)
                        .add(trade(Items.DIAMOND, 1, Items.BEETROOT_SEEDS, 3, range(1, 3)), 10)
                        .build()
                )),
                List.of()
        ));

    }
}
