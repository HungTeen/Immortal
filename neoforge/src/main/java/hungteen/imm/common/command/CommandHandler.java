package hungteen.imm.common.command;

import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 16:37
 **/
public class CommandHandler {

//    public static final SuggestionProvider<CommandSourceStack> ALL_ROOTS = SuggestionProviders.initialize(
//            Util.prefix("roots"), ((context, builder) -> {
//                return SharedSuggestionProvider.suggest(ImmortalAPI.get().getSpiritualTypes().stream().map(IComponentEntry::getName).collect(Collectors.toList()), builder);
//            }));
//
//    public static final SuggestionProvider<CommandSourceStack> ALL_SPELLS = SuggestionProviders.initialize(
//            Util.prefix("entries"), ((context, builder) -> {
//                return SharedSuggestionProvider.suggest(ImmortalAPI.get().getSpells().stream().map(IComponentEntry::getName).collect(Collectors.toList()), builder);
//            }));

    public static void init(RegisterCommandsEvent event) {
        IMMCommand.register(event.getDispatcher());
    }

}
