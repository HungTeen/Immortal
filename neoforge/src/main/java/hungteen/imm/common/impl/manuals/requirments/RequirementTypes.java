package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.api.spell.LearnRequirement;
import hungteen.imm.api.spell.RequirementType;
import hungteen.imm.util.Util;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 15:19
 */
public interface RequirementTypes {

    HTCustomRegistry<RequirementType<?>> TYPES = HTRegistryManager.custom(Util.prefix("requirement_type"));

    RequirementType<AndRequirement> AND = register(new RequirementTypeImpl<>("and", AndRequirement.CODEC));
    RequirementType<OrRequirement> OR = register(new RequirementTypeImpl<>("or", OrRequirement.CODEC));
    RequirementType<NotRequirement> NOT = register(new RequirementTypeImpl<>("not", NotRequirement.CODEC));
    RequirementType<CultivationTypeRequirement> CULTIVATION_TYPE = register(new RequirementTypeImpl<>("cultivation_type", CultivationTypeRequirement.CODEC));
    RequirementType<RealmRequirement> REALM = register(new RequirementTypeImpl<>("realm", RealmRequirement.CODEC));
    RequirementType<SpellRequirement> SPELL = register(new RequirementTypeImpl<>("spell", SpellRequirement.CODEC));
    RequirementType<QiRootRequirement> QI_ROOT = register(new RequirementTypeImpl<>("qi_root", QiRootRequirement.CODEC));
    RequirementType<ElementRequirement> ELEMENT = register(new RequirementTypeImpl<>("element", ElementRequirement.CODEC));
    RequirementType<EMPRequirement> EMP = register(new RequirementTypeImpl<>("emp", EMPRequirement.CODEC));

    static HTCustomRegistry<RequirementType<?>> registry() {
        return TYPES;
    }

    static <T extends LearnRequirement> RequirementType<T> register(RequirementType<T> type) {
        return registry().register(type.getLocation(), type);
    }

    static Codec<LearnRequirement> getCodec() {
        return RequirementTypes.registry().byNameCodec().dispatch(LearnRequirement::getType, RequirementType::codec);
    }

    record RequirementTypeImpl<P extends LearnRequirement>(String name, MapCodec<P> codec) implements RequirementType<P> {

        @Override
        public String getModID() {
            return Util.id();
        }
    }

}
