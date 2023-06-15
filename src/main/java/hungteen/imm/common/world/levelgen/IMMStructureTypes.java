package hungteen.imm.common.world.levelgen;

import hungteen.imm.common.world.levelgen.structure.TeleportRuinStructure;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/15 15:08
 */
public class IMMStructureTypes {

    private static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE = DeferredRegister.create(BuiltInRegistries.STRUCTURE_TYPE.key(), Util.id());

    public static final RegistryObject<StructureType<TeleportRuinStructure>> TELEPORT_RUIN = STRUCTURE_TYPE.register("teleport_ruin", () -> () -> TeleportRuinStructure.CODEC);

    public static void register(IEventBus eventBus){
        STRUCTURE_TYPE.register(eventBus);
    }
}
