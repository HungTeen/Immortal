package hungteen.immortal.common.entity;

import hungteen.htlib.util.Pair;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.common.entity.creature.GrassCarp;
import hungteen.immortal.common.entity.creature.SilkWorm;
import hungteen.immortal.common.entity.golem.IronGolem;
import hungteen.immortal.common.entity.human.HumanEntity;
import hungteen.immortal.common.entity.human.cultivator.EmptyCultivator;
import hungteen.immortal.common.entity.human.cultivator.SpiritualCultivator;
import hungteen.immortal.common.entity.human.villager.DiscipleVillager;
import hungteen.immortal.common.entity.misc.FlyingItemEntity;
import hungteen.immortal.common.entity.misc.SeatEntity;
import hungteen.immortal.common.entity.misc.SpiritualFlame;
import hungteen.immortal.common.entity.misc.SpiritualPearl;
import hungteen.immortal.common.entity.undead.SpiritualZombie;
import hungteen.immortal.utils.Colors;
import hungteen.immortal.utils.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-02 15:01
 **/
public class ImmortalEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Util.id());
    private static final Map<RegistryObject<?>, String> MAP = new HashMap<>();

    /* Misc */

    public static final RegistryObject<EntityType<SpiritualPearl>> SPIRITUAL_PEARL = registerEntityType(SpiritualPearl::new, "spiritual_pearl", MobCategory.MISC);
    public static final RegistryObject<EntityType<FlyingItemEntity>> FLYING_ITEM = registerEntityType(FlyingItemEntity::new, "flying_item", MobCategory.MISC);
    public static final RegistryObject<EntityType<SeatEntity>> SEAT = registerEntityType(SeatEntity::new, "seat", MobCategory.MISC);
//    public static final RegistryObject<EntityType<SpiritualFlame>> SPIRITUAL_FLAME = registerEntityType(SpiritualFlame::new, "spiritual_flame", MobCategory.MISC);

    /* Human */

//    public static final RegistryObject<EntityType<DiscipleVillager>> DISCIPLE_VILLAGER = registerEntityType(DiscipleVillager::new, "disciple_villager", MobCategory.CREATURE);
    public static final RegistryObject<EntityType<EmptyCultivator>> EMPTY_CULTIVATOR = registerEntityType(EmptyCultivator::new, "empty_cultivator", MobCategory.CREATURE);
    public static final RegistryObject<EntityType<SpiritualCultivator>> SPIRITUAL_CULTIVATOR = registerEntityType(SpiritualCultivator::new, "spiritual_cultivator", MobCategory.CREATURE);

//    /* Creature */
//
//    public static final RegistryObject<EntityType<GrassCarp>> GRASS_CARP = registerEntityType(GrassCarp::new, "grass_carp", MobCategory.WATER_CREATURE);
//    public static final RegistryObject<EntityType<SilkWorm>> SILK_WORM = registerEntityType(SilkWorm::new, "silk_worm", MobCategory.CREATURE);
//
//    /* Undead */
//
//    public static final RegistryObject<EntityType<SpiritualZombie>> SPIRITUAL_ZOMBIE = registerEntityType(SpiritualZombie::new, "spiritual_zombie", MobCategory.MONSTER);

    /* Golem */

//    public static final RegistryObject<EntityType<IronGolem>> IRON_GOLEM = registerEntityType(IronGolem::new, "iron_golem", MobCategory.CREATURE);

    public static void addEntityAttributes(EntityAttributeCreationEvent ev) {
        /* human */
//        ev.put(DISCIPLE_VILLAGER.get(), HumanEntity.createAttributes().build());
        ev.put(EMPTY_CULTIVATOR.get(), HumanEntity.createAttributes().build());
        ev.put(SPIRITUAL_CULTIVATOR.get(), HumanEntity.createAttributes().build());
        /* creature */
//        ev.put(GRASS_CARP.get(), GrassCarp.createAttributes().build());
//        ev.put(SILK_WORM.get(), SilkWorm.createAttributes().build());
//
//        /* undead */
//        ev.put(SPIRITUAL_ZOMBIE.get(), SpiritualZombie.createAttributes().build());
//
//        /* golem */
//        ev.put(IRON_GOLEM.get(), SpiritualZombie.createAttributes().build());
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
        RegistryObject<EntityType<T>> object = ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, classification).build(Util.prefix(name).toString()));
        MAP.put(object, name);
        return object;
    }

}
