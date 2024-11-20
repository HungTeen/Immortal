package hungteen.imm.client.model.bake;

import hungteen.imm.client.ClientUtil;
import hungteen.imm.common.impl.manuals.SecretManuals;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.util.ItemUtil;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-10 19:39
 **/
public class IMMBakeModels {

    public static final String INVENTORY = "inventory";

    /**
     * {@link hungteen.imm.client.ClientRegister#bakeModel(ModelEvent.ModifyBakingResult)}
     */
    public static void registerBakeModels(ModelEvent.ModifyBakingResult event){
        ItemUtil.getLargeHeldItems().forEach(item -> {
            event.getModels().put(LargeHeldItemBakeModel.getModelLocation(item), new LargeHeldItemBakeModel(item, event.getModels()));
        });
        {
            final ModelResourceLocation key = ClientUtil.getModelLocation(IMMItems.SECRET_MANUAL.getRegistryName());
            final BakedModel oldModel = event.getModels().get(key);
            if (oldModel != null) {
                event.getModels().put(key, new SecretManualModel(oldModel, event.getModelBakery()));
            }
        }
    }

    /**
     * {@link hungteen.imm.client.ClientRegister#bakeModel(ModelEvent.RegisterAdditional)}
     */
    public static void registerBakeModels(ModelEvent.RegisterAdditional event){
        ItemUtil.getLargeHeldItems().forEach(item -> {
            event.register(LargeHeldItemBakeModel.getHeldModelLocation(item));
        });
        Optional.ofNullable(ClientUtil.level()).ifPresent(level -> {
            SecretManuals.registry().getValues(level).forEach(manual -> {
                event.register(ClientUtil.getModelLocation(manual.model()));
            });
        });
    }
}
