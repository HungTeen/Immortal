package hungteen.immortal.api.registry;

import hungteen.htlib.interfaces.INameEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-06 17:54
 *
 * 取值符文
 **/
public interface IGetterRune extends INameEntry {

    default Optional<Integer> getFrom(Object obj){
        if(obj instanceof Item){getFromItem((Item) obj);}
        else if(obj instanceof Block){getFromBlock((Block) obj);}
        else if(obj instanceof Entity){getFromEntity((Entity) obj);}
        return Optional.empty();
    }

    /**
     * 对物品进行取值。
     */
    default Optional<Integer> getFromItem(Item item){
        return Optional.empty();
    }

    /**
     * 对方块进行取值。
     */
    default Optional<Integer> getFromBlock(Block block){
        return Optional.empty();
    }

    /**
     * 对实体进行取值。
     */
    default Optional<Integer> getFromEntity(Entity entity){
        return Optional.empty();
    }
}
