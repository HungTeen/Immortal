package hungteen.immortal.common;

import hungteen.htlib.util.EffectUtil;
import hungteen.htlib.util.ParticleUtil;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.events.PlayerSpellEvent;
import hungteen.immortal.api.registry.ISpell;
import hungteen.immortal.client.particle.ImmortalParticles;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.blockentity.SpiritualFurnaceBlockEntity;
import hungteen.immortal.common.event.ImmortalPlayerEvents;
import hungteen.immortal.common.event.handler.PlayerEventHandler;
import hungteen.immortal.impl.PlayerDatas;
import hungteen.immortal.impl.Spells;
import hungteen.immortal.common.network.SpellPacket;
import hungteen.immortal.utils.EntityUtil;
import hungteen.immortal.utils.PlayerUtil;
import hungteen.immortal.utils.TipUtil;
import hungteen.immortal.utils.Util;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Nameable;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-14 10:28
 **/
public class SpellManager {

    /**
     * Only on Server Side.
     * {@link SpellPacket.Handler#onMessage(SpellPacket, Supplier)}
     */
    public static void checkActivateSpell(Player player){
        final ISpell spell = PlayerUtil.getSelectedSpell(player);
        if(spell != null){
            final int level = PlayerUtil.getSpellLearnLevel(player, spell);
            if(PlayerUtil.isSpellActivated(player, spell)){
                return;
            }
            if(! canSpellStart(player, spell)){
                return;
            }
            if(!MinecraftForge.EVENT_BUS.post(new PlayerSpellEvent.ActivateSpellEvent.Pre(player, spell, level))){
                if(! player.level.isClientSide){
                    PlayerUtil.activateSpell(player, spell, getSpellActivateTime(player, spell));
                }
                costMana(player, spell.getStartMana());
                MinecraftForge.EVENT_BUS.post(new PlayerSpellEvent.ActivateSpellEvent.Post(player, spell, level));
            }
        }
    }

    /**
     * {@link ImmortalPlayerEvents#onPlayerActivateSpell(PlayerSpellEvent.ActivateSpellEvent.Post)}
     */
    public static void checkSpellAction(Player player, ISpell spell, int level) {
        /* 水之呼吸 */
        if (spell == Spells.WATER_BREATHING) {
            if (!player.level.isClientSide) {
                player.addEffect(EffectUtil.viewEffect(MobEffects.WATER_BREATHING, Spells.WATER_BREATHING.getDuration(), 1));
            }
        }
        /* 调息术 */
        if (spell == Spells.RESTING){
            Util.getProxy().openRestingScreen();
        }
    }

    /**
     * {@link PlayerEventHandler#onTraceEntity(Player, EntityHitResult)}
     */
    public static void checkSpellAction(Player player, EntityHitResult result) {
        if(player.getMainHandItem().isEmpty()){
            /* 隔空取物，空手获取远处的物品 */
            if(result.getEntity() instanceof ItemEntity) {
                checkContinueSpell(player, Spells.ITEM_PICKING, () -> {
                    player.setItemInHand(InteractionHand.MAIN_HAND, ((ItemEntity) result.getEntity()).getItem());
                    result.getEntity().discard();
                });
            } else {
                checkContinueSpell(player, Spells.IGNITE, () -> {
                    ParticleUtil.spawnLineMovingParticle(player.level, ParticleTypes.FLAME, player.getEyePosition(), result.getEntity().getEyePosition(), 1, 0, 0.05);
                    result.getEntity().setSecondsOnFire(5);
                });
            }
        }
    }

    /**
     * {@link PlayerEventHandler#onTraceBlock(Player, BlockHitResult)}
     */
    public static void checkSpellAction(Player player, BlockHitResult result) {
        if(player.getMainHandItem().isEmpty()){
            /* 隔空取物，空手获取远处的方块 */
            checkContinueSpell(player, Spells.BLOCK_PICKING, () -> {
                final BlockState state = player.level.getBlockState(result.getBlockPos());
                // ban bedrock like blocks.
                if(state.getDestroySpeed(player.level, result.getBlockPos()) >= 0){
                    final BlockEntity blockentity = state.hasBlockEntity() ? player.level.getBlockEntity(result.getBlockPos()) : null;
                    final ItemStack stack = new ItemStack(state.getBlock().asItem());
                    if(blockentity != null){
                        blockentity.saveToItem(stack);
                        if (blockentity instanceof Nameable && ((Nameable) blockentity).hasCustomName()) {
                            stack.setHoverName(((Nameable) blockentity).getCustomName());
                        }
                    }
                    player.setItemInHand(InteractionHand.MAIN_HAND, stack);
                    player.level.setBlock(result.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
                }
            });
            /* 灵力释放 点火 */
            checkContinueSpell(player, Spells.RELEASING, () -> {
                final BlockEntity blockEntity = player.level.getBlockEntity(result.getBlockPos());
                if(blockEntity instanceof SpiritualFurnaceBlockEntity){
                    ((SpiritualFurnaceBlockEntity) blockEntity).start();
                }
                hungteen.immortal.utils.ParticleUtil.spawnLineSpiritualParticle(player.level, EntityUtil.getRGBForSpiritual(player), player.getEyePosition(), Vec3.atCenterOf(result.getBlockPos()), 2, 0, 0);
            });
        }
    }

    public static void checkContinueSpell(Player player, @Nonnull ISpell spell, Runnable runnable) {
        if(PlayerUtil.isSpellActivated(player, spell) && canSpellContinue(player, spell)){
            final int level = PlayerUtil.getSpellLearnLevel(player, spell);
            if(!MinecraftForge.EVENT_BUS.post(new PlayerSpellEvent.UsingSpellEvent(player, spell, level))){
                runnable.run();
                costMana(player, spell.getContinueMana());
            }
        }
    }

    public static long getSpellActivateTime(Player player, ISpell spell){
        return player.level.getGameTime() + spell.getDuration();
    }

    public static boolean canSpellStart(Player player, ISpell spell){
        return ImmortalAPI.get().getSpiritualMana(player) >= spell.getStartMana();
    }

    public static boolean canSpellContinue(Player player, ISpell spell){
        return ImmortalAPI.get().getSpiritualMana(player) >= spell.getContinueMana();
    }

    public static void costMana(Player player, int cost){
        PlayerUtil.addIntegerData(player, PlayerDatas.SPIRITUAL_MANA, - cost);
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
