package hungteen.imm.common.cultivation.manual.requirement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.spell.LearnRequirement;
import hungteen.imm.api.spell.RequirementType;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.common.cultivation.SpellManager;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 16:28
 */
public record SpellRequirement(List<Spell> spells) implements LearnRequirement {

    public static final MapCodec<SpellRequirement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Spell.CODEC.listOf().optionalFieldOf("require_spells", List.of()).forGetter(SpellRequirement::spells)
    ).apply(instance, SpellRequirement::new));

    public static SpellRequirement single(SpellType spell, int level) {
        return new SpellRequirement(List.of(Spell.create(spell, level)));
    }

    public static SpellRequirement pair(SpellType spell1, int level1, SpellType spell2, int level2) {
        return new SpellRequirement(List.of(Spell.create(spell1, level1), Spell.create(spell2, level2)));
    }

    @Override
    public boolean check(Level level, Player player) {
        return spells().stream().allMatch(p -> {
            return PlayerUtil.hasLearnedSpell(player, p.spell(), p.level());
        });
    }

    @Override
    public void consume(Level level, Player player) {

    }

    @Override
    public MutableComponent getRequirementInfo(Player player) {
        MutableComponent component = TipUtil.manual("requirement.spell");
        for(int i = 0; i < spells().size(); i++) {
            component.append(SpellManager.spellName(spells().get(i).spell(), spells().get(i).level()));
            if(i < spells().size() - 1) {
                component.append(", ");
            }
        }
        return component;
    }

    @Override
    public RequirementType<?> getType() {
        return RequirementTypes.SPELL;
    }

}
