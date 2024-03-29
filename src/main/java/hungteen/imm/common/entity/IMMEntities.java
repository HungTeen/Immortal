package hungteen.imm.common.entity;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.ImmortalMod;
import hungteen.imm.common.entity.creature.monster.BiFang;
import hungteen.imm.common.entity.creature.monster.SharpStake;
import hungteen.imm.common.entity.creature.spirit.*;
import hungteen.imm.common.entity.golem.CopperGolem;
import hungteen.imm.common.entity.golem.CreeperGolem;
import hungteen.imm.common.entity.golem.IronGolem;
import hungteen.imm.common.entity.golem.SnowGolem;
import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.entity.human.cultivator.EmptyCultivator;
import hungteen.imm.common.entity.human.cultivator.SpiritualBeginnerCultivator;
import hungteen.imm.common.entity.human.villager.CommonVillager;
import hungteen.imm.common.entity.human.villager.IMMVillager;
import hungteen.imm.common.entity.misc.*;
import hungteen.imm.common.entity.misc.formation.TeleportFormation;
import hungteen.imm.common.item.IMMSpawnEggItem;
import hungteen.imm.util.Util;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-02 15:01
 **/
public class IMMEntities {

    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Util.id());
    private static final Map<RegistryObject<?>, String> MAP = new HashMap<>();

    /* Misc */

    public static final RegistryObject<EntityType<ElementCrystal>> ELEMENT_AMETHYST = registerEntityType(ElementCrystal::new, "element_amethyst", MobCategory.MISC, builder -> builder.sized(0.5F, 1.2F));
    public static final RegistryObject<EntityType<TeleportFormation>> TELEPORT_FORMATION = registerEntityType(TeleportFormation::new, "teleport_formation", MobCategory.MISC, builder -> builder.sized(0F, 0F));
    public static final RegistryObject<EntityType<SpiritualPearl>> SPIRITUAL_PEARL = registerEntityType(SpiritualPearl::new, "spiritual_pearl", MobCategory.MISC, builder -> builder.sized(0.4F, 0.4F));
    public static final RegistryObject<EntityType<SpiritualFlame>> SPIRITUAL_FLAME = registerEntityType(SpiritualFlame::new, "spiritual_flame", MobCategory.MISC, builder -> builder.sized(0.9F, 1.8F));
    public static final RegistryObject<EntityType<FlyingItemEntity>> FLYING_ITEM = registerEntityType(FlyingItemEntity::new, "flying_item", MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F));
    public static final RegistryObject<EntityType<ThrowingItemEntity>> THROWING_ITEM = registerEntityType(ThrowingItemEntity::new, "throwing_item", MobCategory.MISC, builder -> builder.clientTrackingRange(3).updateInterval(10));
    public static final RegistryObject<EntityType<PoisonWind>> POISON_WIND = registerEntityType(PoisonWind::new, "poison_wind", MobCategory.MISC, builder -> builder.sized(0.5F, 0.5F).clientTrackingRange(3).updateInterval(10));
    public static final RegistryObject<EntityType<Tornado>> TORNADO = registerEntityType(Tornado::new, "tornado", MobCategory.MISC, builder -> builder.sized(2F, 2F));

    /* Human */

    public static final RegistryObject<EntityType<EmptyCultivator>> EMPTY_CULTIVATOR = registerEntityType(EmptyCultivator::new, "empty_cultivator", IMMMobCategories.HUMAN);
    public static final RegistryObject<EntityType<SpiritualBeginnerCultivator>> SPIRITUAL_BEGINNER_CULTIVATOR = registerEntityType(SpiritualBeginnerCultivator::new, "spiritual_beginner_cultivator", IMMMobCategories.HUMAN);
    public static final RegistryObject<EntityType<CommonVillager>> COMMON_VILLAGER = registerEntityType(CommonVillager::new, "common_villager", IMMMobCategories.HUMAN);

//    /* Creature */
//
//    public static final RegistryObject<EntityType<GrassCarp>> GRASS_CARP = registerEntityType(GrassCarp::new, "grass_carp", MobCategory.WATER_CREATURE);
//    public static final RegistryObject<EntityType<SilkWorm>> SILK_WORM = registerEntityType(SilkWorm::new, "silk_worm", MobCategory.CREATURE);

    /* Monster */

    public static final RegistryObject<EntityType<SharpStake>> SHARP_STAKE = registerEntityType(SharpStake::new, "sharp_stake", MobCategory.MONSTER, builder -> builder.sized(1F, 1.05F));
    public static final RegistryObject<EntityType<BiFang>> BI_FANG = registerEntityType(BiFang::new, "bi_fang", MobCategory.MONSTER, builder -> builder.sized(0.9F, 3.2F).fireImmune());

    /* Spirit */

    public static final RegistryObject<EntityType<MetalSpirit>> METAL_SPIRIT = registerEntityType(MetalSpirit::new, "metal_spirit", MobCategory.CREATURE, builder -> builder.sized(0.9F, 1.4F));
    public static final RegistryObject<EntityType<WoodSpirit>> WOOD_SPIRIT = registerEntityType(WoodSpirit::new, "wood_spirit", MobCategory.CREATURE, builder -> builder.sized(0.95F, 1.6F));
    public static final RegistryObject<EntityType<WaterSpirit>> WATER_SPIRIT = registerEntityType(WaterSpirit::new, "water_spirit", MobCategory.CREATURE, builder -> builder.sized(0.85F, 1.3F));
    public static final RegistryObject<EntityType<FireSpirit>> FIRE_SPIRIT = registerEntityType(FireSpirit::new, "fire_spirit", MobCategory.CREATURE, builder -> builder.sized(0.6F, 0.7F).fireImmune());
    public static final RegistryObject<EntityType<EarthSpirit>> EARTH_SPIRIT = registerEntityType(EarthSpirit::new, "earth_spirit", MobCategory.CREATURE, builder -> builder.sized(0.9F, 1F));

