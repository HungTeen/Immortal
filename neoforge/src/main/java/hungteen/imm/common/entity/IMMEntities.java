package hungteen.imm.common.entity;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.common.registry.EntitySuits;
import hungteen.htlib.common.registry.suit.HTEntitySuit;
import hungteen.htlib.util.NeoHelper;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.impl.ItemHelper;
import hungteen.imm.common.entity.creature.monster.BiFang;
import hungteen.imm.common.entity.creature.monster.SharpStake;
import hungteen.imm.common.entity.creature.spirit.*;
import hungteen.imm.common.entity.golem.CopperGolem;
import hungteen.imm.common.entity.golem.CreeperGolem;
import hungteen.imm.common.entity.golem.IronGolem;
import hungteen.imm.common.entity.golem.SnowGolem;
import hungteen.imm.common.entity.human.HumanLikeEntity;
import hungteen.imm.common.entity.human.cultivator.MortalityPlayer;
import hungteen.imm.common.entity.human.cultivator.RealityPlayer;
import hungteen.imm.common.entity.human.cultivator.WanderingCultivator;
import hungteen.imm.common.entity.human.pillager.Chillager;
import hungteen.imm.common.entity.misc.*;
import hungteen.imm.common.entity.misc.formation.TeleportFormation;
import hungteen.imm.common.entity.misc.talisman.SproutTalismanEntity;
import hungteen.imm.common.entity.misc.talisman.WoodHealingTalismanEntity;
import hungteen.imm.common.entity.undead.QiSkeleton;
import hungteen.imm.common.entity.undead.QiZombie;
import hungteen.imm.common.item.IMMSpawnEggItem;
import hungteen.imm.util.Colors;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-10-02 15:01
 **/
public interface IMMEntities {

    HTVanillaRegistry<EntityType<?>> ENTITY_TYPES = HTRegistryManager.vanilla(Registries.ENTITY_TYPE, Util.id());
    @Deprecated
    Map<HTHolder<?>, String> MAP = new HashMap<>();

    /* Misc */

