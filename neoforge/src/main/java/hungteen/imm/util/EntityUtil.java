package hungteen.imm.util;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.common.entity.SeatEntity;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.api.entity.HasQi;
import hungteen.imm.api.entity.HasRoot;
import hungteen.imm.api.entity.SpellCaster;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.common.capability.IMMAttachments;
import hungteen.imm.common.capability.entity.IMMEntityData;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.event.EventHooks;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-20 21:43
 **/
public class EntityUtil {

    public static void playSound(Entity entity, SoundEvent sound, SoundSource soundSource) {
//        NetworkHelper.sendToClient(entity.level(), entity.position(), 64, new PlaySoundPacket(entity.blockPosition(), effectOn, soundSource));
    }

    public static void knockback(LivingEntity target, double strength, double dx, double dz){
        target.knockback(strength, dx, dz);
        if(target instanceof ServerPlayer serverPlayer){
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
        }
    }

    public static <T extends Entity> Optional<T> spawn(ServerLevel level, EntityType<T> entityType, Vec3 pos) {
        return spawn(level, entityType, pos, false);
    }

    public static <T extends Entity> Optional<T> spawn(ServerLevel level, EntityType<T> entityType, Vec3 pos, boolean checkPosition) {
        T entity = entityType.create(level);
        if (entity != null && (!checkPosition || (level.isUnobstructed(entity) && level.noCollision(entity)))) {
            if (entity instanceof Mob mob) {
                EventHooks.finalizeMobSpawn(mob, level, level.getCurrentDifficultyAt(BlockPos.containing(pos)), MobSpawnType.COMMAND, null);
            }
            entity.setPos(pos);
            level.addFreshEntityWithPassengers(entity);
            return Optional.of(entity);
        }
        return Optional.empty();
    }

    /**
     * @param attacker  考虑范围的中心，通常是攻击者。
     * @param predicate 搜索目标过滤。
     * @param consumer  根据目标到中心的距离，计算得到一个受伤程度。
     * @param <T>       搜索范围的实体类型。
     */
    public static <T extends Entity> void forRange(Entity attacker, Class<T> clazz, float width, float height, Predicate<T> predicate, BiConsumer<T, Float> consumer) {
        final AABB aabb = MathUtil.getUpperAABB(attacker.position(), width, height);
        EntityHelper.getPredicateEntities(attacker, aabb, clazz, predicate)
                .stream().map(target -> Pair.of(target, (float) Math.max(0, 1 - attacker.distanceToSqr(target) / (width * width)))).filter(pair -> pair.getSecond() > 0).forEach(pair -> {
                    consumer.accept(pair.getFirst(), pair.getSecond());
                });
    }

    public static void disableShield(Level level, Entity entity) {
        disableShield(level, entity, 100);
    }

    /**
     * 破盾。{@link Mob#doHurtTarget(Entity)}.
     *
     * @param entity 持盾的实体。
     * @param time   破盾作用时间。
     */
    public static void disableShield(Level level, Entity entity, int time) {
        if (entity instanceof Player player && ItemUtil.canShieldBlock(player.getUseItem())) {
            player.getCooldowns().addCooldown(player.getUseItem().getItem(), time);
            player.stopUsingItem();
            level.broadcastEntityEvent(entity, (byte) 30);
        }
    }

    public static Entity ownerOrSelf(Entity entity) {
        if (entity instanceof TraceableEntity traceableEntity && traceableEntity.getOwner() != null) {
            return traceableEntity.getOwner();
        }
        if (entity instanceof OwnableEntity ownableEntity && ownableEntity.getOwner() != null) {
            return ownableEntity.getOwner();
        }
        return entity;
    }

    /**
     * 发射子弹。
     *
     * @param projectile 子弹。
     * @param vec        子弹发射方向。
     * @param speed      子弹速度。
     * @param variance   子弹精确度，越高越打不准。
     */
    public static void shootProjectile(Projectile projectile, Vec3 vec, float speed, float variance) {
        projectile.shoot(vec.x(), vec.y() + MathUtil.horizontalLength(vec) * 0.2F, vec.z(), speed, variance);
    }

