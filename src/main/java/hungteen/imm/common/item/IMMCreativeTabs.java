package hungteen.imm.common.item;

import hungteen.htlib.util.helper.JavaHelper;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.api.interfaces.IArtifactBlock;
import hungteen.imm.api.interfaces.IArtifactItem;
import hungteen.imm.common.ArtifactManager;
import hungteen.imm.common.block.CushionBlock;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.impl.manuals.SecretManual;
import hungteen.imm.common.impl.manuals.SecretManuals;
import hungteen.imm.common.item.elixirs.CustomElixirItem;
import hungteen.imm.common.item.elixirs.ElixirItem;
import hungteen.imm.common.item.runes.RuneItem;
import hungteen.imm.util.BlockUtil;
import hungteen.imm.util.ItemUtil;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-03 22:45
 **/
public interface IMMCreativeTabs {

    DeferredRegister<CreativeModeTab> TABS = ItemHelper.tab().createRegister(Util.id());

    RegistryObject<CreativeModeTab> MATERIALS = register("materials", builder ->
            builder.icon(() -> new ItemStack((IMMItems.GOURD_SEEDS.get())))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .displayItems((parameters, output) -> {
                        final List<ItemLike> items = new ArrayList<>();
                        // 种子之类的杂项。
                        items.addAll(List.of(
                                IMMItems.RICE_SEEDS.get(), IMMItems.RICE_STRAW.get(),
                                IMMItems.JUTE_SEEDS.get(), IMMItems.JUTE.get(),
                                IMMItems.GOURD_SEEDS.get(), IMMBlocks.GANODERMA.get()
                        ));
                        items.addAll(getBannerPatterns());
                        // 葫芦。
                        BlockUtil.getGourds().forEach(pair -> items.add(pair.getSecond()));
                        // 装饰方块。
                        items.addAll(List.of(
                                IMMBlocks.MULBERRY_LEAVES.get(), IMMBlocks.MULBERRY_LEAVES_WITH_MULBERRIES.get(), IMMBlocks.MULBERRY_SAPLING.get()
                        ));
                        items.forEach(output::accept);
                        // 防止有被遗漏的物品。
                        final Set<Item> itemSet = items.stream().map(ItemLike::asItem).collect(Collectors.toSet());
                        Util.get().filterValues(ItemHelper.get(), item -> {
                            if(itemSet.contains(item)) return false; // 已经被添加，不再考虑。
                            if(item instanceof ElixirItem) return false; // 排除丹药。
                            if(item instanceof SecretManualItem) return false; // 排除秘籍。
                            if(item instanceof IArtifactItem) return false; // 排除物品法器。
                            if(item instanceof BlockItem blockItem) {
                                if (blockItem.getBlock() instanceof IArtifactBlock) return false; // 排除方块法器。
                                if(blockItem.getBlock() instanceof CushionBlock) return false; // 排除坐垫。
                            }
                            if(item instanceof RuneItem) return false;
                            return true;
                        }).forEach(output::accept);
                    })
    );

    RegistryObject<CreativeModeTab> ELIXIRS = register("elixirs", builder ->
            builder.icon(() -> new ItemStack((IMMItems.FIVE_FLOWERS_ELIXIR.get())))
                    .withTabsBefore(MATERIALS.getKey())
                    .displayItems((parameters, output) -> {
                        output.acceptAll(ItemHelper.get().filterValues(ElixirItem.class::isInstance).stream()
                                .filter(JavaHelper.not(CustomElixirItem.class::isInstance)).map(ItemStack::new).toList());
                    })
    );

    RegistryObject<CreativeModeTab> SECRET_MANUALS = register("secret_manuals", builder ->
            builder.icon(() -> new ItemStack((IMMItems.SECRET_MANUAL.get())))
                    .withTabsBefore(ELIXIRS.getKey())
                    .displayItems((parameters, output) -> {
                        HolderLookup.RegistryLookup<SecretManual> secretManuals = parameters.holders().lookupOrThrow(SecretManuals.registry().getRegistryKey());
                        secretManuals.listElementIds().forEach(r -> {
                            output.accept(SecretManualItem.create(r));
                        });
                    })
    );

    RegistryObject<CreativeModeTab> ARTIFACTS = register("artifacts", builder ->
            builder.icon(() -> new ItemStack((IMMItems.SPIRITUAL_PEARL.get())))
                    .withTabsBefore(SECRET_MANUALS.getKey())
                    .displayItems((parameters, output) -> {
                        ItemHelper.get().filterValues(item -> {
                            return ArtifactManager.notCommon(ArtifactManager.getArtifactType(new ItemStack(item)));
                        }).stream().map(ItemStack::new)
                                .sorted(Comparator.comparingInt(ArtifactManager::getRealmValue))
                                .forEach(output::accept);
                    })
    );

    RegistryObject<CreativeModeTab> RUNES = register("runes", builder ->
            builder.icon(() -> new ItemStack((IMMItems.ITEM_FILTER_RUNE.get())))
                    .withTabsBefore(ARTIFACTS.getKey())
                    .displayItems((parameters, output) -> {
                        ItemHelper.get().filterValues(RuneItem.class::isInstance).forEach(item -> {
                            output.accept(new ItemStack(item));
                        });
                    })
    );

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
//            event.acceptAll(List.of(
//                    new ItemStack(IMMBlocks.TELEPORT_ANCHOR.get()),
//                    new ItemStack(IMMBlocks.COPPER_FURNACE.get())
//            ));
        } else if (event.getTabKey().equals(CreativeModeTabs.FOOD_AND_DRINKS)) {
            getFoodItems().forEach(event::accept);
        } else if (event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)) {
            getBannerPatterns().forEach(event::accept);
        }
    }

    private static List<Item> getFoodItems(){
        return Util.get().filterValues(ItemHelper.get(), Item::isEdible);
    }

    private static List<Item> getBannerPatterns(){
        return ItemUtil.getBannerPatterns().stream().map(Map.Entry::getValue).toList();
    }

    static void register(IEventBus modBus) {
        TABS.register(modBus);
    }

    private static RegistryObject<CreativeModeTab> register(String name, Consumer<CreativeModeTab.Builder> consumer) {
        return TABS.register(name, () -> {
            final CreativeModeTab.Builder builder = CreativeModeTab.builder().title(Component.translatable(StringHelper.langKey("itemGroup", Util.id(), name)));
            consumer.accept(builder);
            return builder.build();
        });
    }

}
