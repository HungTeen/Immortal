package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.cultivation.Element;
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
public record ElementRequirement(Element element) implements ILearnRequirement {

    public static final MapCodec<ElementRequirement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Element.CODEC.fieldOf("element").forGetter(ElementRequirement::element)
    ).apply(instance, ElementRequirement::new));

    public static ElementRequirement create(Element element){
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
        return TipUtil.misc("requirement.element", ElementManager.name(element()).getString());
    }

    @Override
    public IRequirementType<?> getType() {
        return RequirementTypes.ELEMENT;
    }
}
