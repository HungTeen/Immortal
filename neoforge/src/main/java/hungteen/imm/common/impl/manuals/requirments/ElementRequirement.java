package hungteen.imm.common.impl.manuals.requirments;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.ILearnRequirement;
import hungteen.imm.api.spell.IRequirementType;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Set;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-17 22:46
 **/
public record ElementRequirement(List<Element> elements) implements ILearnRequirement {

    public static final MapCodec<ElementRequirement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Element.CODEC.listOf().fieldOf("elements").forGetter(ElementRequirement::elements)
    ).apply(instance, ElementRequirement::new));

    public static ElementRequirement create(Element element){
        return new ElementRequirement(List.of(element));
    }

    @Override
    public boolean check(Level level, Player player) {
        Set<Element> elements = PlayerUtil.getElements(player);
        return elements.containsAll(elements());
    }

    @Override
    public void consume(Level level, Player player) {

    }

    @Override
    public MutableComponent getRequirementInfo(Player player) {
        if(! elements().isEmpty()){
            MutableComponent component = ElementManager.name(elements().get(0));
            for(int i = 1; i < elements.size(); ++ i){
                component.append(", ").append(ElementManager.name(elements().get(i)));
            }
            return TipUtil.info("requirement.element", component);
        }
        return Component.empty();
    }

    @Override
    public IRequirementType<?> getType() {
        return RequirementTypes.ELEMENT;
    }
}
