package hungteen.imm.common.entity.misc.talisman;

import hungteen.imm.api.entity.Cultivatable;
import hungteen.imm.api.cultivation.RealmType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-06-04 15:45
 **/
public abstract class TalismanEntity extends ThrowableItemProjectile implements Cultivatable {

    public TalismanEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    public RealmType getRealm() {
        return null;
    }

}