    HTEntitySuit<QiRootCrystal> QI_ROOT_CRYSTAL = registerNonLiving("qi_root_crystal", () -> {
        return EntityType.Builder.of(QiRootCrystal::new, MobCategory.MISC).sized(1F, 1F).clientTrackingRange(3).updateInterval(4);
    });
    HTHolder<EntityType<ElementAmethyst>> ELEMENT_AMETHYST = registerEntityType(ElementAmethyst::new, "element_amethyst", MobCategory.MISC, builder -> builder.sized(0.5F, 1.2F));
    HTEntitySuit<FallingIceEntity> FALLING_ICE = registerNonLiving("falling_ice", () -> {
        return EntityType.Builder.of(FallingIceEntity::new, MobCategory.MISC).sized(3F, 1F).clientTrackingRange(3).updateInterval(10);
    });
    HTEntitySuit<TwistingVines> TWISTING_VINES = registerNonLiving("twisting_vines", () -> {
        return EntityType.Builder.of(TwistingVines::new, MobCategory.MISC).sized(0.9F, 0.9F).clientTrackingRange(3).updateInterval(10);
    });
    HTHolder<EntityType<TeleportFormation>> TELEPORT_FORMATION = registerEntityType(TeleportFormation::new, "teleport_formation", MobCategory.MISC, builder -> builder.sized(0F, 0F));
    HTHolder<EntityType<SpiritualPearl>> SPIRITUAL_PEARL = registerEntityType(SpiritualPearl::new, "spiritual_pearl", MobCategory.MISC, builder -> builder.sized(0.4F, 0.4F));
    HTHolder<EntityType<SpiritualFlame>> SPIRITUAL_FLAME = registerEntityType(SpiritualFlame::new, "spiritual_flame", MobCategory.MISC, builder -> builder.sized(0.9F, 1.8F));
    HTHolder<EntityType<FlyingItemEntity>> FLYING_ITEM = registerEntityType(FlyingItemEntity::new, "flying_item", MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F));
    HTHolder<EntityType<ThrowingItemEntity>> THROWING_ITEM = registerEntityType(ThrowingItemEntity::new, "throwing_item", MobCategory.MISC, builder -> builder.clientTrackingRange(3).updateInterval(10));
    HTHolder<EntityType<PoisonWind>> POISON_WIND = registerEntityType(PoisonWind::new, "poison_wind", MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F).clientTrackingRange(3).updateInterval(10));
    HTHolder<EntityType<Tornado>> TORNADO = registerEntityType(Tornado::new, "tornado", MobCategory.MISC, builder -> builder.sized(2F, 2F));

    /* Human */

    HTEntitySuit<RealityPlayer> REALITY_PLAYER = registerLiving("reality_player", () -> {
                return EntityType.Builder.of(RealityPlayer::new, IMMMobCategory.HUMAN.getValue()).sized(0.6F, 1.95F)
                        .eyeHeight(1.62F).vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT).clientTrackingRange(8);
            }, builder -> builder
                    .attribute(() -> HumanLikeEntity.createAttributes().build())
                    .noEgg()
    );
    HTEntitySuit<MortalityPlayer> MORTALITY_PLAYER = registerLiving("mortality_player", () -> {
                return EntityType.Builder.of(MortalityPlayer::new, IMMMobCategory.HUMAN.getValue()).sized(0.6F, 1.95F)
                        .eyeHeight(1.62F).vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT).clientTrackingRange(8);
            }, builder -> builder
                    .attribute(() -> HumanLikeEntity.createAttributes().build())
                    .spawnEgg(ColorHelper.BLACK.rgb(), ColorHelper.WHITE.rgb())
    );
    HTEntitySuit<WanderingCultivator> WANDERING_CULTIVATOR = registerLiving("wandering_cultivator", () -> {
                return EntityType.Builder.of(WanderingCultivator::new, IMMMobCategory.HUMAN.getValue()).sized(0.6F, 1.8F)
                        .eyeHeight(1.62F).vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT).clientTrackingRange(8);
            }, builder -> builder
                    .attribute(() -> HumanLikeEntity.createAttributes().build())
                    .spawnEgg(ColorHelper.DYE_WHITE.rgb(), ColorHelper.BLACK.rgb())
                    .spawn(SpawnPlacementTypes.ON_GROUND)
    );
    HTEntitySuit<Chillager> CHILLAGER = registerLiving("chillager", () -> {
                return EntityType.Builder.of(Chillager::new, IMMMobCategory.HUMAN.getValue()).sized(0.6F, 1.95F)
                        .passengerAttachments(2.0F).ridingOffset(-0.6F).clientTrackingRange(8);
            }, builder -> builder
                    .attribute(() -> Chillager.createAttributes().build())
                    .spawnEgg(ColorHelper.DYE_WHITE.rgb(), ColorHelper.BLUE.rgb())
                    .spawn(SpawnPlacementTypes.ON_GROUND)
    );

//    public static final HTHolder<EntityType<CommonVillager>> COMMON_VILLAGER = registerEntityType(CommonVillager::new, "common_villager", IMMMobCategory.HUMAN);

