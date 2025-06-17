package hungteen.imm.common.cultivation;

import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.artifact.ArtifactItem;
import hungteen.imm.api.entity.SpellCaster;
import hungteen.imm.api.event.ActivateSpellEvent;
import hungteen.imm.api.spell.*;
import hungteen.imm.common.codec.SpellInstance;
import hungteen.imm.common.item.IMMDataComponents;
import hungteen.imm.common.network.client.ClientSpellPacket;
import hungteen.imm.common.network.server.ServerSpellPacket;
import hungteen.imm.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

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

    public static boolean canPlaceOnCircle(SpellType spell){
        //TODO 可放置在轮盘的法术。
        return true;
    }

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
     * 主动通过按键释放法术。
     */
    public static void activateSpell(Player player) {
        final Optional<Spell> instance = canActivateSpell(player, null, true);
        instance.ifPresent(spell -> activateSpell(player, spell, new SpellCastContext(player, spell)));
    }

    /**
     * 有些被选择的法术可以被动释放法术，此处进行通用检查。
     */
    public static void activateRandomSpell(LivingEntity entity, Predicate<SpellType> predicate) {
        final Optional<Spell> instance = getMatchSpellRandomly(entity, predicate);
        instance.ifPresent(spell -> activateSpell(entity, spell, new SpellCastContext(entity, spell)));
    }

    /**
     * 有些被选择的法术可以被动释放法术，此处进行通用检查。
     */
    public static void activateSpell(LivingEntity entity, @NotNull SpellType spellType) {
        final Optional<Spell> instance = canActivateSpell(entity, spellType, false);
        instance.ifPresent(spell -> activateSpell(entity, spell, new SpellCastContext(entity, spell)));
    }

    /**
     * 检查法器的法术槽位的触发情况。
     */
    public static void activateSpell(LivingEntity entity, TriggerCondition condition) {
        condition.getValidSlots().forEach(slot -> {
            activateSpell(entity, condition, entity.getItemBySlot(slot));
        });
    }

    /**
     * 检查法器的法术槽位的触发情况。
     * @param entity 触发生物。
     * @param condition 触发条件。
     * @param stack 触发的物品。
     */
    public static void activateSpell(LivingEntity entity, TriggerCondition condition, ItemStack stack) {
        if(getMaxSpellSlot(stack) > 0){
            for(SpellInstance instance : getSpellsInItem(stack)){
                if(instance.condition() == condition){
                    activateSpell(entity, instance.spell(), new SpellCastContext(entity, instance.spell().level(), condition, stack));
                    return;
                }
            }
        }
    }

    /**
     * 被动释放法术都会运行这里。
     * @param entity 释放法术的生物。
     * @param spell 被释放的法术。
     */
    private static void activateSpell(LivingEntity entity, Spell spell, SpellCastContext context) {
        if (!EventUtil.post(new ActivateSpellEvent.Pre(entity, spell))) {
            final boolean success = spell.spell().checkActivate(context);
            if (success) {
                // 播放触发音效。
                spell.spell().getTriggerSound().ifPresent(sound -> {
                    if(entity instanceof Player player){
                        PlayerHelper.playClientSound(player, sound);
                    }
                });
                postActivateSpell(entity, spell);
            } else if (entity instanceof Player player) {
                PlayerUtil.cooldownSpell(player, spell.spell(), FAIL_CD);
            }
        }
    }

    private static void postActivateSpell(LivingEntity entity, Spell spell) {
        if (entity instanceof Player player) {
            PlayerUtil.cooldownSpell(player, spell.spell(), getSpellCDTime(player, spell.spell()));
            costMana(player, spell.spell().getConsumeQi());
        } else if (entity instanceof SpellCaster caster) {
            caster.setCoolDown(spell.spell().getCooldown());
            caster.addQiAmount(-spell.spell().getConsumeQi());
        }
        EventUtil.post(new ActivateSpellEvent.Post(entity, spell));
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
     * 随机选择一个法术。
     */
    public static Optional<Spell> getMatchSpellRandomly(LivingEntity entity, Predicate<SpellType> predicate) {
        Stream<SpellType> spellStream = null;
        if (entity instanceof Player player) {
            spellStream = PlayerUtil.getCircleSpells(player);
        } else if (entity instanceof SpellCaster caster) {
            spellStream = caster.getLearnedSpellTypes().stream();
        }
        if(spellStream != null) {
            List<Optional<Spell>> list = spellStream
                    .filter(predicate)
                    .map(spell -> canActivateSpell(entity, spell, false))
                    .filter(Optional::isPresent)
                    .toList();
            if(! list.isEmpty()){
                return RandomUtil.choose(entity.getRandom(), list);
            }
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
                    if (PlayerUtil.getQiAmount(player) >= spell.getConsumeQi()) {
                        return Optional.of(new Spell(spell, level));
                    } else if (sendTip) {
                        PlayerHelper.sendTipTo(player, TipUtil.info("spell.no_enough_qi", spell.getConsumeQi()).withStyle(ChatFormatting.RED));
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
                return Optional.of(new Spell(spell, level));
            }
        }
        return Optional.empty();
    }

    public static HTHitResult createHitResult(LivingEntity entity, @NotNull Spell spell) {
        return HTHitResult.create(entity, spell.spell().getBlockClipMode(spell.level()), spell.spell().getFluidClipMode(spell.level()));
    }

    public static boolean canMobTrigger(SpellType spellType) {
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

    public static List<SpellInstance> getSpellsInItem(ItemStack stack){
        return stack.getOrDefault(IMMDataComponents.SPELL_INSTANCES, new ArrayList<>());
    }

    public static void putSpellInItem(ItemStack stack, SpellInstance spellInstance) {
        List<SpellInstance> spells = getSpellsInItem(stack);
        if(spells.size() < getMaxSpellSlot(stack)){
            spells.add(spellInstance);
            stack.set(IMMDataComponents.SPELL_INSTANCES, spells);
        }
    }

    /**
     * @return 该物品的法术槽位是否已经满了。
     */
    public static boolean isSpellFull(ItemStack stack){
        return getSpellsInItem(stack).size() >= getMaxSpellSlot(stack);
    }

    /**
     * 获取物品最大的法术槽位，如果获取不到则查询获取。
     */
    public static int getMaxSpellSlot(ItemStack stack){
        if(! stack.has(IMMDataComponents.MAX_SPELL_SLOT)){
            setSpellSlot(stack);
        }
        return stack.getOrDefault(IMMDataComponents.MAX_SPELL_SLOT, 0);
    }

    public static void setSpellSlot(ItemStack stack){
        int maxSlot = 0;
        if(stack.getItem() instanceof ArtifactItem artifactItem){
            maxSlot = artifactItem.getMaxSpellSlot(stack);
        }
        if(stack.is(Tags.Items.ENCHANTABLES)){
            maxSlot = 1;
        }
        if(maxSlot != 0){
            stack.set(IMMDataComponents.MAX_SPELL_SLOT, maxSlot);
        }
    }

    public static List<Component> getSpellTooltips(Spell spell){
        final List<Component> components = new ArrayList<>();
        final SpellType item = spell.spell();
        final int lvl = spell.level();
        components.add(item.getComponent().append("(" + lvl + "/" + item.getMaxLevel() + ")").withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.BOLD));
        for (int i = 1; i <= lvl; ++i) {
            components.add(item.getSpellDesc(i).withStyle(ChatFormatting.GREEN).withStyle(ChatFormatting.ITALIC));
        }
        components.add(SpellManager.getCostComponent(item.getConsumeQi()).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.UNDERLINE));
        components.add(SpellManager.getCDComponent(item.getCooldown()).withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.UNDERLINE));
        return components;
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
        return spell.getComponent().append(" ").append(TipUtil.manual("level" + level));
    }

    @FunctionalInterface
    public interface ISpellTrigger {

        boolean checkActivate(SpellCastContext context);

    }

}
