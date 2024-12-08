package hungteen.imm.common.item;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.common.item.HTItem;
import hungteen.htlib.util.NeoHelper;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.htlib.util.helper.impl.ItemHelper;
import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.item.artifact.FlameGourd;
import hungteen.imm.common.item.artifact.SpiritualPearlItem;
import hungteen.imm.common.item.artifact.WoodBowItem;
import hungteen.imm.common.item.elixirs.*;
import hungteen.imm.common.item.runes.BehaviorRuneItem;
import hungteen.imm.common.item.runes.filter.*;
import hungteen.imm.common.item.talisman.*;
import hungteen.imm.common.cultivation.rune.behavior.BehaviorRunes;
import hungteen.imm.common.tag.IMMBannerPatternTags;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-03 22:44
 **/
public interface IMMItems {

    HTVanillaRegistry<Item> ITEMS = HTRegistryManager.vanilla(Registries.ITEM, Util.id());

    /* Spell Books */

    HTHolder<Item> SECRET_MANUAL = ITEMS.register("secret_manual", SecretManualItem::new);

    /* Material Tab Items */

    HTHolder<Item> SPIRITUAL_WOOD = ITEMS.register("spiritual_wood", HTItem::new);
    HTHolder<Item> RICE_SEEDS = ITEMS.register("rice_seeds", () -> new ItemNameBlockItem(IMMBlocks.RICE.get(), new Item.Properties()));
    HTHolder<Item> RICE_STRAW = ITEMS.register("rice_straw", () -> new Item(new Item.Properties()));
    HTHolder<Item> JUTE_SEEDS = ITEMS.register("jute_seeds", () -> new ItemNameBlockItem(IMMBlocks.JUTE.get(), new Item.Properties()));
    HTHolder<Item> JUTE = ITEMS.register("jute", HTItem::new);
    HTHolder<Item> GOURD_SEEDS = ITEMS.register("gourd_seeds", () -> new ItemNameBlockItem(IMMBlocks.GOURD_STEM.get(), new Item.Properties()));
    HTHolder<Item> JOSS_PAPER = ITEMS.register("joss_paper", HTItem::new);
    HTHolder<Item> CINNABAR = ITEMS.register("cinnabar", HTItem::new);

    /* Rune Tab Items */

    HTHolder<ItemFilterRune> ITEM_FILTER_RUNE = ITEMS.register("item_filter_rune", ItemFilterRune::new);
    HTHolder<BlockFilterRune> BLOCK_FILTER_RUNE = ITEMS.register("block_filter_rune", BlockFilterRune::new);
    HTHolder<EntityFilterRune> ENTITY_FILTER_RUNE = ITEMS.register("entity_filter_rune", EntityFilterRune::new);
    HTHolder<BoolFilterRune> BOOL_FILTER_RUNE = ITEMS.register("bool_filter_rune", BoolFilterRune::new);
    HTHolder<PercentFilterRune> PERCENT_FILTER_RUNE = ITEMS.register("percent_filter_rune", PercentFilterRune::new);

    /* Elixir Tab Items */

    HTHolder<Item> FIVE_FLOWERS_ELIXIR = ITEMS.register("five_flowers_elixir", FiveFlowersElixir::new);
    HTHolder<Item> SPIRITUAL_INSPIRATION_ELIXIR = ITEMS.register("spiritual_inspiration_elixir", SpiritualInspirationElixir::new);
    HTHolder<Item> GATHER_BREATH_ELIXIR = ITEMS.register("gather_breath_elixir", GatherBreathElixir::new);
    HTHolder<Item> REFINE_BREATH_ELIXIR = ITEMS.register("refine_breath_elixir", RefineBreathElixir::new);
    //    public static final HTHolder<Item> ANTIDOTE_ELIXIR = ITEMS.initialize("antidote_elixir", AntidoteElixir::new);
//    public static final HTHolder<Item> SPIRIT_RECOVERY_ELIXIR = ITEMS.initialize("spirit_recovery_elixir", SpiritualElixir.SpiritRecoveryElixir::new);
//    public static final HTHolder<Item> ABSTINENCE_ELIXIR = ITEMS.initialize("abstinence_elixir", AbstinenceElixir::new);
//    public static final HTHolder<Item> FOUNDATION_ELIXIR = ITEMS.initialize("foundation_elixir", FoundationElixir::new);
    HTHolder<Item> CUSTOM_ELIXIR = ITEMS.register("custom_elixir", CustomElixirItem::new);

    /* Artifact Tab Items */

