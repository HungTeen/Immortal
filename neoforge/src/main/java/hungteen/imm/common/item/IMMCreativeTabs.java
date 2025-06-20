package hungteen.imm.common.item;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.htlib.util.helper.JavaHelper;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.htlib.util.helper.impl.ItemHelper;
import hungteen.imm.api.artifact.ArtifactItem;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.block.base.CushionBlock;
import hungteen.imm.common.cultivation.RealmManager;
import hungteen.imm.common.cultivation.manual.SecretManual;
import hungteen.imm.common.cultivation.impl.SecretManuals;
import hungteen.imm.common.item.elixir.CustomElixirItem;
import hungteen.imm.common.item.elixir.ElixirItem;
import hungteen.imm.common.item.runes.RuneItem;
import hungteen.imm.util.BlockUtil;
import hungteen.imm.util.ItemUtil;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-03 22:45
 **/
public interface IMMCreativeTabs {

    HTVanillaRegistry<CreativeModeTab> TABS = HTRegistryManager.vanilla(Registries.CREATIVE_MODE_TAB, Util.id());

    HTHolder<CreativeModeTab> MATERIALS = register("materials", builder ->
            builder.icon(() -> new ItemStack((IMMBlocks.GANODERMA.get())))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .displayItems((parameters, output) -> {
                        //TODO 等待下次更新。
                        final Set<ItemLike> blackList = Set.of(
                                IMMItems.RICE_SEEDS.get(), IMMItems.RICE_STRAW.get(),
                                IMMItems.JUTE_SEEDS.get(), IMMItems.JUTE.get(),
                                IMMBlocks.CINNABAR_ORE.get(),
                                IMMItems.MULBERRY.get(), IMMBlocks.MULBERRY_LEAVES.get(), IMMBlocks.MULBERRY_LEAVES_WITH_MULBERRIES.get(), IMMBlocks.MULBERRY_SAPLING.get()
                        );
                        final List<ItemLike> items = new ArrayList<>();
                        // 种子之类的杂项。
                        items.addAll(List.of(
//                                IMMItems.RICE_SEEDS.get(), IMMItems.RICE_STRAW.get(),
//                                IMMItems.JUTE_SEEDS.get(), IMMItems.JUTE.get(),
                                IMMItems.GOURD_SEEDS.get(), IMMBlocks.GANODERMA.get()
                        ));

                        // 葫芦。
                        BlockUtil.getGourds().forEach(pair -> items.add(pair.getSecond()));
                        items.addAll(getBannerPatterns());
                        // 装饰方块。
                        items.addAll(List.of(
//                                IMMBlocks.MULBERRY_LEAVES.get(), IMMBlocks.MULBERRY_LEAVES_WITH_MULBERRIES.get(), IMMBlocks.MULBERRY_SAPLING.get()
                        ));
                        // 刷怪蛋。
                        items.addAll(ItemUtil.getSpawnEggs());
                        items.forEach(output::accept);
                        // 防止有被遗漏的物品。
                        final Set<Item> itemSet = items.stream().map(ItemLike::asItem).collect(Collectors.toSet());
                        itemSet.addAll(blackList.stream().map(ItemLike::asItem).collect(Collectors.toSet()));
                        Util.get().filterValues(ItemHelper.get(), item -> {
                            if(itemSet.contains(item)) return false; // 已经被添加，不再考虑。
                            if(item instanceof ElixirItem) return false; // 排除丹药。
                            if(item instanceof SecretManualItem) return false; // 排除秘籍。
                            if(item instanceof ArtifactItem) return false; // 排除法器。
                            if(item instanceof BlockItem blockItem) {
                                if(blockItem.getBlock() instanceof CushionBlock) return false; // 排除坐垫。
                            }
                            if(item instanceof RuneItem) return false;
                            return true;
                        }).forEach(output::accept);
                    })
    );

