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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-14 10:28
 **/
public class SpellManager {

    private static final MutableComponent FORGOT_SPELL = TipUtil.info("spell.forgot");
    private static final MutableComponent SPELL_ON_CD = TipUtil.info("spell.on_cooldown");
    private static final MutableComponent NO_ENOUGH_MANA = TipUtil.info("spell.no_enough_mana");
    private static final int FAIL_CD = 20;

    public static void pressToActivateSpell(Player player){
        if(EntityHelper.isServer(player)){
            final HitResult hitResult = PlayerUtil.getHitResult(player);
            // 使用法术的判断。
            if(EntityHelper.isEntityValid(player) && SpellManager.hasSpellSelected(player)){
                SpellManager.activateSpell(player, HTHitResult.create(hitResult));
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
        if(position >= 0 && position < Constants.SPELL_CIRCLE_SIZE){
            NetworkHandler.sendToServer(new SpellPacket(null, SpellPacket.SpellOptions.SELECT_ON_CIRCLE, position));
        }
    }

    /**
     * Only on Server Side.
     * {@link SpellPacket.Handler#onMessage(SpellPacket, Supplier)}
     */
    public static void selectSpellOnCircle(Player player, int position){
        selectSpellOnCircle(player, PlayerUtil.getSpellAt(player, position));
    }

    /**
     * Use this method to select spell.
     */
    public static void selectSpellOnCircle(Player player, @Nullable ISpellType spell){
        PlayerUtil.setPreparingSpell(player, spell);
    }

    public static void activateSpell(Player player, HTHitResult result){
        final ISpellType spell = PlayerUtil.getPreparingSpell(player);
        if(spell != null){
            final int level = PlayerUtil.getSpellLevel(player, spell);
            if(level > 0){
                // 冷却结束。
                if(! PlayerUtil.isSpellOnCooldown(player, spell)){
                    // 灵力足够。
                    if(PlayerUtil.getMana(player) >= spell.getConsumeMana()){
                        if(result != null && !MinecraftForge.EVENT_BUS.post(new PlayerSpellEvent.ActivateSpellEvent.Pre(player, spell, level))){
                            final boolean success = spell.checkActivate(player, result, level);
                            if(success){
                                PlayerUtil.cooldownSpell(player, spell, getSpellCDTime(player, spell));
                                costMana(player, spell.getConsumeMana());
                                MinecraftForge.EVENT_BUS.post(new PlayerSpellEvent.ActivateSpellEvent.Post(player, spell, level, success));
                            } else {
                                PlayerUtil.cooldownSpell(player, spell, FAIL_CD);
                            }
                        }
                    } else {
                        PlayerHelper.sendTipTo(player, NO_ENOUGH_MANA);
                    }
                } else {
                    PlayerHelper.sendTipTo(player, SPELL_ON_CD);
                }
            } else {
                PlayerHelper.sendTipTo(player, FORGOT_SPELL);
            }
        }
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

    public static Component getCostComponent(int cost){
        return TipUtil.SPELL_COST.apply(cost);
    }

    /**
     * cd is tick, change it to seconds.
     */
    public static Component getCDComponent(int cd){
        return TipUtil.SPELL_CD.apply(Mth.ceil(cd * 1.0F / 20));
    }

    public static MutableComponent spellName(ISpellType spell, int level){
        return spell.getComponent().append(TipUtil.misc("level" + level));
    }

}
