package hungteen.imm.client.render.entity;

import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.QiZombieModel;
import hungteen.imm.common.entity.undead.QiZombie;
import hungteen.imm.util.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-20 22:06
 **/
public class QiZombieRender extends HumanoidMobRenderer<QiZombie, QiZombieModel<QiZombie>> {

    private static final ResourceLocation VANILLA_ZOMBIE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/zombie/zombie.png");
    private static final ResourceLocation RED_ZOMBIE_LOCATION = Util.get().entityTexture("undead/zombie/red_zombie.png");
    private static final ResourceLocation PVZ_ZOMBIE_LOCATION = Util.get().entityTexture("undead/zombie/pvz_zombie.png");

    public QiZombieRender(EntityRendererProvider.Context context) {
        super(context, new QiZombieModel<>(context.bakeLayer(IMMModelLayers.QI_ZOMBIE)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(
                        this,
                        new QiZombieModel<>(context.bakeLayer(IMMModelLayers.QI_ZOMBIE_INNER_ARMOR)),
                        new QiZombieModel<>(context.bakeLayer(IMMModelLayers.QI_ZOMBIE_OUTER_ARMOR)),
                        context.getModelManager()
                )
        );
    }

    @Override
    public ResourceLocation getTextureLocation(QiZombie entity) {
        if (entity.hasCustomName() && "pvz_zombie".equals(entity.getName().getString())){
            return PVZ_ZOMBIE_LOCATION;
        }
        return VANILLA_ZOMBIE_LOCATION;
    }
}
