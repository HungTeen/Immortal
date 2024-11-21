package hungteen.imm.common.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/29 14:31
 **/
public class IMMSpawnEggItem extends DeferredSpawnEggItem {


    @Nullable
    private String name;

    public IMMSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Properties props) {
        super(type, backgroundColor, highlightColor, props);
    }

    @Override
    protected String getOrCreateDescriptionId() {
        if(name == null){
            name = this.getDefaultType().getDescriptionId() + ".egg";
        }
        return name;
    }
}
