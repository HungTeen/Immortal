package hungteen.imm.common.entity.golem;

import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-29 15:19
 **/
public class CopperGolem extends GolemEntity{

    public CopperGolem(EntityType<? extends GolemEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .build();
    }

    @Override
    public List<ISpiritualType> getSpiritualTypes() {
        return List.of(SpiritualTypes.METAL);
    }

    @Override
    public int getRuneInventorySize() {
        return 9;
    }
}
