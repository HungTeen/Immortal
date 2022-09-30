package hungteen.immortal.command;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import hungteen.htlib.interfaces.IComponentEntry;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.utils.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.stream.Collectors;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 16:37
 **/
public class CommandHandler {

//    public static final SuggestionProvider<CommandSourceStack> ALL_ROOTS = SuggestionProviders.register(
//            Util.prefix("roots"), ((context, builder) -> {
//                return SharedSuggestionProvider.suggest(ImmortalAPI.get().getSpiritualRoots().stream().map(IComponentEntry::getName).collect(Collectors.toList()), builder);
//            }));
//
//    public static final SuggestionProvider<CommandSourceStack> ALL_SPELLS = SuggestionProviders.register(
//            Util.prefix("spells"), ((context, builder) -> {
//                return SharedSuggestionProvider.suggest(ImmortalAPI.get().getSpells().stream().map(IComponentEntry::getName).collect(Collectors.toList()), builder);
//            }));

    /**
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void init(RegisterCommandsEvent event) {
        ImmortalCommand.register(event.getDispatcher());
    }

}
