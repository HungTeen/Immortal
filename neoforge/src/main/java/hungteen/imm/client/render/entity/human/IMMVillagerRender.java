package hungteen.imm.client.render.entity.human;

import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.villager.VillagerModel;
import hungteen.imm.common.entity.human.villager.IMMVillager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-22 22:21
 **/
public abstract class IMMVillagerRender<T extends IMMVillager> extends VillagerLikeRender<T, VillagerModel<T>> {

    public IMMVillagerRender(EntityRendererProvider.Context context) {
        super(context, new VillagerModel<>(context.bakeLayer(IMMModelLayers.VILLAGER)));
    }

}
