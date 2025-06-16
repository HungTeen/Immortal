package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.cultivation.CultivationType;
import hungteen.imm.api.spell.LearnRequirement;
import hungteen.imm.api.spell.RequirementType;
import hungteen.imm.common.cultivation.CultivationTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 16:02
 */
public record CultivationTypeRequirement(CultivationType cultivationType) implements LearnRequirement {

    public static final MapCodec<CultivationTypeRequirement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CultivationTypes.registry().byNameCodec().fieldOf("cultivation_type").forGetter(CultivationTypeRequirement::cultivationType)
    ).apply(instance, CultivationTypeRequirement::new));

    public static LearnRequirement create(CultivationType type){
        return new CultivationTypeRequirement(type);
    }

    @Override
    public boolean check(Level level, Player player) {
        return PlayerUtil.getCultivationType(player).equals(cultivationType());
    }

    @Override
    public void consume(Level level, Player player) {

    }

    @Override
    public MutableComponent getRequirementInfo(Player player) {
        return TipUtil.manual("requirement.cultivation_type", cultivationType().getComponent());
    }

    @Override
    public RequirementType<?> getType() {
        return RequirementTypes.CULTIVATION_TYPE;
    }
}
