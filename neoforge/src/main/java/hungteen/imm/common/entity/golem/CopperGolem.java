package hungteen.imm.common.entity.golem;

import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.cultivation.QiRootTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import java.util.Set;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-29 15:19
 **/
public class CopperGolem extends GolemEntity{

    public CopperGolem(EntityType<? extends GolemEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                ;
    }

    @Override
    public Set<QiRootType> getRoots() {
        return Set.of(QiRootTypes.METAL);
    }

    @Override
    public int getRuneInventorySize() {
        return 9;
    }
}
