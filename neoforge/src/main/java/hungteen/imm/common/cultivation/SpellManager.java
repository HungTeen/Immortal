package hungteen.imm.common.cultivation;

import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.entity.SpellCaster;
import hungteen.imm.api.event.ActivateSpellEvent;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.cultivation.spell.SpellResult;
import hungteen.imm.common.network.client.ClientSpellPacket;
import hungteen.imm.common.network.server.ServerSpellPacket;
import hungteen.imm.util.Constants;
import hungteen.imm.util.EventUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-10-14 10:28
 **/
public class SpellManager {

    private static final MutableComponent FORGOT_SPELL = TipUtil.info("spell.forgot");
    public static final MutableComponent SPELL_ON_CD = TipUtil.info("spell.on_cooldown");
    private static final int FAIL_CD = 20;

//    public static void pressToActivateSpell(Player player) {
//        // 使用法术的判断。
//        if (EntityHelper.isEntityValid(player) && SpellManager.hasSpellSelected(player)) {
//            if (EntityHelper.isServer(player)) {
//                SpellManager.activateSpell(player);
//            }
//        }
//    }

    /**
     * Only on Client Side.
     *
     * @param position determines which spell will be selected.
     */
    public static void selectSpellOnCircle(int position) {
        NetworkHelper.sendToServer(new ServerSpellPacket(ServerSpellPacket.SpellOption.SELECT_SPELL, position));
    }

    /**
     * Only on Server Side.
     * {@link ClientSpellPacket}
     */
    public static void selectSpellOnCircle(Player player, int position) {
        if (isValidSpellPos(position)) {
            selectSpellOnCircle(player, PlayerUtil.getSpellAt(player, position));
        } else {
            selectSpellOnCircle(player, null);
        }
    }

    /**
     * Use this method to select spell.
     */
    public static void selectSpellOnCircle(Player player, @Nullable SpellType spell) {
        PlayerUtil.setPreparingSpell(player, spell);
    }

    /**
     * 有些被选择的法术可以被动释放法术，此处进行通用检查。
     */
    public static void activateSpell(LivingEntity entity, @NotNull SpellType spell, ISpellTrigger trigger) {
        final Optional<Spell> instance = canActivateSpell(entity, spell, false);
        if (instance.isPresent()) {
            final HTHitResult result = createHitResult(entity, instance.get());
            final SpellResult spellResult = trigger.checkActivate(entity, result, instance.get().spell(), instance.get().level());
            if (spellResult.isSuccess()) {
                // 当法术有触发声音时，先按法术结果保存的目标播放声音。
                instance.get().spell().getTriggerSound().ifPresent(sound -> {
                    spellResult.getAffectedTarget().orElse(entity).playSound(sound);
                });
                postActivateSpell(entity, instance.get());
            } else if (entity instanceof Player player) {
                PlayerUtil.cooldownSpell(player, instance.get().spell(), FAIL_CD);
            }
        }
    }

    /**
     * 主动通过按键释放法术。
     */
    public static void activateSpell(Player player) {
        final Optional<Spell> instance = canActivateSpell(player, null, true);
        if (instance.isPresent()) {
            final HTHitResult result = createHitResult(player, instance.get());
            final boolean success = instance.get().spell().checkActivate(player, result, instance.get().level());
            if (success) {
                postActivateSpell(player, instance.get());
            } else {
                PlayerUtil.cooldownSpell(player, instance.get().spell(), FAIL_CD);
            }
        }
    }

    public static Optional<Spell> canActivateSpell(LivingEntity entity, @Nullable SpellType targetSpell, boolean sendTip) {
        if (entity instanceof Player player) {
            return canActivateSpell(player, targetSpell, sendTip);
        } else if (targetSpell != null) {
            return canActivateSpell(entity, targetSpell);
        }
        return Optional.empty();
    }

