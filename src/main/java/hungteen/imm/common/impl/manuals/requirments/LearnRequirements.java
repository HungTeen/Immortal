package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.IHTCodecRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IRequirementType;
import hungteen.imm.common.impl.registry.CultivationTypes;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:48
 */
public class LearnRequirements {

    private static final IHTCodecRegistry<ILearnRequirement> TYPES = HTRegistryManager.create(Util.prefix("learn_requirement"), LearnRequirements::getDirectCodec, LearnRequirements::getDirectCodec);

    public static final ResourceKey<ILearnRequirement> SPIRITUAL_CULTIVATOR = create("spiritual_cultivator");

    public static void register(BootstapContext<ILearnRequirement> context){
        context.register(SPIRITUAL_CULTIVATOR, new CultivationTypeRequirement(CultivationTypes.SPIRITUAL));
    }

    public static Codec<ILearnRequirement> getDirectCodec(){
        return RequirementTypes.registry().byNameCodec().dispatch(ILearnRequirement::getType, IRequirementType::codec);
    }

    public static Codec<Holder<ILearnRequirement>> getCodec(){
        return registry().getHolderCodec(getDirectCodec());
    }

    public static IHTCodecRegistry<ILearnRequirement> registry(){
        return TYPES;
    }

    public static ResourceKey<ILearnRequirement> create(String name) {
        return registry().createKey(Util.prefix(name));
    }
}
