package hungteen.immortal.common.world.structure;

import hungteen.immortal.utils.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-20 09:39
 **/
public class ImmortalStructures {

    public static final ResourceKey<StructureSet> SPIRITUAL_PLAINS_VILLAGE_SET = registerStructureSet("spiritual_plains_village");

    public static final ResourceKey<Structure> SPIRITUAL_PLAINS_VILLAGE = registerStructure("village/spiritual_plains");

    /**
     * {@link hungteen.immortal.ImmortalMod#defferRegister(IEventBus)}
     */
    public static void register(){

    }

    private static ResourceKey<Structure> registerStructure(String name) {
        return ResourceKey.create(Registry.STRUCTURE_REGISTRY, Util.prefix(name));
    }

    private static ResourceKey<StructureSet> registerStructureSet(String name) {
        return ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, Util.prefix(name));
    }
}
