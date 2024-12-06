package hungteen.imm.common.misc;

import hungteen.htlib.api.registry.HTHolder;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.common.impl.registry.HTVanillaRegistry;
import hungteen.htlib.util.NeoHelper;
import hungteen.imm.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/14 19:08
 **/
public interface IMMSounds {

    HTVanillaRegistry<SoundEvent> SOUNDS = HTRegistryManager.vanilla(Registries.SOUND_EVENT, Util.id());

    /* Spell */

    HTHolder<SoundEvent> RELEASING = create("releasing");

    /* Item */

    HTHolder<SoundEvent> TALISMAN_USE = create("talisman_use");

    /* Entity */

    HTHolder<SoundEvent> PVZ_ZOMBIE_AMBIENT = create("pvz_zombie_ambient");

    HTHolder<SoundEvent> FALLING_ICE_FINISH = create("falling_ice_finish");
    HTHolder<SoundEvent> FALLING_ICE_HIT = create("falling_ice_hit");

    HTHolder<SoundEvent> BI_FANG_AMBIENT = create("bi_fang_ambient");
    HTHolder<SoundEvent> BI_FANG_ROAR = create("bi_fang_roar");
    HTHolder<SoundEvent> BI_FANG_FLAP = create("bi_fang_flap");

    static HTHolder<SoundEvent> create(String name){
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(Util.prefix(name)));
    }

    static void initialize(IEventBus event){
        NeoHelper.initRegistry(SOUNDS, event);
    }
}
