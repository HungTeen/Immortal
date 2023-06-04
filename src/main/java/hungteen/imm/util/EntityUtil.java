package hungteen.imm.util;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.api.interfaces.IHasRoot;
import hungteen.imm.api.registry.ISpiritualType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 21:43
 **/
public class EntityUtil {

    public static boolean notConsume(Entity entity){
        return entity instanceof Player player && PlayerUtil.notConsume(player);
    }

    public static void playSound(Level level, Entity entity, SoundEvent soundEvent){
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), soundEvent, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    public static boolean isMainHolding(LivingEntity entity, Predicate<ItemStack> predicate) {
        return predicate.test(entity.getMainHandItem());
    }

    public static boolean isOffHolding(LivingEntity entity, Predicate<ItemStack> predicate) {
        return predicate.test(entity.getOffhandItem());
    }

    public static void multiblockSpawn(Level level, BlockPos blockPos, BlockPattern blockPattern, EntityType<?> entityType){
        if(! level.isClientSide){
            BlockPattern.BlockPatternMatch match = blockPattern.find(level, blockPos);
            if (match != null) {
                for(int i = 0; i < blockPattern.getHeight(); ++i) {
                    BlockInWorld blockinworld = match.getBlock(0, i, 0);
                    level.setBlock(blockinworld.getPos(), Blocks.AIR.defaultBlockState(), 2);
                    level.levelEvent(2001, blockinworld.getPos(), Block.getId(blockinworld.getState()));
                }

                Entity entity = entityType.create(level);
                BlockPos pos = match.getBlock(0, 2, 0).getPos();
                entity.moveTo((double)pos.getX() + 0.5D, (double)pos.getY() + 0.05D, (double)pos.getZ() + 0.5D, 0.0F, 0.0F);
                level.addFreshEntity(entity);

                for(ServerPlayer serverplayer : level.getEntitiesOfClass(ServerPlayer.class, entity.getBoundingBox().inflate(5.0D))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, entity);
                }

                for(int l = 0; l < blockPattern.getHeight(); ++l) {
                    BlockInWorld world = match.getBlock(0, l, 0);
                    level.blockUpdated(world.getPos(), Blocks.AIR);
                }
            }
        }
    }

    public static Triple<Float, Float, Float> getRGBForSpiritual(Entity entity){
        Collection<ISpiritualType> roots = new ArrayList<>();
        if(entity instanceof Player){
            roots = PlayerUtil.getSpiritualRoots((Player) entity);
        } else if(entity instanceof IHasRoot){
            roots = ((IHasRoot) entity).getSpiritualTypes();
        }
        return getRGBForSpiritual(roots);
    }

    public static Triple<Float, Float, Float> getRGBForSpiritual(Collection<ISpiritualType> roots){
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