    public static AABB getEntityAABB(Entity entity, Vec3 offset) {
        return getEntityAABB(entity).move(offset);
    }

    public static AABB getEntityAABB(Entity entity) {
        final float offset = entity.getBbWidth() / 2.0F;
        return new AABB(entity.getX() - offset, entity.getY(), entity.getZ() - offset, entity.getX() + offset, entity.getY() + entity.getBbHeight(), entity.getZ() + offset);
    }

    public static boolean addItem(Entity entity, ItemStack stack) {
        if (entity instanceof Player player) {
            return PlayerUtil.addItem(player, stack);
        } else if (entity instanceof InventoryCarrier carrier) {
            final ItemStack result = carrier.getInventory().addItem(stack);
            if (!result.isEmpty()) {
                entity.spawnAtLocation(result);
                return false;
            }
            return true;
        } else if (entity instanceof LivingEntity living) {
            if (living.getMainHandItem().isEmpty()) {
                living.setItemInHand(InteractionHand.MAIN_HAND, stack);
                return true;
            } else if (living.getOffhandItem().isEmpty()) {
                living.setItemInHand(InteractionHand.OFF_HAND, stack);
                return true;
            }
        }
        return false;
    }

    public static boolean sitToMeditate(LivingEntity living, BlockPos pos, float yOffset, boolean relyOnBlock) {
        if (EntityUtil.hasItemInHand(living)) {
            if (living instanceof Player player) {
                PlayerHelper.sendTipTo(player, TipUtil.info("spell.has_item_in_hand").withStyle(ChatFormatting.RED));
            }
            return false;
        }
        return SeatEntity.seatAt(living.level(), living, pos, yOffset, living.getYRot(), 120F, relyOnBlock);
    }

    public static HitResult getHitResult(Entity entity, ClipContext.Block blockMode, ClipContext.Fluid fluidMode) {
        final double range = 20;
        //TODO 神识决定距离。
        return getHitResult(entity, blockMode, fluidMode, range);
    }

    public static HitResult getHitResult(Entity entity, ClipContext.Block blockMode, ClipContext.Fluid fluidMode, double distance) {
        final Vec3 startVec = entity.getEyePosition(1.0F);
        final Vec3 lookVec = entity.getViewVector(1.0F);
        Vec3 endVec = startVec.add(lookVec.scale(distance));
        final BlockHitResult blockHitResult = entity.level().clip(new ClipContext(startVec, endVec, blockMode, fluidMode, entity));
        if (blockHitResult.getType() != HitResult.Type.MISS) {
            endVec = blockHitResult.getLocation();
        }
        final AABB aabb = entity.getBoundingBox().inflate(distance);
        final EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(entity.level(), entity, startVec, endVec, aabb, (target) -> {
            return !target.isSpectator();
        });
        return entityHitResult != null ? entityHitResult : blockHitResult;
    }

    public static Optional<InteractionHand> getEmptyHand(LivingEntity living) {
        return living.getMainHandItem().isEmpty() ? Optional.of(InteractionHand.MAIN_HAND) : living.getOffhandItem().isEmpty() ? Optional.of(InteractionHand.OFF_HAND) : Optional.empty();
    }

    public static boolean hasEmptyHand(LivingEntity living) {
        return living.getMainHandItem().isEmpty() || living.getOffhandItem().isEmpty();
    }

    public static boolean hasItemInHand(LivingEntity living) {
        return !living.getMainHandItem().isEmpty() || !living.getOffhandItem().isEmpty();
    }

    public static boolean checkHand(LivingEntity living, InteractionHand hand, Predicate<ItemStack> predicate) {
        return predicate.test(living.getItemInHand(hand));
    }

    public static Optional<InteractionHand> getHandOpt(LivingEntity living, Predicate<ItemStack> predicate) {
        return predicate.test(living.getMainHandItem()) ? Optional.of(InteractionHand.MAIN_HAND) : predicate.test(living.getOffhandItem()) ? Optional.of(InteractionHand.OFF_HAND) : Optional.empty();
    }

