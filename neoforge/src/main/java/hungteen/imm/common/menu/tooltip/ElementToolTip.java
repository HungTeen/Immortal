package hungteen.imm.common.menu.tooltip;

import com.mojang.datafixers.util.Pair;
import hungteen.imm.api.cultivation.QiRootType;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.Collection;
import java.util.Collections;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-27 21:17
 **/
public class ElementToolTip implements TooltipComponent {

    public static final int TEXT_HEIGHT = 8;
    public static final int ICON_WIDTH = 10;
    public static final int SINGLE_HEIGHT = TEXT_HEIGHT + ICON_WIDTH;
    public static final int SINGLE_WIDTH = ICON_WIDTH + 4;

    private final Collection<Pair<QiRootType, Integer>> ingredients;

    public ElementToolTip(Collection<Pair<QiRootType, Integer>> ingredients){
        this.ingredients = ingredients;
    }

    public Collection<Pair<QiRootType, Integer>> getIngredients() {
        return Collections.unmodifiableCollection(ingredients);
    }

    public int getHeight(){
        return SINGLE_HEIGHT;
    }

    public int getWidth(){
        return this.ingredients.size() * SINGLE_WIDTH;
    }

}
