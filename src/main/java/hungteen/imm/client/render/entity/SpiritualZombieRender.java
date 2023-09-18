package hungteen.imm.client.render.entity;

import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.SpiritualZombieModel;
import hungteen.imm.common.entity.undead.SpiritualZombie;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 22:06
 **/
public class SpiritualZombieRender extends HumanoidMobRenderer<SpiritualZombie, SpiritualZombieModel<SpiritualZombie>> {

    private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation("textures/entity/zombie/zombie.png");

    public SpiritualZombieRender(EntityRendererProvider.Context context) {
        super(context, new SpiritualZombieModel<>(context.bakeLayer(IMMModelLayers.SPIRITUAL_ZOMBIE)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(
                        this,
                        new SpiritualZombieModel<>(context.bakeLayer(IMMModelLayers.SPIRITUAL_ZOMBIE_INNER_ARMOR)),
                        new SpiritualZombieModel<>(context.bakeLayer(IMMModelLayers.SPIRITUAL_ZOMBIE_OUTER_ARMOR)),
                        context.getModelManager()
                )
        );
    }

    @Override
    public ResourceLocation getTextureLocation(SpiritualZombie entity) {
        return ZOMBIE_LOCATION;
    }
}
