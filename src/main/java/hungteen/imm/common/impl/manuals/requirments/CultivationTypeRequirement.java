package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IRequirementType;
import hungteen.imm.common.impl.registry.CultivationTypes;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 16:02
 */
public record CultivationTypeRequirement(ICultivationType cultivationType) implements ILearnRequirement {

    public static final Codec<CultivationTypeRequirement> CODEC = RecordCodecBuilder.<CultivationTypeRequirement>mapCodec(instance -> instance.group(
            CultivationTypes.registry().byNameCodec().fieldOf("cultivation_type").forGetter(CultivationTypeRequirement::cultivationType)
    ).apply(instance, CultivationTypeRequirement::new)).codec();

    @Override
    public boolean check(Level level, Player player) {
        return PlayerUtil.getCultivationType(player).equals(cultivationType());
    }

    @Override
    public void consume(Level level, Player player) {

    }

    @Override
    public MutableComponent getRequirementInfo(Player player) {
        return cultivationType().getComponent();
    }

    @Override
    public IRequirementType<?> getType() {
        return RequirementTypes.CULTIVATION_TYPE;
    }
}
