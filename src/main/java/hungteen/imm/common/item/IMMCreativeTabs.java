package hungteen.imm.common.item;

import hungteen.htlib.util.helper.StringHelper;
import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.api.interfaces.IArtifactItem;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.item.runes.RuneItem;
import hungteen.imm.util.BlockUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-03 22:45
 **/
public class IMMCreativeTabs {

    public static CreativeModeTab MATERIALS;
    public static CreativeModeTab ELIXIRS;
    public static CreativeModeTab SPELL_BOOKS;
    public static CreativeModeTab ARTIFACTS;
    public static CreativeModeTab RUNES;

    public static void register(CreativeModeTabEvent.Register event){
        MATERIALS = register(event, "materials", List.of(CreativeModeTabs.SPAWN_EGGS), List.of(), builder -> builder
                .icon(() -> new ItemStack((IMMItems.GOURD_SEEDS.get())))
                .displayItems((featureFlagSet, output, hasPermission) -> {
                    output.acceptAll(Arrays.asList(
                            IMMItems.RICE_SEEDS.get(), IMMItems.RICE_STRAW.get(),
                            IMMItems.JUTE_SEEDS.get(), IMMItems.JUTE.get(),
                            IMMItems.GOURD_SEEDS.get()
                    ).stream().map(ItemStack::new).toList());
                })
        );

//        ELIXIRS = register(event, "elixirs", List.of(MATERIALS), List.of(), builder -> builder
//                .icon(() -> new ItemStack((IMMItems.FIVE_FLOWERS_ELIXIR.get())))
//                .displayItems((featureFlagSet, output, hasPermission) -> {
//                })
//        );
//
//        SPELL_BOOKS = register(event, "books", List.of(ELIXIRS), List.of(), builder -> builder
//                .icon(() -> new ItemStack((IMMItems.SPELL_BOOK.get())))
//                .displayItems((featureFlagSet, output, hasPermission) -> {
////                    ItemHelper.get().filterValues(IArtifactItem.class::isInstance).forEach(item -> {
////                        output.accept(new ItemStack(item));
////                    });
//                })
//        );

        ARTIFACTS = register(event, "artifacts", List.of(MATERIALS), List.of(), builder -> builder
                .icon(() -> new ItemStack((IMMItems.SPIRITUAL_PEARL.get())))
                .displayItems((featureFlagSet, output, hasPermission) -> {
                    ItemHelper.get().filterValues(IArtifactItem.class::isInstance).forEach(item -> {
                        output.accept(new ItemStack(item));
                    });
                })
        );

        RUNES = register(event, "runes", List.of(ARTIFACTS), List.of(), builder -> builder
                .icon(() -> new ItemStack((IMMItems.ITEM_FILTER_RUNE.get())))
                .displayItems((featureFlagSet, output, hasPermission) -> {
                    ItemHelper.get().filterValues(RuneItem.class::isInstance).forEach(item -> {
                        output.accept(new ItemStack(item));
                    });
                })
        );
    }

    public static void fillCreativeTabs(CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == CreativeModeTabs.BUILDING_BLOCKS){

        } else if(event.getTab() == CreativeModeTabs.COLORED_BLOCKS){

        } else if(event.getTab() == CreativeModeTabs.NATURAL_BLOCKS){
            BlockUtil.getGourds().forEach(pair -> {
                event.accept(new ItemStack(pair.getSecond()));
            });
            Arrays.asList(
                    IMMBlocks.MULBERRY_LEAVES, IMMBlocks.MULBERRY_LEAVES_WITH_MULBERRIES, IMMBlocks.MULBERRY_SAPLING
            ).forEach(obj -> {
                event.accept(obj.get());
            });
        } else if(event.getTab() == CreativeModeTabs.FUNCTIONAL_BLOCKS){
            BlockUtil.getWoolCushions().forEach(pair -> {
                event.accept(new ItemStack(pair.getSecond()));
            });
            event.accept(new ItemStack(IMMBlocks.TELEPORT_ANCHOR.get()));
        } else if(event.getTab() == CreativeModeTabs.FOOD_AND_DRINKS){
            Arrays.asList(
                    IMMItems.MULBERRY
            ).forEach(obj -> {
                event.accept(obj.get());
            });
        } else if(event.getTab() == CreativeModeTabs.INGREDIENTS){
            Arrays.asList(
                    IMMItems.CONTINUOUS_MOUNTAIN_PATTERN, IMMItems.FLOWING_CLOUD_PATTERN,
                    IMMItems.FOLDED_THUNDER_PATTERN, IMMItems.RHOMBUS_PATTERN
            ).forEach(obj -> {
                event.accept(obj.get());
            });
        }
    }

    private static CreativeModeTab register(CreativeModeTabEvent.Register event, String name, List<Object> after, List<Object> before, Consumer<CreativeModeTab.Builder> consumer){
        return event.registerCreativeModeTab(Util.prefix(name), before, after, builder -> {
            builder.title(Component.translatable(StringHelper.langKey("itemGroup", Util.id(), name)));
            consumer.accept(builder);
        });
    }


}
