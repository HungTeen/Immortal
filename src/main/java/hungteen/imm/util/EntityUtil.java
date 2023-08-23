package hungteen.imm.util;

import hungteen.htlib.common.entity.SeatEntity;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.interfaces.IHasMana;
import hungteen.imm.api.interfaces.IHasRoot;
import hungteen.imm.api.interfaces.IHasSpell;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.capability.CapabilityHandler;
import hungteen.imm.common.capability.entity.IMMEntityCapability;
import hungteen.imm.common.entity.misc.FlyingItemEntity;
import hungteen.imm.common.impl.registry.ElementReactions;
import hungteen.imm.common.impl.registry.PlayerRangeFloats;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 21:43
 **/
public class EntityUtil {

    public static boolean addItem(Entity entity, ItemStack stack){
        if(entity instanceof Player player){
            PlayerUtil.addItem(player, stack);
            return true;
        } else if(entity instanceof InventoryCarrier carrier){
            carrier.getInventory().addItem(stack);
            return true;
        } else if(entity instanceof LivingEntity living){
            if(living.getMainHandItem().isEmpty()){
                living.setItemInHand(InteractionHand.MAIN_HAND, stack);
                return true;
            } else if(living.getOffhandItem().isEmpty()) {
                living.setItemInHand(InteractionHand.OFF_HAND, stack);
                return true;
            }
        }
        return false;
    }

    public static boolean sitToMeditate(LivingEntity living, BlockPos pos, float yOffset, boolean relyOnBlock){
        if(EntityUtil.hasItemInHand(living)){
            if(living instanceof Player player){
                PlayerHelper.sendTipTo(player, TipUtil.info("spell.has_item_in_hand").withStyle(ChatFormatting.RED));
            }
            return false;
        }
        return SeatEntity.seatAt(living.level(), living, pos, yOffset, living.getYRot(), 120F, relyOnBlock);
    }

    public static boolean hasEmptyHand(LivingEntity living){
        return living.getMainHandItem().isEmpty() || living.getOffhandItem().isEmpty();
    }

    public static boolean hasItemInHand(LivingEntity living){
        return ! living.getMainHandItem().isEmpty() || ! living.getOffhandItem().isEmpty();
    }

    public static boolean canManaIncrease(Entity entity){
        return ! (entity.getVehicle() instanceof FlyingItemEntity)
                && (entity.getId() + entity.level().getGameTime()) % Constants.SPIRITUAL_ABSORB_TIME == 0
                && ! ElementManager.isActiveReaction(entity, ElementReactions.PARASITISM);
    }

    public static int getSpellLevel(Entity entity, ISpellType spell){
        return entity instanceof Player player ? PlayerUtil.getSpellLevel(player, spell) : entity instanceof IHasSpell e ? e.getSpellLevel(spell) : 0;
    }

    public static boolean hasLearnedSpell(Entity entity, ISpellType spell, int level){
        return entity instanceof Player player ? PlayerUtil.hasLearnedSpell(player, spell, level) : entity instanceof IHasSpell e && e.hasLearnedSpell(spell, level);
    }

    public static boolean hasLearnedSpell(Entity entity, ISpellType spell){
        return hasLearnedSpell(entity, spell, 1);
    }

    public static float getMana(Entity entity) {
        return entity instanceof Player player ? PlayerUtil.getMana(player) : entity instanceof IHasMana manaEntity ? manaEntity.getMana() : 0;
    }

    public static void addMana(Entity entity, float amount) {
        if(entity instanceof Player player){
            PlayerUtil.addFloatData(player, PlayerRangeFloats.SPIRITUAL_MANA, amount);
        } else if(entity instanceof IHasMana manaEntity){
            manaEntity.addMana(amount);
        }
    }

    public static boolean hasMana(Entity entity) {
        return getMana(entity) > 0;
    }

    public static ItemStack getItemInHand(LivingEntity living, Predicate<ItemStack> predicate) {
        return predicate.test(living.getMainHandItem()) ? living.getMainHandItem() : predicate.test(living.getOffhandItem()) ? living.getOffhandItem() : ItemStack.EMPTY;
    }

    @Nullable
    public static IMMEntityCapability getEntityCapability(Entity entity) {
        return entity.getCapability(CapabilityHandler.ENTITY_CAP).resolve().orElse(null);
    }

    public static Optional<IMMEntityCapability> getOptCapability(Entity entity) {
        return Optional.ofNullable(getEntityCapability(entity));
    }

    public static <U> U getCapabilityResult(Entity entity, Function<IMMEntityCapability, U> function, U defaultValue) {
        return getOptCapability(entity).map(function).orElse(defaultValue);
    }

    public static boolean notConsume(Entity entity) {
        return entity instanceof Player player && PlayerUtil.notConsume(player);
    }

    public static void playSound(Level level, Entity entity, SoundEvent soundEvent) {
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), soundEvent, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    public static boolean isMainHolding(LivingEntity entity, Predicate<ItemStack> predicate) {
        return predicate.test(entity.getMainHandItem());
    }

    public static boolean isOffHolding(LivingEntity entity, Predicate<ItemStack> predicate) {
        return predicate.test(entity.getOffhandItem());
    }

    public static void multiblockSpawn(Level level, BlockPos blockPos, BlockPattern blockPattern, EntityType<?> entityType) {
        if (!level.isClientSide) {
            BlockPattern.BlockPatternMatch match = blockPattern.find(level, blockPos);
            if (match != null) {
                for (int i = 0; i < blockPattern.getHeight(); ++i) {
                    BlockInWorld blockinworld = match.getBlock(0, i, 0);
                    level.setBlock(blockinworld.getPos(), Blocks.AIR.defaultBlockState(), 2);
                    level.levelEvent(2001, blockinworld.getPos(), Block.getId(blockinworld.getState()));
                }

                Entity entity = entityType.create(level);
                BlockPos pos = match.getBlock(0, 2, 0).getPos();
                entity.moveTo((double) pos.getX() + 0.5D, (double) pos.getY() + 0.05D, (double) pos.getZ() + 0.5D, 0.0F, 0.0F);
                level.addFreshEntity(entity);

                for (ServerPlayer serverplayer : level.getEntitiesOfClass(ServerPlayer.class, entity.getBoundingBox().inflate(5.0D))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, entity);
                }

                for (int l = 0; l < blockPattern.getHeight(); ++l) {
                    BlockInWorld world = match.getBlock(0, l, 0);
                    level.blockUpdated(world.getPos(), Blocks.AIR);
                }
            }
        }
    }

    public static List<ISpiritualType> getSpiritualRoots(Entity entity) {
        return entity instanceof Player player ? PlayerUtil.getSpiritualRoots(player)
                : entity instanceof IHasRoot iHasRoot ? iHasRoot.getSpiritualTypes() : List.of();
    }

    public static Triple<Float, Float, Float> getRGBForSpiritual(Entity entity) {
        return getRGBForSpiritual(getSpiritualRoots(entity));
    }

    public static Triple<Float, Float, Float> getRGBForSpiritual(Collection<ISpiritualType> roots) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (ISpiritualType root : roots) {
            r += ColorHelper.getRedFromRGB(root.getSpiritualColor());
            g += ColorHelper.getGreenFromRGB(root.getSpiritualColor());
            b += ColorHelper.getBlueFromRGB(root.getSpiritualColor());
        }
        final float multiply = 1F / 255 / roots.size();
        return Triple.of(r / multiply, g / multiply, b / multiply);
    }

    public static boolean isEntityValid(Entity entity) {
        return entity != null && entity.isAlive();
    }
}
