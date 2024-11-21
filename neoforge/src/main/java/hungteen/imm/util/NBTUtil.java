package hungteen.imm.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-04-06 22:48
 **/
public class NBTUtil {

    /**
     * 返回的list类型为tag。
     */
    public static ListTag list(CompoundTag nbt, String name){
        return nbt.getList(name, Tag.TAG_COMPOUND);
    }

}
