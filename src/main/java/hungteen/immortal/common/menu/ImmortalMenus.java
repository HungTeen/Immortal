package hungteen.immortal.common.menu;

import hungteen.immortal.utils.Util;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-09 10:19
 **/
public class ImmortalMenus {

    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Util.id());

    public static final RegistryObject<MenuType<SpiritualFurnaceMenu>> SPIRITUAL_FURNACE = CONTAINER_TYPES.register("spiritual_furnace", () -> {
        return IForgeMenuType.create(SpiritualFurnaceMenu::new);
    });

    public static final RegistryObject<MenuType<ElixirRoomMenu>> ELIXIR_ROOM = CONTAINER_TYPES.register("elixir_room", () -> {
        return IForgeMenuType.create(ElixirRoomMenu::new);
    });

    public static final RegistryObject<MenuType<GolemMenu>> GOLEM_INVENTORY = CONTAINER_TYPES.register("golem_inventory", () -> {
        return IForgeMenuType.create((windowId, inv, data) -> {
            return new GolemMenu(windowId, inv, data.readInt());
        });
    });

}