    HTHolder<Item> WOOD_BOW = ITEMS.register("wood_bow", WoodBowItem::new);
    HTHolder<Item> SPIRITUAL_PEARL = ITEMS.register("spiritual_pearl", SpiritualPearlItem::new);
//    public static final HTHolder<Item> RAW_ARTIFACT_BOX = ITEMS.initialize("raw_artifact_box", RawArtifactBox::new);
//    public static final HTHolder<Item> STONE_HAMMER = ITEMS.initialize("stone_hammer", () -> new HammerItem(ImmortalTiers.STONE));
HTHolder<Item> FLAME_GOURD = ITEMS.register("flame_gourd", () -> new FlameGourd(ArtifactRank.COMMON));
//    public static final HTHolder<Item> BRONZE_SWORD = ITEMS.initialize("bronze_sword", () -> new SwordItem(ImmortalTiers.BRONZE));
//    public static final HTHolder<Item> BRONZE_SHORT_SWORD = ITEMS.initialize("bronze_short_sword", () -> new ShortSwordItem(ImmortalTiers.BRONZE));
//    public static final HTHolder<Item> BRONZE_AXE = ITEMS.initialize("bronze_axe", () -> new AxeItem(ImmortalTiers.BRONZE, 7));
//    public static final HTHolder<Item> BRONZE_PICKAXE = ITEMS.initialize("bronze_pickaxe", () -> new PickaxeItem(ImmortalTiers.BRONZE, 7));
//    public static final HTHolder<Item> BRONZE_SHOVEL = ITEMS.initialize("bronze_shovel", () -> new ShovelItem(ImmortalTiers.BRONZE, 7));
//    public static final HTHolder<Item> BRONZE_HOE = ITEMS.initialize("bronze_hoe", () -> new HoeItem(ImmortalTiers.BRONZE, 7));
//    public static final HTHolder<Item> BRONZE_HAMMER = ITEMS.initialize("bronze_hammer", () -> new HammerItem(ImmortalTiers.BRONZE));

    /* Talisman Tab Items */

    HTHolder<Item> LIGHTNING_TALISMAN = ITEMS.register("lightning_talisman", LightningTalisman::new);
    HTHolder<Item> TWISTING_VINE_TALISMAN = ITEMS.register("twisting_vine_talisman", TwistingVineTalisman::new);
    HTHolder<Item> FALLING_ICE_TALISMAN = ITEMS.register("falling_ice_talisman", FallingIceTalisman::new);
    HTHolder<Item> FIREBALL_TALISMAN = ITEMS.register("fireball_talisman", FireballTalisman::new);
    HTHolder<Item> EARTH_FANG_TALISMAN = ITEMS.register("earth_fang_talisman", EarthFangTalisman::new);

    /* Misc Tab Items */

//    public static final HTHolder<Item> GRASS_CARP_BUCKET = ITEMS.initialize("grass_carp_bucket", () -> {
//        return new MobBucketItem(() -> ImmortalEntities.GRASS_CARP.get(), () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_AXOLOTL, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC));
//    });

    HTHolder<Item> CONTINUOUS_MOUNTAIN_PATTERN = ITEMS.register("continuous_mountain_pattern", () -> new BannerPatternItem(IMMBannerPatternTags.CONTINUOUS_MOUNTAIN, new Item.Properties().stacksTo(1)));
    HTHolder<Item> FLOWING_CLOUD_PATTERN = ITEMS.register("flowing_cloud_pattern", () -> new BannerPatternItem(IMMBannerPatternTags.FLOWING_CLOUD, new Item.Properties().stacksTo(1)));
    HTHolder<Item> FOLDED_THUNDER_PATTERN = ITEMS.register("folded_thunder_pattern", () -> new BannerPatternItem(IMMBannerPatternTags.FOLDED_THUNDER, new Item.Properties().stacksTo(1)));
    HTHolder<Item> RHOMBUS_PATTERN = ITEMS.register("rhombus_pattern", () -> new BannerPatternItem(IMMBannerPatternTags.RHOMBUS, new Item.Properties().stacksTo(1)));
    HTHolder<Item> TALISMAN_PATTERN = ITEMS.register("talisman_pattern", () -> new BannerPatternItem(IMMBannerPatternTags.TALISMAN, new Item.Properties().stacksTo(1)));
    HTHolder<Item> COILED_LOONG_PATTERN = ITEMS.register("coiled_loong_pattern", () -> new BannerPatternItem(IMMBannerPatternTags.COILED_LOONG, new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    HTHolder<Item> HOVERING_PHOENIX_PATTERN = ITEMS.register("hovering_phoenix_pattern", () -> new BannerPatternItem(IMMBannerPatternTags.HOVERING_PHOENIX, new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    /* Food Tab Items */

    HTHolder<Item> MULBERRY = ITEMS.register("mulberry", () -> new Item(new Item.Properties()));

    static void registerItems(RegisterEvent event){
        BehaviorRunes.registry().getValues().forEach(rune -> {
            NeoHelper.register(event, ItemHelper.get(), StringHelper.suffix(rune.getLocation(), "rune"), () -> new BehaviorRuneItem(rune));
        });
    }

    static HTVanillaRegistry<Item> registry(){
        return ITEMS;
    }

    static void initialize(IEventBus event){
        NeoHelper.initRegistry(registry(), event);
    }

//    /**
//     * initialize spawn eggs
//     */
//    private static HTHolder<ForgeSpawnEggItem> registerSpawnEgg(String name, HTHolder<? extends EntityType<? extends Mob>> entityType, int color1, int color2){
//        return ITEMS.initialize(name + "_spawn_egg", () -> new ForgeSpawnEggItem(entityType, color1, color2, new Item.Properties().tab(PVZItemTabs.PVZ_MISC)));
//    }

}
