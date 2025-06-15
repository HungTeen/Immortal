package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.spell.ILearnRequirement;
import hungteen.imm.api.spell.IRequirementType;
import hungteen.imm.common.capability.player.IMMPlayerData;
import hungteen.imm.common.cultivation.spell.basic.ElementalMasterySpell;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-17 22:53
 **/
public record EMPRequirement(int level) implements ILearnRequirement {

    public static final MapCodec<EMPRequirement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("level").forGetter(EMPRequirement::level)
    ).apply(instance, EMPRequirement::new));

    @Override
    public boolean check(Level level, Player player) {
        return PlayerUtil.getIntegerData(player, IMMPlayerData.IntegerData.ELEMENTAL_MASTERY_POINTS) >= ElementalMasterySpell.requireEMP(player, level());
    }

    @Override
    public void consume(Level level, Player player) {
        PlayerUtil.addIntegerData(player, IMMPlayerData.IntegerData.ELEMENTAL_MASTERY_POINTS, - ElementalMasterySpell.requireEMP(player, level()));
    }

    @Override
    public MutableComponent getRequirementInfo(Player player) {
        return TipUtil.manual("requirement.emp", ElementalMasterySpell.requireEMP(player, level()));
    }

    @Override
    public IRequirementType<?> getType() {
        return RequirementTypes.EMP;
    }
}
