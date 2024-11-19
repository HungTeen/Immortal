package hungteen.imm.common.menu;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.IMMInitializer;
import hungteen.imm.common.menu.furnace.ElixirRoomMenu;
import hungteen.imm.common.menu.furnace.SpiritualFurnaceMenu;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 10:19
 **/
public class IMMMenus {

    private static final HTVanillaRegistry<MenuType<?>> CONTAINER_TYPES = HTRegistryManager.vanilla(Registries.MENU, Util.id());

    public static final HTHolder<MenuType<MerchantTradeMenu>> CULTIVATOR_TRADE = CONTAINER_TYPES.register("cultivator_trade", () -> {
        return IMenuTypeExtension.create(((windowId, inv, data) -> new MerchantTradeMenu(windowId, inv)));
    });

    public static final HTHolder<MenuType<SpiritualFurnaceMenu>> SPIRITUAL_FURNACE = CONTAINER_TYPES.register("spiritual_furnace", () -> {
        return IMenuTypeExtension.create(SpiritualFurnaceMenu::new);
    });

    public static final HTHolder<MenuType<ElixirRoomMenu>> ELIXIR_ROOM = CONTAINER_TYPES.register("elixir_room", () -> {
        return IMenuTypeExtension.create(ElixirRoomMenu::new);
    });

//    public static final HTHolder<MenuType<SmithingArtifactMenu>> SMITHING_ARTIFACT = CONTAINER_TYPES.initialize("smithing_artifact", () -> {
//        return IMenuTypeExtension.create(SmithingArtifactMenu::new);
//    });

    public static final HTHolder<MenuType<GolemInventoryMenu>> GOLEM_INVENTORY = CONTAINER_TYPES.register("golem_inventory", () -> {
        return IMenuTypeExtension.create(GolemInventoryMenu::new);
    });

    public static final HTHolder<MenuType<RuneCraftingMenu>> RUNE_CRAFT = CONTAINER_TYPES.register("rune_craft", () -> {
        return IMenuTypeExtension.create((id, inventory, data) -> new RuneCraftingMenu(id, inventory));
    });

    public static final HTHolder<MenuType<RuneGateMenu>> RUNE_GATE = CONTAINER_TYPES.register("rune_gate", () -> {
        return IMenuTypeExtension.create((id, inventory, data) -> new RuneGateMenu(id, inventory));
    });

    public static final HTHolder<MenuType<RuneBindMenu>> RUNE_BIND = CONTAINER_TYPES.register("rune_bind", () -> {
        return IMenuTypeExtension.create((id, inventory, data) -> new RuneBindMenu(id, inventory));
    });

    public static HTVanillaRegistry<MenuType<?>> registry() {
        return CONTAINER_TYPES;
    }
    /**
     * {@link IMMInitializer#defferRegister(IEventBus)}
     */
    public static void initialize(IEventBus event){
        NeoHelper.initRegistry(registry(), event);
    }

}
