package hungteen.imm.common.entity.human.setting;

import hungteen.htlib.api.registry.HTCodecRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.util.SimpleWeightedList;
import hungteen.htlib.util.WeightedList;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.block.plants.GourdGrownBlock;
import hungteen.imm.common.codec.ItemEntry;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.human.setting.trade.TradeEntry;
import hungteen.imm.common.entity.human.setting.trade.TradeSetting;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.item.SecretManualItem;
import hungteen.imm.common.item.artifacts.FlameGourd;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/2/24 15:09
 */
public interface HumanSettings {

    HTCodecRegistry<HumanSetting> SETTING = HTRegistryManager.codec(Util.prefix("human_setting"), () -> HumanSetting.CODEC, () -> HumanSetting.NETWORK_CODEC);

    ResourceKey<HumanSetting> DEFAULT = create("default");
    ResourceKey<HumanSetting> POOR_VANILLA = create("poor_vanilla");
    ResourceKey<HumanSetting> BEGINNER = create("beginner");
    ResourceKey<HumanSetting> METAL_BEGINNER = create("metal_beginner");
    ResourceKey<HumanSetting> WOOD_BEGINNER = create("wood_beginner");
    ResourceKey<HumanSetting> WATER_BEGINNER = create("water_beginner");
    ResourceKey<HumanSetting> FIRE_BEGINNER = create("fire_beginner");
    ResourceKey<HumanSetting> EARTH_BEGINNER = create("earth_beginner");

