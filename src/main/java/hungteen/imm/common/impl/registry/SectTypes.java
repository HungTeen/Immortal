package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.api.registry.ISectType;
import hungteen.imm.util.Util;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-24 19:46
 **/
public class SectTypes {

    private static final HTSimpleRegistry<ISectType> TRADE_TYPES = HTRegistryManager.create(Util.prefix("sect_type"));

    public static final ISectType VILLAGER_KINGDOM = register(new SectType("villager_kingdom"));
    public static final ISectType PILLAGER_TRIBE = register(new SectType("pillager_tribe"));

    public static IHTSimpleRegistry<ISectType> registry() {
        return TRADE_TYPES;
    }

    private static ISectType register(ISectType type){
        return registry().register(type);
    }

    public static void register(){
    }


    public record SectType(String name) implements ISectType {

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
