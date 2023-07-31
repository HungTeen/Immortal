package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IRequirementType;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:50
 */
public record NotRequirement(Holder<ILearnRequirement> requirement) implements ILearnRequirement{

    public static final Codec<NotRequirement> CODEC = RecordCodecBuilder.<NotRequirement>mapCodec(instance -> instance.group(
            LearnRequirements.getCodec().fieldOf("requirement").forGetter(NotRequirement::requirement)
    ).apply(instance, NotRequirement::new)).codec();

    @Override
    public boolean check(Level level, Player player) {
        return ! requirement().get().check(level, player);
    }

    @Override
    public void consume(Level level, Player player) {
    }

    @Override
    public IRequirementType<?> getType() {
        return RequirementTypes.NOT;
    }
}
