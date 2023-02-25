package hungteen.immortal.common;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.events.PlayerSpellEvent;
import hungteen.immortal.api.registry.ISpellType;
import hungteen.immortal.common.event.ImmortalPlayerEvents;
import hungteen.immortal.common.event.handler.PlayerEventHandler;
import hungteen.immortal.common.impl.RealmTypes;
import hungteen.immortal.common.impl.SpellTypes;
import hungteen.immortal.common.network.SpellPacket;
import hungteen.immortal.common.impl.PlayerRangeNumbers;
import hungteen.immortal.utils.PlayerUtil;
import hungteen.immortal.utils.TipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-14 10:28
 **/
public class SpellManager {

    private static final Component NO_ROOT_FOR_INSPIRATION = Component.translatable("info.immortal.no_root_for_inspiration");
    private static final Component CLOSE_TO_INSPIRATION = Component.translatable("info.immortal.close_to_inspiration");

    /**
     * Only on Server Side.
     * {@link SpellPacket.Handler#onMessage(SpellPacket, Supplier)}
     */
    public static void checkActivateSpell(Player player){
        checkActivateSpell(player, PlayerUtil.getSelectedSpell(player));
    }

    /**
     * Use this method to activate spell.
     */
    public static void checkActivateSpell(Player player, ISpellType spell){
        if(spell != null){
            final int level = PlayerUtil.getSpellLevel(player, spell);
            // 冷却中或还没结束
            if(PlayerUtil.isSpellOnCooldown(player, spell)){
                return;
            }
            if(! canSpellStart(player, spell)){
                return;
            }
            if(!MinecraftForge.EVENT_BUS.post(new PlayerSpellEvent.ActivateSpellEvent.Pre(player, spell, level))){
                PlayerUtil.activateSpell(player, spell, getSpellActivateTime(player, spell));
                PlayerUtil.cooldownSpell(player, spell, getSpellCDTime(player, spell));
                costMana(player, spell.getStartMana());
                MinecraftForge.EVENT_BUS.post(new PlayerSpellEvent.ActivateSpellEvent.Post(player, spell, level));
            }
        }
    }

    /**
     * 刚触发时的效果 <br>
     * {@link ImmortalPlayerEvents#onPlayerActivateSpell(PlayerSpellEvent.ActivateSpellEvent.Post)}
     */
    public static void checkSpellAction(Player player, ISpellType spell, int level) {
        /* 启灵 */
        if(spell == SpellTypes.INSPIRATION){
            int rootCount = PlayerUtil.getSpiritualRoots(player).size();
            if(rootCount == 0){
                PlayerHelper.sendTipTo(player, NO_ROOT_FOR_INSPIRATION);
            } else {
                //TODO 启灵有点粗糙
                if(RandomHelper.chance(player.getRandom(), 0.9F / rootCount)){
                    PlayerUtil.setRealm(player, RealmTypes.SPIRITUAL_LEVEL_1);
                } else {
                    PlayerHelper.sendTipTo(player, CLOSE_TO_INSPIRATION);
                }
            }
        }
//        /* 水之呼吸 */
//        if (spell == SpellTypes.WATER_BREATHING) {
//            if (!player.level.isClientSide) {
//                player.addEffect(EffectHelper.viewEffect(MobEffects.WATER_BREATHING, SpellTypes.WATER_BREATHING.getDuration(), 1));
//            }
//        }
//        /* 调息术 */
//        if (spell == SpellTypes.RESTING){
//            Util.getProxy().openRestingScreen();
//        }
    }

    /**
     * {@link PlayerEventHandler#onTraceEntity(Player, EntityHitResult)}
     */
    public static void checkSpellAction(Player player, EntityHitResult result) {
        if(player.getMainHandItem().isEmpty()){
            /* 隔空取物，空手获取远处的物品 */
//            if(result.getEntity() instanceof ItemEntity) {
//                checkContinueSpell(player, SpellTypes.ITEM_PICKING, () -> {
//                    player.setItemInHand(InteractionHand.MAIN_HAND, ((ItemEntity) result.getEntity()).getItem());
//                    result.getEntity().discard();
//                });
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
            /* 隔空取物，空手获取远处的方块 */
//            checkContinueSpell(player, SpellTypes.BLOCK_PICKING, () -> {
//                final BlockState state = player.level.getBlockState(result.getBlockPos());
//                // ban bedrock like blocks.
//                if(state.getDestroySpeed(player.level, result.getBlockPos()) >= 0){
//                    final BlockEntity blockentity = state.hasBlockEntity() ? player.level.getBlockEntity(result.getBlockPos()) : null;
//                    final ItemStack stack = new ItemStack(state.getBlock().asItem());
//                    if(blockentity != null){
//                        blockentity.saveToItem(stack);
//                        if (blockentity instanceof Nameable && ((Nameable) blockentity).hasCustomName()) {
//                            stack.setHoverName(((Nameable) blockentity).getCustomName());
//                        }
//                    }
//                    player.setItemInHand(InteractionHand.MAIN_HAND, stack);
//                    player.level.setBlock(result.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
//                }
//            });
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

    public static void checkContinueSpell(Player player, @Nonnull ISpellType spell, Runnable runnable) {
        if(PlayerUtil.isSpellActivated(player, spell) && canSpellContinue(player, spell)){
            final int level = PlayerUtil.getSpellLevel(player, spell);
            if(!MinecraftForge.EVENT_BUS.post(new PlayerSpellEvent.UsingSpellEvent(player, spell, level))){
                runnable.run();
                costMana(player, spell.getContinueMana());
            }
        }
    }

    public static long getSpellActivateTime(Player player, ISpellType spell){
        return player.level.getGameTime() + spell.getDuration();
    }

    public static long getSpellCDTime(Player player, ISpellType spell){
        return player.level.getGameTime() + spell.getCooldown();
    }

    public static boolean canSpellStart(Player player, ISpellType spell){
        return ImmortalAPI.get().getSpiritualMana(player) >= spell.getStartMana();
    }

    public static boolean canSpellContinue(Player player, ISpellType spell){
        return ImmortalAPI.get().getSpiritualMana(player) >= spell.getContinueMana();
    }

    public static void costMana(Player player, int cost){
        PlayerUtil.addIntegerData(player, PlayerRangeNumbers.SPIRITUAL_MANA, - cost);
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