    HTHolder<CreativeModeTab> ELIXIRS = register("elixirs", builder ->
            builder.icon(() -> new ItemStack((IMMItems.INSPIRATION_ELIXIR.get())))
                    .withTabsBefore(MATERIALS.getRegistryName())
                    .displayItems((parameters, output) -> {
                        output.acceptAll(ItemHelper.get().filterValues(ElixirItem.class::isInstance).stream()
                                .filter(JavaHelper.not(CustomElixirItem.class::isInstance)).map(ItemStack::new).toList());
                    })
    );

    HTHolder<CreativeModeTab> SECRET_MANUALS = register("secret_manuals", builder ->
            builder.icon(() -> new ItemStack((IMMItems.SECRET_MANUAL.get())))
                    .withTabsBefore(ELIXIRS.getRegistryName())
                    .displayItems((parameters, output) -> {
                        HolderLookup.RegistryLookup<SecretManual> secretManuals = parameters.holders().lookupOrThrow(SecretManuals.registry().getRegistryKey());
                        secretManuals.listElementIds().forEach(r -> {
                            output.accept(SecretManualItem.create(r));
                        });
                    })
    );

    HTHolder<CreativeModeTab> ARTIFACTS = register("artifacts", builder ->
            builder.icon(() -> new ItemStack((IMMItems.SPIRITUAL_PEARL.get())))
                    .withTabsBefore(SECRET_MANUALS.getRegistryName())
                    .displayItems((parameters, output) -> {
                        ItemHelper.get().filterValues(item -> {
                            return RealmManager.notCommon(RealmManager.getRank(new ItemStack(item))) && item != IMMBlocks.RUNE_WORK_BENCH.get().asItem();
                        }).stream().map(ItemStack::new)
                                .sorted(Comparator.comparingInt(l -> RealmManager.getRank(l).ordinal()))
                                .forEach(output::accept);
                    })
    );

//    HTHolder<CreativeModeTab> RUNES = register("runes", builder ->
//            builder.icon(() -> new ItemStack((IMMItems.ITEM_FILTER_RUNE.get())))
//                    .withTabsBefore(ARTIFACTS.getRegistryName())
//                    .displayItems((parameters, output) -> {
//                        ItemHelper.get().filterValues(RuneItem.class::isInstance).forEach(item -> {
//                            output.accept(new ItemStack(item));
//                        });
//                    })
//    );

    static void fillCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.BUILDING_BLOCKS)) {

        } else if (event.getTabKey().equals(CreativeModeTabs.COLORED_BLOCKS)) {
            BlockUtil.getWoolCushions().forEach(pair -> event.accept(new ItemStack(pair.getSecond())));
        } else if (event.getTabKey().equals(CreativeModeTabs.NATURAL_BLOCKS)) {
//            BlockUtil.getGourds().forEach(pair -> event.accept(new ItemStack(pair.getSecond())));
//            Arrays.asList(
//                    IMMBlocks.MULBERRY_LEAVES, IMMBlocks.MULBERRY_LEAVES_WITH_MULBERRIES, IMMBlocks.MULBERRY_SAPLING
//            ).forEach(obj -> {
//                event.accept(obj.get());
//            });
        } else if (event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            BlockUtil.getWoolCushions().forEach(pair -> event.accept(new ItemStack(pair.getSecond())));
        } else if (event.getTabKey().equals(CreativeModeTabs.FOOD_AND_DRINKS)) {
            getFoodItems().forEach(event::accept);
        } else if (event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)) {
            getBannerPatterns().forEach(event::accept);
        }
    }

    private static List<Item> getFoodItems(){
        return Util.get().filterValues(ItemHelper.get(), stack -> stack.getDefaultInstance().getFoodProperties(null) != null);
    }

    private static List<Item> getBannerPatterns(){
        return ItemUtil.getBannerPatterns().stream().map(Map.Entry::getValue).toList();
    }

    static void initialize(IEventBus modBus) {
        NeoHelper.initRegistry(TABS, modBus);
    }

    private static HTHolder<CreativeModeTab> register(String name, Consumer<CreativeModeTab.Builder> consumer) {
        return TABS.register(name, () -> {
            final CreativeModeTab.Builder builder = CreativeModeTab.builder().title(Component.translatable(StringHelper.langKey("itemGroup", Util.id(), name)));
            consumer.accept(builder);
            return builder.build();
        });
    }

}
