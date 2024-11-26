package hungteen.imm.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/26 16:41
 **/
public interface HTPlayerData extends INBTSerializable<CompoundTag> {

    void syncToClient();

    boolean isServer();

}
