package hungteen.immortal.impl;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.ISectType;
import hungteen.immortal.utils.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-12-04 23:15
 **/
public class SectTypes {

    private static final List<ISectType> TYPES = new ArrayList<>();

    public static class SectType implements ISectType {

        private final String name;

        public SectType(String name) {
            this.name = name;
        }

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register(){
            ImmortalAPI.get().sectRegistry().ifPresent(l -> l.register(TYPES));
        }



        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getModID() {
            return Util.id();
        }

        @Override
        public MutableComponent getComponent() {
            return Component.translatable("sect." + getModID() +"." + getName());
        }

    }
}
