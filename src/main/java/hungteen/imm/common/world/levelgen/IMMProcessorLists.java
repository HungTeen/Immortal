package hungteen.imm.common.world.levelgen;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-21 11:48
 **/
public interface IMMProcessorLists {

//    private static final DeferredRegister<StructureProcessorList> PROCESSOR_LISTS = DeferredRegister.create(BuiltInRegistries.STRUCTURE_PROCESSOR, Util.id());
//
//    public static final RegistryObject<StructureProcessorList> SPIRITUAL_PLAINS_STREETS = PROCESSOR_LISTS.register("spiritual_plains_street", SpiritualPlainsVillage::getStreetProcessor);

    static void register(BootstapContext<StructureProcessorList> context){
//        PROCESSOR_LISTS.register(event);
    }
}