    public static ItemStack getItemInHand(LivingEntity living, Predicate<ItemStack> predicate) {
        return getHandItemOpt(living, predicate).orElse(ItemStack.EMPTY);
    }

    public static Optional<ItemStack> getHandItemOpt(LivingEntity living, Predicate<ItemStack> predicate) {
        return getHandOpt(living, predicate).map(living::getItemInHand);
    }

    public static int getSpellLevel(Entity entity, SpellType spell) {
        return entity instanceof Player player ? PlayerUtil.getSpellLevel(player, spell) : entity instanceof SpellCaster e ? e.getSpellLevel(spell) : 0;
    }

    public static boolean hasLearnedSpell(Entity entity, SpellType spell, int level) {
        return entity instanceof Player player ? PlayerUtil.hasLearnedSpell(player, spell, level) : entity instanceof SpellCaster e && e.hasLearnedSpell(spell, level);
    }

    public static boolean hasLearnedSpell(Entity entity, SpellType spell) {
        return hasLearnedSpell(entity, spell, 1);
    }

    public static float getMana(Entity entity) {
        return entity instanceof Player player ? PlayerUtil.getQiAmount(player) : entity instanceof HasQi manaEntity ? manaEntity.getQiAmount() : 0;
    }

    public static void addMana(Entity entity, float amount) {
        if (entity instanceof Player player) {
            PlayerUtil.addQiAmount(player, amount);
        } else if (entity instanceof HasQi manaEntity) {
            manaEntity.addQiAmount(amount);
        }
    }

    public static boolean hasMana(Entity entity) {
        return getMana(entity) > 0;
    }

    public static boolean isManaFull(Entity entity) {
        if (entity instanceof Player player) {
            return PlayerUtil.isQiFull(player);
        } else if (entity instanceof HasQi manaEntity) {
            return manaEntity.isQiFull();
        }
        return true;
    }

    /* Data Operations */

    public static IMMEntityData getData(Entity entity) {
        return entity.getData(IMMAttachments.ENTITY_DATA);
    }

    @Deprecated(since = "0.2.0", forRemoval = true)
    public static Optional<IMMEntityData> getDataOpt(Entity entity) {
        return Optional.of(getData(entity));
    }

    public static <U> U getData(Entity entity, Function<IMMEntityData, U> function) {
        return function.apply(getData(entity));
    }

    public static void setData(Entity entity, Consumer<IMMEntityData> consumer) {
        consumer.accept(getData(entity));
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

    public static List<QiRootType> getRoots(Entity entity) {
        return getRoots(entity, null);
    }

    /**
     * 是否有玩家视角，限制部分灵根的显示。
     *
     * @param viewPlayer 玩家的视角。
     */
    public static List<QiRootType> getRoots(Entity entity, @Nullable Player viewPlayer) {
        if (viewPlayer == null) {
            return entity instanceof Player player ? PlayerUtil.getRoots(player)
                    : entity instanceof HasRoot iHasRoot ? iHasRoot.getRoots().stream().toList() : List.of();
        }
        return PlayerUtil.filterSpiritRoots(viewPlayer, getRoots(entity));
    }

    public static List<Element> getElements(Entity entity) {
        return getElements(entity, null);
    }

    /**
     * 是否有玩家视角，限制部分元素的显示。
     *
     * @param viewPlayer 玩家的视角。
     */
    public static List<Element> getElements(Entity entity, @Nullable Player viewPlayer) {
        return viewPlayer == null ? getRoots(entity).stream().flatMap(l -> l.getElements().stream()).collect(Collectors.toList()) : PlayerUtil.filterElements(viewPlayer, getElements(entity));
    }

    public static Triple<Float, Float, Float> getRGBForSpiritual(Entity entity) {
        return getRGBForSpiritual(getRoots(entity));
    }

    public static Triple<Float, Float, Float> getRGBForSpiritual(Collection<QiRootType> roots) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (QiRootType root : roots) {
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
