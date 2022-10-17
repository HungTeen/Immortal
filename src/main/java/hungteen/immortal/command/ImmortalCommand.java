package hungteen.immortal.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import hungteen.htlib.interfaces.IRangeData;
import hungteen.immortal.SpellManager;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.ISpell;
import hungteen.immortal.api.interfaces.ISpiritualRoot;
import hungteen.immortal.utils.PlayerUtil;
import hungteen.immortal.world.ImmortalTeleporters;
import hungteen.immortal.world.dimension.ImmortalDimensions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.commands.EffectCommands;
import net.minecraft.server.commands.LocateCommand;
import net.minecraft.server.commands.SetBlockCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.dimension.DimensionType;

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
                                        .then(Commands.literal(root.getRegistryName())
                                                .executes(command -> addSpiritualRoot(command.getSource(), EntityArgument.getPlayers(command, "targets"), root))
                                        ))
                                .then(Commands.literal("remove")
                                        .then(Commands.literal(root.getRegistryName())
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
        {// about spells.
            ImmortalAPI.get().getSpells().forEach(spell -> {
                builder.then(Commands.literal("spell")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.literal("learn")
                                        .then(Commands.literal(spell.getRegistryName())
                                                .then(Commands.argument("level", IntegerArgumentType.integer())
                                                        .executes(command -> learnSpell(command.getSource(), EntityArgument.getPlayers(command, "targets"), spell, IntegerArgumentType.getInteger(command, "level")))
                                                )))
                                .then(Commands.literal("forget")
                                        .then(Commands.literal(spell.getRegistryName())
                                                .executes(command -> forgetSpell(command.getSource(), EntityArgument.getPlayers(command, "targets"), spell))
                                        ))
                                .then(Commands.literal("activate")
                                        .then(Commands.literal(spell.getRegistryName())
                                                .executes(command -> activateSpell(command.getSource(), EntityArgument.getPlayers(command, "targets"), spell))
                                        ))
                                .then(Commands.literal("set")
                                        .then(Commands.literal(spell.getRegistryName())
                                                .then(Commands.argument("pos", IntegerArgumentType.integer(0, 7))
                                                    .executes(command -> setSpellAt(command.getSource(), EntityArgument.getPlayers(command, "targets"), spell, IntegerArgumentType.getInteger(command, "pos")))
                                                )))
                        ));
            });
        }
        {// about spells.
            ImmortalAPI.get().getIntegerCollection().forEach(data -> {
                builder.then(Commands.literal("data")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.literal("set")
                                        .then(Commands.literal(data.getRegistryName())
                                                .then(Commands.argument("value", IntegerArgumentType.integer(data.getMinData(), data.getMaxData()))
                                                        .executes(command -> setIntegerData(command.getSource(), EntityArgument.getPlayers(command, "targets"), data, IntegerArgumentType.getInteger(command, "value")))
                                                )))
                                .then(Commands.literal("add")
                                        .then(Commands.literal(data.getRegistryName())
                                                .then(Commands.argument("value", IntegerArgumentType.integer())
                                                        .executes(command -> addIntegerData(command.getSource(), EntityArgument.getPlayers(command, "targets"), data, IntegerArgumentType.getInteger(command, "value")))
                                                )))
                                .then(Commands.literal("show")
                                        .then(Commands.literal(data.getRegistryName())
                                                .executes(command -> showIntegerData(command.getSource(), EntityArgument.getPlayers(command, "targets"), data))
                                        ))
                        ));
            });
        }
        {
            builder.then(Commands.literal("tp")
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.literal("spiritual_land")
                                    .executes(command -> tp(command.getSource(), EntityArgument.getPlayers(command, "targets"), ImmortalDimensions.SPIRITUAL_LAND_DIMENSION))
                            )
                            .then(Commands.literal("overworld")
                                    .executes(command -> tp(command.getSource(), EntityArgument.getPlayers(command, "targets"), Level.OVERWORLD))
                            )
                    )
            );
        }
        dispatcher.register(builder);
    }

    private static int tp(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ResourceKey<Level> resourceKey){
        for (ServerPlayer player : targets) {
            ServerLevel serverlevel = ((ServerLevel) player.level).getServer().getLevel(resourceKey);
            if (serverlevel != null) {
                player.changeDimension(serverlevel, ImmortalTeleporters.INSTANCE);
            }
        }
        return targets.size();
    }

    private static int resetSpiritualRoot(CommandSourceStack source, Collection<? extends ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            PlayerUtil.spawnSpiritualRoots(player);
            PlayerUtil.showPlayerSpiritualRoots(player);
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
                source.sendSuccess(root.getComponent(), true);
            });
        }
        return targets.size();
    }

    private static int removeSpiritualRoot(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ISpiritualRoot root) {
        for (ServerPlayer player : targets) {
            PlayerUtil.getOptManager(player).ifPresent(l -> {
                l.removeSpiritualRoot(root);
                source.sendSuccess(root.getComponent(), true);
            });
        }
        return targets.size();
    }

    private static int learnSpell(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ISpell spell, int level) {
        for (ServerPlayer player : targets) {
            PlayerUtil.learnSpell(player, spell, level);
            source.sendSuccess(spell.getComponent(), true);
        }
        return targets.size();
    }

    private static int forgetSpell(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ISpell spell) {
        for (ServerPlayer player : targets) {
            PlayerUtil.forgetSpell(player, spell);
            source.sendSuccess(spell.getComponent(), true);
        }
        return targets.size();
    }

    private static int activateSpell(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ISpell spell) {
        for (ServerPlayer player : targets) {
            PlayerUtil.activateSpell(player, spell, SpellManager.getSpellActivateTime(player, spell));
            source.sendSuccess(spell.getComponent(), true);
        }
        return targets.size();
    }

    private static int setSpellAt(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ISpell spell, int pos) {
        for (ServerPlayer player : targets) {
            PlayerUtil.setSpellList(player, pos, spell);
            source.sendSuccess(spell.getComponent(), true);
        }
        return targets.size();
    }

    private static int setIntegerData(CommandSourceStack source, Collection<? extends ServerPlayer> targets, IRangeData data, int value) {
        for (ServerPlayer player : targets) {
            PlayerUtil.setIntegerData(player, data, value);
            source.sendSuccess(data.getComponent(), true);
        }
        return targets.size();
    }

    private static int addIntegerData(CommandSourceStack source, Collection<? extends ServerPlayer> targets, IRangeData data, int value) {
        for (ServerPlayer player : targets) {
            PlayerUtil.addIntegerData(player, data, value);
            source.sendSuccess(data.getComponent(), true);
        }
        return targets.size();
    }

    private static int showIntegerData(CommandSourceStack source, Collection<? extends ServerPlayer> targets, IRangeData data) {
        for (ServerPlayer player : targets) {
            final int result = PlayerUtil.getIntegerData(player, data);
            source.sendSuccess(new TextComponent(data.getComponent().getString() + " : " + result), true);
        }
        return targets.size();
    }

}
