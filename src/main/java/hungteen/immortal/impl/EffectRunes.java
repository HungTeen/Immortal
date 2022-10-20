package hungteen.immortal.impl;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.IEffectRune;
import hungteen.immortal.utils.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 18:02
 **/
public class EffectRunes {

    private static final List<IEffectRune> TYPES = new ArrayList<>();

    public static final IEffectRune ON_FIRE = new EffectRune("on_fire", (i) -> {}, (b) -> {}, (e) -> {
       e.setSecondsOnFire(100);
    });

    public record EffectRune(String name, Consumer<Item> itemConsumer,
                             Consumer<Block> blockConsumer,
                             Consumer<Entity> entityConsumer) implements IEffectRune {

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register() {
            TYPES.forEach(type -> ImmortalAPI.get().registerEffectRune(type));
        }

        public EffectRune(String name, Consumer<Item> itemConsumer, Consumer<Block> blockConsumer, Consumer<Entity> entityConsumer) {
            this.name = name;
            this.itemConsumer = itemConsumer;
            this.blockConsumer = blockConsumer;
            this.entityConsumer = entityConsumer;
            TYPES.add(this);
        }

        @Override
        public void effectItem(Item item) {
            this.itemConsumer.accept(item);
        }

        @Override
        public void effectBlock(Block block) {
            this.blockConsumer.accept(block);
        }

        @Override
        public void effectEntity(Entity entity) {
            this.entityConsumer.accept(entity);
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
