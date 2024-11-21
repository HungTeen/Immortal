package hungteen.imm.common.entity.undead;

import hungteen.imm.api.interfaces.IUndead;
import hungteen.imm.common.entity.IMMMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-19 23:11
 **/
public abstract class UndeadEntity extends IMMMob implements IUndead, Enemy {

    public UndeadEntity(EntityType<? extends UndeadEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE);
    }

}
