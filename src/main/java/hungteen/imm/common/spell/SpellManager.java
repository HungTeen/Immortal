package hungteen.imm.common.spell;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.api.events.PlayerSpellEvent;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.event.handler.PlayerEventHandler;
import hungteen.imm.common.impl.registry.PlayerRangeFloats;
import hungteen.imm.common.network.NetworkHandler;
import hungteen.imm.common.network.SpellPacket;
import hungteen.imm.util.Constants;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-14 10:28
 **/
public class SpellManager {

    private static final MutableComponent FORGOT_SPELL = TipUtil.info("spell.forgot");
    private static final MutableComponent SPELL_ON_CD = TipUtil.info("spell.on_cooldown");
    private static final int FAIL_CD = 20;

    public static void pressToActivateSpell(Player player){
        if(EntityHelper.isServer(player)){
            // 使用法术的判断。
            if(EntityHelper.isEntityValid(player) && SpellManager.hasSpellSelected(player)){
                SpellManager.activateSpell(player, HTHitResult.create(player));
            }
        } else {
            NetworkHandler.sendToServer(new SpellPacket(SpellPacket.SpellOptions.ACTIVATE));
        }
    }

    /**
     * Only on Client Side.
     * @param position determines which spell will be selected.
     */
    public static void selectSpellOnCircle(int position){
        NetworkHandler.sendToServer(new SpellPacket(SpellPacket.SpellOptions.SELECT_SPELL, position));
    }

    /**
     * Only on Server Side.
     * {@link SpellPacket.Handler#onMessage(SpellPacket, Supplier)}
     */
    public static void selectSpellOnCircle(Player player, int position){
        if(isValidSpellPos(position)){
            selectSpellOnCircle(player, PlayerUtil.getSpellAt(player, position));
        } else {
            selectSpellOnCircle(player, null);
        }
    }

    /**
     * Use this method to select spell.
     */
    public static void selectSpellOnCircle(Player player, @Nullable ISpellType spell){
        PlayerUtil.setPreparingSpell(player, spell);
    }

    /**
     * 有些被选择的法术可以被动释放法术，此处进行通用检查。
     */
    public static void activateSpell(Player player, @NotNull ISpellType spell, ISpellTrigger trigger){
        final HTHitResult result = HTHitResult.create(player);
        final Optional<SpellInstance> instance = canActivateSpell(player, result, spell, false);
        if(instance.isPresent()){
            if(trigger.checkActivate(player, result, instance.get().spell(), instance.get().level())){
                postActivateSpell(player, instance.get());
            } else {
                PlayerUtil.cooldownSpell(player, instance.get().spell(), FAIL_CD);
            }
        }
    }

    /**
     * 主动通过按键释放法术。
     */
    public static void activateSpell(Player player, @NotNull HTHitResult result){
        final Optional<SpellInstance> instance = canActivateSpell(player, result, null, true);
        if(instance.isPresent()){
            final boolean success = instance.get().spell().checkActivate(player, result, instance.get().level());
            if(success){
                postActivateSpell(player, instance.get());
            } else {
                PlayerUtil.cooldownSpell(player, instance.get().spell(), FAIL_CD);
            }
        }
    }

    /**
     * 是否能够触发法术。
     * @param player 释放法术的玩家。
     * @param result 射线检测结果。
     * @param targetSpell 当前要求触发的法术（检查与选择的法术是否匹配）。
     * @param sendTip 是否给予不能触发的反馈。
     * @return empty 表示不能触发。
     */
    public static Optional<SpellInstance> canActivateSpell(Player player, @NotNull HTHitResult result, @Nullable ISpellType targetSpell, boolean sendTip){
        final ISpellType spell = PlayerUtil.getPreparingSpell(player);
        if(spell != null && (targetSpell == null || spell == targetSpell)) {
            final int level = PlayerUtil.getSpellLevel(player, spell);
            if (level > 0) {
                // 冷却结束。
                if (!PlayerUtil.isSpellOnCooldown(player, spell)) {
                    // 灵力足够。
                    if (PlayerUtil.getMana(player) >= spell.getConsumeMana()) {
                        if (!MinecraftForge.EVENT_BUS.post(new PlayerSpellEvent.ActivateSpellEvent.Pre(player, spell, level))) {
                            return Optional.of(new SpellInstance(spell, level));
                        }
                    } else if(sendTip){
                        PlayerHelper.sendTipTo(player, TipUtil.info("spell.no_enough_mana", spell.getConsumeMana()).withStyle(ChatFormatting.RED));
                    }
                } else if(sendTip){
                    PlayerHelper.sendTipTo(player, SPELL_ON_CD.withStyle(ChatFormatting.RED));
                }
            } else if(sendTip){
                PlayerHelper.sendTipTo(player, FORGOT_SPELL.withStyle(ChatFormatting.RED));
            }
        }
        return Optional.empty();
    }

