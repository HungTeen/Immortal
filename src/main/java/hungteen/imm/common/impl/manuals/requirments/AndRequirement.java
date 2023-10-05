package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IRequirementType;
import hungteen.imm.util.TipUtil;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:50
 */
public record AndRequirement(List<ILearnRequirement> requirements) implements ILearnRequirement{

    public static final Codec<AndRequirement> CODEC = RecordCodecBuilder.<AndRequirement>mapCodec(instance -> instance.group(
            RequirementTypes.getCodec().listOf().fieldOf("requirements").forGetter(AndRequirement::requirements)
    ).apply(instance, AndRequirement::new)).codec();

    @Override
    public boolean check(Level level, Player player) {
        return requirements().stream().allMatch(r -> r.check(level, player));
    }

    @Override
    public void consume(Level level, Player player) {
        requirements().forEach(r -> r.consume(level, player));
    }

    @Override
    public MutableComponent getRequirementInfo(Player player) {
        MutableComponent component = Component.literal("");
        for(int i = 0; i < requirements().size(); ++ i){
            component.append("{").append(requirements().get(i).getRequirementInfo(player)).append("}");
            if(i < requirements().size() - 1) component.append(TipUtil.misc("requirement.and"));
        }
        return component;
    }

    @Override
    public IRequirementType<?> getType() {
        return RequirementTypes.AND;
    }
}
