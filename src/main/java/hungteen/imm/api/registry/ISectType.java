package hungteen.imm.api.registry;

import hungteen.htlib.api.interfaces.ISimpleEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-12-04 22:56
 **/
public interface ISectType extends ISimpleEntry {

    @Override
    default MutableComponent getComponent() {
        return Component.translatable("sect." + getModID() +"." + getName());
    }

}
