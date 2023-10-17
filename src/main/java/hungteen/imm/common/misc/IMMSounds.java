package hungteen.imm.common.misc;

import hungteen.htlib.util.helper.registry.SoundHelper;
import hungteen.imm.util.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/14 19:08
 **/
public interface IMMSounds {

    DeferredRegister<SoundEvent> SOUNDS = SoundHelper.get().createRegister(Util.id());

    RegistryObject<SoundEvent> BI_FANG_AMBIENT = create("bi_fang_ambient");
    RegistryObject<SoundEvent> BI_FANG_ROAR = create("bi_fang_roar");
    RegistryObject<SoundEvent> BI_FANG_FLAP = create("bi_fang_flap");

    static RegistryObject<SoundEvent> create(String name){
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(Util.prefix(name)));
    }

    static void register(IEventBus event){
        SOUNDS.register(event);
    }
}
