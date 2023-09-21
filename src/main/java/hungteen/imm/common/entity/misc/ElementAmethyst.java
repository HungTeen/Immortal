package hungteen.imm.common.entity.misc;

import hungteen.htlib.common.entity.HTEntity;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.common.ElementManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-09-18 12:49
 **/
public class ElementAmethyst extends HTEntity {

    public ElementAmethyst(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        this.tickMove();
        if(EntityHelper.isServer(this) && (this.tickCount & 1) == 0){
            if(! ElementManager.hasElement(this, Elements.EARTH, true)){
                this.discard();
            }
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}
