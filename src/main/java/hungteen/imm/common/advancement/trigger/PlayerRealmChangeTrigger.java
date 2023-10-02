package hungteen.imm.common.advancement.trigger;

import com.google.gson.JsonObject;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.util.Util;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-09-24 13:53
 **/
public class PlayerRealmChangeTrigger extends SimpleCriterionTrigger<PlayerRealmChangeTrigger.TriggerInstance> {

    public static final PlayerRealmChangeTrigger INSTANCE = new PlayerRealmChangeTrigger();
    static final ResourceLocation ID = Util.prefix("player_realm_change");

    public ResourceLocation getId() {
        return ID;
    }

    protected PlayerRealmChangeTrigger.TriggerInstance createInstance(JsonObject jsonObject, ContextAwarePredicate predicate, DeserializationContext context) {
        final String realm = GsonHelper.getAsString(jsonObject, "realm");
        return new PlayerRealmChangeTrigger.TriggerInstance(predicate, realm);
    }

    public void trigger(ServerPlayer player, IRealmType realm) {
        this.trigger(player, (instance) -> instance.matches(player, realm.getRegistryName()));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final String realm;

        public TriggerInstance(ContextAwarePredicate predicate, String realm) {
            super(ID, predicate);
            this.realm = realm;
        }

        public static PlayerRealmChangeTrigger.TriggerInstance test(ContextAwarePredicate playerPredicate, String realm) {
            return new PlayerRealmChangeTrigger.TriggerInstance(playerPredicate, realm);
        }

        public static PlayerRealmChangeTrigger.TriggerInstance test(IRealmType realm) {
            return test(ContextAwarePredicate.ANY, realm.getRegistryName());
        }

        public boolean matches(ServerPlayer player, String realm) {
            return this.realm.equals(realm);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject jsonobject = super.serializeToJson(context);
            jsonobject.addProperty("realm", this.realm);
            return jsonobject;
        }

    }
}
