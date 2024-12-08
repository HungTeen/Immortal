package hungteen.imm.client.render;

import hungteen.htlib.client.render.ClientEntitySuits;
import hungteen.htlib.client.render.HTClientEntitySuit;
import hungteen.htlib.client.util.ModelLayerType;
import hungteen.htlib.common.registry.suit.HTEntitySuit;
import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.human.ChillagerModel;
import hungteen.imm.client.model.entity.human.CultivatorModel;
import hungteen.imm.client.model.entity.undead.QiSkeletonModel;
import hungteen.imm.client.model.entity.undead.QiZombieModel;
import hungteen.imm.client.render.entity.human.ChillagerRender;
import hungteen.imm.client.render.entity.human.CultivatorRender;
import hungteen.imm.client.render.entity.misc.FallingIceRender;
import hungteen.imm.client.render.entity.misc.TwistingVinesRender;
import hungteen.imm.client.render.entity.undead.QiSkeletonRender;
import hungteen.imm.client.render.entity.undead.QiZombieRender;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.human.cultivator.WanderingCultivator;
import hungteen.imm.common.entity.human.pillager.Chillager;
import hungteen.imm.common.entity.misc.FallingIceEntity;
import hungteen.imm.common.entity.misc.TwistingVines;
import hungteen.imm.common.entity.undead.QiSkeleton;
import hungteen.imm.common.entity.undead.QiZombie;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/2 22:23
 **/
public interface IMMEntityRenderers {

    /* Misc */

    HTClientEntitySuit<FallingIceEntity> FALLING_ICE = registerSimple(IMMEntities.FALLING_ICE, FallingIceRender::new);
    HTClientEntitySuit<TwistingVines> TWISTING_VINES = registerSimple(IMMEntities.TWISTING_VINES, TwistingVinesRender::new);

    /* Human */

    HTClientEntitySuit<WanderingCultivator> WANDERING_CULTIVATOR = register(IMMEntities.WANDERING_CULTIVATOR, builder -> builder
            .renderer(CultivatorRender::new)
            .mainLayer(() -> CultivatorModel.createBodyLayer(CubeDeformation.NONE, false))
            .layer(ModelLayerType.INNER_ARMOR, IMMModelLayers::innerArmor)
            .layer(ModelLayerType.OUTER_ARMOR, IMMModelLayers::outerArmor)
            .layer(ModelLayerType.MAIN_SLIM, () -> CultivatorModel.createBodyLayer(CubeDeformation.NONE, true))
            .layer(ModelLayerType.INNER_ARMOR_SLIM, IMMModelLayers::innerArmor)
            .layer(ModelLayerType.OUTER_ARMOR_SLIM, IMMModelLayers::outerArmor)
    );
    HTClientEntitySuit<Chillager> CHILLAGER = register(IMMEntities.CHILLAGER, builder -> builder
            .renderer(ChillagerRender::new)
            .mainLayer(ChillagerModel::createBodyLayer)
    );

    /* Undead */

    HTClientEntitySuit<QiZombie> QI_ZOMBIE = register(IMMEntities.QI_ZOMBIE, builder -> builder
            .renderer(QiZombieRender::new)
            .mainLayer(QiZombieModel::createBodyLayer)
            .layer(ModelLayerType.INNER_ARMOR, IMMModelLayers::innerArmor)
            .layer(ModelLayerType.OUTER_ARMOR, IMMModelLayers::outerArmor)
    );

    HTClientEntitySuit<QiSkeleton> QI_SKELETON = register(IMMEntities.QI_SKELETON, builder -> builder
            .renderer(QiSkeletonRender::new)
            .mainLayer(QiSkeletonModel::createBodyLayer)
            .layer(ModelLayerType.INNER_ARMOR, IMMModelLayers::innerArmor)
            .layer(ModelLayerType.OUTER_ARMOR, IMMModelLayers::outerArmor)
    );

    static <T extends Entity> HTClientEntitySuit<T> registerSimple(HTEntitySuit<T> suit, EntityRendererProvider<T> provider){
        return ClientEntitySuits.register(ClientEntitySuits.builder(suit).renderer(provider).build());
    }

    static <T extends Entity> HTClientEntitySuit<T> register(HTEntitySuit<T> suit, Consumer<HTClientEntitySuit.EntitySuitBuilder<T>> consumer){
        HTClientEntitySuit.EntitySuitBuilder<T> builder = ClientEntitySuits.builder(suit);
        consumer.accept(builder);
        return ClientEntitySuits.register(builder.build());
    }

    static void initialize(){

    }
}
