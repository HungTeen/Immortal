package hungteen.imm.api.event;

import hungteen.imm.api.cultivation.RealmType;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/1 17:27
 **/
public class EntityRealmEvent extends EntityEvent {

    private final RealmType beforeRealm;
    protected RealmType afterRealm;

    public EntityRealmEvent(Entity entity, RealmType beforeRealm, RealmType afterRealm) {
        super(entity);
        this.beforeRealm = beforeRealm;
        this.afterRealm = afterRealm;
    }

    public RealmType getBeforeRealm() {
        return beforeRealm;
    }

    public RealmType getAfterRealm() {
        return afterRealm;
    }



    public static class Pre extends EntityRealmEvent implements ICancellableEvent {

        public Pre(Entity entity, RealmType beforeRealm, RealmType afterRealm) {
            super(entity, beforeRealm, afterRealm);
        }

        public void setAfterRealm(RealmType afterRealm) {
            this.afterRealm = afterRealm;
        }
    }

    public static class Post extends EntityRealmEvent {

        public Post(Entity entity, RealmType beforeRealm, RealmType afterRealm) {
            super(entity, beforeRealm, afterRealm);
        }

    }

}
