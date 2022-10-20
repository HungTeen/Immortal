package hungteen.immortal.common.entity;

import hungteen.immortal.common.entity.misc.FlyingItemEntity;
import hungteen.immortal.common.entity.misc.SpiritualFlame;
import hungteen.immortal.common.entity.pet.GrassCarp;
import hungteen.immortal.common.entity.undead.SpiritualZombie;
import hungteen.immortal.utils.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-02 15:01
 **/
public class ImmortalEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =  DeferredRegister.create(ForgeRegistries.ENTITIES, Util.id());

    /* Misc */
    public static final RegistryObject<EntityType<FlyingItemEntity>> FLYING_ITEM = registerEntityType(FlyingItemEntity::new, "flying_item", MobCategory.MISC);
    public static final RegistryObject<EntityType<SpiritualFlame>> SPIRITUAL_FLAME = registerEntityType(SpiritualFlame::new, "spiritual_flame", MobCategory.MISC);

    /* Creature */
    public static final RegistryObject<EntityType<GrassCarp>> GRASS_CARP = registerEntityType(GrassCarp::new, "grass_carp", MobCategory.WATER_CREATURE);

    /* Undead */
    public static final RegistryObject<EntityType<SpiritualZombie>> SPIRITUAL_ZOMBIE = registerEntityType(SpiritualZombie::new, "spiritual_zombie", MobCategory.MONSTER);


    public static void addEntityAttributes(EntityAttributeCreationEvent ev) {
        /* creature */
        ev.put(GRASS_CARP.get(), GrassCarp.createAttributes().build());

        /* undead */
        ev.put(SPIRITUAL_ZOMBIE.get(), SpiritualZombie.createAttributes().build());
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntityType(EntityType.EntityFactory factory, String name, MobCategory classification){
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, classification).build(Util.prefix(name).toString()));
    }

}
