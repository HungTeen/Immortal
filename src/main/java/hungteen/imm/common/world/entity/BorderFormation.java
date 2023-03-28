package hungteen.imm.common.world.entity;

import hungteen.htlib.common.world.entity.DummyEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-18 22:55
 **/
public class BorderFormation extends Formation{

    public BorderFormation(DummyEntityType<?> entityType, Level level, int entityID, Vec3 position) {
        super(entityType, level, entityID, position);
    }

    public BorderFormation(DummyEntityType<?> entityType, Level level, CompoundTag tag) {
        super(entityType, level, tag);
    }

    @Override
    public double getMinY() {
        return this.getLevel().getMinBuildHeight();
    }

    @Override
    public double getMaxY() {
        return this.getLevel().getMaxBuildHeight();
    }
}
