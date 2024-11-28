package hungteen.imm.common.entity.misc.formation;

import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.world.levelgen.IMMLevels;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/6/19 16:38
 */
public class TeleportFormation extends FormationEntity {

    private static final float TELEPORT_WIDTH = 4F;
    private static final float TELEPORT_HEIGHT = 3F;
    private static final float TELEPORT_CD = 100;
    private final HashMap<Integer, Integer> entityInsideTicks = new HashMap<>();
    private int remainExistTick = 0;

    public TeleportFormation(EntityType<?> type, Level level) {
        super(type, level);
    }

    public TeleportFormation(Level level, BlockPos pos, int remainExistTick) {
        super(IMMEntities.TELEPORT_FORMATION.get(), level);
        this.setPos(MathHelper.toVec3(pos.below()));
        this.setRemainExistTick(remainExistTick);
    }

    @Override
    public void tick() {
        super.tick();
        if (EntityHelper.isServer(this) && level() instanceof ServerLevel serverLevel) {
            if (--this.remainExistTick < 0) {
                this.discard();
            } else {
                final AABB aabb = new AABB(this.position().add(-TELEPORT_WIDTH, -TELEPORT_HEIGHT, -TELEPORT_WIDTH), this.position().add(TELEPORT_WIDTH, TELEPORT_HEIGHT, TELEPORT_WIDTH));
                // Update ticks.
                Iterator<Map.Entry<Integer, Integer>> iterator = entityInsideTicks.entrySet().iterator();
                final MinecraftServer server = serverLevel.getServer();
                final ResourceKey<Level> dst = EntityHelper.inDimension(this, IMMLevels.EAST_WORLD) ? Level.OVERWORLD : IMMLevels.EAST_WORLD;
                final ServerLevel dstLevel = server.getLevel(dst);
                while (iterator.hasNext()) {
                    final Map.Entry<Integer, Integer> pair = iterator.next();
                    final int id = pair.getKey();
                    final int tick = pair.getValue();
                    final Entity entity = this.level().getEntity(id);
                    // Not in same dimension.
                    if (!EntityHelper.isEntityValid(entity) || !this.level().dimension().equals(entity.level().dimension())) {
                        iterator.remove();
                    } else {
                        // Still inside.
                        final int ticks = tick + (aabb.intersects(entity.getBoundingBox()) ? 1 : -2);
                        if (ticks > TELEPORT_CD) {
                            if (dstLevel != null) {
                                entity.setPortalCooldown();
                                entity.changeDimension(new DimensionTransition(serverLevel, entity, DimensionTransition.PLAY_PORTAL_SOUND));
                            }
                        } else {
                            this.spawnTeleportParticles(entity, ticks);
                        }
                        pair.setValue(ticks);
                    }
                }
                // Find new inside entities.
                if (this.random.nextFloat() < 0.1F) {
                    EntityHelper.getPredicateEntities(this, aabb, Entity.class, e -> e.canChangeDimensions(level(), dstLevel)).forEach(entity -> {
                        if (!entityInsideTicks.containsKey(entity.getId())) {
                            entityInsideTicks.put(entity.getId(), 0);
                        }
                    });
                }
            }
        } else {
            if (random.nextInt(150) == 0) {
                level().playLocalSound(getX(), getY(), getZ(), SoundEvents.PORTAL_AMBIENT, SoundSource.AMBIENT, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
            }
            ParticleHelper.spawnParticles(this.level(), IMMParticles.QI.get(), getX(), getY(), getZ(), 5, TELEPORT_WIDTH, TELEPORT_HEIGHT, 0.2F, 0.1F);
        }
    }

    private void spawnTeleportParticles(Entity entity, int ticks) {
        final int cnt = ticks / 8 + 1;
        ParticleHelper.spawnParticles(this.level(), IMMParticles.QI.get(), getX(), getY(), getZ(), cnt, 0.5, 0.25, 0.2F, 0.1F);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("RemainExistTick")) {
            this.remainExistTick = tag.getInt("RemainExistTick");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("RemainExistTick", this.remainExistTick);
    }

    public void setRemainExistTick(int remainExistTick) {
        this.remainExistTick = remainExistTick;
    }


}
