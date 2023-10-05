package hungteen.imm.common.advancement.trigger;

import com.google.gson.JsonObject;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.util.Util;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-09-24 13:53
 **/
public class PlayerLearnSpellsTrigger extends SimpleCriterionTrigger<PlayerLearnSpellsTrigger.TriggerInstance> {

    public static final PlayerLearnSpellsTrigger INSTANCE = new PlayerLearnSpellsTrigger();
    static final ResourceLocation ID = Util.prefix("player_learn_spells");

    public ResourceLocation getId() {
        return ID;
    }

    protected PlayerLearnSpellsTrigger.TriggerInstance createInstance(JsonObject jsonObject, ContextAwarePredicate predicate, DeserializationContext context) {
        final int count = GsonHelper.getAsInt(jsonObject, "count", 0);
        return new PlayerLearnSpellsTrigger.TriggerInstance(predicate, count);
    }

    public void trigger(ServerPlayer player, int count) {
        this.trigger(player, (instance) -> instance.matches(player, count));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final int count;

        public TriggerInstance(ContextAwarePredicate predicate, int count) {
            super(ID, predicate);
            this.count = count;
        }

        public static PlayerLearnSpellsTrigger.TriggerInstance test(ContextAwarePredicate playerPredicate, int count) {
            return new PlayerLearnSpellsTrigger.TriggerInstance(playerPredicate, count);
        }

        public static PlayerLearnSpellsTrigger.TriggerInstance test(int count) {
            return test(ContextAwarePredicate.ANY, count);
        }

        public boolean matches(ServerPlayer player, int count) {
            return this.count == count;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject jsonobject = super.serializeToJson(context);
            jsonobject.addProperty("count", this.count);
            return jsonobject;
        }

    }
}
