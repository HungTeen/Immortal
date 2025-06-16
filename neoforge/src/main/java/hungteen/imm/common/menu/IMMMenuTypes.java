package hungteen.imm.common.menu;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.common.menu.furnace.ElixirRoomMenu;
import hungteen.imm.common.menu.furnace.SpiritualFurnaceMenu;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-09 10:19
 **/
public interface IMMMenuTypes {

    HTVanillaRegistry<MenuType<?>> TYPES = HTRegistryManager.vanilla(Registries.MENU, Util.id());

    HTHolder<MenuType<MerchantTradeMenu>> CULTIVATOR_TRADE = registry().register("cultivator_trade", () -> {
        return IMenuTypeExtension.create(((windowId, inv, data) -> new MerchantTradeMenu(windowId, inv)));
    });

    HTHolder<MenuType<SpiritualFurnaceMenu>> SPIRITUAL_FURNACE = registry().register("spiritual_furnace", () -> {
        return IMenuTypeExtension.create(SpiritualFurnaceMenu::new);
    });

    HTHolder<MenuType<ElixirRoomMenu>> ELIXIR_ROOM = registry().register("elixir_room", () -> {
        return IMenuTypeExtension.create(ElixirRoomMenu::new);
    });

//    public static final HTHolder<MenuType<SmithingArtifactMenu>> SMITHING_ARTIFACT = CONTAINER_TYPES.initialize("smithing_artifact", () -> {
//        return IMenuTypeExtension.create(SmithingArtifactMenu::new);
//    });

    HTHolder<MenuType<GolemInventoryMenu>> GOLEM_INVENTORY = registry().register("golem_inventory", () -> {
        return IMenuTypeExtension.create(GolemInventoryMenu::new);
    });

    HTHolder<MenuType<InscriptionTableMenu>> INSCRIPTION_TABLE = registry().register("inscription_table", () -> {
        return IMenuTypeExtension.create((id, inventory, data) -> new InscriptionTableMenu(id, inventory));
    });

    HTHolder<MenuType<RuneCraftingMenu>> RUNE_CRAFT = registry().register("rune_craft", () -> {
        return IMenuTypeExtension.create((id, inventory, data) -> new RuneCraftingMenu(id, inventory));
    });

    HTHolder<MenuType<RuneGateMenu>> RUNE_GATE = registry().register("rune_gate", () -> {
        return IMenuTypeExtension.create((id, inventory, data) -> new RuneGateMenu(id, inventory));
    });

    HTHolder<MenuType<RuneBindMenu>> RUNE_BIND = registry().register("rune_bind", () -> {
        return IMenuTypeExtension.create((id, inventory, data) -> new RuneBindMenu(id, inventory));
    });

    static HTVanillaRegistry<MenuType<?>> registry() {
        return TYPES;
    }

    static void initialize(IEventBus event){
        NeoHelper.initRegistry(registry(), event);
    }

}
