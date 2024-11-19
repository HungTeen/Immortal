package hungteen.imm.common.advancement.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.util.Util;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

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

    public void trigger(ServerPlayer player, IRealmType realm) {
        this.trigger(player, (instance) -> instance.matches(player, realm));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> playerPredicate, IRealmType realm) implements SimpleCriterionTrigger.SimpleInstance  {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::playerPredicate),
                RealmTypes.registry().byNameCodec().fieldOf("realm").forGetter(TriggerInstance::realm)
        ).apply(instance, TriggerInstance::new));

        public static PlayerRealmChangeTrigger.TriggerInstance test(Optional<ContextAwarePredicate> playerPredicate, IRealmType realm) {
            return new PlayerRealmChangeTrigger.TriggerInstance(playerPredicate, realm);
        }

        public static PlayerRealmChangeTrigger.TriggerInstance test(IRealmType realm) {
            return test(Optional.empty(), realm);
        }

        public boolean matches(ServerPlayer player, IRealmType realm) {
            return this.realm.equals(realm);
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return playerPredicate();
        }
    }
}
