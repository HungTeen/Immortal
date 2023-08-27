package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IRequirementType;
import hungteen.imm.common.ElementManager;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-17 22:46
 **/
public record ElementRequirement(Elements element) implements ILearnRequirement {

    public static final Codec<ElementRequirement> CODEC = RecordCodecBuilder.<ElementRequirement>mapCodec(instance -> instance.group(
            Elements.CODEC.fieldOf("element").forGetter(ElementRequirement::element)
    ).apply(instance, ElementRequirement::new)).codec();

    public static ElementRequirement create(Elements element){
        return new ElementRequirement(element);
    }

    @Override
    public boolean check(Level level, Player player) {
        return PlayerUtil.getSpiritualRoots(player).stream().anyMatch(root -> root.getElements().contains(element()));
    }

    @Override
    public void consume(Level level, Player player) {

    }

    @Override
    public MutableComponent getRequirementInfo(Player player) {
        return TipUtil.misc("requirement.element", ElementManager.name(element()));
    }

    @Override
    public IRequirementType<?> getType() {
        return RequirementTypes.ELEMENT;
    }
}
