package hungteen.immortal.common.command;

import hungteen.immortal.ImmortalMod;
import net.minecraftforge.event.RegisterCommandsEvent;

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
