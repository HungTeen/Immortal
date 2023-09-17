package hungteen.imm.common.item;

import hungteen.htlib.util.helper.JavaHelper;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.htlib.util.helper.registry.BlockHelper;
import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.api.interfaces.IArtifactBlock;
import hungteen.imm.api.interfaces.IArtifactItem;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.impl.manuals.SecretManual;
import hungteen.imm.common.impl.manuals.SecretManuals;
import hungteen.imm.common.item.elixirs.CustomElixirItem;
import hungteen.imm.common.item.elixirs.ElixirItem;
import hungteen.imm.common.item.runes.RuneItem;
import hungteen.imm.util.BlockUtil;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-03 22:45
 **/
public class IMMCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> TABS = ItemHelper.tab().createRegister(Util.id());

    public static final RegistryObject<CreativeModeTab> MATERIALS = register("materials", builder ->
            builder.icon(() -> new ItemStack((IMMItems.GOURD_SEEDS.get())))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .displayItems((parameters, output) -> {
                        output.acceptAll(Stream.of(
                                IMMItems.RICE_SEEDS.get(), IMMItems.RICE_STRAW.get(),
                                IMMItems.JUTE_SEEDS.get(), IMMItems.JUTE.get(),
                                IMMItems.GOURD_SEEDS.get(), IMMBlocks.GANODERMA.get()
                        ).map(ItemStack::new).toList());
                    })
    );

    public static final RegistryObject<CreativeModeTab> ELIXIRS = register("elixirs", builder ->
            builder.icon(() -> new ItemStack((IMMItems.FIVE_FLOWERS_ELIXIR.get())))
                    .withTabsBefore(MATERIALS.getKey())
                    .displayItems((parameters, output) -> {
                        output.acceptAll(ItemHelper.get().filterValues(ElixirItem.class::isInstance).stream()
                                .filter(JavaHelper.not(CustomElixirItem.class::isInstance)).map(ItemStack::new).toList());
                    })
    );

    public static final RegistryObject<CreativeModeTab> SECRET_MANUALS = register("secret_manuals", builder ->
            builder.icon(() -> new ItemStack((IMMItems.SECRET_MANUAL.get())))
                    .withTabsBefore(ELIXIRS.getKey())
                    .displayItems((parameters, output) -> {
                        HolderLookup.RegistryLookup<SecretManual> secretManuals = parameters.holders().lookupOrThrow(SecretManuals.registry().getRegistryKey());
                        secretManuals.listElementIds().forEach(r -> {
                            output.accept(SecretManualItem.create(r));
                        });
                    })
    );

    public static final RegistryObject<CreativeModeTab> ARTIFACTS = register("artifacts", builder ->
            builder.icon(() -> new ItemStack((IMMItems.SPIRITUAL_PEARL.get())))
                    .withTabsBefore(SECRET_MANUALS.getKey())
                    .displayItems((parameters, output) -> {
                        ItemHelper.get().filterValues(IArtifactItem.class::isInstance).forEach(item -> {
                            output.accept(new ItemStack(item));
                        });
                        BlockHelper.get().filterValues(IArtifactBlock.class::isInstance).forEach(block -> {
                            output.accept(new ItemStack(block));
                        });
                    })
    );

    public static final RegistryObject<CreativeModeTab> RUNES = register("runes", builder ->
            builder.icon(() -> new ItemStack((IMMItems.ITEM_FILTER_RUNE.get())))
                    .withTabsBefore(ARTIFACTS.getKey())
                    .displayItems((parameters, output) -> {
                        ItemHelper.get().filterValues(RuneItem.class::isInstance).forEach(item -> {
                            output.accept(new ItemStack(item));
                        });
                    })
    );

    public static void fillCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.BUILDING_BLOCKS)) {

        } else if (event.getTabKey().equals(CreativeModeTabs.COLORED_BLOCKS)) {
            BlockUtil.getWoolCushions().forEach(pair -> {
                event.accept(new ItemStack(pair.getSecond()));
            });
        } else if (event.getTabKey().equals(CreativeModeTabs.NATURAL_BLOCKS)) {
            BlockUtil.getGourds().forEach(pair -> {
                event.accept(new ItemStack(pair.getSecond()));
            });
            Arrays.asList(
                    IMMBlocks.MULBERRY_LEAVES, IMMBlocks.MULBERRY_LEAVES_WITH_MULBERRIES, IMMBlocks.MULBERRY_SAPLING
            ).forEach(obj -> {
                event.accept(obj.get());
            });
        } else if (event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            BlockUtil.getWoolCushions().forEach(pair -> {
                event.accept(new ItemStack(pair.getSecond()));
            });
            event.acceptAll(List.of(
                    new ItemStack(IMMBlocks.TELEPORT_ANCHOR.get()),
                    new ItemStack(IMMBlocks.COPPER_FURNACE.get())
            ));
        } else if (event.getTabKey().equals(CreativeModeTabs.FOOD_AND_DRINKS)) {
            Arrays.asList(
                    IMMItems.MULBERRY
            ).forEach(obj -> {
                event.accept(obj.get());
            });
        } else if (event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)) {
            Arrays.asList(
                    IMMItems.CONTINUOUS_MOUNTAIN_PATTERN, IMMItems.FLOWING_CLOUD_PATTERN,
                    IMMItems.FOLDED_THUNDER_PATTERN, IMMItems.RHOMBUS_PATTERN
            ).forEach(obj -> {
                event.accept(obj.get());
            });
        }
    }

    public static void register(IEventBus modBus) {
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
