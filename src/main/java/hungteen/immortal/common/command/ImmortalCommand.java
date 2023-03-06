package hungteen.immortal.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.ISpellType;
import hungteen.immortal.api.registry.ISpiritualType;
import hungteen.immortal.common.spell.SpellManager;
import hungteen.immortal.common.world.ImmortalTeleporters;
import hungteen.immortal.common.world.dimension.ImmortalDimensions;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-25 16:38
 **/
public class ImmortalCommand {

    private static final String COMMAND_SPIRITUAL_ROOT = "command.immortal.spiritual_root";
    private static final Component COMMAND_LEARN_ALL_SPELLS = Component.translatable("command.immortal.learn_all_spells");
    private static final Component COMMAND_FORGET_ALL_SPELLS = Component.translatable("command.immortal.forget_all_spells");

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("immortal").requires((ctx) -> ctx.hasPermission(2));
        if(ImmortalAPI.get().spiritualRegistry().isPresent()){// about spiritual roots.
            ImmortalAPI.get().spiritualRegistry().get().getValues().forEach(root -> {
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
        if(ImmortalAPI.get().spellRegistry().isPresent()){// about spells.
            ImmortalAPI.get().spellRegistry().get().getValues().forEach(spell -> {
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
            builder.then(Commands.literal("spell")
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.literal("learn")
                                    .then(Commands.literal("all")
                                            .then(Commands.argument("level", IntegerArgumentType.integer())
                                                    .executes(command -> learnAllSpell(command.getSource(), EntityArgument.getPlayers(command, "targets"), IntegerArgumentType.getInteger(command, "level")))
                                            )))
                            .then(Commands.literal("forget")
                                    .then(Commands.literal("all")
                                            .executes(command -> forgetAllSpell(command.getSource(), EntityArgument.getPlayers(command, "targets")))
                                    ))
                    ));
        }
        if(ImmortalAPI.get().integerDataRegistry().isPresent()){// about other data.
            ImmortalAPI.get().integerDataRegistry().get().getValues().forEach(data -> {
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

    /* Spiritual Roots */

    private static int resetSpiritualRoot(CommandSourceStack source, Collection<? extends ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            PlayerUtil.resetSpiritualRoots(player);
            showPlayerSpiritualRoots(source, player, true);
        }
        return targets.size();
    }

    private static int querySpiritualRoot(CommandSourceStack source, Collection<? extends ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            showPlayerSpiritualRoots(source, player, false);
        }
        return targets.size();
    }

    private static int addSpiritualRoot(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ISpiritualType root) {
        for (ServerPlayer player : targets) {
            PlayerUtil.getOptManager(player).ifPresent(l -> l.addSpiritualRoot(root));
            showPlayerSpiritualRoots(source, player, true);
        }
        return targets.size();
    }

    private static int removeSpiritualRoot(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ISpiritualType root) {
        for (ServerPlayer player : targets) {
            PlayerUtil.getOptManager(player).ifPresent(l -> l.removeSpiritualRoot(root));
            showPlayerSpiritualRoots(source, player, true);
        }
        return targets.size();
    }

    /**
     * @param spread tell the target player or not.
     */
    private static void showPlayerSpiritualRoots(CommandSourceStack source, Player player, boolean spread){
        PlayerUtil.getOptManager(player).ifPresent(l -> {
            final MutableComponent component = Component.translatable(COMMAND_SPIRITUAL_ROOT, player.getName().getString());
            final List<ISpiritualType> list = l.getSpiritualRoots();
            for(int i = 0; i < list.size(); ++ i){
                if(i > 0) component.append(Component.literal(","));
                component.append(list.get(i).getComponent());
            }
            if(spread) PlayerHelper.sendMsgTo(player, component);
            source.sendSuccess(component, true);
        });
    }

    /* Spells */

    private static int learnSpell(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ISpellType spell, int level) {
        for (ServerPlayer player : targets) {
            PlayerUtil.learnSpell(player, spell, level);
            PlayerHelper.sendMsgTo(player, spell.getComponent());
        }
        source.sendSuccess(spell.getComponent(), true);
        return targets.size();
    }

    private static int forgetSpell(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ISpellType spell) {
        for (ServerPlayer player : targets) {
            PlayerUtil.forgetSpell(player, spell);
            PlayerHelper.sendMsgTo(player, spell.getComponent());
        }
        source.sendSuccess(spell.getComponent(), true);
        return targets.size();
    }

    private static int activateSpell(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ISpellType spell) {
        for (ServerPlayer player : targets) {
            SpellManager.checkActivateSpell(player, spell);
            PlayerHelper.sendMsgTo(player, spell.getComponent());
        }
        source.sendSuccess(spell.getComponent(), true);
        return targets.size();
    }

    private static int setSpellAt(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ISpellType spell, int pos) {
        for (ServerPlayer player : targets) {
            PlayerUtil.setSpellList(player, pos, spell);
            PlayerHelper.sendMsgTo(player, spell.getComponent());
        }
        source.sendSuccess(spell.getComponent(), true);
        return targets.size();
    }

    private static int learnAllSpell(CommandSourceStack source, Collection<? extends ServerPlayer> targets, int level) {
        for (ServerPlayer player : targets) {
            PlayerUtil.learnAllSpell(player, level);
            PlayerHelper.sendMsgTo(player, COMMAND_LEARN_ALL_SPELLS);
        }
        source.sendSuccess(COMMAND_LEARN_ALL_SPELLS, true);
        return targets.size();
    }

    private static int forgetAllSpell(CommandSourceStack source, Collection<? extends ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            PlayerUtil.forgetAllSpell(player);
            PlayerHelper.sendMsgTo(player, COMMAND_FORGET_ALL_SPELLS);
        }
        source.sendSuccess(COMMAND_FORGET_ALL_SPELLS, true);
        return targets.size();
    }

    /* Integers */

    private static int setIntegerData(CommandSourceStack source, Collection<? extends ServerPlayer> targets, IRangeNumber<Integer> data, int value) {
        for (ServerPlayer player : targets) {
            PlayerUtil.setIntegerData(player, data, value);
            PlayerHelper.sendMsgTo(player, getIntegerComponent(data, value));
            source.sendSuccess(getIntegerComponent(player, data, value), true);
        }
        return targets.size();
    }

    private static int addIntegerData(CommandSourceStack source, Collection<? extends ServerPlayer> targets, IRangeNumber<Integer> data, int value) {
        for (ServerPlayer player : targets) {
            PlayerUtil.addIntegerData(player, data, value);
            PlayerHelper.sendMsgTo(player, getIntegerComponent(data, PlayerUtil.getIntegerData(player, data)));
            source.sendSuccess(getIntegerComponent(player, data, PlayerUtil.getIntegerData(player, data)), true);
        }
        return targets.size();
    }

    private static int showIntegerData(CommandSourceStack source, Collection<? extends ServerPlayer> targets, IRangeNumber<Integer> data) {
        for (ServerPlayer player : targets) {
            source.sendSuccess(getIntegerComponent(player, data, PlayerUtil.getIntegerData(player, data)), true);
        }
        return targets.size();
    }

    private static Component getIntegerComponent(Player player, IRangeNumber<Integer> data, int value){
        return Component.literal(player.getName().getString() + " -> " + data.getComponent().getString() + " : " + value);
    }

    private static Component getIntegerComponent(IRangeNumber<Integer> data, int value){
        return Component.literal(data.getComponent().getString() + " : " + value);
    }

}
