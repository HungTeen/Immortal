package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IRequirementType;
import hungteen.imm.util.Util;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:19
 */
public class RequirementTypes {

    private static final IHTSimpleRegistry<IRequirementType<?>> TYPES = HTRegistryManager.createSimple(Util.prefix("requirement_type"));

    public static final IRequirementType<AndRequirement> AND = register(new RequirementType<>("and", AndRequirement.CODEC));
    public static final IRequirementType<CultivationTypeRequirement> CULTIVATION_TYPE = register(new RequirementType<>("cultivation_type", CultivationTypeRequirement.CODEC));
    public static final IRequirementType<SpellRequirement> SPELL = register(new RequirementType<>("spell", SpellRequirement.CODEC));

    public static IHTSimpleRegistry<IRequirementType<?>> registry(){
        return TYPES;
    }

    public static <T extends ILearnRequirement> IRequirementType<T> register(IRequirementType<T> type){
        return registry().register(type);
    }

    record RequirementType<P extends ILearnRequirement>(String name, Codec<P> codec) implements IRequirementType<P> {

        @Override
        public String getName() {
            return name();
        }

        @Override
        public String getModID() {
            return Util.id();
        }
    }

}
