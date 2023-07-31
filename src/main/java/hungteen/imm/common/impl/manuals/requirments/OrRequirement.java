package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IRequirementType;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:50
 */
public record OrRequirement(List<Holder<ILearnRequirement>> requirements) implements ILearnRequirement{

    public static final Codec<OrRequirement> CODEC = RecordCodecBuilder.<OrRequirement>mapCodec(instance -> instance.group(
            LearnRequirements.getCodec().listOf().fieldOf("requirements").forGetter(OrRequirement::requirements)
    ).apply(instance, OrRequirement::new)).codec();

    @Override
    public boolean check(Level level, Player player) {
        return requirements().stream().anyMatch(r -> r.get().check(level, player));
    }

    @Override
    public void consume(Level level, Player player) {
    }

    @Override
    public IRequirementType<?> getType() {
        return RequirementTypes.OR;
    }
}
