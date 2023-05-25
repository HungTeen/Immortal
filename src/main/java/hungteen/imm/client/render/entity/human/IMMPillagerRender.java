package hungteen.imm.client.render.entity.human;

import hungteen.imm.client.model.ModelLayers;
import hungteen.imm.client.model.entity.PillagerModel;
import hungteen.imm.common.entity.human.pillager.IMMPillager;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-22 22:21
 **/
public abstract class IMMPillagerRender<T extends IMMPillager> extends VillagerLikeRender<T, PillagerModel<T>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/illager/pillager.png");

    public IMMPillagerRender(EntityRendererProvider.Context context) {
        super(context, new PillagerModel<>(context.bakeLayer(ModelLayers.VILLAGER)));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }

}
