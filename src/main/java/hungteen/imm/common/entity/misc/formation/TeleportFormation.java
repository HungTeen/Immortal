package hungteen.imm.common.entity.misc.formation;

import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.world.levelgen.IMMLevels;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/19 16:38
 */
public class TeleportFormation extends FormationEntity {

    private static final float TELEPORT_WIDTH = 4F;
    private static final float TELEPORT_HEIGHT = 3F;
    private static final float TELEPORT_CD = 30;
    private final HashMap<Integer, Integer> entityInsideTicks = new HashMap<>();
    private int remainExistTick = 0;

    public TeleportFormation(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();
        if(! this.level.isClientSide && level instanceof ServerLevel serverLevel){
            if(-- this.remainExistTick < 0){
                this.discard();
            } else {
                final AABB aabb = new AABB(this.position().add(- TELEPORT_WIDTH, - TELEPORT_HEIGHT, - TELEPORT_WIDTH), this.position().add(TELEPORT_WIDTH, TELEPORT_HEIGHT, TELEPORT_WIDTH));
                // Update ticks.
                entityInsideTicks.keySet().forEach(id -> {
                    final Entity entity = this.level.getEntity(id);
                    // Not in same dimension.
                    if(! EntityHelper.isEntityValid(entity) || ! this.level.dimension().equals(entity.level.dimension())){
                        entityInsideTicks.remove(id);
                    } else {
                        // Still inside.
                        final int ticks = entityInsideTicks.get(id) + (aabb.intersects(entity.getBoundingBox()) ? 1 : -2);
                        if(ticks > TELEPORT_CD){
                            final MinecraftServer server = serverLevel.getServer();
                            final ResourceKey<Level> dst = this.level.dimension() == IMMLevels.EAST_WORLD ? Level.OVERWORLD : IMMLevels.EAST_WORLD;
                            final ServerLevel dstLevel = server.getLevel(dst);
                            if(dstLevel != null){
                                this.level.getProfiler().push("portal");
                                this.setPortalCooldown();
                                this.changeDimension(dstLevel);
                                this.level.getProfiler().pop();
                            }
                        } else {
                            this.spawnTeleportParticles(entity, ticks);
                        }
                        entityInsideTicks.put(id, ticks);
                    }
                });
                // Find new inside entities.
                if(this.random.nextFloat() < 0.1F){
                    EntityHelper.getPredicateEntities(this, aabb, Entity.class, Entity::canChangeDimensions).forEach(entity -> {
                        if(! entityInsideTicks.containsKey(entity.getId())){
                            entityInsideTicks.put(entity.getId(), 0);
                        }
                    });
                }
            }
        } else {
            if (random.nextInt(150) == 0) {
                level.playLocalSound(getX(), getY(), getZ(), SoundEvents.PORTAL_AMBIENT, SoundSource.AMBIENT, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
            }
            for(int i = 0; i < 5; ++ i){
                final Vec3 pos = position().add(RandomHelper.doubleRange(this.random, TELEPORT_WIDTH), RandomHelper.doubleRange(this.random, TELEPORT_HEIGHT), RandomHelper.doubleRange(this.random, TELEPORT_WIDTH));
                ParticleHelper.spawnRandomSpeedParticle(this.level, IMMParticles.SPIRIT.get(), pos, 0.2F, 0.1F);
            }
        }
    }

    private void spawnTeleportParticles(Entity entity, int ticks){
        final int cnt = ticks / 8 + 1;
        for(int i = 0; i < cnt; ++i) {
            final Vec3 pos = new Vec3(entity.getRandomX(0.5D), entity.getRandomY() - 0.25D, entity.getRandomZ(0.5D));
            ParticleHelper.spawnRandomSpeedParticle(this.level, IMMParticles.SPIRIT.get(), pos, 0.2F, 0.1F);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("RemainExistTick")){
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
