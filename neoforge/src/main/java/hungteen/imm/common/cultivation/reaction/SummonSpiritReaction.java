package hungteen.imm.common.cultivation.reaction;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.entity.creature.spirit.ElementSpirit;
import hungteen.imm.util.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.function.Supplier;

/**
 * 化灵反应。
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/28 22:34
 **/
public class SummonSpiritReaction<T extends ElementSpirit> extends ElementReactionImpl {

    private final Supplier<EntityType<T>> spiritType;
    private final Element element;
    private final float amount;
    private final float spiritAmount;

    public SummonSpiritReaction(String name, Supplier<EntityType<T>> spiritType, Element element, float amount){
        this(name, spiritType, element, amount, 0.5F);
    }

    public SummonSpiritReaction(String name, Supplier<EntityType<T>> spiritType, Element element, float amount, float percent) {
        super(name, true, 200, List.of(
                new ElementEntry(Element.SPIRIT, true, amount * percent),
                new ElementEntry(element, true, amount)
        ));
        this.spiritType = spiritType;
        this.element = element;
        this.amount = amount;
        this.spiritAmount = amount * percent;
    }

    @Override
    public void doReaction(Entity entity, float scale) {
        if(entity.level() instanceof ServerLevel serverLevel){
            EntityUtil.spawn(serverLevel, this.spiritType.get(), entity.position()).ifPresent(spirit -> {
                ElementManager.addElementAmount(spirit, Element.SPIRIT, false, scale * spiritAmount);
                ElementManager.addElementAmount(spirit, element, false, scale * amount);
            });
        }
    }
}
