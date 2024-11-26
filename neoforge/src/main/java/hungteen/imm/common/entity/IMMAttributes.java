package hungteen.imm.common.entity;

import hungteen.htlib.api.registry.PTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-26 18:03
 **/
public interface IMMAttributes {

    HTVanillaRegistry<Attribute> ATTRIBUTES = HTRegistryManager.vanilla(Registries.ATTRIBUTE, Util.id());

    PTHolder<Attribute> MAX_QI_AMOUNT = ATTRIBUTES.registerForHolder("max_qi_amount", () -> {
        return new RangedAttribute("attribute.name.imm.max_qi_amount", 0.0D, 0.0D, Double.MAX_VALUE);
    });

    PTHolder<Attribute> ELEMENT_DECAY_FACTOR = ATTRIBUTES.registerForHolder("element_decay_factor", () -> {
        return new RangedAttribute("attribute.name.imm.element_decay_factor", 1.0D, 0.0D, 2.0D);
    });

    static void initialize(IEventBus bus){
        NeoHelper.initRegistry(ATTRIBUTES, bus);
    }

}
