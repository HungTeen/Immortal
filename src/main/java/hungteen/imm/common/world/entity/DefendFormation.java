package hungteen.imm.common.world.entity;

import hungteen.htlib.common.world.entity.DummyEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-12-04 19:38
 **/
public class DefendFormation extends Formation{
    public DefendFormation(DummyEntityType<?> entityType, Level level, int entityID, Vec3 position) {
        super(entityType, level, entityID, position);
    }

    public DefendFormation(DummyEntityType<?> entityType, Level level, CompoundTag tag) {
        super(entityType, level, tag);
    }
}
