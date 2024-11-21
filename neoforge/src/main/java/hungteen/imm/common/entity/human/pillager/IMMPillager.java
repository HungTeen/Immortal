package hungteen.imm.common.entity.human.pillager;

import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.entity.human.VillagerLikeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-11-17 10:50
 **/
public abstract class IMMPillager extends VillagerLikeEntity {

    public IMMPillager(EntityType<? extends HumanEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return HumanEntity.createAttributes()
                .add(Attributes.MAX_HEALTH, 35D)
                .add(Attributes.ATTACK_DAMAGE, 4D)
                .add(Attributes.MOVEMENT_SPEED, 0.33D);
    }



}