    static void register(BootstrapContext<HumanSetting> context) {
        context.register(DEFAULT, new HumanSetting(
                IMMEntities.EMPTY_CULTIVATOR.get(),
                1,
                Optional.empty(),
                List.of(),
                Optional.empty()
        ));
        context.register(POOR_VANILLA, new HumanSetting(
                IMMEntities.EMPTY_CULTIVATOR.get(),
                100,
                Optional.empty(),
                Arrays.asList(
                        single(new ItemEntry(stack(Items.BREAD), range(5, 15), constant(0))),
                        single(new ItemEntry(stack(Items.ROTTEN_FLESH), range(32, 64), constant(0))),
                        single(new ItemEntry(stack(Items.GOLDEN_APPLE), range(0, 1), constant(0))),
                        single(new ItemEntry(stack(Items.ARROW), range(16, 32), constant(0))),
                        single(new ItemEntry(stack(Items.ENDER_PEARL), range(0, 1), constant(0))),
                        single(new ItemEntry(stack(Items.SHIELD), constant(1), constant(0))),
                        single(new ItemEntry(stack(Items.BOW), constant(1), range(0, 4))),
                        pair(
                                new ItemEntry(stack(Items.IRON_SWORD), constant(1), range(0, 5)),
                                new ItemEntry(stack(Items.STONE_SWORD), constant(1), range(0, 8)
                                )),
                        pair(
                                new ItemEntry(stack(Items.IRON_CHESTPLATE), constant(1), range(0, 4)),
                                new ItemEntry(stack(Items.CHAINMAIL_CHESTPLATE), constant(1), range(0, 8)
                                )),
                        pair(
                                new ItemEntry(stack(Items.IRON_LEGGINGS), constant(1), range(0, 5)),
                                new ItemEntry(stack(Items.CHAINMAIL_LEGGINGS), constant(1), range(0, 8)
                                )),
                        pair(
                                new ItemEntry(stack(Items.CHAINMAIL_HELMET), constant(1), range(0, 8)),
                                new ItemEntry(stack(Items.GOLDEN_HELMET), constant(1), range(0, 10)
                                )),
                        pair(
                                new ItemEntry(stack(Items.IRON_BOOTS), constant(1), range(0, 4)),
                                new ItemEntry(stack(Items.CHAINMAIL_BOOTS), constant(1), range(0, 8)
                                ))
                ),
                Optional.of(
                        new TradeSetting(
                                range(2, 3),
                                true,
                                SimpleWeightedList.<TradeEntry>builder()
                                        .add(spellTrade(3, SpellTypes.MEDITATION, 1), 10)
                                        .add(new TradeEntry(
                                                List.of(new ItemStack(Items.EMERALD, 4)),
                                                List.of(new ItemStack(Items.DIAMOND_SWORD)),
                                                range(3, 3)
                                        ), 10)
                                        .add(new TradeEntry(
                                                List.of(new ItemStack(Items.EMERALD, 6)),
                                                List.of(new ItemStack(Items.DIAMOND_HELMET)),
                                                range(3, 3)
                                        ), 10)
                                        .add(new TradeEntry(
                                                List.of(
                                                        new ItemStack(Items.EMERALD, 8),
                                                        new ItemStack(Items.DIAMOND, 2)
                                                ),
                                                List.of(new ItemStack(Items.DIAMOND_CHESTPLATE)),
                                                range(3, 3)
                                        ), 10)
                                        .add(new TradeEntry(
                                                List.of(new ItemStack(Items.EMERALD, 1)),
                                                List.of(new ItemStack(Items.GOLDEN_HOE)),
                                                range(3, 3)
                                        ), 10)
                                        .add(new TradeEntry(
                                                List.of(new ItemStack(Items.EMERALD, 2)),
                                                List.of(new ItemStack(Items.LAPIS_BLOCK)),
                                                range(3, 3)
                                        ), 10)
                                        .build()
                        )
                )
        ));
        context.register(BEGINNER, new HumanSetting(
                IMMEntities.SPIRITUAL_BEGINNER_CULTIVATOR.get(),
                100,
                Optional.empty(),
                List.of(
                        single(new ItemEntry(
                                new ItemStack(Items.BREAD),
                                range(24, 48),
                                constant(0)
                        )),
                        single(new ItemEntry(
                                new ItemStack(Items.GOLDEN_APPLE),
                                range(1, 3),
                                constant(0)
                        )),
                        single(new ItemEntry(
                                new ItemStack(Items.ARROW),
                                range(24, 48),
                                constant(0)
                        )),
                        single(new ItemEntry(
                                new ItemStack(Items.ENDER_PEARL),
                                range(1, 2),
                                constant(0)
                        )),
                        single(new ItemEntry(
                                new ItemStack(Items.SHIELD),
                                constant(1),
                                constant(0)
                        )),
                        single(new ItemEntry(
                                new ItemStack(Items.BOW),
                                constant(1),
                                range(4, 8)
                        )),
                        pair(new ItemEntry(
                                new ItemStack(Items.IRON_SWORD),
                                constant(1),
                                range(5, 10)
                        ), new ItemEntry(
                                new ItemStack(Items.DIAMOND_SWORD),
                                constant(1),
                                range(2, 5)
                        )),
                        pair(new ItemEntry(
                                new ItemStack(Items.IRON_CHESTPLATE),
                                constant(1),
                                range(5, 10)
                        ), new ItemEntry(
                                new ItemStack(Items.DIAMOND_CHESTPLATE),
                                constant(1),
                                range(2, 5)
                        )),
                        pair(new ItemEntry(
                                new ItemStack(Items.IRON_LEGGINGS),
                                constant(1),
                                range(5, 10)
                        ), new ItemEntry(
                                new ItemStack(Items.DIAMOND_LEGGINGS),
                                constant(1),
                                range(2, 5)
                        )),
                        pair(new ItemEntry(
                                new ItemStack(Items.IRON_HELMET),
                                constant(1),
                                range(5, 10)
                        ), new ItemEntry(
                                new ItemStack(Items.GOLDEN_HELMET),
                                constant(1),
                                range(10, 15)
                        )),
                        pair(new ItemEntry(
                                new ItemStack(Items.IRON_BOOTS),
                                constant(1),
                                range(5, 10)
                        ), new ItemEntry(
                                new ItemStack(Items.DIAMOND_BOOTS),
                                constant(1),
                                range(2, 5)
                        ))
                ),
                Optional.of(new TradeSetting(
                        range(6, 10),
                        true,
                        SimpleWeightedList.<TradeEntry>builder()
                                .add(spellTrade(4, SpellTypes.MEDITATION, 1), 10)
                                .add(spellTrade(4, SpellTypes.DISPERSAL, 1), 10)
                                .add(spellTrade(4, SpellTypes.RELEASING, 1), 10)
                                .add(spellTrade(5, SpellTypes.INTIMIDATION, 1), 10)
                                .add(spellTrade(6, SpellTypes.SPIRIT_EYES, 1), 10)
                                .add(spellTrade(10, SpellTypes.SPIRIT_EYES, 2), 5)
                                .add(spellTrade(4, SpellTypes.PICKUP_ITEM, 1), 9)
                                .add(spellTrade(6, SpellTypes.PICKUP_ITEM, 2), 5)
                                .add(spellTrade(10, SpellTypes.THROW_ITEM, 1), 7)
                                .add(spellTrade(6, SpellTypes.PICKUP_BLOCK, 1), 9)
                                .add(spellTrade(8, SpellTypes.PICKUP_BLOCK, 2), 6)
                                .add(spellTrade(12, SpellTypes.FLY_WITH_ITEM, 1), 7)
                                .add(spellTrade(16, SpellTypes.FLY_WITH_ITEM, 2), 4)
                                .add(new TradeEntry(
                                        List.of(new ItemStack(Items.EMERALD, 2)),
                                        List.of(new ItemStack(IMMItems.GOURD_SEEDS.get(), 4)),
                                        range(2, 4)
                                ), 12)
                                .add(new TradeEntry(
                                        List.of(new ItemStack(Items.EMERALD, 32), new ItemStack(GourdGrownBlock.GourdTypes.GREEN.getGourdGrownBlock())),
                                        List.of(FlameGourd.createFlameGourd(0.2F)),
                                        constant(1)
                                ), 9)
                                .build()
                        )
                )
        ));
//        context.register(METAL_BEGINNER, new HumanSetting(
//                IMMEntities.SPIRITUAL_BEGINNER_CULTIVATOR.get(),
//                50,
//                Optional.of(new CultivationSetting(
//                                Optional.of(RealmTypes.CORE_SHAPING),
//                                List.of(QiRootTypes.METAL)
//                        )
//                ),
//                List.of(
//                        single(new ItemEntry(stack(Items.NETHERITE_SWORD))),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_HELMET, List.of(DyeColor.YELLOW))),
//                                new ItemEntry(stack(Items.IRON_HELMET)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_CHESTPLATE, List.of(DyeColor.YELLOW))),
//                                new ItemEntry(stack(Items.IRON_CHESTPLATE)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_LEGGINGS, List.of(DyeColor.YELLOW))),
//                                new ItemEntry(stack(Items.IRON_LEGGINGS)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_BOOTS, List.of(DyeColor.YELLOW))),
//                                new ItemEntry(stack(Items.IRON_BOOTS)
//                                ))
//                ),
//                Optional.of(new TradeSetting(
//                        range(4, 6),
//                        true,
//                        SimpleWeightedList.<TradeEntry>builder()
//                                .add(spellTrade(4, SpellTypes.MEDITATION, 1), 10)
//                                .add(spellTrade(4, SpellTypes.DISPERSAL, 1), 10)
//                                .add(spellTrade(4, SpellTypes.RELEASING, 1), 10)
//                                .add(spellTrade(5, SpellTypes.INTIMIDATION, 1), 10)
//                                .add(spellTrade(6, SpellTypes.SPIRIT_EYES, 1), 10)
//                                .add(spellTrade(8, SpellTypes.METAL_MASTERY, 1), 10)
//                                .add(spellTrade(8, SpellTypes.METAL_MASTERY, 2), 8)
//                                .add(spellTrade(16, SpellTypes.METAL_MASTERY, 3), 6)
//                                .add(spellTrade(6, SpellTypes.CRITICAL_HIT, 1), 7)
//                                .add(spellTrade(12, SpellTypes.METAL_MENDING, 1), 4)
//                                .add(spellTrade(8, SpellTypes.SHARPNESS, 1), 6)
//                                .build()
//                ))
//        ));
//        context.register(WOOD_BEGINNER, new HumanSetting(
//                IMMEntities.SPIRITUAL_BEGINNER_CULTIVATOR.get(),
//                50,
//                Optional.of(new CultivationSetting(
//                                Optional.of(RealmTypes.CORE_SHAPING),
//                                List.of(QiRootTypes.WOOD)
//                        )
//                ),
//                List.of(
//                        single(new ItemEntry(stack(Items.BOW))),
//                        single(new ItemEntry(stack(Items.WOODEN_SWORD))),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_HELMET, List.of(DyeColor.GREEN))),
//                                new ItemEntry(stack(Items.IRON_HELMET)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_CHESTPLATE, List.of(DyeColor.GREEN))),
//                                new ItemEntry(stack(Items.IRON_CHESTPLATE)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_LEGGINGS, List.of(DyeColor.GREEN))),
//                                new ItemEntry(stack(Items.IRON_LEGGINGS)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_BOOTS, List.of(DyeColor.GREEN))),
//                                new ItemEntry(stack(Items.IRON_BOOTS)
//                                ))
//                ),
//                Optional.of(new TradeSetting(
//                        range(4, 6),
//                        true,
//                        SimpleWeightedList.<TradeEntry>builder()
//                                .add(spellTrade(4, SpellTypes.MEDITATION, 1), 10)
//                                .add(spellTrade(4, SpellTypes.DISPERSAL, 1), 10)
//                                .add(spellTrade(4, SpellTypes.RELEASING, 1), 10)
//                                .add(spellTrade(5, SpellTypes.INTIMIDATION, 1), 10)
//                                .add(spellTrade(6, SpellTypes.SPIRIT_EYES, 1), 10)
//                                .add(spellTrade(8, SpellTypes.WOOD_MASTERY, 1), 10)
//                                .add(spellTrade(8, SpellTypes.WOOD_MASTERY, 2), 8)
//                                .add(spellTrade(16, SpellTypes.WOOD_MASTERY, 3), 6)
//                                .add(spellTrade(6, SpellTypes.LEVITATION, 1), 8)
//                                .add(spellTrade(8, SpellTypes.SPROUT, 1), 7)
//                                .add(spellTrade(10, SpellTypes.WITHER, 1), 6)
//                                .add(spellTrade(10, SpellTypes.WOOD_HEALING, 1), 6)
//                                .build()
//                ))
//        ));
//        context.register(WATER_BEGINNER, new HumanSetting(
//                IMMEntities.SPIRITUAL_BEGINNER_CULTIVATOR.get(),
//                50,
//                Optional.of(new CultivationSetting(
//                                Optional.of(RealmTypes.CORE_SHAPING),
//                                List.of(QiRootTypes.WATER)
//                        )
//                ),
//                List.of(
//                        pair(
//                                new ItemEntry(stack(Items.IRON_SWORD)),
//                                new ItemEntry(stack(Items.DIAMOND_SWORD)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_HELMET, List.of(DyeColor.BLUE))),
//                                new ItemEntry(stack(Items.IRON_HELMET)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_CHESTPLATE, List.of(DyeColor.BLUE))),
//                                new ItemEntry(stack(Items.IRON_CHESTPLATE)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_LEGGINGS, List.of(DyeColor.BLUE))),
//                                new ItemEntry(stack(Items.IRON_LEGGINGS)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_BOOTS, List.of(DyeColor.BLUE))),
//                                new ItemEntry(stack(Items.IRON_BOOTS)
//                                ))
//                ),
//                Optional.of(new TradeSetting(
//                        range(4, 6),
//                        true,
//                        SimpleWeightedList.<TradeEntry>builder()
//                                .add(spellTrade(4, SpellTypes.MEDITATION, 1), 10)
//                                .add(spellTrade(4, SpellTypes.DISPERSAL, 1), 10)
//                                .add(spellTrade(4, SpellTypes.RELEASING, 1), 10)
//                                .add(spellTrade(5, SpellTypes.INTIMIDATION, 1), 10)
//                                .add(spellTrade(6, SpellTypes.SPIRIT_EYES, 1), 10)
//                                .add(spellTrade(8, SpellTypes.WATER_MASTERY, 1), 10)
//                                .add(spellTrade(8, SpellTypes.WATER_MASTERY, 2), 8)
//                                .add(spellTrade(16, SpellTypes.WATER_MASTERY, 3), 6)
//                                .add(spellTrade(6, SpellTypes.WATER_BREATHING, 1), 8)
//                                .build()
//                ))
//        ));
//        context.register(FIRE_BEGINNER, new HumanSetting(
//                IMMEntities.SPIRITUAL_BEGINNER_CULTIVATOR.get(),
//                50,
//                Optional.of(new CultivationSetting(
//                                Optional.of(RealmTypes.CORE_SHAPING),
//                                List.of(QiRootTypes.FIRE)
//                        )
//                ),
//                List.of(
//                        pair(
//                                new ItemEntry(stack(Items.IRON_SWORD)),
//                                new ItemEntry(stack(Items.NETHERITE_SWORD)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_HELMET, List.of(DyeColor.RED))),
//                                new ItemEntry(stack(Items.IRON_HELMET)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_CHESTPLATE, List.of(DyeColor.RED))),
//                                new ItemEntry(stack(Items.IRON_CHESTPLATE)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_LEGGINGS, List.of(DyeColor.RED))),
//                                new ItemEntry(stack(Items.IRON_LEGGINGS)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_BOOTS, List.of(DyeColor.RED))),
//                                new ItemEntry(stack(Items.IRON_BOOTS)
//                                ))
//                ),
//                Optional.of(new TradeSetting(
//                        range(4, 6),
//                        true,
//                        SimpleWeightedList.<TradeEntry>builder()
//                                .add(spellTrade(4, SpellTypes.MEDITATION, 1), 10)
//                                .add(spellTrade(4, SpellTypes.DISPERSAL, 1), 10)
//                                .add(spellTrade(4, SpellTypes.RELEASING, 1), 10)
//                                .add(spellTrade(5, SpellTypes.INTIMIDATION, 1), 10)
//                                .add(spellTrade(6, SpellTypes.SPIRIT_EYES, 1), 10)
//                                .add(spellTrade(8, SpellTypes.FIRE_MASTERY, 1), 10)
//                                .add(spellTrade(8, SpellTypes.FIRE_MASTERY, 2), 8)
//                                .add(spellTrade(16, SpellTypes.FIRE_MASTERY, 3), 6)
//                                .add(spellTrade(8, SpellTypes.BURNING, 1), 8)
//                                .add(spellTrade(8, SpellTypes.BURNING, 2), 6)
//                                .add(spellTrade(7, SpellTypes.LAVA_BREATHING, 1), 8)
//                                .add(spellTrade(10, SpellTypes.IGNITION, 1), 7)
//                                .build()
//                ))
//        ));
//        context.register(EARTH_BEGINNER, new HumanSetting(
//                IMMEntities.SPIRITUAL_BEGINNER_CULTIVATOR.get(),
//                50,
//                Optional.of(new CultivationSetting(
//                                Optional.of(RealmTypes.CORE_SHAPING),
//                                List.of(QiRootTypes.EARTH)
//                        )
//                ),
//                List.of(
//                        single(new ItemEntry(stack(Items.SHIELD))),
//                        single(new ItemEntry(stack(Items.STONE_SWORD))),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_HELMET, List.of(DyeColor.GRAY))),
//                                new ItemEntry(stack(Items.DIAMOND_HELMET)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_CHESTPLATE, List.of(DyeColor.GRAY))),
//                                new ItemEntry(stack(Items.IRON_CHESTPLATE)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_LEGGINGS, List.of(DyeColor.GRAY))),
//                                new ItemEntry(stack(Items.IRON_LEGGINGS)
//                                )),
//                        pair(
//                                new ItemEntry(ItemUtil.dyeArmor(Items.LEATHER_BOOTS, List.of(DyeColor.GRAY))),
//                                new ItemEntry(stack(Items.DIAMOND_BOOTS)
//                                ))
//                ),
//                Optional.of(new TradeSetting(
//                        range(4, 6),
//                        true,
//                        SimpleWeightedList.<TradeEntry>builder()
//                                .add(spellTrade(4, SpellTypes.MEDITATION, 1), 10)
//                                .add(spellTrade(4, SpellTypes.DISPERSAL, 1), 10)
//                                .add(spellTrade(4, SpellTypes.RELEASING, 1), 10)
//                                .add(spellTrade(5, SpellTypes.INTIMIDATION, 1), 10)
//                                .add(spellTrade(6, SpellTypes.SPIRIT_EYES, 1), 10)
//                                .add(spellTrade(8, SpellTypes.EARTH_MASTERY, 1), 10)
//                                .add(spellTrade(8, SpellTypes.EARTH_MASTERY, 2), 8)
//                                .add(spellTrade(16, SpellTypes.EARTH_MASTERY, 3), 6)
//                                .add(spellTrade(8, SpellTypes.CRYSTAL_EXPLOSION, 1), 7)
//                                .add(spellTrade(8, SpellTypes.CRYSTAL_HEART, 1), 5)
//                                .build()
//                ))
//        ));
    }

//    public static HTRegistryHolder<HumanSetting> RICH_VANILLA = LOOTS.innerRegister(
//            Util.prefix("rich_vanilla"),
//            new HumanSetting(
//                    InventoryLootTypes.VANILLA,
//                    100,
//                    Arrays.asList(
//                            single(new ItemEntry(
//                                    new ItemStack(Items.PUMPKIN_PIE),
//                                    range(16, 32),
//                                    constant(0)
//                            )),
//                            single(new ItemEntry(
//                                    new ItemStack(Items.GOLDEN_APPLE),
//                                    range(2, 5),
//                                    constant(0)
//                            )),
//                            single(new ItemEntry(
//                                    new ItemStack(Items.ARROW),
//                                    range(32, 64),
//                                    constant(0)
//                            )),
//                            single(new ItemEntry(
//                                    new ItemStack(Items.ENDER_PEARL),
//                                    range(2, 5),
//                                    constant(0)
//                            )),
//                            single(new ItemEntry(
//                                    new ItemStack(Items.SHIELD),
//                                    constant(1),
//                                    constant(0)
//                            )),
//                            single(new ItemEntry(
//                                    new ItemStack(Items.BOW),
//                                    constant(1),
//                                    range(8, 15)
//                            )),
//                            pair(new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_SWORD),
//                                    constant(1),
//                                    range(5, 15)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.NETHERITE_SWORD),
//                                    constant(1),
//                                    range(5, 10)
//                            )),
//                            pair(new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_CHESTPLATE),
//                                    constant(1),
//                                    range(5, 10)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.NETHERITE_CHESTPLATE),
//                                    constant(1),
//                                    range(3, 8)
//                            )),
//                            pair(new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_LEGGINGS),
//                                    constant(1),
//                                    range(5, 10)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.NETHERITE_LEGGINGS),
//                                    constant(1),
//                                    range(3, 8)
//                            )),
//                            pair(new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_HELMET),
//                                    constant(1),
//                                    range(8, 12)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.IRON_HELMET),
//                                    constant(1),
//                                    range(10, 15)
//                            )),
//                            pair(new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_BOOTS),
//                                    constant(1),
//                                    range(8, 12)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.IRON_BOOTS),
//                                    constant(1),
//                                    range(10, 15)
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
//                            single(new ItemEntry(
//                                    new ItemStack(Items.BREAD),
//                                    range(24, 48),
//                                    constant(0)
//                            )),
//                            single(new ItemEntry(
//                                    new ItemStack(Items.GOLDEN_APPLE),
//                                    range(1, 3),
//                                    constant(0)
//                            )),
//                            single(new ItemEntry(
//                                    new ItemStack(Items.ARROW),
//                                    range(24, 48),
//                                    constant(0)
//                            )),
//                            single(new ItemEntry(
//                                    new ItemStack(Items.ENDER_PEARL),
//                                    range(1, 2),
//                                    constant(0)
//                            )),
//                            single(new ItemEntry(
//                                    new ItemStack(Items.SHIELD),
//                                    constant(1),
//                                    constant(0)
//                            )),
//                            single(new ItemEntry(
//                                    new ItemStack(Items.BOW),
//                                    constant(1),
//                                    range(4, 8)
//                            )),
//                            pair(new ItemEntry(
//                                    new ItemStack(Items.IRON_SWORD),
//                                    constant(1),
//                                    range(5, 10)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_SWORD),
//                                    constant(1),
//                                    range(2, 5)
//                            )),
//                            pair(new ItemEntry(
//                                    new ItemStack(Items.IRON_CHESTPLATE),
//                                    constant(1),
//                                    range(5, 10)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_CHESTPLATE),
//                                    constant(1),
//                                    range(2, 5)
//                            )),
//                            pair(new ItemEntry(
//                                    new ItemStack(Items.IRON_LEGGINGS),
//                                    constant(1),
//                                    range(5, 10)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_LEGGINGS),
//                                    constant(1),
//                                    range(2, 5)
//                            )),
//                            pair(new ItemEntry(
//                                    new ItemStack(Items.IRON_HELMET),
//                                    constant(1),
//                                    range(5, 10)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.GOLDEN_HELMET),
//                                    constant(1),
//                                    range(10, 15)
//                            )),
//                            pair(new ItemEntry(
//                                    new ItemStack(Items.IRON_BOOTS),
//                                    constant(1),
//                                    range(5, 10)
//                            ), new ItemEntry(
//                                    new ItemStack(Items.DIAMOND_BOOTS),
//                                    constant(1),
//                                    range(2, 5)
//                            ))
//                    ),
//                    Optional.empty()
//            )
//    );

