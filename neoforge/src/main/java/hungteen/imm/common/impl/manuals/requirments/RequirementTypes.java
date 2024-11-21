package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IRequirementType;
import hungteen.imm.util.Util;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 15:19
 */
public interface RequirementTypes {

    HTCustomRegistry<IRequirementType<?>> TYPES = HTRegistryManager.custom(Util.prefix("requirement_type"));

    IRequirementType<AndRequirement> AND = register(new RequirementType<>("and", AndRequirement.CODEC));
    IRequirementType<OrRequirement> OR = register(new RequirementType<>("or", OrRequirement.CODEC));
    IRequirementType<NotRequirement> NOT = register(new RequirementType<>("not", NotRequirement.CODEC));
    IRequirementType<CultivationTypeRequirement> CULTIVATION_TYPE = register(new RequirementType<>("cultivation_type", CultivationTypeRequirement.CODEC));
    IRequirementType<RealmRequirement> REALM = register(new RequirementType<>("realm", RealmRequirement.CODEC));
    IRequirementType<SpellRequirement> SPELL = register(new RequirementType<>("spell", SpellRequirement.CODEC));
    IRequirementType<SpiritualRootRequirement> SPIRITUAL_ROOT = register(new RequirementType<>("spiritual_root", SpiritualRootRequirement.CODEC));
    IRequirementType<ElementRequirement> ELEMENT = register(new RequirementType<>("element", ElementRequirement.CODEC));
    IRequirementType<EMPRequirement> EMP = register(new RequirementType<>("emp", EMPRequirement.CODEC));

    static HTCustomRegistry<IRequirementType<?>> registry() {
        return TYPES;
    }

    static <T extends ILearnRequirement> IRequirementType<T> register(IRequirementType<T> type) {
        return registry().register(type.getLocation(), type);
    }

    static Codec<ILearnRequirement> getCodec() {
        return RequirementTypes.registry().byNameCodec().dispatch(ILearnRequirement::getType, IRequirementType::codec);
    }

    record RequirementType<P extends ILearnRequirement>(String name, MapCodec<P> codec) implements IRequirementType<P> {

        @Override
        public String getModID() {
            return Util.id();
        }
    }

}
