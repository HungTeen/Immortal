package hungteen.immortal.api.registry;

import hungteen.htlib.interfaces.INameEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 17:14
 *
 * 效果符文。
 **/
public interface IEffectRune extends INameEntry {

    default void effect(Object obj){
        if(obj instanceof Item){effectItem((Item) obj);}
        else if(obj instanceof Block){effectBlock((Block) obj);}
        else if(obj instanceof Entity){effectEntity((Entity) obj);}
    }

    /**
     * 对物品进行作用。
     */
    default void effectItem(Item item){

    }

    /**
     * 对方块进行作用。
     */
    default void effectBlock(Block block){

    }

    /**
     * 对实体进行作用。
     */
    default void effectEntity(Entity entity){

    }
}
