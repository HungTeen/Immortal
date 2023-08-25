package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.IHTCodecRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.api.registry.*;
import hungteen.imm.common.impl.registry.CultivationTypes;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:48
 */
public interface LearnRequirements {

    IHTCodecRegistry<ILearnRequirement> TYPES = HTRegistryManager.create(Util.prefix("learn_requirement"), LearnRequirements::getDirectCodec);

    static void register(BootstapContext<ILearnRequirement> context){
        CultivationTypes.registry().getValues().forEach(type -> {
            context.register(cultivation(type), new CultivationTypeRequirement(type));
        });
        RealmTypes.registry().getValues().forEach(type -> {
            context.register(realm(type, true), new RealmRequirement(type, true));
        });
    }

    static Codec<ILearnRequirement> getDirectCodec(){
        return RequirementTypes.registry().byNameCodec().dispatch(ILearnRequirement::getType, IRequirementType::codec);
    }

    static Codec<Holder<ILearnRequirement>> getCodec(){
        return registry().getHolderCodec(getDirectCodec());
    }

    static IHTCodecRegistry<ILearnRequirement> registry(){
        return TYPES;
    }

    static ResourceKey<ILearnRequirement> spell(ISpellType spell, int level){
        return registry().createKey(StringHelper.suffix(spell.getLocation(), String.valueOf(level)));
    }

    static ResourceKey<ILearnRequirement> cultivation(ICultivationType type){
        return registry().createKey(type.getLocation());
    }

    static ResourceKey<ILearnRequirement> realm(IRealmType type, boolean lowest){
        return registry().createKey(StringHelper.suffix(type.getLocation(), lowest ? "lowest" : "highest"));
    }

    static ResourceKey<ILearnRequirement> create(String name) {
        return registry().createKey(Util.prefix(name));
    }
}
