package hungteen.immortal.common.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.ISpiritualRoot;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 16:38
 **/
public class ImmortalCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("immortal").requires((ctx) -> ctx.hasPermission(2));
        {// about spiritual roots.
            ImmortalAPI.get().getSpiritualRoots().forEach(root -> {
                builder.then(Commands.literal("root")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.literal("add")
                                        .then(Commands.literal(root.getName())
                                                .executes(command -> addSpiritualRoot(command.getSource(), EntityArgument.getPlayers(command, "targets"), root))
                                        ))
                                .then(Commands.literal("remove")
                                        .then(Commands.literal(root.getName())
                                                .executes(command -> removeSpiritualRoot(command.getSource(), EntityArgument.getPlayers(command, "targets"), root))
                                        ))
                        ));
            });

            builder.then(Commands.literal("root")
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.literal("reset")
                                    .executes(command -> resetSpiritualRoot(command.getSource(), EntityArgument.getPlayers(command, "targets")))
                            )
                            .then(Commands.literal("query")
                                    .executes(command -> querySpiritualRoot(command.getSource(), EntityArgument.getPlayers(command, "targets")))
                            )
                    ));
        }
        dispatcher.register(builder);
    }

    private static int resetSpiritualRoot(CommandSourceStack source, Collection<? extends ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            PlayerUtil.spawnSpiritualRoots(player);
        }
        return targets.size();
    }

    private static int querySpiritualRoot(CommandSourceStack source, Collection<? extends ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            PlayerUtil.showPlayerSpiritualRoots(player);
        }
        return targets.size();
    }

    private static int addSpiritualRoot(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ISpiritualRoot root) {
        for (ServerPlayer player : targets) {
            PlayerUtil.getOptManager(player).ifPresent(l -> {
                l.addSpiritualRoot(root);
                source.sendSuccess(new TextComponent(root.getName()), true);
            });
        }
        return targets.size();
    }

    private static int removeSpiritualRoot(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ISpiritualRoot root) {
        for (ServerPlayer player : targets) {
            PlayerUtil.getOptManager(player).ifPresent(l -> {
                l.removeSpiritualRoot(root);
                source.sendSuccess(new TextComponent(root.getName()), true);
            });
        }
        return targets.size();
    }

}
