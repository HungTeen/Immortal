package hungteen.imm.common.advancement.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.util.Util;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-09-24 13:53
 **/
public class PlayerLearnSpellsTrigger extends SimpleCriterionTrigger<PlayerLearnSpellsTrigger.TriggerInstance> {

    public static final PlayerLearnSpellsTrigger INSTANCE = new PlayerLearnSpellsTrigger();
    static final ResourceLocation ID = Util.prefix("player_learn_spells");

    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player, int count) {
        this.trigger(player, (instance) -> instance.matches(player, count));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> playerPredicate, int count) implements SimpleCriterionTrigger.SimpleInstance  {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::playerPredicate),
                Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("count", 0).forGetter(TriggerInstance::count)
        ).apply(instance, TriggerInstance::new));

        public static TriggerInstance test(Optional<ContextAwarePredicate> playerPredicate, int count) {
            return new PlayerLearnSpellsTrigger.TriggerInstance(playerPredicate, count);
        }

        public static TriggerInstance test(int count) {
            return test(Optional.empty(), count);
        }

        public boolean matches(ServerPlayer player, int count) {
            return this.count == count;
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return playerPredicate;
        }
    }
}
