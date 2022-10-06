package hungteen.immortal.item;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.item.rune.EffectRuneItem;
import hungteen.immortal.item.rune.GetterRuneItem;
import hungteen.immortal.item.rune.RuneItem;
import hungteen.immortal.utils.Util;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-03 22:44
 **/
public class ImmortalItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Util.id());

    /* Rune items */

    public static final RegistryObject<Item> RUNE = rune("rune");
    public static final RegistryObject<Item> BIGGER_RUNE = rune("bigger_rune");
    public static final RegistryObject<Item> SMALLER_RUNE = rune("smaller_rune");
    public static final RegistryObject<Item> BIGGER_EQUAL_RUNE = rune("bigger_equal_rune");
    public static final RegistryObject<Item> SMALL_EQUAL_RUNE = rune("small_equal_rune");
    public static final RegistryObject<Item> EQUAL_RUNE = rune("equal_rune");
    public static final RegistryObject<Item> NOT_EQUAL_RUNE = rune("not_equal_rune");
    public static final RegistryObject<Item> AND_RUNE = rune("and_rune");
    public static final RegistryObject<Item> OR_RUNE = rune("or_rune");
    public static final RegistryObject<Item> NOT_RUNE = rune("not_rune");
    public static final RegistryObject<Item> IF_RUNE = rune("if_rune");
    public static final RegistryObject<Item> IF_ELSE_RUNE = rune("if_else_rune");
    public static final RegistryObject<Item> GOTO_RUNE = rune("goto_rune");
    public static final RegistryObject<Item> CLOSE_RUNE = rune("close_rune");
    public static final RegistryObject<Item> BOOL_RUNE = rune("bool_rune");
    public static final RegistryObject<Item> NUMBER_RUNE = rune("number_rune");
    public static final RegistryObject<Item> GET_SELF_ENTITY_RUNE = rune("get_self_entity_rune");
    public static final RegistryObject<Item> GET_SELF_ITEM_RUNE = rune("get_self_item_rune");
    public static final RegistryObject<Item> GET_OWNER_ENTITY_RUNE = rune("get_owner_entity_rune");

    /**
     * register items.
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void registerItems(RegistryEvent.Register<Item> ev){
        ImmortalAPI.get().getGetterRunes().forEach(getter -> {
            ev.getRegistry().register(new GetterRuneItem(getter).setRegistryName(getter.getRegistryName() + "_rune"));
        });
        ImmortalAPI.get().getEffectRunes().forEach(effect -> {
            ev.getRegistry().register(new EffectRuneItem(effect).setRegistryName(effect.getRegistryName() + "_rune"));
        });
    }

    private static RegistryObject<Item> rune(String name){
        return ITEMS.register(name, RuneItem::new);
    }

}
