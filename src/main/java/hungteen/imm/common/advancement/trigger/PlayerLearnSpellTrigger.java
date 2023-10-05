package hungteen.imm.common.advancement.trigger;

import com.google.gson.JsonObject;
import hungteen.imm.api.registry.IRealmType;
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
public class PlayerLearnSpellTrigger extends SimpleCriterionTrigger<PlayerLearnSpellTrigger.TriggerInstance> {

    public static final PlayerLearnSpellTrigger INSTANCE = new PlayerLearnSpellTrigger();
    static final ResourceLocation ID = Util.prefix("player_learn_spell");

    public ResourceLocation getId() {
        return ID;
    }

    protected PlayerLearnSpellTrigger.TriggerInstance createInstance(JsonObject jsonObject, ContextAwarePredicate predicate, DeserializationContext context) {
        final String spell = GsonHelper.getAsString(jsonObject, "spell");
        final int level = GsonHelper.getAsInt(jsonObject, "level", 1);
        return new PlayerLearnSpellTrigger.TriggerInstance(predicate, spell, level);
    }

    public void trigger(ServerPlayer player, ISpellType spell, int level) {
        this.trigger(player, (instance) -> instance.matches(player, spell.getRegistryName(), level));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final String realm;
        private final int level;

        public TriggerInstance(ContextAwarePredicate predicate, String realm, int level) {
            super(ID, predicate);
            this.realm = realm;
            this.level = level;
        }

        public static PlayerLearnSpellTrigger.TriggerInstance test(ContextAwarePredicate playerPredicate, String realm, int level) {
            return new PlayerLearnSpellTrigger.TriggerInstance(playerPredicate, realm, level);
        }

        public static PlayerLearnSpellTrigger.TriggerInstance test(ISpellType spell, int level) {
            return test(ContextAwarePredicate.ANY, spell.getRegistryName(), level);
        }

        public boolean matches(ServerPlayer player, String realm, int level) {
            return this.realm.equals(realm) && level == this.level;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject jsonobject = super.serializeToJson(context);
            jsonobject.addProperty("spell", this.realm);
            jsonobject.addProperty("level", this.level);
            return jsonobject;
        }

    }
}
