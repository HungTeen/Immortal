package hungteen.immortal.menu;

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

    public static final RegistryObject<MenuType<SpiritualStoveMenu>> SPIRITUAL_STOVE = CONTAINER_TYPES.register("spiritual_stove", () -> {
        return IForgeMenuType.create((windowId, inv, data) -> {
            return new SpiritualStoveMenu(windowId, inv);
        });
    });
}