    /**
     * 是否能够触发法术。
     *
     * @param player      释放法术的玩家。
     * @param targetSpell null表示主动触发手上的法术，否则为被动触发。
     * @param sendTip     是否给予不能触发的反馈。
     * @return empty 表示不能触发。
     */
    public static Optional<Spell> canActivateSpell(Player player, @Nullable SpellType targetSpell, boolean sendTip) {
        final SpellType spell = targetSpell == null ? PlayerUtil.getPreparingSpell(player) : targetSpell;
        if (spell != null && (targetSpell == null || PlayerUtil.isSpellOnCircle(player, spell))) {
            final int level = PlayerUtil.getSpellLevel(player, spell);
            if (level > 0) {
                // 冷却结束。
                if (!PlayerUtil.isSpellOnCoolDown(player, spell)) {
                    // 灵力足够。
                    if (PlayerUtil.getQiAmount(player) >= spell.getConsumeMana()) {
                        if (!EventUtil.post(new ActivateSpellEvent.Pre(player, spell, level))) {
                            return Optional.of(new Spell(spell, level));
                        }
                    } else if (sendTip) {
                        PlayerHelper.sendTipTo(player, TipUtil.info("spell.no_enough_mana", spell.getConsumeMana()).withStyle(ChatFormatting.RED));
                    }
                } else if (sendTip) {
                    PlayerHelper.sendTipTo(player, SPELL_ON_CD.withStyle(ChatFormatting.RED));
                }
            } else if (sendTip) {
                PlayerHelper.sendTipTo(player, FORGOT_SPELL.withStyle(ChatFormatting.RED));
            }
        }
        return Optional.empty();
    }

    /**
     * 是否能够触发法术。
     *
     * @param living 释放法术的生物。
     * @param spell  能否触发某个法术。
     * @return empty 表示不能触发。
     */
    public static Optional<Spell> canActivateSpell(LivingEntity living, SpellType spell) {
        if (living instanceof SpellCaster caster) {
            final int level = caster.getSpellLevel(spell);
            if (level > 0 && !caster.isOnCoolDown() && caster.canUseSpell(spell)) {
                if (!EventUtil.post(new ActivateSpellEvent.Pre(living, spell, level))) {
                    return Optional.of(new Spell(spell, level));
                }
            }
        }
        return Optional.empty();
    }

    public static void postActivateSpell(LivingEntity entity, Spell instance) {
        if (entity instanceof Player player) {
            PlayerUtil.cooldownSpell(player, instance.spell(), getSpellCDTime(player, instance.spell()));
            costMana(player, instance.spell().getConsumeMana());
        } else if (entity instanceof SpellCaster caster) {
            caster.setCoolDown(instance.spell().getCooldown());
            caster.addQiAmount(-instance.spell().getConsumeMana());
        }
        EventUtil.post(new ActivateSpellEvent.Post(entity, instance.spell(), instance.level()));
    }

    public static HTHitResult createHitResult(LivingEntity entity, @NotNull Spell spell) {
        return HTHitResult.create(entity, spell.spell().getBlockClipMode(spell.level()), spell.spell().getFluidClipMode(spell.level()));
    }

    public static boolean canMobTrigger(SpellType spellType){
        return spellType.getCategory() != SpellUsageCategory.PLAYER_ONLY && spellType.getCategory() != SpellUsageCategory.TRIGGERED_PASSIVE;
    }

    public static long getSpellCDTime(Player player, SpellType spell) {
        return player.level().getGameTime() + spell.getCooldown();
    }

    public static void costMana(Player player, int cost) {
        PlayerUtil.addQiAmount(player, -cost);
    }

    /**
     * 是否已经选择了法术。
     */
    public static boolean hasSpellSelected(Player player) {
        return PlayerUtil.getPreparingSpell(player) != null;
    }

    /**
     * 能否使用法术轮盘。
     */
    public static boolean canUseCircle(Player player) {
        return true;
    }

    public static int changeCircleMode(int mode) {
        return mode == 1 ? 2 : 1;
    }

    public static boolean isValidSpellPos(int position) {
        return position >= 0 && position < Constants.SPELL_CIRCLE_SIZE;
    }

    public static MutableComponent getCostComponent(int cost) {
        return TipUtil.tooltip("spell_cost", cost);
    }

    /**
     * cd is tick, change it to seconds.
     */
    public static MutableComponent getCDComponent(int cd) {
        return TipUtil.tooltip("spell_cd", Mth.ceil(cd * 1.0F / 20));
    }

    public static MutableComponent spellName(SpellType spell, int level) {
        if (spell.getMaxLevel() == 1) {
            return spell.getComponent();
        }
        return spell.getComponent().append("(").append(TipUtil.misc("level" + level)).append(")");
    }

    @FunctionalInterface
    public interface ISpellTrigger {

        SpellResult checkActivate(LivingEntity owner, HTHitResult result, SpellType spell, int level);

    }

}
