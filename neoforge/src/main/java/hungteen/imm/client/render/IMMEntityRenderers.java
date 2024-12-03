package hungteen.imm.client.render;

import hungteen.htlib.client.render.ClientEntitySuits;
import hungteen.htlib.client.render.HTClientEntitySuit;
import hungteen.htlib.client.util.ModelLayer;
import hungteen.htlib.common.registry.suit.HTEntitySuit;
import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.entity.QiZombieModel;
import hungteen.imm.client.render.entity.QiZombieRender;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.undead.QiZombie;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/2 22:23
 **/
public interface IMMEntityRenderers {

    HTClientEntitySuit<QiZombie> QI_ZOMBIE = register(IMMEntities.QI_ZOMBIE, builder -> builder
            .renderer(QiZombieRender::new)
            .mainLayer(QiZombieModel::createBodyLayer)
            .layer(ModelLayer.INNER_ARMOR, () -> IMMModelLayers.INNER_ARMOR)
            .layer(ModelLayer.OUTER_ARMOR, () -> IMMModelLayers.OUTER_ARMOR)
    );

    static <T extends Entity> HTClientEntitySuit<T> register(HTEntitySuit<T> suit, Consumer<HTClientEntitySuit.EntitySuitBuilder<T>> consumer){
        HTClientEntitySuit.EntitySuitBuilder<T> builder = ClientEntitySuits.builder(suit);
        consumer.accept(builder);
        return ClientEntitySuits.register(builder.build());
    }

    static void initialize(){

    }
}
