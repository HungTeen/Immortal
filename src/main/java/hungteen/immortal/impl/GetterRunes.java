package hungteen.immortal.impl;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.IGetterRune;
import hungteen.immortal.utils.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 18:16
 **/
public class GetterRunes {

    private static final List<IGetterRune> TYPES = new ArrayList<>();

    public static final IGetterRune GET_HEALTH = new GetterRune("get_health", i -> null, b -> null, e -> {
        if(e instanceof LivingEntity){
            return (int)((LivingEntity) e).getHealth();
        }
        return null;
    });

    public record GetterRune(String name, Function<Item, Integer> itemFunction,
                             Function<Block, Integer> blockFunction,
                             Function<Entity, Integer> entityFunction) implements IGetterRune {

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register() {
            TYPES.forEach(type -> ImmortalAPI.get().registerGetterRune(type));
        }

        public GetterRune(String name, Function<Item, Integer> itemFunction, Function<Block, Integer> blockFunction, Function<Entity, Integer> entityFunction) {
            this.name = name;
            this.itemFunction = itemFunction;
            this.blockFunction = blockFunction;
            this.entityFunction = entityFunction;
            TYPES.add(this);
        }

        @Override
        public Optional<Integer> getFromItem(Item item) {
            return Optional.ofNullable(this.itemFunction.apply(item));
        }

        @Override
        public Optional<Integer> getFromBlock(Block block) {
            return Optional.ofNullable(this.blockFunction.apply(block));
        }

        @Override
        public Optional<Integer> getFromEntity(Entity entity) {
            return Optional.ofNullable(this.entityFunction.apply(entity));
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getModID() {
            return Util.id();
        }

    }
}
