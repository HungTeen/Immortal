package hungteen.imm.common.item;

import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.ImmortalMod;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.item.artifacts.SpiritualPearlItem;
import hungteen.imm.common.item.elixirs.*;
import hungteen.imm.common.item.runes.BehaviorRuneItem;
import hungteen.imm.common.item.runes.MemoryRuneItem;
import hungteen.imm.common.item.runes.RuneItem;
import hungteen.imm.common.item.runes.info.ItemFilterRune;
import hungteen.imm.common.rune.behavior.BehaviorRunes;
import hungteen.imm.common.rune.memory.MemoryRunes;
import hungteen.imm.common.tag.IMMBannerPatternTags;
import hungteen.imm.util.Util;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-03 22:44
 **/
public class IMMItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Util.id());

    /* Spell Books */

    public static final RegistryObject<Item> SPELL_BOOK = ITEMS.register("spell_book", SpellBookItem::new);

    /* Material Tab Items */

    public static final RegistryObject<Item> GOURD_SEEDS = ITEMS.register("gourd_seeds", () -> new ItemNameBlockItem(IMMBlocks.GOURD_STEM.get(), new Item.Properties()));

    /* Rune Tab Items */

    public static final RegistryObject<Item> RUNE = ITEMS.register("rune", RuneItem::new);
    public static final RegistryObject<Item> ITEM_FILTER_RUNE = ITEMS.register("item_filter_rune", ItemFilterRune::new);

    /* Elixir Tab Items */

    public static final RegistryObject<Item> FIVE_FLOWERS_ELIXIR = ITEMS.register("five_flowers_elixir", CultivationElixir.FiveFlowersElixir::new);
    public static final RegistryObject<Item> ANTIDOTE_ELIXIR = ITEMS.register("antidote_elixir", AntidoteElixir::new);
    public static final RegistryObject<Item> SPIRIT_RECOVERY_ELIXIR = ITEMS.register("spirit_recovery_elixir", SpiritualElixir.SpiritRecoveryElixir::new);
    public static final RegistryObject<Item> ABSTINENCE_ELIXIR = ITEMS.register("abstinence_elixir", AbstinenceElixir::new);
    public static final RegistryObject<Item> GATHER_BREATH_ELIXIR = ITEMS.register("gather_breath_elixir", CultivationElixir.GatherBreathElixir::new);
    public static final RegistryObject<Item> FOUNDATION_ELIXIR = ITEMS.register("foundation_elixir", FoundationElixir::new);

    /* Artifact Tab Items */

    public static final RegistryObject<Item> SPIRITUAL_PEARL = ITEMS.register("spiritual_pearl", SpiritualPearlItem::new);
//    public static final RegistryObject<Item> RAW_ARTIFACT_BOX = ITEMS.register("raw_artifact_box", RawArtifactBox::new);
//    public static final RegistryObject<Item> STONE_HAMMER = ITEMS.register("stone_hammer", () -> new HammerItem(ImmortalTiers.STONE));
//    public static final RegistryObject<Item> FLAME_GOURD = ITEMS.register("flame_gourd", () -> new FlameGourd(ArtifactTypes.COMMON_ITEM));
//    public static final RegistryObject<Item> BRONZE_SWORD = ITEMS.register("bronze_sword", () -> new SwordItem(ImmortalTiers.BRONZE));
//    public static final RegistryObject<Item> BRONZE_SHORT_SWORD = ITEMS.register("bronze_short_sword", () -> new ShortSwordItem(ImmortalTiers.BRONZE));
//    public static final RegistryObject<Item> BRONZE_AXE = ITEMS.register("bronze_axe", () -> new AxeItem(ImmortalTiers.BRONZE, 7));
//    public static final RegistryObject<Item> BRONZE_PICKAXE = ITEMS.register("bronze_pickaxe", () -> new PickaxeItem(ImmortalTiers.BRONZE, 7));
//    public static final RegistryObject<Item> BRONZE_SHOVEL = ITEMS.register("bronze_shovel", () -> new ShovelItem(ImmortalTiers.BRONZE, 7));
//    public static final RegistryObject<Item> BRONZE_HOE = ITEMS.register("bronze_hoe", () -> new HoeItem(ImmortalTiers.BRONZE, 7));
//    public static final RegistryObject<Item> BRONZE_HAMMER = ITEMS.register("bronze_hammer", () -> new HammerItem(ImmortalTiers.BRONZE));

    /* Misc Tab Items */

//    public static final RegistryObject<Item> GRASS_CARP_BUCKET = ITEMS.register("grass_carp_bucket", () -> {
//        return new MobBucketItem(() -> ImmortalEntities.GRASS_CARP.get(), () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_AXOLOTL, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC));
//    });

    public static final RegistryObject<Item> CONTINUOUS_MOUNTAIN_PATTERN = ITEMS.register("continuous_mountain_pattern", () -> new BannerPatternItem(IMMBannerPatternTags.CONTINUOUS_MOUNTAIN, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FLOWING_CLOUD_PATTERN = ITEMS.register("flowing_cloud_pattern", () -> new BannerPatternItem(IMMBannerPatternTags.FLOWING_CLOUD, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FOLDED_THUNDER_PATTERN = ITEMS.register("folded_thunder_pattern", () -> new BannerPatternItem(IMMBannerPatternTags.FOLDED_THUNDER, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> RHOMBUS_PATTERN = ITEMS.register("rhombus_pattern", () -> new BannerPatternItem(IMMBannerPatternTags.RHOMBUS, new Item.Properties().stacksTo(1)));


    /* Food Tab Items */

    public static final RegistryObject<Item> MULBERRY = ITEMS.register("mulberry", () -> new Item(new Item.Properties()));


    /**
     * {@link ImmortalMod#register(RegisterEvent)}
     */
    public static void registerItems(RegisterEvent event){
        MemoryRunes.registry().getValues().forEach(rune -> {
            ItemHelper.get().register(event, rune.getLocation(), () -> new MemoryRuneItem(rune));
        });
        BehaviorRunes.registry().getValues().forEach(rune -> {
            ItemHelper.get().register(event, rune.getLocation(), () -> new BehaviorRuneItem(rune));
        });
    }

    /**
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void register(IEventBus event){
        ITEMS.register(event);
    }

//    /**
//     * register spawn eggs
//     */
//    private static RegistryObject<ForgeSpawnEggItem> registerSpawnEgg(String name, RegistryObject<? extends EntityType<? extends Mob>> entityType, int color1, int color2){
//        return ITEMS.register(name + "_spawn_egg", () -> new ForgeSpawnEggItem(entityType, color1, color2, new Item.Properties().tab(PVZItemTabs.PVZ_MISC)));
//    }

}
