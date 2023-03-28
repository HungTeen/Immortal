package hungteen.imm.common.impl;

import hungteen.imm.ImmortalMod;
import hungteen.imm.api.ImmortalAPI;
import hungteen.imm.api.registry.ISectType;
import hungteen.imm.utils.Util;
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

    public static final ISectType VILLAGER_KINGDOM = new SectType("villager_kingdom");

    public static class SectType implements ISectType {

        private final String name;

        public SectType(String name) {
            this.name = name;
            TYPES.add(this);
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