    public static void postActivateSpell(Player player, SpellInstance instance){
        PlayerUtil.cooldownSpell(player, instance.spell(), getSpellCDTime(player, instance.spell()));
        costMana(player, instance.spell().getConsumeMana());
        MinecraftForge.EVENT_BUS.post(new PlayerSpellEvent.ActivateSpellEvent.Post(player, instance.spell(), instance.level()));
    }

    /**
     * 刚触发时的效果 <br>
     */
    public static void checkSpellAction(Player player, ISpellType spell, int level) {
//        /* 水之呼吸 */
//        if (spell == SpellTypes.WATER_BREATHING) {
//            if (!player.level.isClientSide) {
//                player.addEffect(EffectHelper.viewEffect(MobEffects.WATER_BREATHING, SpellTypes.WATER_BREATHING.getDuration(), 1));
//            }
//        }
    }

    /**
     * {@link PlayerEventHandler#onTraceEntity(Player, EntityHitResult)}
     */
    public static void checkSpellAction(Player player, EntityHitResult result) {
        if(player.getMainHandItem().isEmpty()){
//            if(result.getEntity() instanceof ItemEntity) {
//            } else {
//                checkContinueSpell(player, SpellTypes.IGNITE, () -> {
//                    ParticleHelper.spawnLineMovingParticle(player.level, ParticleTypes.FLAME, player.getEyePosition(), result.getEntity().getEyePosition(), 1, 0, 0.05);
//                    result.getEntity().setSecondsOnFire(5);
//                });
//            }
        }
    }

    /**
     * {@link PlayerEventHandler#onTraceBlock(Player, BlockHitResult)}
     */
    public static void checkSpellAction(Player player, BlockHitResult result) {
        if(player.getMainHandItem().isEmpty()){
//            /* 灵力释放 点火 */
//            checkContinueSpell(player, SpellTypes.RELEASING, () -> {
//                final BlockEntity blockEntity = player.level.getBlockEntity(result.getBlockPos());
//                if(blockEntity instanceof SpiritualFurnaceBlockEntity){
//                    ((SpiritualFurnaceBlockEntity) blockEntity).start();
//                }
//                hungteen.immortal.utils.ParticleUtil.spawnLineSpiritualParticle(player.level, EntityUtil.getRGBForSpiritual(player), player.getEyePosition(), Vec3.atCenterOf(result.getBlockPos()), 2, 0, 0);
//            });
        }
    }

    public static long getSpellCDTime(Player player, ISpellType spell){
        return player.level().getGameTime() + spell.getCooldown();
    }

    public static void costMana(Player player, int cost){
        PlayerUtil.addFloatData(player, PlayerRangeFloats.SPIRITUAL_MANA, - cost);
    }

    /**
     * 是否已经选择了法术。
     */
    public static boolean hasSpellSelected(Player player){
        return PlayerUtil.getPreparingSpell(player) != null;
    }

    /**
     * 能否使用法术轮盘。
     */
    public static boolean canUseCircle(Player player){
        return true;
    }

    public static int changeCircleMode(int mode){
        return mode == 1 ? 2 : 1;
    }

    public static boolean isValidSpellPos(int position){
        return position >= 0 && position < Constants.SPELL_CIRCLE_SIZE;
    }

    public static MutableComponent getCostComponent(int cost){
        return TipUtil.SPELL_COST.apply(cost);
    }

    /**
     * cd is tick, change it to seconds.
     */
    public static MutableComponent getCDComponent(int cd){
        return TipUtil.SPELL_CD.apply(Mth.ceil(cd * 1.0F / 20));
    }

    public static MutableComponent spellName(ISpellType spell, int level){
        if(spell.getMaxLevel() == 1) return spell.getComponent();
        return spell.getComponent().append("(").append(TipUtil.misc("level" + level)).append(")");
    }

    record SpellInstance(ISpellType spell, int level){

    }

    @FunctionalInterface
    public interface ISpellTrigger {

        boolean checkActivate(Player owner, HTHitResult result, ISpellType spell, int level);

    }

}
