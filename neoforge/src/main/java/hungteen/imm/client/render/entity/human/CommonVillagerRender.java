package hungteen.imm.client.render.entity.human;

import hungteen.imm.common.entity.human.villager.CommonVillager;
import hungteen.imm.util.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-05-25 22:20
 **/
public class CommonVillagerRender extends IMMVillagerRender<CommonVillager> {

    private static final ResourceLocation TEXTURE = Util.get().entityTexture("villager/common");

    public CommonVillagerRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(CommonVillager villager) {
        return TEXTURE;
    }
}
