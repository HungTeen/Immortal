package hungteen.imm.common.entity.ai;

import com.google.common.collect.ImmutableSet;
import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-17 11:00
 **/
public class IMMProfessions {

    private static final HTVanillaRegistry<VillagerProfession> PROFESSION_TYPES = HTRegistryManager.vanilla(Registries.VILLAGER_PROFESSION, Util.id());

//    public static final HTHolder<VillagerProfession> ELIXIR_CRAFTER = initialize("elixir_crafter", ImmortalPoiTypes.ELIXIR_ROOM.getKey());

    private static HTHolder<VillagerProfession> register(String name, ResourceKey<PoiType> poiTypeKey){
        return register(name, poiTypeKey, null);
    }

    private static HTHolder<VillagerProfession> register(String name, ResourceKey<PoiType> poiTypeKey, SoundEvent soundEvent){
        return PROFESSION_TYPES.register(name, () -> new VillagerProfession(
                Util.prefixName(name),
                poi -> poi.is(poiTypeKey),
                poi -> poi.is(poiTypeKey),
                ImmutableSet.of(),
                ImmutableSet.of(),
                soundEvent
        ));
    }

    public static void initialize(IEventBus event){
        NeoHelper.initRegistry(PROFESSION_TYPES, event);
    }
    
}