    static TradeEntry spellTrade(int emerald, ISpellType spell, int level){
        return new TradeEntry(
                List.of(new ItemStack(Items.EMERALD, emerald)),
                List.of(SecretManualItem.create(spell, level)),
                constant(1)
        );
    }
    static ItemStack stack(Item item) {
        return new ItemStack(item);
    }

    static IntProvider constant(int value) {
        return ConstantInt.of(value);
    }

    static IntProvider range(int from, int to) {
        return UniformInt.of(from, to);
    }

    static Optional<HumanSetting> getRandomSetting(Level level, EntityType<?> type, RandomSource random) {
        return WeightedList.create(SETTING.getValues(level).stream().filter(l -> l.type() == type).toList()).getRandomItem(random);
    }

    static List<HumanSetting> getInventoryLoots(Level level, EntityType<?> type) {
        return SETTING.getValues(level).stream().filter(l -> l.type() == type).toList();
    }

    static LootSetting single(ItemEntry entry) {
        return new LootSetting(SimpleWeightedList.single(entry));
    }

    static LootSetting pair(ItemEntry entry1, ItemEntry entry2) {
        return new LootSetting(SimpleWeightedList.pair(entry1, entry2));
    }

    static HTCodecRegistry<HumanSetting> registry() {
        return SETTING;
    }

    static ResourceKey<HumanSetting> create(String name) {
        return registry().createKey(Util.prefix(name));
    }

}
