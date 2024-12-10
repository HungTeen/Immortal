package hungteen.imm.client.render.entity.human;

import hungteen.htlib.client.util.ModelLayerType;
import hungteen.imm.client.model.entity.human.PlayerLikeModel;
import hungteen.imm.client.render.IMMEntityRenderers;
import hungteen.imm.client.render.entity.layer.PlayerLikeArmorLayer;
import hungteen.imm.common.entity.human.cultivator.PlayerLikeEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/10 18:32
 **/
public class RealityPlayerRender<T extends PlayerLikeEntity> extends PlayerLikeRender<T> {

    public RealityPlayerRender(EntityRendererProvider.Context context) {
        super(
                context,
                new PlayerLikeModel<>(IMMEntityRenderers.REALITY_PLAYER.getPart(context, ModelLayerType.MAIN), false),
                new PlayerLikeModel<>(IMMEntityRenderers.REALITY_PLAYER.getPart(context, ModelLayerType.MAIN_SLIM), true)
        );
        this.addLayer(new PlayerLikeArmorLayer<>(
                        this,
                        new HumanoidModel<>(IMMEntityRenderers.REALITY_PLAYER.getPart(context, ModelLayerType.INNER_ARMOR)),
                        new HumanoidModel<>(IMMEntityRenderers.REALITY_PLAYER.getPart(context, ModelLayerType.OUTER_ARMOR)),
                        context.getModelManager(),
                        false
                )
        );
        this.addLayer(new PlayerLikeArmorLayer<>(
                        this,
                        new HumanoidModel<>(IMMEntityRenderers.REALITY_PLAYER.getPart(context, ModelLayerType.INNER_ARMOR_SLIM)),
                        new HumanoidModel<>(IMMEntityRenderers.REALITY_PLAYER.getPart(context, ModelLayerType.OUTER_ARMOR_SLIM)),
                        context.getModelManager(),
                        true
                )
        );
    }

}
