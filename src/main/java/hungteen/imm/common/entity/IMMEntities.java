package hungteen.imm.common.entity;

import hungteen.imm.ImmortalMod;
import hungteen.imm.common.entity.creature.monster.SharpStake;
import hungteen.imm.common.entity.golem.CopperGolem;
import hungteen.imm.common.entity.golem.CreeperGolem;
import hungteen.imm.common.entity.golem.IronGolem;
import hungteen.imm.common.entity.golem.SnowGolem;
import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.entity.human.cultivator.EmptyCultivator;
import hungteen.imm.common.entity.human.cultivator.SpiritualCultivator;
import hungteen.imm.common.entity.human.villager.CommonVillager;
import hungteen.imm.common.entity.human.villager.IMMVillager;
import hungteen.imm.common.entity.misc.FlyingItemEntity;
import hungteen.imm.common.entity.misc.SpiritualPearl;
import hungteen.imm.common.entity.misc.formation.TeleportFormation;
import hungteen.imm.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
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

    public static final RegistryObject<EntityType<TeleportFormation>> TELEPORT_FORMATION = registerEntityType(TeleportFormation::new, "teleport_formation", MobCategory.MISC);
    public static final RegistryObject<EntityType<SpiritualPearl>> SPIRITUAL_PEARL = registerEntityType(SpiritualPearl::new, "spiritual_pearl", MobCategory.MISC);
    public static final RegistryObject<EntityType<FlyingItemEntity>> FLYING_ITEM = registerEntityType(FlyingItemEntity::new, "flying_item", MobCategory.MISC);
//    public static final RegistryObject<EntityType<SpiritualFlame>> SPIRITUAL_FLAME = registerEntityType(SpiritualFlame::new, "spiritual_flame", MobCategory.MISC);

    /* Human */

    public static final RegistryObject<EntityType<EmptyCultivator>> EMPTY_CULTIVATOR = registerEntityType(EmptyCultivator::new, "empty_cultivator", IMMMobCategories.HUMAN);
    public static final RegistryObject<EntityType<SpiritualCultivator>> SPIRITUAL_CULTIVATOR = registerEntityType(SpiritualCultivator::new, "spiritual_cultivator", IMMMobCategories.HUMAN);
    public static final RegistryObject<EntityType<CommonVillager>> COMMON_VILLAGER = registerEntityType(CommonVillager::new, "common_villager", IMMMobCategories.HUMAN);

//    /* Creature */
//
//    public static final RegistryObject<EntityType<GrassCarp>> GRASS_CARP = registerEntityType(GrassCarp::new, "grass_carp", MobCategory.WATER_CREATURE);
//    public static final RegistryObject<EntityType<SilkWorm>> SILK_WORM = registerEntityType(SilkWorm::new, "silk_worm", MobCategory.CREATURE);

    /* Monster */
    public static final RegistryObject<EntityType<SharpStake>> SHARP_STAKE = registerEntityType(SharpStake::new, "sharp_stake", MobCategory.MONSTER);
//    /* Undead */
//
//    public static final RegistryObject<EntityType<SpiritualZombie>> SPIRITUAL_ZOMBIE = registerEntityType(SpiritualZombie::new, "spiritual_zombie", MobCategory.MONSTER);

    /* Golem */

    public static final RegistryObject<EntityType<IronGolem>> IRON_GOLEM = registerEntityType(IronGolem::new, "iron_golem", IMMMobCategories.GOLEM, b -> b.sized(1.4F, 2.7F).clientTrackingRange(10));
    public static final RegistryObject<EntityType<SnowGolem>> SNOW_GOLEM = registerEntityType(SnowGolem::new, "snow_golem", IMMMobCategories.GOLEM, b -> b.sized(0.7F, 1.9F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<CreeperGolem>> CREEPER_GOLEM = registerEntityType(CreeperGolem::new, "creeper_golem", IMMMobCategories.GOLEM, b -> b.sized(0.6F, 1.7F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<CopperGolem>> COPPER_GOLEM = registerEntityType(CopperGolem::new, "copper_golem", IMMMobCategories.GOLEM, b -> b.sized(0.8F, 1.1F).clientTrackingRange(8));

    public static void addEntityAttributes(EntityAttributeCreationEvent ev) {
        /* human */
        ev.put(EMPTY_CULTIVATOR.get(), HumanEntity.createAttributes().build());
        ev.put(SPIRITUAL_CULTIVATOR.get(), HumanEntity.createAttributes().build());
        ev.put(COMMON_VILLAGER.get(), IMMVillager.createAttributes().build());

        /* creature */
//        ev.put(GRASS_CARP.get(), GrassCarp.createAttributes().build());
//        ev.put(SILK_WORM.get(), SilkWorm.createAttributes().build());

        /* Monster */
        ev.put(SHARP_STAKE.get(), SharpStake.createAttributes().build());
//        /* undead */
//        ev.put(SPIRITUAL_ZOMBIE.get(), SpiritualZombie.createAttributes().build());
//
        /* golem */
        ev.put(IRON_GOLEM.get(), IronGolem.createAttributes());
        ev.put(SNOW_GOLEM.get(), SnowGolem.createAttributes());
        ev.put(CREEPER_GOLEM.get(), CreeperGolem.createAttributes());
        ev.put(COPPER_GOLEM.get(), CopperGolem.createAttributes());
    }

    /**
     * register spawn eggs.
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void registerSpawnEggs(RegisterEvent event) {
//        Arrays.asList(
//                /* Human */
//                Pair.of(DISCIPLE_VILLAGER, Pair.of(ColorHelper.WHITE, ColorHelper.BLACK)),
//
//                /* Creature */
//                Pair.of(GRASS_CARP, Pair.of(ColorHelper.GREEN, ColorHelper.DARK_GREEN)),
//                Pair.of(SILK_WORM, Pair.of(ColorHelper.WHITE, ColorHelper.EARTH_ROOT)),
//
//                /* Undead */
//                Pair.of(SPIRITUAL_ZOMBIE, Pair.of(Colors.ZOMBIE_AQUA, Colors.ZOMBIE_SKIN))
//        ).forEach(pair -> {
//            if (MAP.containsKey(pair.getFirst())) {
//                event.register(ForgeRegistries.ITEMS.getRegistryKey(), Util.prefix(MAP.get(pair.getFirst()) + "_spawn_egg"), () ->
//                        new ForgeSpawnEggItem(
//                                () -> pair.getFirst().get(),
//                                pair.getSecond().getFirst(),
//                                pair.getSecond().getSecond(),
//                                new Item.Properties().tab(CreativeModeTab.TAB_MISC)
//                        )
//                );
//            }
//        });
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntityType(EntityType.EntityFactory factory, String name, MobCategory classification) {
        return registerEntityType(factory, name, classification, e -> {});
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
    public static void register(IEventBus event){
        ENTITY_TYPES.register(event);
    }

}
