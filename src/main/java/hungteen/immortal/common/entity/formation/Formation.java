package hungteen.immortal.common.entity.formation;

import hungteen.htlib.entity.HTEntity;
import hungteen.immortal.common.world.data.Formations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-18 22:53
 **/
public abstract class Formation extends HTEntity {

    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(Formation.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HEIGHT = SynchedEntityData.defineId(Formation.class, EntityDataSerializers.FLOAT);

    public Formation(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(RADIUS, 20F);
        entityData.define(HEIGHT, 20F);
    }

    @Override
    public void baseTick() {
        super.baseTick();
    }

    @Override
    public void onFirstSpawn() {
        if (this.level instanceof ServerLevel) {
            Formations.addFormation((ServerLevel) this.level, this);
        }
    }

    public void onCollideWith(Entity entity) {

    }

    public boolean isWithinBounds(Vec3 position, double leastDistance) {
        return position.x >= this.getMinX() - leastDistance && position.x <= this.getMaxX() + leastDistance &&
                position.z >= this.getMinZ() - leastDistance && position.z <= this.getMaxZ() + leastDistance &&
                position.y >= this.getMinY() - leastDistance && position.y <= this.getMaxY() + leastDistance;
    }

    public boolean isCloseToBorder(Entity entity, double leastDistance) {
        return this.getDistanceToBorder(entity.position()) < leastDistance;
    }

    public boolean isInsideCloseToBorder(Entity entity, AABB aabb) {
        final double leastDistance = leastDistance(aabb);
        return this.isCloseToBorder(entity, leastDistance * 2) && this.isWithinBounds(entity.position(), leastDistance);
    }

    public VoxelShape getFormationCollisionShape() {
        final VoxelShape shape = Shapes.box(this.getMinX(), this.getMinY(), this.getMinZ(), this.getMaxX(), this.getMaxY(), this.getMaxZ());
        return this.isTrapFormation() ? shape : Shapes.join(Shapes.INFINITY, shape, BooleanOp.ONLY_FIRST);
    }

    protected static double leastDistance(AABB aabb) {
        return Math.max(Mth.absMax(aabb.getYsize(), Mth.absMax(aabb.getXsize(), aabb.getZsize())), 1.0D);
    }

    /**
     * Distance to border.
     */
    public double getDistanceToBorder(Vec3 position) {
        return Math.min(
                Math.min(
                        Math.min(Math.abs(position.z - this.getMinZ()), Math.abs(position.z - this.getMaxZ())),
                        Math.min(Math.abs(position.x - this.getMinX()), Math.abs(position.x - this.getMaxX()))
                ),
                Math.min(Math.abs(position.y - this.getMinY()), Math.abs(position.y - this.getMaxY()))
        );
    }

    /**
     * 困阵和防御法阵相反。
     * Trap formation is opposite to defence formation.
     */
    public boolean isTrapFormation() {
        return false;
    }

    @Override
    public void remove(RemovalReason removalReason) {
        if (this.level instanceof ServerLevel) {
            Formations.removeFormation((ServerLevel) this.level, this);
        }
        super.remove(removalReason);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Radius")) {
            this.setRadius(tag.getFloat("Radius"));
        }
        if (tag.contains("Height")) {
            this.setHeight(tag.getFloat("Height"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Radius", this.getRadius());
        tag.putFloat("Height", this.getHeight());
    }

    public double getMinX() {
        return this.getX() - getRadius();
    }

    public double getMinZ() {
        return this.getZ() - getRadius();
    }

    public double getMaxX() {
        return this.getX() + getRadius();
    }

    public double getMaxZ() {
        return this.getZ() + getRadius();
    }

    public double getMinY() {
        return this.getY();
    }

    public double getMaxY() {
        return this.getY() + getHeight();
    }

    public float getRadius() {
        return entityData.get(RADIUS);
    }

    public void setRadius(float radius) {
        entityData.set(RADIUS, radius);
    }

    public float getHeight() {
        return entityData.get(HEIGHT);
    }

    public void setHeight(float height) {
        entityData.set(HEIGHT, height);
    }

}
