package hungteen.imm.common.world.entity;

import hungteen.htlib.common.world.entity.DummyEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-12-04 19:39
 **/
public class TrapFormation extends Formation{
    public TrapFormation(DummyEntityType<?> entityType, ServerLevel level, Vec3 position) {
        super(entityType, level, position);
    }

    public TrapFormation(DummyEntityType<?> entityType, Level level, CompoundTag tag) {
        super(entityType, level, tag);
    }
}
