package hungteen.imm.common.cultivation.manual.requirement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.spell.LearnRequirement;
import hungteen.imm.api.spell.RequirementType;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 15:50
 */
public record NotRequirement(LearnRequirement requirement) implements LearnRequirement {

    public static final MapCodec<NotRequirement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RequirementTypes.getCodec().fieldOf("requirement").forGetter(NotRequirement::requirement)
    ).apply(instance, NotRequirement::new));

    @Override
    public boolean check(Level level, Player player) {
        return ! requirement().check(level, player);
    }

    @Override
    public void consume(Level level, Player player) {
    }

    @Override
    public MutableComponent getRequirementInfo(Player player) {
        return TipUtil.manual("requirement.not").append("{").append(requirement().getRequirementInfo(player)).append("}");
    }

    @Override
    public RequirementType<?> getType() {
        return RequirementTypes.NOT;
    }
}