//    /* Undead */
//
//    public static final RegistryObject<EntityType<SpiritualZombie>> SPIRITUAL_ZOMBIE = registerEntityType(SpiritualZombie::new, "spiritual_zombie", MobCategory.MONSTER);

    /* Golem */

    public static final RegistryObject<EntityType<IronGolem>> IRON_GOLEM = registerEntityType(IronGolem::new, "iron_golem", MobCategory.CREATURE, b -> b.sized(1.4F, 2.7F).clientTrackingRange(10));
    public static final RegistryObject<EntityType<SnowGolem>> SNOW_GOLEM = registerEntityType(SnowGolem::new, "snow_golem", MobCategory.CREATURE, b -> b.sized(0.7F, 1.9F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<CreeperGolem>> CREEPER_GOLEM = registerEntityType(CreeperGolem::new, "creeper_golem", MobCategory.CREATURE, b -> b.sized(0.6F, 1.7F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<CopperGolem>> COPPER_GOLEM = registerEntityType(CopperGolem::new, "copper_golem", MobCategory.CREATURE, b -> b.sized(0.8F, 1.1F).clientTrackingRange(8));

    public static void addEntityAttributes(EntityAttributeCreationEvent ev) {
        /* Human */
        ev.put(EMPTY_CULTIVATOR.get(), HumanEntity.createAttributes().build());
        ev.put(SPIRITUAL_BEGINNER_CULTIVATOR.get(), HumanEntity.createAttributes().build());
        ev.put(COMMON_VILLAGER.get(), IMMVillager.createAttributes().build());

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

//        /* undead */
//        ev.put(SPIRITUAL_ZOMBIE.get(), SpiritualZombie.createAttributes().build());
//
        /* Golem */
        ev.put(IRON_GOLEM.get(), IronGolem.createAttributes());
        ev.put(SNOW_GOLEM.get(), SnowGolem.createAttributes());
        ev.put(CREEPER_GOLEM.get(), CreeperGolem.createAttributes());
        ev.put(COPPER_GOLEM.get(), CopperGolem.createAttributes());
    }

    public static void registerPlacements(SpawnPlacementRegisterEvent ev) {
        /* Human */
        ev.register(EMPTY_CULTIVATOR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        ev.register(SPIRITUAL_BEGINNER_CULTIVATOR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        ev.register(COMMON_VILLAGER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);

        /* Creature */

        /* Monster */
        ev.register(SHARP_STAKE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        ev.register(BI_FANG.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);

        /* Spirit */
        ev.register(METAL_SPIRIT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        ev.register(WOOD_SPIRIT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        ev.register(WATER_SPIRIT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        ev.register(FIRE_SPIRIT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        ev.register(EARTH_SPIRIT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);

        /* undead */

        /* golem */
    }

    /**
     * register spawn eggs.
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void registerSpawnEggs(RegisterEvent event) {
        List.of(
                /* Human */
                Pair.of(EMPTY_CULTIVATOR, Pair.of(ColorHelper.DYE_WHITE, ColorHelper.BLACK)),
                Pair.of(SPIRITUAL_BEGINNER_CULTIVATOR, Pair.of(ColorHelper.DYE_WHITE, ColorHelper.BLACK)),

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

                /* Undead */
//                Pair.of(SPIRITUAL_ZOMBIE, Pair.of(Colors.ZOMBIE_AQUA, Colors.ZOMBIE_SKIN))

                /* Golem */
//                Pair.of(IRON_GOLEM, Pair.of(ColorHelper.WHITE, ColorHelper.BLACK)),
//                Pair.of(SNOW_GOLEM, Pair.of(ColorHelper.WHITE, ColorHelper.BLACK)),
//                Pair.of(CREEPER_GOLEM, Pair.of(ColorHelper.WHITE, ColorHelper.BLACK)),
//                Pair.of(COPPER_GOLEM, Pair.of(ColorHelper.WHITE, ColorHelper.BLACK))
        ).forEach(pair -> {
            if (MAP.containsKey(pair.getFirst())) {
                event.register(ForgeRegistries.ITEMS.getRegistryKey(), Util.prefix(MAP.get(pair.getFirst()) + "_spawn_egg"), () ->
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

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntityType(EntityType.EntityFactory factory, String name, MobCategory classification) {
        return registerEntityType(factory, name, classification, e -> {
        });
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntityType(EntityType.EntityFactory factory, String name, MobCategory classification, Consumer<EntityType.Builder<T>> consumer) {
        RegistryObject<EntityType<T>> object = ENTITY_TYPES.register(name, () -> {
            EntityType.Builder<T> builder = EntityType.Builder.of(factory, classification);
            consumer.accept(builder);
            return builder.build(Util.prefix(name).toString());
        });
        MAP.put(object, name);
        return object;
    }

    /**
     * {@link ImmortalMod#defferRegister(IEventBus)}
     */
    public static void register(IEventBus event) {
        ENTITY_TYPES.register(event);
    }

}
