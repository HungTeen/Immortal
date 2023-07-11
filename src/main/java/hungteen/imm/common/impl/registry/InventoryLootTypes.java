package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.ImmortalMod;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.registry.IInventoryLootType;
import hungteen.imm.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/1 16:19
 */
public class InventoryLootTypes {

    private static final HTSimpleRegistry<IInventoryLootType> LOOT_TYPES = HTRegistryManager.createSimple(Util.prefix("inventory_loot_type"));
    private static final List<IInventoryLootType> LIST = new ArrayList<>();

    public static IHTSimpleRegistry<IInventoryLootType> registry(){
        return LOOT_TYPES;
    }

    public static final IInventoryLootType VANILLA = new InventoryLootType("vanilla");

    public static final IInventoryLootType SPIRITUAL = new InventoryLootType("spiritual");

    public record InventoryLootType(String name) implements IInventoryLootType {

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register(){
            IMMAPI.get().inventoryLootRegistry().ifPresent(l -> l.register(LIST));
        }

        public InventoryLootType(String name) {
            this.name = name;
            LIST.add(this);
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
