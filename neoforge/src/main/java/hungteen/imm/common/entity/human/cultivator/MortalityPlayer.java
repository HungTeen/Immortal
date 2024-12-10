package hungteen.imm.common.entity.human.cultivator;

import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.entity.human.HumanLikeEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.Set;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/10 20:38
 **/
public class MortalityPlayer extends PlayerLikeEntity {

    public MortalityPlayer(EntityType<? extends HumanLikeEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {

    }

    @Override
    public Set<QiRootType> getRoots() {
        return Set.of();
    }
}
