package hungteen.imm.common.entity;

import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.util.Util;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-26 18:03
 **/
public interface IMMAttributes {

    DeferredRegister<Attribute> ATTRIBUTES = EntityHelper.attribute().createRegister(Util.id());

    RegistryObject<Attribute> ELEMENT_DECAY_FACTOR = ATTRIBUTES.register("element_decay_factor", () -> {
        return new RangedAttribute("attribute.name.imm.element_decay_factor", 1.0D, 0.0D, 2.0D);
    });

    static void register(IEventBus bus){
        ATTRIBUTES.register(bus);
    }

}
