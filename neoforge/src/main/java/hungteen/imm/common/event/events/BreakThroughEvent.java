package hungteen.imm.common.event.events;

import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.world.entity.trial.BreakThroughTrial;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/10 23:02
 **/
public class BreakThroughEvent extends LivingEvent {

    private final RealmType realm;

    public BreakThroughEvent(LivingEntity entity, RealmType realm) {
        super(entity);
        this.realm = realm;
    }

    public static class Start extends BreakThroughEvent implements ICancellableEvent {

        private BreakThroughTrial trial;

        public Start(LivingEntity entity, BreakThroughTrial trial) {
            super(entity, trial.getRealmType());
            this.trial = trial;
        }

        public void setTrial(BreakThroughTrial trial) {
            this.trial = trial;
        }

        public BreakThroughTrial getTrial() {
            return trial;
        }

    }

    public static class Success extends BreakThroughEvent {

        public Success(LivingEntity entity, RealmType realm) {
            super(entity, realm);
        }

    }

    public static class Failure extends BreakThroughEvent {

        public Failure(LivingEntity entity, RealmType realm) {
            super(entity, realm);
        }

    }


}
