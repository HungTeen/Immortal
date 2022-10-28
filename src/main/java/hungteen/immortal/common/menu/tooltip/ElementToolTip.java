package hungteen.immortal.common.menu.tooltip;

import hungteen.htlib.util.Pair;
import hungteen.immortal.api.registry.ISpiritualRoot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-27 21:17
 **/
public class ElementToolTip implements TooltipComponent {

    public static final int TEXT_HEIGHT = 8;
    public static final int ICON_WIDTH = 10;
    public static final int SINGLE_HEIGHT = TEXT_HEIGHT + ICON_WIDTH;
    public static final int SINGLE_WIDTH = ICON_WIDTH + 4;

    private final Collection<Pair<ISpiritualRoot, Integer>> ingredients;

    public ElementToolTip(Collection<Pair<ISpiritualRoot, Integer>> ingredients){
        this.ingredients = ingredients;
    }

    public Collection<Pair<ISpiritualRoot, Integer>> getIngredients() {
        return Collections.unmodifiableCollection(ingredients);
    }

    public int getHeight(){
        return SINGLE_HEIGHT;
    }

    public int getWidth(){
        return this.ingredients.size() * SINGLE_WIDTH;
    }

}
