package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * 修行的方式。
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-13 15:09
 **/
public class CultivationTypes {

    private static final IHTSimpleRegistry<ICultivationType> TYPES = HTRegistryManager.createSimple(Util.prefix("cultivation_type"));

    public static final ICultivationType EMPTY = register(new CultivationType("empty"));
    public static final ICultivationType MORTAL = register(new CultivationType("mortal"));
    public static final ICultivationType SPIRITUAL = register(new CultivationType("spiritual"));
    public static final ICultivationType MAGIC = register(new CultivationType("magic"));
    public static final ICultivationType MONSTER = register(new CultivationType("monster"));
    public static final ICultivationType GHOST = register(new CultivationType("ghost"));
    public static final ICultivationType BLOOD = register(new CultivationType("blood"));

    public static IHTSimpleRegistry<ICultivationType> registry(){
        return TYPES;
    }

    public static ICultivationType register(ICultivationType type){
        return registry().register(type);
    }

    public record CultivationType(String name) implements ICultivationType {

        @Override
        public MutableComponent getComponent() {
            return Component.translatable("misc." + getModID() + ".cultivation_type." + getName());
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public String getModID() {
            return Util.id();
        }
    }

}