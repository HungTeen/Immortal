package hungteen.immortal.client.model.bake;

import hungteen.immortal.utils.ItemUtil;
import net.minecraftforge.client.event.ModelEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-10 19:39
 **/
public class ImmortalBakeModels {

    public static final String INVENTORY = "inventory";

    /**
     * {@link hungteen.immortal.client.ClientRegister#bakeModel(ModelEvent.BakingCompleted)}
     */
    public static void registerBakeModels(ModelEvent.BakingCompleted event){
        ItemUtil.getLargeHeldItems().forEach(item -> {
            event.getModels().put(LargeHeldItemBakeModel.getModelLocation(item), new LargeHeldItemBakeModel(item, event.getModelManager()));
        });
    }

    /**
     * {@link hungteen.immortal.client.ClientRegister#bakeModel(ModelEvent.RegisterAdditional)}
     */
    public static void registerBakeModels(ModelEvent.RegisterAdditional event){
        ItemUtil.getLargeHeldItems().forEach(item -> {
            event.register(LargeHeldItemBakeModel.getHeldModelLocation(item));
        });
    }
}
