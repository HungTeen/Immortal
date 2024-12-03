package hungteen.imm.client.render.entity;

import hungteen.htlib.client.util.ModelLayer;
import hungteen.imm.api.cultivation.RealmLevel;
import hungteen.imm.client.model.entity.QiZombieModel;
import hungteen.imm.client.render.IMMEntityRenderers;
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
    private static final ResourceLocation RED_ZOMBIE_LOCATION = Util.get().entityTexture("undead/zombie/red_zombie");
    private static final ResourceLocation PVZ_ZOMBIE_LOCATION = Util.get().entityTexture("undead/zombie/pvz_zombie");

    public QiZombieRender(EntityRendererProvider.Context context) {
        super(context, new QiZombieModel<>(IMMEntityRenderers.QI_ZOMBIE.getPart(context, ModelLayer.MAIN)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(
                        this,
                        new QiZombieModel<>(IMMEntityRenderers.QI_ZOMBIE.getPart(context, ModelLayer.INNER_ARMOR)),
                        new QiZombieModel<>(IMMEntityRenderers.QI_ZOMBIE.getPart(context, ModelLayer.OUTER_ARMOR)),
                        context.getModelManager()
                )
        );
    }

    @Override
    public ResourceLocation getTextureLocation(QiZombie entity) {
        if (entity.hasCustomName() && "pvz_zombie".equals(entity.getName().getString())){
            return PVZ_ZOMBIE_LOCATION;
        }
        if(entity.getRealm().getRealmLevel().atLeast(RealmLevel.FOUNDATION)){
            return RED_ZOMBIE_LOCATION;
        }
        return VANILLA_ZOMBIE_LOCATION;
    }
}
