package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IRequirementType;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 16:28
 */
public record SpellRequirement(List<Pair<ISpellType, Integer>> spells) implements ILearnRequirement {

    public static final MapCodec<SpellRequirement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.mapPair(
                    SpellTypes.registry().byNameCodec().fieldOf("spell"),
                    Codec.intRange(0, Integer.MAX_VALUE).fieldOf("level")
            ).codec().listOf().optionalFieldOf("require_spells", List.of()).forGetter(SpellRequirement::spells)
    ).apply(instance, SpellRequirement::new));

    public static SpellRequirement single(ISpellType spell, int level) {
        return new SpellRequirement(List.of(Pair.of(spell, level)));
    }

    public static SpellRequirement pair(ISpellType spell1, int level1, ISpellType spell2, int level2) {
        return new SpellRequirement(List.of(Pair.of(spell1, level1), Pair.of(spell2, level2)));
    }

    @Override
    public boolean check(Level level, Player player) {
        return spells().stream().allMatch(p -> {
            return PlayerUtil.hasLearnedSpell(player, p.getFirst(), p.getSecond());
        });
    }

    @Override
    public void consume(Level level, Player player) {

    }

    @Override
    public MutableComponent getRequirementInfo(Player player) {
        MutableComponent component = TipUtil.misc("requirement.spell");
        for(int i = 0; i < spells().size(); i++) {
            component.append(SpellManager.spellName(spells().get(i).getFirst(), spells().get(i).getSecond()));
            if(i < spells().size() - 1) {
                component.append(", ");
            }
        }
        return component;
    }

    @Override
    public IRequirementType<?> getType() {
        return RequirementTypes.SPELL;
    }

}
