package hungteen.imm.common.entity.human;

import hungteen.htlib.api.registry.HTCodecRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.human.cultivator.CultivatorSettings;
import hungteen.imm.common.entity.human.pillager.PillagerSettings;
import hungteen.imm.common.entity.human.setting.HumanSetting;
import hungteen.imm.common.entity.human.setting.HumanSettingHelper;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/2/24 15:09
 */
public interface HumanSettings {

    HTCodecRegistry<HumanSetting> SETTING = HTRegistryManager.codec(Util.prefix("human_setting"), () -> HumanSetting.CODEC, () -> HumanSetting.NETWORK_CODEC);

    ResourceKey<HumanSetting> EMPTY = create("empty");
    ResourceKey<HumanSetting> POOR_MORTALITY = create("poor_mortality");
    ResourceKey<HumanSetting> COMMON_MORTALITY = create("common_mortality");
    ResourceKey<HumanSetting> STRONG_MORTALITY = create("strong_mortality");
    ResourceKey<HumanSetting> CHILLAGER = create("chillager");
    ResourceKey<HumanSetting> BEGINNER = create("beginner");
    ResourceKey<HumanSetting> METAL_BEGINNER = create("metal_beginner");
    ResourceKey<HumanSetting> WOOD_BEGINNER = create("wood_beginner");
    ResourceKey<HumanSetting> WATER_BEGINNER = create("water_beginner");
    ResourceKey<HumanSetting> FIRE_BEGINNER = create("fire_beginner");
    ResourceKey<HumanSetting> EARTH_BEGINNER = create("earth_beginner");

    static void register(BootstrapContext<HumanSetting> context) {
        context.register(EMPTY, new HumanSetting(
                HumanSettingHelper.predicate(IMMEntities.WANDERING_CULTIVATOR.get()),
                1,
                List.of(),
                Optional.empty(),
                List.of()
        ));
        CultivatorSettings.register(context);
        PillagerSettings.register(context);
//        context.register(BEGINNER, new HumanSetting(
//                IMMEntities.WANDERING_CULTIVATOR.get(),
//                100,
//                Optional.empty(),
//                List.of(
//                        single(new ItemEntry(
//                                new ItemStack(Items.BREAD),
//                                range(24, 48),
//                                constant(0)
//                        )),
//                        single(new ItemEntry(
//                                new ItemStack(Items.GOLDEN_APPLE),
//                                range(1, 3),
//                                constant(0)
//                        )),
//                        single(new ItemEntry(
//                                new ItemStack(Items.ARROW),
//                                range(24, 48),
//                                constant(0)
//                        )),
//                        single(new ItemEntry(
//                                new ItemStack(Items.ENDER_PEARL),
//                                range(1, 2),
//                                constant(0)
//                        )),
//                        single(new ItemEntry(
//                                new ItemStack(Items.SHIELD),
//                                constant(1),
//                                constant(0)
//                        )),
//                        single(new ItemEntry(
//                                new ItemStack(Items.BOW),
//                                constant(1),
//                                range(4, 8)
//                        )),
//                        pair(new ItemEntry(
//                                new ItemStack(Items.IRON_SWORD),
//                                constant(1),
//                                range(5, 10)
//                        ), new ItemEntry(
//                                new ItemStack(Items.DIAMOND_SWORD),
//                                constant(1),
//                                range(2, 5)
//                        )),
//                        pair(new ItemEntry(
//                                new ItemStack(Items.IRON_CHESTPLATE),
//                                constant(1),
//                                range(5, 10)
//                        ), new ItemEntry(
//                                new ItemStack(Items.DIAMOND_CHESTPLATE),
//                                constant(1),
//                                range(2, 5)
//                        )),
//                        pair(new ItemEntry(
//                                new ItemStack(Items.IRON_LEGGINGS),
//                                constant(1),
//                                range(5, 10)
//                        ), new ItemEntry(
//                                new ItemStack(Items.DIAMOND_LEGGINGS),
//                                constant(1),
//                                range(2, 5)
//                        )),
//                        pair(new ItemEntry(
//                                new ItemStack(Items.IRON_HELMET),
//                                constant(1),
//                                range(5, 10)
//                        ), new ItemEntry(
//                                new ItemStack(Items.GOLDEN_HELMET),
//                                constant(1),
//                                range(10, 15)
//                        )),
//                        pair(new ItemEntry(
//                                new ItemStack(Items.IRON_BOOTS),
//                                constant(1),
//                                range(5, 10)
//                        ), new ItemEntry(
//                                new ItemStack(Items.DIAMOND_BOOTS),
//                                constant(1),
//                                range(2, 5)
//                        ))
//                ),
//                Optional.of(new TradeSetting(
//                        range(6, 10),
//                        true,
//                        SimpleWeightedList.<TradeEntry>builder()
//                                .add(spellTrade(4, SpellTypes.MEDITATION, 1), 10)
//                                .add(spellTrade(4, SpellTypes.DISPERSAL, 1), 10)
//                                .add(spellTrade(4, SpellTypes.RELEASING, 1), 10)
//                                .add(spellTrade(5, SpellTypes.INTIMIDATION, 1), 10)
//                                .add(spellTrade(6, SpellTypes.SPIRIT_EYES, 1), 10)
//                                .add(spellTrade(10, SpellTypes.SPIRIT_EYES, 2), 5)
//                                .add(spellTrade(4, SpellTypes.PICKUP_ITEM, 1), 9)
//                                .add(spellTrade(6, SpellTypes.PICKUP_ITEM, 2), 5)
//                                .add(spellTrade(10, SpellTypes.THROW_ITEM, 1), 7)
//                                .add(spellTrade(6, SpellTypes.PICKUP_BLOCK, 1), 9)
//                                .add(spellTrade(8, SpellTypes.PICKUP_BLOCK, 2), 6)
//                                .add(spellTrade(12, SpellTypes.FLY_WITH_ITEM, 1), 7)
//                                .add(spellTrade(16, SpellTypes.FLY_WITH_ITEM, 2), 4)
//                                .add(new TradeEntry(
//                                        List.of(new ItemStack(Items.EMERALD, 2)),
//                                        List.of(new ItemStack(IMMItems.GOURD_SEEDS.get(), 4)),
//                                        range(2, 4)
//                                ), 12)
//                                .add(new TradeEntry(
//                                        List.of(new ItemStack(Items.EMERALD, 32), new ItemStack(GourdGrownBlock.GourdTypes.GREEN.getGourdGrownBlock())),
//                                        List.of(FlameGourd.createFlameGourd(0.2F)),
//                                        constant(1)
//                                ), 9)
//                                .build()
//                        )
//                )
//        ));
//        context.postRegister(METAL_BEGINNER, new HumanSetting(
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
//        context.postRegister(WOOD_BEGINNER, new HumanSetting(
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
//        context.postRegister(WATER_BEGINNER, new HumanSetting(
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
//        context.postRegister(FIRE_BEGINNER, new HumanSetting(
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
//        context.postRegister(EARTH_BEGINNER, new HumanSetting(
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

    static HTCodecRegistry<HumanSetting> registry() {
        return SETTING;
    }

    static ResourceKey<HumanSetting> create(String name) {
        return registry().createKey(Util.prefix(name));
    }

}
