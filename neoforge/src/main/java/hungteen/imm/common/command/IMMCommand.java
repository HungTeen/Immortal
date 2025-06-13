package hungteen.imm.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import hungteen.htlib.api.registry.SimpleEntry;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.cultivation.*;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.common.cultivation.*;
import hungteen.imm.common.capability.player.IMMPlayerData;
import hungteen.imm.common.world.levelgen.IMMLevels;
import hungteen.imm.common.world.levelgen.spiritworld.SpiritWorldDimension;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-09-25 16:38
 **/
public class IMMCommand {

    private static final DynamicCommandExceptionType ERROR_INVALID_LEVEL = new DynamicCommandExceptionType(
            o -> Component.translatableEscape("commands.place.feature.invalid", o)
    );
    private static final Component COMMAND_LEARN_ALL_SPELLS = TipUtil.command("learn_all_spells");
    private static final Component COMMAND_FORGET_ALL_SPELLS = TipUtil.command("forget_all_spells");
    private static final Component COMMAND_CULTIVATION_NOT_ENOUGH = TipUtil.command("cultivation_not_enough");

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("imm").requires((ctx) -> ctx.hasPermission(2));
        // Command about spiritual roots.
        QiRootTypes.registry().getValues().forEach(root -> {
            builder.then(Commands.literal("root")
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.literal("add")
                                    .then(Commands.literal(root.getRegistryName())
                                            .executes(command -> addRoot(command.getSource(), EntityArgument.getPlayers(command, "targets"), root))
                                    ))
                            .then(Commands.literal("remove")
                                    .then(Commands.literal(root.getRegistryName())
                                            .executes(command -> removeRoot(command.getSource(), EntityArgument.getPlayers(command, "targets"), root))
                                    ))
                    ));
        });
        builder.then(Commands.literal("root")
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.literal("reset")
                                .executes(command -> resetRoot(command.getSource(), EntityArgument.getPlayers(command, "targets")))
                        )
                        .then(Commands.literal("query")
                                .executes(command -> queryRoot(command.getSource(), EntityArgument.getPlayers(command, "targets")))
                        )
                        .then(Commands.literal("clear")
                                .executes(command -> clearRoot(command.getSource(), EntityArgument.getPlayers(command, "targets")))
                        )
                ));

        // Command about spells.
        SpellTypes.registry().getValues().forEach(spell -> {
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
                            .then(Commands.literal("select")
                                    .then(Commands.literal(spell.getRegistryName())
                                            .executes(command -> selectSpell(command.getSource(), EntityArgument.getPlayers(command, "targets"), spell))
                                    ).then(Commands.literal("nothing")
                                            .executes(command -> selectSpell(command.getSource(), EntityArgument.getPlayers(command, "targets"), null))
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

        // Command about cultivation.
        for (ExperienceType experienceType : ExperienceType.values()) {
            builder.then(Commands.literal("experience")
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.literal("set")
                                    .then(Commands.literal(experienceType.toString().toLowerCase())
                                            .then(Commands.argument("value", FloatArgumentType.floatArg(0))
                                                    .executes(command -> setExperience(command.getSource(), EntityArgument.getPlayers(command, "targets"), experienceType, FloatArgumentType.getFloat(command, "value")))
                                            )))
                            .then(Commands.literal("add")
                                    .then(Commands.literal(experienceType.toString().toLowerCase())
                                            .then(Commands.argument("value", FloatArgumentType.floatArg())
                                                    .executes(command -> addExperience(command.getSource(), EntityArgument.getPlayers(command, "targets"), experienceType, FloatArgumentType.getFloat(command, "value")))
                                            )))
                            .then(Commands.literal("show")
                                    .then(Commands.literal(experienceType.toString().toLowerCase())
                                            .executes(command -> showExperience(command.getSource(), EntityArgument.getPlayers(command, "targets"), experienceType))
                                    ))
                    ));
        }

        // Command about realm.
        RealmTypes.registry().getValues().forEach(realm -> {
            builder.then(Commands.literal("realm")
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.literal("set")
                                    .then(Commands.literal(realm.getRegistryName())
                                            .executes(command -> setRealm(command.getSource(), EntityArgument.getPlayers(command, "targets"), realm, false))
                                            .then(Commands.literal("force")
                                                    .executes(command -> setRealm(command.getSource(), EntityArgument.getPlayers(command, "targets"), realm, true))
                                            )))
                    )
                    .then(Commands.literal("show")
                            .executes(command -> showRealm(command.getSource(), EntityArgument.getPlayers(command, "targets")))
                    )
            );
        });

        // Command about player number data.
        for (IMMPlayerData.IntegerData data : IMMPlayerData.IntegerData.values()) {
            builder.then(Commands.literal("data")
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.literal("set")
                                    .then(Commands.literal(data.getRegistryName())
                                            .then(Commands.argument("value", IntegerArgumentType.integer())
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
        }
        for (IMMPlayerData.FloatData data : IMMPlayerData.FloatData.values()) {
            builder.then(Commands.literal("data")
                    .then(Commands.argument("targets", EntityArgument.players())
                            .then(Commands.literal("set")
                                    .then(Commands.literal(data.getRegistryName())
                                            .then(Commands.argument("value", FloatArgumentType.floatArg())
                                                    .executes(command -> setFloatData(command.getSource(), EntityArgument.getPlayers(command, "targets"), data, FloatArgumentType.getFloat(command, "value")))
                                            )))
                            .then(Commands.literal("add")
                                    .then(Commands.literal(data.getRegistryName())
                                            .then(Commands.argument("value", FloatArgumentType.floatArg())
                                                    .executes(command -> addFloatData(command.getSource(), EntityArgument.getPlayers(command, "targets"), data, FloatArgumentType.getFloat(command, "value")))
                                            )))
                            .then(Commands.literal("show")
                                    .then(Commands.literal(data.getRegistryName())
                                            .executes(command -> showFloatData(command.getSource(), EntityArgument.getPlayers(command, "targets"), data))
                                    ))
                    ));
        }

        // Command about element reactions.
        Arrays.stream(Element.values()).forEach(element -> {
            builder.then(Commands.literal("element")
                    .then(Commands.literal("add")
                            .then(Commands.argument("targets", EntityArgument.entities())
                                    .then(Commands.literal(element.name().toLowerCase())
                                            .then(Commands.argument("robust", BoolArgumentType.bool())
                                                    .then(Commands.argument("value", FloatArgumentType.floatArg())
                                                            .executes(command -> addElementAmount(command.getSource(), EntityArgument.getEntities(command, "targets"), element, BoolArgumentType.getBool(command, "robust"), FloatArgumentType.getFloat(command, "value")))
                                                    ))))
                    )
            );
        });

        // Misc Commands.
        builder.then(Commands.literal("tp")
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("dimension", ResourceKeyArgument.key(Registries.DIMENSION))
                                .executes(command -> tp(command.getSource(), EntityArgument.getPlayers(command, "targets"), ResourceKeyArgument.getRegistryKey(command, "dimension", Registries.DIMENSION, ERROR_INVALID_LEVEL)))
                        )
                )
        );
        dispatcher.register(builder);
    }

    private static int tp(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ResourceKey<Level> resourceKey) {
        for (ServerPlayer player : targets) {
            if (resourceKey.equals(IMMLevels.SPIRIT_WORLD)) {
                SpiritWorldDimension.teleportToSpiritRegion((source.getLevel()), player);
            } else {
                ServerLevel serverlevel = source.getLevel().getServer().getLevel(resourceKey);
                if (serverlevel != null) {
                    player.changeDimension(new DimensionTransition(serverlevel, player, DimensionTransition.PLAY_PORTAL_SOUND));
                }
            }
        }
        return targets.size();
    }

    /* Spiritual Roots */

    private static int resetRoot(CommandSourceStack source, Collection<? extends ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            PlayerUtil.resetRoots(player);
            showPlayerRoots(source, player, true);
        }
        return targets.size();
    }

    private static int clearRoot(CommandSourceStack source, Collection<? extends ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            PlayerUtil.setRoots(player, List.of());
        }
        return targets.size();
    }

    private static int queryRoot(CommandSourceStack source, Collection<? extends ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            showPlayerRoots(source, player, false);
        }
        return targets.size();
    }

    private static int addRoot(CommandSourceStack source, Collection<? extends ServerPlayer> targets, QiRootType root) {
        for (ServerPlayer player : targets) {
            PlayerUtil.setData(player, l -> l.getCultivationData().addRoot(root));
            showPlayerRoots(source, player, true);
        }
        return targets.size();
    }

    private static int removeRoot(CommandSourceStack source, Collection<? extends ServerPlayer> targets, QiRootType root) {
        for (ServerPlayer player : targets) {
            PlayerUtil.setData(player, l -> l.getCultivationData().removeRoot(root));
            showPlayerRoots(source, player, true);
        }
        return targets.size();
    }

    /**
     * @param spread tell the target player or not.
     */
    private static void showPlayerRoots(CommandSourceStack source, Player player, boolean spread) {
        PlayerUtil.setData(player, l -> {
            final MutableComponent component = TipUtil.command("qi_root", player.getName().getString());
            component.append(QiRootTypes.getRoots(l.getCultivationData().getRoots()));
            if (spread) {
                PlayerHelper.sendMsgTo(player, component);
            }
            source.sendSuccess(() -> component, true);
        });
    }

    /* Spells */

    private static int learnSpell(CommandSourceStack source, Collection<? extends ServerPlayer> targets, SpellType spell, int level) {
        for (ServerPlayer player : targets) {
            PlayerUtil.learnSpell(player, spell, level);
            PlayerHelper.sendMsgTo(player, spell.getComponent());
        }
        source.sendSuccess(spell::getComponent, true);
        return targets.size();
    }

    private static int forgetSpell(CommandSourceStack source, Collection<? extends ServerPlayer> targets, SpellType spell) {
        for (ServerPlayer player : targets) {
            PlayerUtil.forgetSpell(player, spell);
            PlayerHelper.sendMsgTo(player, spell.getComponent());
        }
        source.sendSuccess(spell::getComponent, true);
        return targets.size();
    }

    private static int selectSpell(CommandSourceStack source, Collection<? extends ServerPlayer> targets, @Nullable SpellType spell) {
        for (ServerPlayer player : targets) {
            SpellManager.selectSpellOnCircle(player, spell);
            if (spell != null) {
                PlayerHelper.sendMsgTo(player, spell.getComponent());
            }
        }
        if (spell != null) {
            source.sendSuccess(spell::getComponent, true);
        }
        return targets.size();
    }

    private static int setSpellAt(CommandSourceStack source, Collection<? extends ServerPlayer> targets, SpellType spell, int pos) {
        for (ServerPlayer player : targets) {
            PlayerUtil.setSpellAt(player, pos, spell);
            PlayerHelper.sendMsgTo(player, spell.getComponent());
        }
        source.sendSuccess(spell::getComponent, true);
        return targets.size();
    }

    private static int learnAllSpell(CommandSourceStack source, Collection<? extends ServerPlayer> targets, int level) {
        for (ServerPlayer player : targets) {
            SpellTypes.registry().getValues().forEach(spell -> {
                PlayerUtil.learnSpell(player, spell, level);
            });
            PlayerHelper.sendMsgTo(player, COMMAND_LEARN_ALL_SPELLS);
        }
        source.sendSuccess(() -> COMMAND_LEARN_ALL_SPELLS, true);
        return targets.size();
    }

    private static int forgetAllSpell(CommandSourceStack source, Collection<? extends ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            SpellTypes.registry().getValues().forEach(spell -> {
                PlayerUtil.forgetSpell(player, spell);
            });
            PlayerHelper.sendMsgTo(player, COMMAND_FORGET_ALL_SPELLS);
        }
        source.sendSuccess(() -> COMMAND_FORGET_ALL_SPELLS, true);
        return targets.size();
    }

    /* Cultivation */

    private static int setExperience(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ExperienceType type, float value) {
        for (ServerPlayer player : targets) {
            PlayerUtil.setExperience(player, type, value);
            final Component info = getExperienceComponent(type, value);
            PlayerHelper.sendMsgTo(player, info);
            source.sendSuccess(() -> getPlayerInfo(player, info), true);
        }
        return targets.size();
    }

    private static int addExperience(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ExperienceType type, float value) {
        for (ServerPlayer player : targets) {
            PlayerUtil.addExperience(player, type, value);
            final Component info = getExperienceComponent(type, PlayerUtil.getExperience(player, type));
            PlayerHelper.sendMsgTo(player, info);
            source.sendSuccess(() -> getPlayerInfo(player, info), true);
        }
        return targets.size();
    }

    private static int showExperience(CommandSourceStack source, Collection<? extends ServerPlayer> targets, ExperienceType type) {
        for (ServerPlayer player : targets) {
            source.sendSuccess(() -> getPlayerInfo(player, getExperienceComponent(type, PlayerUtil.getExperience(player, type))), true);
        }
        return targets.size();
    }

    /* Realm */

    private static int setRealm(CommandSourceStack source, Collection<? extends ServerPlayer> targets, RealmType realm, boolean force) {
        for (ServerPlayer player : targets) {
            final boolean result = RealmManager.checkAndSetRealm(player, realm, force);
            final Component info = result ?realm.getComponent() : COMMAND_CULTIVATION_NOT_ENOUGH;
            PlayerHelper.sendMsgTo(player, info);
            source.sendSuccess(() -> getPlayerInfo(player, info), true);
        }
        return targets.size();
    }

    private static int showRealm(CommandSourceStack source, Collection<? extends ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            final RealmType realm = PlayerUtil.getPlayerRealm(player);
            source.sendSuccess(() -> getPlayerInfo(player, realm.getComponent()), true);
        }
        return targets.size();
    }

    /* Numbers */

    private static int setIntegerData(CommandSourceStack source, Collection<? extends ServerPlayer> targets, IMMPlayerData.IntegerData data, int value) {
        for (ServerPlayer player : targets) {
            PlayerUtil.setIntegerData(player, data, value);
            PlayerHelper.sendMsgTo(player, getNumberComponent(data, value));
            source.sendSuccess(() -> getNumberComponent(player, data, value), true);
        }
        return targets.size();
    }

    private static int addIntegerData(CommandSourceStack source, Collection<? extends ServerPlayer> targets, IMMPlayerData.IntegerData data, int value) {
        for (ServerPlayer player : targets) {
            PlayerUtil.addIntegerData(player, data, value);
            PlayerHelper.sendMsgTo(player, getNumberComponent(data, PlayerUtil.getIntegerData(player, data)));
            source.sendSuccess(() -> getNumberComponent(player, data, PlayerUtil.getIntegerData(player, data)), true);
        }
        return targets.size();
    }

    private static int showIntegerData(CommandSourceStack source, Collection<? extends ServerPlayer> targets, IMMPlayerData.IntegerData data) {
        for (ServerPlayer player : targets) {
            source.sendSuccess(() -> getNumberComponent(player, data, PlayerUtil.getIntegerData(player, data)), true);
        }
        return targets.size();
    }

    private static int setFloatData(CommandSourceStack source, Collection<? extends ServerPlayer> targets, IMMPlayerData.FloatData data, float value) {
        for (ServerPlayer player : targets) {
            PlayerUtil.setFloatData(player, data, value);
            PlayerHelper.sendMsgTo(player, getNumberComponent(data, value));
            source.sendSuccess(() -> getNumberComponent(player, data, value), true);
        }
        return targets.size();
    }

    private static int addFloatData(CommandSourceStack source, Collection<? extends ServerPlayer> targets, IMMPlayerData.FloatData data, float value) {
        for (ServerPlayer player : targets) {
            PlayerUtil.addFloatData(player, data, value);
            PlayerHelper.sendMsgTo(player, getNumberComponent(data, PlayerUtil.getFloatData(player, data)));
            source.sendSuccess(() -> getNumberComponent(player, data, PlayerUtil.getFloatData(player, data)), true);
        }
        return targets.size();
    }

    private static int showFloatData(CommandSourceStack source, Collection<? extends ServerPlayer> targets, IMMPlayerData.FloatData data) {
        for (ServerPlayer player : targets) {
            source.sendSuccess(() -> getNumberComponent(player, data, PlayerUtil.getFloatData(player, data)), true);
        }
        return targets.size();
    }

    /* Element Reaction */

    private static int addElementAmount(CommandSourceStack source, Collection<? extends Entity> targets, Element element, boolean robust, float amount) {
        targets.forEach(target -> {
            ElementManager.addElementAmount(target, element, robust, amount);
        });
        return targets.size();
    }

    /* Misc Methods */

    private static Component getPlayerInfo(Player player, Component component) {
        return Component.literal(player.getName().getString() + " -> ").append(component);
    }

    private static <T extends Number> Component getNumberComponent(Player player, SimpleEntry data, T value) {
        return getPlayerInfo(player, getNumberComponent(data, value));
    }

    private static <T extends Number> Component getNumberComponent(SimpleEntry data, T value) {
        return Component.literal(data.getComponent().getString() + " : " + value);
    }

    private static Component getExperienceComponent(ExperienceType type, float value) {
        return CultivationManager.getExperienceComponent().append(" - ").append(CultivationManager.getExperienceComponent(type).append(" : " + value));
    }

}
