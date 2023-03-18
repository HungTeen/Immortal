package hungteen.immortal.common.menu;

import hungteen.immortal.utils.Util;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 10:19
 **/
public class ImmortalMenus {

    private static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Util.id());

    public static final RegistryObject<MenuType<CultivatorTradeMenu>> CULTIVATOR_TRADE = CONTAINER_TYPES.register("cultivator_trade", () -> {
        return IForgeMenuType.create(CultivatorTradeMenu::new);
    });

//    public static final RegistryObject<MenuType<SpiritualFurnaceMenu>> SPIRITUAL_FURNACE = CONTAINER_TYPES.register("spiritual_furnace", () -> {
//        return IForgeMenuType.create(SpiritualFurnaceMenu::new);
//    });
//
//    public static final RegistryObject<MenuType<ElixirRoomMenu>> ELIXIR_ROOM = CONTAINER_TYPES.register("elixir_room", () -> {
//        return IForgeMenuType.create(ElixirRoomMenu::new);
//    });
//
//    public static final RegistryObject<MenuType<SmithingArtifactMenu>> SMITHING_ARTIFACT = CONTAINER_TYPES.register("smithing_artifact", () -> {
//        return IForgeMenuType.create(SmithingArtifactMenu::new);
//    });

    public static final RegistryObject<MenuType<GolemMenu>> GOLEM_INVENTORY = CONTAINER_TYPES.register("golem_inventory", () -> {
        return IForgeMenuType.create((windowId, inv, data) -> {
            return new GolemMenu(windowId, inv, data.readInt());
        });
    });

    public static void register(IEventBus event){
        CONTAINER_TYPES.register(event);
    }

}
