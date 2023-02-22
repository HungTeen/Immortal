package hungteen.immortal.common.impl;

import hungteen.immortal.api.registry.ICultivationType;
import hungteen.immortal.utils.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Locale;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-13 15:09
 **/
public enum CultivationTypes implements ICultivationType {

    MORTAL,
    ELIXIR,
    MONSTER,
    GHOST,
    BLOOD;

    @Override
    public MutableComponent getComponent() {
        return Component.translatable("misc." + getModID() + ".cultivation_type." + getName());
    }

    @Override
    public String getName() {
        return toString().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getModID() {
        return Util.id();
    }
}
