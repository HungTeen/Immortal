package hungteen.imm.common.advancement.trigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.spell.SpellTypes;
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
public class PlayerLearnSpellTrigger extends SimpleCriterionTrigger<PlayerLearnSpellTrigger.TriggerInstance> {

    public static final PlayerLearnSpellTrigger INSTANCE = new PlayerLearnSpellTrigger();
    static final ResourceLocation ID = Util.prefix("player_learn_spell");

    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player, ISpellType spell, int level) {
        this.trigger(player, (instance) -> instance.matches(player, spell, level));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> playerPredicate, ISpellType spell, int level) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::playerPredicate),
                SpellTypes.registry().byNameCodec().fieldOf("spell").forGetter(TriggerInstance::spell),
                Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("level", 0).forGetter(TriggerInstance::level)
        ).apply(instance, TriggerInstance::new));

        public static PlayerLearnSpellTrigger.TriggerInstance test(Optional<ContextAwarePredicate> playerPredicate, ISpellType spell, int level) {
            return new PlayerLearnSpellTrigger.TriggerInstance(playerPredicate, spell, level);
        }

        public static PlayerLearnSpellTrigger.TriggerInstance test(ISpellType spell, int level) {
            return test(Optional.empty(), spell, level);
        }

        public boolean matches(ServerPlayer player, ISpellType spell, int level) {
            return this.spell().equals(spell) && level == this.level;
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return playerPredicate;
        }
    }
}
