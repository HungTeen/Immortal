package hungteen.immortal.common.entity;

import com.google.common.collect.ImmutableSet;
import hungteen.immortal.utils.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-17 11:00
 **/
public class ImmortalProfessions {

    private static final DeferredRegister<VillagerProfession> PROFESSION_TYPES = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, Util.id());

    public static final RegistryObject<VillagerProfession> ELIXIR_CRAFTER = register("elixir_crafter", ImmortalPoiTypes.ELIXIR_ROOM.getKey());

    private static RegistryObject<VillagerProfession> register(String name, ResourceKey<PoiType> poiTypeKey){
        return register(name, poiTypeKey, null);
    }

    private static RegistryObject<VillagerProfession> register(String name, ResourceKey<PoiType> poiTypeKey, SoundEvent soundEvent){
        return PROFESSION_TYPES.register(name, () -> new VillagerProfession(
                Util.prefixName(name),
                poi -> poi.is(poiTypeKey),
                poi -> poi.is(poiTypeKey),
                ImmutableSet.of(),
                ImmutableSet.of(),
                soundEvent
        ));
    }

    public static void register(IEventBus event){
        PROFESSION_TYPES.register(event);
    }
    
}
