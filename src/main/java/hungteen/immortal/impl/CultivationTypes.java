package hungteen.immortal.impl;

import hungteen.immortal.api.interfaces.ICultivationType;
import hungteen.immortal.utils.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Locale;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-13 15:09
 **/
public enum CultivationTypes implements ICultivationType {

    MORTAL,
    SPIRITUAL,
    GHOST,
    BLOOD;

    @Override
    public Component getComponent() {
        return new TranslatableComponent("misc." + getModID() + ".cultivation_type." + getName());
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
