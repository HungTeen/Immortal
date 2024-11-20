package hungteen.imm.common.entity.misc.formation;

import hungteen.htlib.common.entity.HTEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/6/19 16:27
 */
public abstract class FormationEntity extends HTEntity {

    public FormationEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean canChangeDimensions(Level oldLevel, Level newLevel) {
        return false;
    }

    @Override
    protected float getGravityVelocity() {
        return 0F;
    }

}
