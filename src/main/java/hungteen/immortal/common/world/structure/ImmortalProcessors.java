package hungteen.immortal.common.world.structure;

import hungteen.immortal.utils.Util;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-21 11:48
 **/
public class ImmortalProcessors {

    private static final DeferredRegister<StructureProcessorList> PROCESSOR_LISTS = DeferredRegister.create(Registry.PROCESSOR_LIST_REGISTRY, Util.id());

    public static final RegistryObject<StructureProcessorList> SPIRITUAL_PLAINS_STREETS = PROCESSOR_LISTS.register("spiritual_plains_street", SpiritualPlainsVillage::getStreetProcessor);

    public static void register(IEventBus event){
        PROCESSOR_LISTS.register(event);
    }
}
