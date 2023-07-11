package hungteen.imm.common.impl.codec;

import hungteen.htlib.api.interfaces.IHTCodecRegistry;
import hungteen.htlib.common.registry.HTCodecRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.util.SimpleWeightedList;
import hungteen.htlib.util.WeightedList;
import hungteen.imm.api.registry.IInventoryLootType;
import hungteen.imm.common.codec.ItemEntry;
import hungteen.imm.common.entity.human.setting.HumanSetting;
import hungteen.imm.common.entity.human.setting.LootSetting;
import hungteen.imm.common.entity.human.setting.trade.TradeEntry;
import hungteen.imm.common.entity.human.setting.trade.TradeSetting;
import hungteen.imm.common.impl.registry.InventoryLootTypes;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/24 15:09
 */
public class HumanSettings {

    /**
     * 不是全局数据包！
     */
    private static final HTCodecRegistry<HumanSetting> SETTING = HTRegistryManager.create(Util.prefix("human_setting"), () -> HumanSetting.CODEC, () -> HumanSetting.CODEC);

    public static final ResourceKey<HumanSetting> DEFAULT = create("default");
    public static final ResourceKey<HumanSetting> POOR_VANILLA = create("poor_vanilla");

    public static void register(BootstapContext<HumanSetting> context){
        context.register(DEFAULT, new HumanSetting(
                InventoryLootTypes.VANILLA,
                0,
                List.of(),
                Optional.empty()
        ));
        context.register(POOR_VANILLA, new HumanSetting(
                InventoryLootTypes.VANILLA,
                200,
                Arrays.asList(
                        singleLoot(new ItemEntry(
                                new ItemStack(Items.BREAD),
                                UniformInt.of(5, 15),
                                ConstantInt.of(0)
                        )),
                        singleLoot(new ItemEntry(
                                new ItemStack(Items.ROTTEN_FLESH),
                                UniformInt.of(32, 64),
                                ConstantInt.of(0)
                        )),
                        singleLoot(new ItemEntry(
                                new ItemStack(Items.GOLDEN_APPLE),
                                UniformInt.of(0, 1),
                                ConstantInt.of(0)
                        )),
                        singleLoot(new ItemEntry(
                                new ItemStack(Items.ARROW),
                                UniformInt.of(16, 32),
                                ConstantInt.of(0)
                        )),
                        singleLoot(new ItemEntry(
                                new ItemStack(Items.ENDER_PEARL),
                                UniformInt.of(0, 1),
                                ConstantInt.of(0)
                        )),
                        singleLoot(new ItemEntry(
                                new ItemStack(Items.SHIELD),
                                ConstantInt.of(1),
                                ConstantInt.of(0)
                        )),
                        singleLoot(new ItemEntry(
                                new ItemStack(Items.BOW),
                                ConstantInt.of(1),
                                UniformInt.of(0, 4)
                        )),
                        pairLoot(new ItemEntry(
                                new ItemStack(Items.IRON_SWORD),
                                ConstantInt.of(1),
                                UniformInt.of(0, 5)
                        ), new ItemEntry(
                                new ItemStack(Items.STONE_SWORD),
                                ConstantInt.of(1),
                                UniformInt.of(0, 8)
                        )),
                        pairLoot(new ItemEntry(
                                new ItemStack(Items.IRON_CHESTPLATE),
                                ConstantInt.of(1),
                                UniformInt.of(0, 4)
                        ), new ItemEntry(
                                new ItemStack(Items.CHAINMAIL_CHESTPLATE),
                                ConstantInt.of(1),
                                UniformInt.of(0, 8)
                        )),
                        pairLoot(new ItemEntry(
                                new ItemStack(Items.IRON_LEGGINGS),
                                ConstantInt.of(1),
                                UniformInt.of(0, 5)
                        ), new ItemEntry(
                                new ItemStack(Items.CHAINMAIL_LEGGINGS),
                                ConstantInt.of(1),
                                UniformInt.of(0, 8)
                        )),
                        pairLoot(new ItemEntry(
                                new ItemStack(Items.CHAINMAIL_HELMET),
                                ConstantInt.of(1),
                                UniformInt.of(0, 8)
                        ), new ItemEntry(
                                new ItemStack(Items.GOLDEN_HELMET),
                                ConstantInt.of(1),
                                UniformInt.of(0, 10)
                        )),
                        pairLoot(new ItemEntry(
                                new ItemStack(Items.IRON_BOOTS),
                                ConstantInt.of(1),
                                UniformInt.of(0, 4)
                        ), new ItemEntry(
                                new ItemStack(Items.CHAINMAIL_BOOTS),
                                ConstantInt.of(1),
                                UniformInt.of(0, 8)
                        ))
                ),
                Optional.of(
                        new TradeSetting(
                                UniformInt.of(2, 3),
                                true,
                                SimpleWeightedList.<TradeEntry>builder()
                                        .add(new TradeEntry(
                                                List.of(new ItemStack(Items.EMERALD, 3)),
                                                List.of(new ItemStack(Items.DIAMOND_SWORD)),
                                                UniformInt.of(3, 3)
                                        ), 10)
                                        .add(new TradeEntry(
                                                List.of(new ItemStack(Items.EMERALD, 3)),
                                                List.of(new ItemStack(Items.DIAMOND_HELMET)),
                                                UniformInt.of(3, 3)
                                        ), 10)
                                        .add(new TradeEntry(
                                                List.of(
                                                        new ItemStack(Items.EMERALD, 3),
                                                        new ItemStack(Items.DIAMOND, 2)
                                                ),
                                                List.of(new ItemStack(Items.DIAMOND_CHESTPLATE)),
                                                UniformInt.of(3, 3)
                                        ), 10)
                                        .add(new TradeEntry(
                                                List.of(new ItemStack(Items.EMERALD, 1)),
                                                List.of(new ItemStack(Items.GOLDEN_HOE)),
                                                UniformInt.of(3, 3)
                                        ), 10)
                                        .add(new TradeEntry(
                                                List.of(new ItemStack(Items.EMERALD, 2)),
                                                List.of(new ItemStack(Items.LAPIS_BLOCK)),
                                                UniformInt.of(3, 3)
                                        ), 10)
                                        .build()
                        )
                )
        ));
    }

//    public static HTRegistryHolder<HumanSetting> RICH_VANILLA = LOOTS.innerRegister(
//            Util.prefix("rich_vanilla"),
//            new HumanSetting(
//                    InventoryLootTypes.VANILLA,
//                    100,
//                    Arrays.asList(
//                            singleLoot(new ItemEntry(
//                                    new ItemStack(Items.PUMPKIN_PIE),
//                                    UniformInt.of(16, 32),
//                                    ConstantInt.of(0)
//                            )),
//                            singleLoot(new ItemEntry(
//                                    new ItemStack(Items.GOLDEN_APPLE),
//                                    UniformInt.of(2, 5),
//                                    ConstantInt.of(0)
//                            )),
//                            singleLoot(new ItemEntry(
//                                    new ItemStack(Items.ARROW),
//                                    UniformInt.of(32, 64),
//                                    ConstantInt.of(0)
//                            )),
//                            singleLoot(new ItemEntry(
//                                    new ItemStack(Items.ENDER_PEARL),
//                                    UniformInt.of(2, 5),
//                                    ConstantInt.of(0)
//                            )),
//                            singleLoot(new ItemEntry(
//                                    new ItemStack(Items.SHIELD),
//                                    ConstantInt.of(1),
//                                    ConstantInt.of(0)
//                            )),
//                            singleLoot(new ItemEntry(
//                                    new ItemStack(Items.BOW),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(8, 15)
//                            )),
//                            pairLoot(new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_SWORD),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(5, 15)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.NETHERITE_SWORD),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(5, 10)
//                            )),
//                            pairLoot(new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_CHESTPLATE),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(5, 10)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.NETHERITE_CHESTPLATE),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(3, 8)
//                            )),
//                            pairLoot(new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_LEGGINGS),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(5, 10)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.NETHERITE_LEGGINGS),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(3, 8)
//                            )),
//                            pairLoot(new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_HELMET),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(8, 12)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.IRON_HELMET),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(10, 15)
//                            )),
//                            pairLoot(new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_BOOTS),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(8, 12)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.IRON_BOOTS),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(10, 15)
//                            ))
//                    ),
//                    Optional.empty()
//            )
//    );
//
//    public static HTRegistryHolder<HumanSetting> NORMAL_VANILLA = LOOTS.innerRegister(
//            Util.prefix("normal_vanilla"),
//            new HumanSetting(
//                    InventoryLootTypes.VANILLA,
//                    300,
//                    Arrays.asList(
//                            singleLoot(new ItemEntry(
//                                    new ItemStack(Items.BREAD),
//                                    UniformInt.of(24, 48),
//                                    ConstantInt.of(0)
//                            )),
//                            singleLoot(new ItemEntry(
//                                    new ItemStack(Items.GOLDEN_APPLE),
//                                    UniformInt.of(1, 3),
//                                    ConstantInt.of(0)
//                            )),
//                            singleLoot(new ItemEntry(
//                                    new ItemStack(Items.ARROW),
//                                    UniformInt.of(24, 48),
//                                    ConstantInt.of(0)
//                            )),
//                            singleLoot(new ItemEntry(
//                                    new ItemStack(Items.ENDER_PEARL),
//                                    UniformInt.of(1, 2),
//                                    ConstantInt.of(0)
//                            )),
//                            singleLoot(new ItemEntry(
//                                    new ItemStack(Items.SHIELD),
//                                    ConstantInt.of(1),
//                                    ConstantInt.of(0)
//                            )),
//                            singleLoot(new ItemEntry(
//                                    new ItemStack(Items.BOW),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(4, 8)
//                            )),
//                            pairLoot(new ItemEntry(
//                                    new ItemStack(Items.IRON_SWORD),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(5, 10)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_SWORD),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(2, 5)
//                            )),
//                            pairLoot(new ItemEntry(
//                                    new ItemStack(Items.IRON_CHESTPLATE),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(5, 10)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_CHESTPLATE),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(2, 5)
//                            )),
//                            pairLoot(new ItemEntry(
//                                    new ItemStack(Items.IRON_LEGGINGS),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(5, 10)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_LEGGINGS),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(2, 5)
//                            )),
//                            pairLoot(new ItemEntry(
//                                    new ItemStack(Items.IRON_HELMET),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(5, 10)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.GOLDEN_HELMET),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(10, 15)
//                            )),
//                            pairLoot(new ItemEntry(
//                                    new ItemStack(Items.IRON_BOOTS),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(5, 10)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_BOOTS),
//                                    ConstantInt.of(1),
//                                    UniformInt.of(2, 5)
//                            ))
//                    ),
//                    Optional.empty()
//            )
//    );

    public static Optional<HumanSetting> getRandomSetting(Level level, IInventoryLootType type, RandomSource random) {
        return WeightedList.create(SETTING.getValues(level).stream().filter(l -> l.type() == type).toList()).getRandomItem(random);
    }

    public static List<HumanSetting> getInventoryLoots(Level level, IInventoryLootType type) {
        return SETTING.getValues(level).stream().filter(l -> l.type() == type).toList();
    }

    public static LootSetting singleLoot(ItemEntry entry) {
        return new LootSetting(SimpleWeightedList.single(entry));
    }

    public static LootSetting pairLoot(ItemEntry entry1, ItemEntry entry2) {
        return new LootSetting(SimpleWeightedList.pair(entry1, entry2));
    }

    public static IHTCodecRegistry<HumanSetting> registry(){
        return SETTING;
    }

    public static ResourceKey<HumanSetting> create(String name){
        return registry().createKey(Util.prefix(name));
    }


}
