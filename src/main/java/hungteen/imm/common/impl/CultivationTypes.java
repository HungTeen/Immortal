package hungteen.imm.common.impl;

import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Locale;

/**
 * 修行的方式。
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-13 15:09
 **/
public enum CultivationTypes implements ICultivationType {

    EMPTY,
    MORTAL,
    SPIRITUAL,
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