//    /* Creature */
//
//    public static final HTHolder<EntityType<GrassCarp>> GRASS_CARP = registerEntityType(GrassCarp::new, "grass_carp", MobCategory.WATER_CREATURE);
//    public static final HTHolder<EntityType<SilkWorm>> SILK_WORM = registerEntityType(SilkWorm::new, "silk_worm", MobCategory.CREATURE);

    /* Monster */

    HTHolder<EntityType<SharpStake>> SHARP_STAKE = registerEntityType(SharpStake::new, "sharp_stake", MobCategory.MONSTER, builder -> builder.sized(1F, 1.05F));
    HTHolder<EntityType<BiFang>> BI_FANG = registerEntityType(BiFang::new, "bi_fang", MobCategory.MONSTER, builder -> builder.sized(0.9F, 3.2F).fireImmune());

    /* Spirit */

    HTHolder<EntityType<MetalSpirit>> METAL_SPIRIT = registerEntityType(MetalSpirit::new, "metal_spirit", MobCategory.CREATURE, builder -> builder.sized(0.9F, 1.4F));
    HTHolder<EntityType<WoodSpirit>> WOOD_SPIRIT = registerEntityType(WoodSpirit::new, "wood_spirit", MobCategory.CREATURE, builder -> builder.sized(0.95F, 1.6F));
    HTHolder<EntityType<WaterSpirit>> WATER_SPIRIT = registerEntityType(WaterSpirit::new, "water_spirit", MobCategory.CREATURE, builder -> builder.sized(0.85F, 1.3F));
    HTHolder<EntityType<FireSpirit>> FIRE_SPIRIT = registerEntityType(FireSpirit::new, "fire_spirit", MobCategory.CREATURE, builder -> builder.sized(0.6F, 0.7F).fireImmune());
    HTHolder<EntityType<EarthSpirit>> EARTH_SPIRIT = registerEntityType(EarthSpirit::new, "earth_spirit", MobCategory.CREATURE, builder -> builder.sized(0.9F, 1F));

    /* Undead */

    HTEntitySuit<QiZombie> QI_ZOMBIE = registerLiving("qi_zombie", () -> {
                return EntityType.Builder.of(QiZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F)
                        .eyeHeight(1.74F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(8);
            }, builder -> builder
                    .attribute(() -> QiZombie.createAttributes().build())
                    .spawnEgg(Colors.ZOMBIE_AQUA, Colors.ZOMBIE_SKIN)
                    .spawn(SpawnPlacementTypes.ON_GROUND)
    );
    HTEntitySuit<QiSkeleton> QI_SKELETON = registerLiving("lingerer", () -> {
                return EntityType.Builder.of(QiSkeleton::new, MobCategory.MONSTER).sized(0.6F, 1.9F)
                        .eyeHeight(1.74F).ridingOffset(-0.7F).clientTrackingRange(8);
            }, builder -> builder
                    .attribute(() -> QiSkeleton.createAttributes().build())
                    .spawnEgg(12698049, 4802889)
                    .spawn(SpawnPlacementTypes.ON_GROUND)
    );

    /* Golem */

    HTHolder<EntityType<IronGolem>> IRON_GOLEM = registerEntityType(IronGolem::new, "iron_golem", MobCategory.CREATURE, b -> b.sized(1.4F, 2.7F).clientTrackingRange(10));
    HTHolder<EntityType<SnowGolem>> SNOW_GOLEM = registerEntityType(SnowGolem::new, "snow_golem", MobCategory.CREATURE, b -> b.sized(0.7F, 1.9F).clientTrackingRange(8));
    HTHolder<EntityType<CreeperGolem>> CREEPER_GOLEM = registerEntityType(CreeperGolem::new, "creeper_golem", MobCategory.CREATURE, b -> b.sized(0.6F, 1.7F).clientTrackingRange(8));
    HTHolder<EntityType<CopperGolem>> COPPER_GOLEM = registerEntityType(CopperGolem::new, "copper_golem", MobCategory.CREATURE, b -> b.sized(0.8F, 1.1F).clientTrackingRange(8));

    /* Talisman */

    HTEntitySuit<SproutTalismanEntity> SPROUT_TALISMAN = registerNonLiving("sprout_talisman", () -> {
        return EntityType.Builder.of(SproutTalismanEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(3).updateInterval(10);
    });
    HTEntitySuit<WoodHealingTalismanEntity> WOOD_HEALING_TALISMAN = registerNonLiving("wood_healing_talisman", () -> {
        return EntityType.Builder.of(WoodHealingTalismanEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(3).updateInterval(10);
    });

    static void addEntityAttributes(EntityAttributeCreationEvent ev) {
        /* Creature */
//        ev.put(GRASS_CARP.get(), GrassCarp.createAttributes().build());
//        ev.put(SILK_WORM.get(), SilkWorm.createAttributes().build());

        /* Monster */
        ev.put(SHARP_STAKE.get(), SharpStake.createAttributes().build());
        ev.put(BI_FANG.get(), BiFang.createAttributes().build());

        /* Spirit */
        ev.put(METAL_SPIRIT.get(), MetalSpirit.createAttributes().build());
        ev.put(WOOD_SPIRIT.get(), WoodSpirit.createAttributes().build());
        ev.put(WATER_SPIRIT.get(), WaterSpirit.createAttributes().build());
        ev.put(FIRE_SPIRIT.get(), FireSpirit.createAttributes().build());
        ev.put(EARTH_SPIRIT.get(), EarthSpirit.createAttributes().build());

        /* Golem */
        ev.put(IRON_GOLEM.get(), IronGolem.createAttributes().build());
        ev.put(SNOW_GOLEM.get(), SnowGolem.createAttributes().build());
        ev.put(CREEPER_GOLEM.get(), CreeperGolem.createAttributes().build());
        ev.put(COPPER_GOLEM.get(), CopperGolem.createAttributes().build());
    }

    static void registerPlacements(RegisterSpawnPlacementsEvent ev) {
        /* Creature */

        /* Monster */
        ev.register(SHARP_STAKE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        ev.register(BI_FANG.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);

        /* Spirit */
        ev.register(METAL_SPIRIT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        ev.register(WOOD_SPIRIT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        ev.register(WATER_SPIRIT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        ev.register(FIRE_SPIRIT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        ev.register(EARTH_SPIRIT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);

        /* golem */
    }

    /**
     * initialize spawn eggs.
     */
    static void registerSpawnEggs(RegisterEvent event) {
        List.of(
                /* Creature */
//                Pair.of(GRASS_CARP, Pair.of(ColorHelper.GREEN, ColorHelper.DARK_GREEN)),
//                Pair.of(SILK_WORM, Pair.of(ColorHelper.WHITE, ColorHelper.EARTH_ROOT)),

                /* Monster */
                Pair.of(SHARP_STAKE, Pair.of(ColorHelper.DARK_GREEN, ColorHelper.DYE_BROWN)),
                Pair.of(BI_FANG, Pair.of(ColorHelper.RED, ColorHelper.DARK_AQUA)),

                /* Spirit */
                Pair.of(METAL_SPIRIT, Pair.of(ColorHelper.create(0xffdb3d), ColorHelper.create(0xfeffe1))),
                Pair.of(WOOD_SPIRIT, Pair.of(ColorHelper.create(0x87e45c), ColorHelper.create(0xc4ffe2))),
                Pair.of(WATER_SPIRIT, Pair.of(ColorHelper.create(0x50c9e2), ColorHelper.create(0xccfffd))),
                Pair.of(FIRE_SPIRIT, Pair.of(ColorHelper.create(0xff6100), ColorHelper.create(0xffe638))),
                Pair.of(EARTH_SPIRIT, Pair.of(ColorHelper.create(0xbb8c41), ColorHelper.create(0xd5d5d5)))

                /* Golem */
//                Pair.of(IRON_GOLEM, Pair.of(ColorHelper.WHITE, ColorHelper.BLACK)),
//                Pair.of(SNOW_GOLEM, Pair.of(ColorHelper.WHITE, ColorHelper.BLACK)),
//                Pair.of(CREEPER_GOLEM, Pair.of(ColorHelper.WHITE, ColorHelper.BLACK)),
//                Pair.of(COPPER_GOLEM, Pair.of(ColorHelper.WHITE, ColorHelper.BLACK))
        ).forEach(pair -> {
            if (MAP.containsKey(pair.getFirst())) {
                NeoHelper.register(event, ItemHelper.get(), Util.prefix(MAP.get(pair.getFirst()) + "_spawn_egg"), () ->
                        new IMMSpawnEggItem(
                                () -> pair.getFirst().get(),
                                pair.getSecond().getFirst().rgb(),
                                pair.getSecond().getSecond().rgb(),
                                new Item.Properties()
                        )
                );
            }
        });
    }

    private static <T extends Entity> HTHolder<EntityType<T>> registerEntityType(EntityType.EntityFactory factory, String name, MobCategory classification) {
        return registerEntityType(factory, name, classification, e -> {
        });
    }

    private static <T extends Entity> HTHolder<EntityType<T>> registerEntityType(EntityType.EntityFactory factory, String name, MobCategory classification, Consumer<EntityType.Builder<T>> consumer) {
        HTHolder<EntityType<T>> object = registry().register(name, () -> {
            EntityType.Builder<T> builder = EntityType.Builder.of(factory, classification);
            consumer.accept(builder);
            return builder.build(Util.prefix(name).toString());
        });
        MAP.put(object, name);
        return object;
    }

    static HTVanillaRegistry<EntityType<?>> registry() {
        return ENTITY_TYPES;
    }

    static <T extends Entity> HTEntitySuit<T> registerLiving(String name, Supplier<EntityType.Builder<T>> builder, Consumer<HTEntitySuit.EntitySuitBuilder<T>> consumer) {
        HTEntitySuit.EntitySuitBuilder<T> suitBuilder = EntitySuits.living(Util.prefix(name), builder);
        consumer.accept(suitBuilder);
        return EntitySuits.register(suitBuilder.build());
    }

    static <T extends Entity> HTEntitySuit<T> registerNonLiving(String name, Supplier<EntityType.Builder<T>> builder) {
        return EntitySuits.register(EntitySuits.nonLiving(Util.prefix(name), builder).build());
    }

    static void initialize(IEventBus event) {
        NeoHelper.initRegistry(registry(), event);
    }

}
