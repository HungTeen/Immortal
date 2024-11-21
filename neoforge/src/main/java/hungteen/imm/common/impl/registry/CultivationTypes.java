package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * 修行的方式。
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-13 15:09
 **/
public interface CultivationTypes {

    HTCustomRegistry<ICultivationType> TYPES = HTRegistryManager.custom(Util.prefix("cultivation_type"));

    ICultivationType UNKNOWN = register(new CultivationType("unknown", true, false));
    ICultivationType MORTAL = register(new CultivationType("mortal", true, false));
    ICultivationType SPIRITUAL = register(new CultivationType("spiritual", false, true));
    ICultivationType MONSTER = register(new CultivationType("monster", false, true));
    ICultivationType UNDEAD = register(new CultivationType("undead", true, true));
    ICultivationType WIZARD = register(new CultivationType("wizard", true, false));
    ICultivationType ARTIFACT = register(new CultivationType("artifact", false, true));
//    public static final ICultivationType GHOST = initialize(new CultivationType("ghost"));
//    public static final ICultivationType BLOOD = initialize(new CultivationType("blood"));

    static HTCustomRegistry<ICultivationType> registry(){
        return TYPES;
    }

    static ICultivationType register(ICultivationType type){
        return registry().register(type.getLocation(), type);
    }

    record CultivationType(String name, boolean canEnchant, boolean isSpiritual) implements ICultivationType {

        @Override
        public MutableComponent getComponent() {
            return Component.translatable("misc." + getModID() + ".cultivation_type." + getName());
        }

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
