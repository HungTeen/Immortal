package hungteen.imm.common.cultivation.reaction;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.common.cultivation.ElementManager;
import net.minecraft.world.entity.Entity;

import java.util.List;

/**
 * 相克反应。
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/28 22:30
 **/
public abstract class InhibitionReaction extends ElementReactionImpl {

    private final Element ingredient;
    private final Element production;
    private final float mainAmount;
    private final float offAmount;
    private final boolean spawnAmethyst;

    public InhibitionReaction(String name, boolean once, Element main, float mainAmount, Element off, float offAmount) {
        super(name, once, 80, List.of(new ElementEntry(main, false, mainAmount), new ElementEntry(off, false, offAmount)));
        this.ingredient = main;
        this.production = off;
        this.mainAmount = mainAmount;
        this.offAmount = offAmount;
        this.spawnAmethyst = (main == Element.EARTH || off == Element.EARTH);
    }

    @Override
    public void doReaction(Entity entity, float scale) {
        if (this.spawnAmethyst && canSpawnAmethyst(entity)) {
            this.spawnAmethyst(entity).ifPresent(elementAmethyst -> {
                final float multiplier = scale * (this.once() ? 1 : 20);
                ElementManager.addElementAmount(elementAmethyst, this.ingredient, true, this.mainAmount * multiplier);
                ElementManager.addElementAmount(elementAmethyst, this.production, true, this.offAmount * multiplier);
                entity.level().addFreshEntity(elementAmethyst);
            });
        }
    }
}
