package hungteen.imm.api;

import com.google.common.base.Suppliers;
import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.imm.api.registry.*;
import hungteen.imm.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 22:26
 **/
public class IMMAPI {

    private static final Supplier<IImmortalAPI> LAZY_INSTANCE = Suppliers.memoize(() -> {
        try {
            Class<?> classes = Class.forName("hungteen.imm.common.impl.IMMAPIImpl");
            Constructor<?> constructor = classes.getDeclaredConstructor();
            return (IImmortalAPI) constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            Util.warn("Unable to find implemented API, using a dummy one");
            return DummyAPI.INSTANCE;
        }
    });

    /**
     * Obtain the Mod API, either a valid implementation if mod is present, else
     * a dummy instance instead if mod is absent.
     */
    public static IImmortalAPI get() {
        return LAZY_INSTANCE.get();
    }

    /**
     * mod has two implemented API. <br>
     * a dummy one {@link DummyAPI} and an implemented one {@link } <br>
     * all implement code are below impl package.
     */
    public interface IImmortalAPI {

        /**
         * 注册灵根类型。<br>
         * Only registered roots can participate in human's randomly choosing roots when born.
         * @return Registry interface.
         */
        Optional<IHTSimpleRegistry<ISpiritualType>> spiritualRegistry();

        /**
         * 注册境界类型。 <br>
         * Register the realm type so that it can be stored as nbt.
         * @return Registry interface.
         */
        Optional<IHTSimpleRegistry<IRealmType>> realmRegistry();

        /**
         * 注册法术类型。 <br>
         * Only registered entries can be displayed on Spell Menu.
         * @return Registry interface.
         */
        Optional<IHTSimpleRegistry<ISpellType>> spellRegistry();


        /**
         * 注册玩家Int数据类型。<br>
         * Stored in player capabilities.
         * @return Registry interface.
         */
        Optional<IHTSimpleRegistry<IRangeNumber<Integer>>> integerDataRegistry();

        /**
         * 注册玩家Float数据类型。<br>
         * Stored in player capabilities.
         * @return Registry interface.
         */
        Optional<IHTSimpleRegistry<IRangeNumber<Float>>> floatDataRegistry();

        /**
         * 注册宗门类型。 <br>
         * @return Registry interface.
         */
        Optional<IHTSimpleRegistry<ISectType>> sectRegistry();

        /**
         * 获取生物的境界。
         * @param entity the entity that wants to know its realm type.
         * @return the realm type of the given entity.
         */
        IRealmType getEntityRealm(Entity entity);

        /**
         * 获取玩家灵气值。
         *
         * @param player query player.
         * @return how many spiritual mana in player.
         */
        float getSpiritualMana(Player player);

        /**
         * 设置维度的境界限制。
         * @param level the resource key of level.
         * @param lowestRealm the lower bound realm value in the given level.
         * @param highestRealm the upper bound realm value in the given level.
         */
        void registerLevelRealmSetting(ResourceKey<Level> level, int lowestRealm, int highestRealm);

        /**
         * 设置群系的灵气回复速率。
         * @param biome the resource key of biome.
         * @param minChange the min change value of spiritual value in the given biome.
         * @param maxChange the max change value of spiritual value in the given biome.
         */
        void registerBiomeRealmSetting(ResourceKey<Biome> biome, float minChange, float maxChange);

        /**
         * 获取当前位置的灵气恢复值。
         * @param level current level to be queried.
         * @param pos current position to be queried.
         * @return calculate the spiritual change value at the given level and position.
         */
        float getSpiritualRate(Level level, BlockPos pos);

    }

}
