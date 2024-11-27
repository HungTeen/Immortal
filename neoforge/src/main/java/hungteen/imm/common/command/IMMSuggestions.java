package hungteen.imm.common.command;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import hungteen.imm.util.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/2 13:14
 **/
public class IMMSuggestions {

    public static SuggestionProvider<CommandSourceStack> register(String name, SuggestionProvider<SharedSuggestionProvider> provider){
        return SuggestionProviders.register(Util.prefix(name), provider);
    }
}
