package hungteen.imm.common.world.entity;

import hungteen.htlib.common.world.entity.DummyEntity;
import hungteen.htlib.common.world.entity.DummyEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-12-04 19:33
 **/
public abstract class Formation extends DummyEntity {

    protected double width = 0.5D;
    protected double height = 0.5D;

    public Formation(DummyEntityType<?> entityType, Level level, int entityID, Vec3 position) {
        super(entityType, level, entityID, position);
    }

    public Formation(DummyEntityType<?> entityType, Level level, CompoundTag tag) {
        super(entityType, level, tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Width")) {
            this.setWidth(tag.getDouble("Width"));
        }
        if (tag.contains("Height")) {
            this.setHeight(tag.getDouble("Height"));
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putDouble("Width", this.getWidth());
        tag.putDouble("Height", this.getHeight());
        return super.save(tag);
    }

    @Override
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
