package hungteen.immortal.common.item;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.entity.ImmortalEntities;
import hungteen.immortal.common.item.artifacts.FlameGourd;
import hungteen.immortal.common.item.eixirs.AntidoteElixir;
import hungteen.immortal.common.item.eixirs.FiveFlowersElixir;
import hungteen.immortal.common.item.runes.RuneItem;
import hungteen.immortal.utils.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluids;
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

    /* Material Tab Items */
    public static final RegistryObject<Item> GOURD_SEEDS = ITEMS.register("gourd_seeds", () -> new ItemNameBlockItem(ImmortalBlocks.GOURD_STEM.get(), new Item.Properties().tab(ItemTabs.MATERIALS)));

    /* Rune Tab Items */
    public static final RegistryObject<Item> RUNE = ITEMS.register("rune", () -> new RuneItem(RuneItem.RuneTypes.DEFAULT));
    public static final RegistryObject<Item> MEMORY_RUNE = ITEMS.register("memory_rune", () -> new RuneItem(RuneItem.RuneTypes.MEMORY));
    public static final RegistryObject<Item> SENSOR_RUNE = ITEMS.register("sensor_rune", () -> new RuneItem(RuneItem.RuneTypes.SENSOR));
    public static final RegistryObject<Item> BEHAVIOR_RUNE = ITEMS.register("behavior_rune", () -> new RuneItem(RuneItem.RuneTypes.BEHAVIOR));

    /* Elixir Tab Items */
    public static final RegistryObject<Item> FIVE_FLOWERS_ELIXIR = ITEMS.register("five_flowers_elixir", FiveFlowersElixir::new);
    public static final RegistryObject<Item> ANTIDOTE_ELIXIR = ITEMS.register("antidote_elixir", AntidoteElixir::new);

    /* Artifact Tab Items */
    public static final RegistryObject<Item> FLAME_GOURD = ITEMS.register("flame_gourd", () -> new FlameGourd(0));

    /* Misc Tab Items */
    public static final RegistryObject<Item> GRASS_CARP_BUCKET = ITEMS.register("grass_carp_bucket", () -> {
        return new MobBucketItem(() -> ImmortalEntities.GRASS_CARP.get(), () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_AXOLOTL, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC));
    });

    /**
     * register items.
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void registerItems(RegistryEvent.Register<Item> ev){
    }


//    /**
//     * register spawn eggs
//     */
//    private static RegistryObject<ForgeSpawnEggItem> registerSpawnEgg(String name, RegistryObject<? extends EntityType<? extends Mob>> entityType, int color1, int color2){
//        return ITEMS.register(name + "_spawn_egg", () -> new ForgeSpawnEggItem(entityType, color1, color2, new Item.Properties().tab(PVZItemTabs.PVZ_MISC)));
//    }

}
