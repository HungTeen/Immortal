package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.api.registry.IInventoryLootType;
import hungteen.imm.util.Util;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/1 16:19
 */
public class InventoryLootTypes {

    private static final HTSimpleRegistry<IInventoryLootType> LOOT_TYPES = HTRegistryManager.createSimple(Util.prefix("inventory_loot_type"));

    public static final IInventoryLootType VANILLA = register(new InventoryLootType("vanilla"));

    public static final IInventoryLootType SPIRITUAL = register(new InventoryLootType("spiritual"));

    public static IHTSimpleRegistry<IInventoryLootType> registry(){
        return LOOT_TYPES;
    }

    public static IInventoryLootType register(IInventoryLootType type){
        return registry().register(type);
    }

    public record InventoryLootType(String name) implements IInventoryLootType {

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
