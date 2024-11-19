package hungteen.imm.common.advancement.trigger;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-09-24 13:53
 **/
public class SpiritualPearlTrigger {
//        extends SimpleCriterionTrigger<SpiritualPearlTrigger.TriggerInstance> {
//
//    public static final SpiritualPearlTrigger INSTANCE = new SpiritualPearlTrigger();
//    static final ResourceLocation ID = Util.prefix("spiritual_pearl");
//
//    public ResourceLocation getId() {
//        return ID;
//    }
//
//    protected SpiritualPearlTrigger.TriggerInstance createInstance(JsonObject jsonObject, ContextAwarePredicate predicate, DeserializationContext context) {
//        final EntityPredicate entityPredicate = EntityPredicate.fromJson(jsonObject.get("entity"));
//        final int count = GsonHelper.getAsInt(jsonObject, "count", 0);
//        return new SpiritualPearlTrigger.TriggerInstance(predicate, entityPredicate, count);
//    }
//
//    public void trigger(ServerPlayer player, Entity target, int rootCount) {
//        this.trigger(player, (instance) -> instance.matches(player, target, rootCount));
//    }
//
//    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
//
//        private final EntityPredicate entity;
//        private final int count;
//
//        public TriggerInstance(ContextAwarePredicate predicate, EntityPredicate entity, int count) {
//            super(ID, predicate);
//            this.entity = entity;
//            this.count = count;
//        }
//
//        public static SpiritualPearlTrigger.TriggerInstance test(ContextAwarePredicate playerPredicate, EntityPredicate targetPredicate, int count) {
//            return new SpiritualPearlTrigger.TriggerInstance(playerPredicate, targetPredicate, count);
//        }
//
//        public static SpiritualPearlTrigger.TriggerInstance test(EntityPredicate entity, int count) {
//            return test(ContextAwarePredicate.ANY, entity, count);
//        }
//
//        public static SpiritualPearlTrigger.TriggerInstance test(EntityType<?> type, int count) {
//            return test(ContextAwarePredicate.ANY, new EntityPredicate.Builder().of(type).build(), count);
//        }
//
//        public boolean matches(ServerPlayer player, Entity target, int count) {
//            return this.entity.matches(player, target) && this.count == count;
//        }
//
//        @Override
//        public JsonObject serializeToJson(SerializationContext context) {
//            JsonObject jsonobject = super.serializeToJson(context);
//            jsonobject.add("entity", this.entity.serializeToJson());
//            jsonobject.addProperty("count", this.count);
//            return jsonobject;
//        }
//
//    }
}
