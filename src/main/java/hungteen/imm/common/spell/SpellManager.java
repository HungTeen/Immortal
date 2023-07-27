package hungteen.imm.common.spell;

import hungteen.imm.api.EntityBlockResult;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.events.PlayerSpellEvent;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.event.handler.PlayerEventHandler;
import hungteen.imm.common.impl.registry.PlayerRangeFloats;
import hungteen.imm.common.impl.registry.PlayerRangeIntegers;
import hungteen.imm.common.network.NetworkHandler;
import hungteen.imm.common.network.SpellPacket;
import hungteen.imm.util.Constants;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.MinecraftForge;

import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-14 10:28
 **/
public class SpellManager {

    /**
     * Only on Client Side.
     * @param position determines which spell will be triggered.
     */
    public static void activateAt(int position){
        if(position >= 0 && position < Constants.SPELL_CIRCLE_SIZE){
            NetworkHandler.sendToServer(new SpellPacket(null, SpellPacket.SpellOptions.ACTIVATE, position));
        }
    }

    /**
     * Only on Server Side.
     * {@link SpellPacket.Handler#onMessage(SpellPacket, Supplier)}
     */
    public static void checkActivateSpell(Player player, int position){
        checkActivateSpell(player, PlayerUtil.getSpellAt(player, position));
    }

    /**
     * Use this method to activate spell.
     */
    public static void checkActivateSpell(Player player, ISpellType spell){
        checkActivateSpell(player, spell, SpellManager.getResult(player));
    }

    public static void checkActivateSpell(Player player, ISpellType spell, EntityBlockResult result){
        if(spell != null){
            final int level = PlayerUtil.getSpellLevel(player, spell);
            // 等级不够 或 在冷却。
            if(level <= 0 || PlayerUtil.isSpellOnCooldown(player, spell)){
                return;
            }
            if(! canSpellStart(player, spell)){
                return;
            }
            if(result != null && !MinecraftForge.EVENT_BUS.post(new PlayerSpellEvent.ActivateSpellEvent.Pre(player, spell, level))){
                final boolean success = onSpellTriggered(player, result, spell, level);
                if(success){
                    PlayerUtil.cooldownSpell(player, spell, getSpellCDTime(player, spell));
                    costMana(player, spell.getConsumeMana());
                }
                MinecraftForge.EVENT_BUS.post(new PlayerSpellEvent.ActivateSpellEvent.Post(player, spell, level, success));
            }
        }
    }

    public static boolean onSpellTriggered(Player player, EntityBlockResult result, ISpellType spell, int level){
        if(spell.canTrigger()){
            if(! spell.isImmediateSpell()){
                PlayerUtil.setIntegerData(player, PlayerRangeIntegers.SPELL_PRE_TICK, spell.getPrepareCD());
                return true;
            }
            return spell.onActivate(player, result, level);
        }
        return false;
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

    public static EntityBlockResult getResult(Player player) {
        final HitResult hitResult = PlayerUtil.getHitResult(player);
        if(hitResult instanceof EntityHitResult result){
            return new EntityBlockResult(player.level(), result.getEntity());
        } else if(hitResult instanceof BlockHitResult result){
            return new EntityBlockResult(player.level(), result.getBlockPos(), result.getDirection());
        }
        return null;
    }

    public static long getSpellCDTime(Player player, ISpellType spell){
        return player.level().getGameTime() + spell.getCooldown();
    }

    public static boolean canSpellStart(Player player, ISpellType spell){
        return IMMAPI.get().getSpiritualMana(player) >= spell.getConsumeMana() && ! isPreparing(player);
    }

    public static void costMana(Player player, int cost){
        PlayerUtil.addFloatData(player, PlayerRangeFloats.SPIRITUAL_MANA, - cost);
    }

    /**
     * 能否使用法术轮盘。
     */
    public static boolean canUseCircle(Player player){
        return ! isPreparing(player);
    }

    /**
     * 是否正在吟唱法术。
     */
    public static boolean isPreparing(Player player){
        return PlayerUtil.getIntegerData(player, PlayerRangeIntegers.SPELL_PRE_TICK) > 0;
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

}
