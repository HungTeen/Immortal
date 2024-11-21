package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IRequirementType;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.impl.registry.QiRootTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-17 21:21
 **/
public record SpiritualRootRequirement(List<QiRootType> roots) implements ILearnRequirement {

    public static final MapCodec<SpiritualRootRequirement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            QiRootTypes.registry().byNameCodec().listOf().optionalFieldOf("roots", List.of()).forGetter(SpiritualRootRequirement::roots)
    ).apply(instance, SpiritualRootRequirement::new));

    public static SpiritualRootRequirement single(QiRootType root){
        return new SpiritualRootRequirement(List.of(root));
    }

    @Override
    public boolean check(Level level, Player player) {
        return roots().stream().allMatch(r -> {
            return PlayerUtil.hasRoot(player, r);
        });
    }

    @Override
    public void consume(Level level, Player player) {

    }

    @Override
    public MutableComponent getRequirementInfo(Player player) {
        MutableComponent component = TipUtil.misc("requirement.root");
        for(int i = 0; i < roots().size(); i++) {
            component.append(roots().get(i).getComponent());
            if(i < roots().size() - 1) {
                component.append(", ");
            }
        }
        return component;
    }

    @Override
    public IRequirementType<?> getType() {
        return RequirementTypes.SPIRITUAL_ROOT;
    }
}
