package hungteen.imm.common.cultivation;

import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.api.cultivation.CultivationType;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * 修行的方式。
 *
 * @author HungTeen
 * @program Immortal
 * @create 2022-10-13 15:09
 **/
public interface CultivationTypes {

    HTCustomRegistry<CultivationType> TYPES = HTRegistryManager.custom(Util.prefix("cultivation_type"));
    /**
     * 这种修行方式暂时未知，通常用于其他模组的生物。
     */
    CultivationType UNKNOWN = register("unknown", false);

    /**
     * 暂时没有修行，比如动物、凡人。
     */
    CultivationType NONE = register("none",false);

    /**
     * 正常意义的灵修。
     */
    CultivationType QI = register("qi",true);

    /**
     * 妖修 & 妖兽.
     */
    CultivationType YAOGUAI = register("yaoguai",true);

    /**
     * 亡灵。
     */
    CultivationType UNDEAD = register("undead", true);

    /**
     * 魔法师是异教徒！
     */
    CultivationType WIZARD = register("wizard", false);

    /**
     * 法器实体也许需要。
     */
    CultivationType ARTIFACT = register("artifact", true);

    static CultivationType register(String name, boolean requireQi){
        return registry().register(Util.prefix(name), new CultivationTypeImpl(name, requireQi));
    }

    static HTCustomRegistry<CultivationType> registry(){
        return TYPES;
    }

    record CultivationTypeImpl(String name, boolean requireQi) implements CultivationType {


        @Override
        public MutableComponent getComponent() {
            return Component.translatable("misc." + getModID() + ".cultivation_type." + name());
        }


        @Override
        public String getModID() {
            return Util.id();
        }

    }
}
