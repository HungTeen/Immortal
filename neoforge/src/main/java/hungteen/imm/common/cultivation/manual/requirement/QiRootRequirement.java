package hungteen.imm.common.cultivation.manual.requirement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.spell.LearnRequirement;
import hungteen.imm.api.spell.RequirementType;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-17 21:21
 **/
public record QiRootRequirement(List<QiRootType> roots) implements LearnRequirement {

    public static final MapCodec<QiRootRequirement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            QiRootTypes.registry().byNameCodec().listOf().optionalFieldOf("roots", List.of()).forGetter(QiRootRequirement::roots)
    ).apply(instance, QiRootRequirement::new));

    public static QiRootRequirement single(QiRootType root){
        return new QiRootRequirement(List.of(root));
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
        if(! roots().isEmpty()){
            MutableComponent component = roots().get(0).getComponent();
            for(int i = 1; i < roots().size(); i++) {
                component.append(", ").append(roots().get(i).getComponent());
            }
            return TipUtil.manual("requirement.root", component);
        }
        return Component.empty();
    }

    @Override
    public RequirementType<?> getType() {
        return RequirementTypes.QI_ROOT;
    }
}
