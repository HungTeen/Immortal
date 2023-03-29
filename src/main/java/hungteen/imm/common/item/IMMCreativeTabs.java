package hungteen.imm.common.item;

import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.api.interfaces.IArtifactItem;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;

import java.util.Arrays;

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
        MATERIALS = event.registerCreativeModeTab(Util.prefix("materials"), builder -> builder
                .title(Component.translatable("itemGroup.immortal.materials"))
                .icon(() -> new ItemStack((IMMItems.GOURD_SEEDS.get())))
                .displayItems((featureFlagSet, output, hasPermission) -> {
                    output.acceptAll(Arrays.asList(
                            IMMItems.GOURD_SEEDS.get()
                    ).stream().map(ItemStack::new).toList());
                })
        );

        ELIXIRS = event.registerCreativeModeTab(Util.prefix("elixirs"), builder -> builder
                .title(Component.translatable("itemGroup.immortal.elixirs"))
                .icon(() -> new ItemStack((IMMItems.FIVE_FLOWERS_ELIXIR.get())))
                .displayItems((featureFlagSet, output, hasPermission) -> {
                })
        );

        SPELL_BOOKS = event.registerCreativeModeTab(Util.prefix("books"), builder -> builder
                .title(Component.translatable("itemGroup.immortal.books"))
                .icon(() -> new ItemStack((IMMItems.SPELL_BOOK.get())))
                .displayItems((featureFlagSet, output, hasPermission) -> {
                    ItemHelper.get().filterValues(IArtifactItem.class::isInstance).forEach(item -> {
                        output.accept(new ItemStack(item));
                    });
                })
        );

        ARTIFACTS = event.registerCreativeModeTab(Util.prefix("artifacts"), builder -> builder
                .title(Component.translatable("itemGroup.immortal.artifacts"))
                .icon(() -> new ItemStack((IMMItems.SPIRITUAL_PEARL.get())))
                .displayItems((featureFlagSet, output, hasPermission) -> {
                    ItemHelper.get().filterValues(IArtifactItem.class::isInstance).forEach(item -> {
                        output.accept(new ItemStack(item));
                    });
                })
        );

        RUNES = event.registerCreativeModeTab(Util.prefix("runes"), builder -> builder
                .title(Component.translatable("itemGroup.immortal.runes"))
                .icon(() -> new ItemStack((IMMItems.RUNE.get())))
                .displayItems((featureFlagSet, output, hasPermission) -> {
                    ItemHelper.get().filterValues(IArtifactItem.class::isInstance).forEach(item -> {
                        output.accept(new ItemStack(item));
                    });
                })
        );
    }


}
