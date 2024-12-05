package hungteen.imm.common.cultivation.reaction;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.common.cultivation.ElementManager;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * (强土 + 其他元素)为镀光反应。
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/28 22:34
 **/
public class GildingReaction extends ElementReactionImpl {

    private static final List<GildingReaction> REACTIONS = new ArrayList<>();
    private final Element element;
    private final float amount;

    public GildingReaction(String name, float mainAmount, Element off, float percent) {
        super(name, false, 150, List.of(new ElementEntry(Element.EARTH, true, mainAmount), new ElementEntry(off, false, mainAmount * percent)));
        this.element = off;
        this.amount = mainAmount * percent;
        REACTIONS.add(this);
    }

    public static void ifGlidingActive(Entity attacker, Entity target){
        REACTIONS.forEach(reaction -> {
            ElementManager.ifActiveReaction(attacker, reaction, (action, scale) -> {
                ElementManager.addElementAmount(target, reaction.element, false, reaction.amount * scale * 0.6F);
            });
        });
    }

    @Override
    public void doReaction(Entity entity, float scale) {

    }
}
