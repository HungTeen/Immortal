package hungteen.immortal.api;

import com.google.common.base.Suppliers;
import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.immortal.api.registry.IRealmType;
import hungteen.immortal.api.registry.ISectType;
import hungteen.immortal.api.registry.ISpellType;
import hungteen.immortal.api.registry.ISpiritualType;
import hungteen.immortal.utils.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
public class ImmortalAPI {

    private static final Supplier<IImmortalAPI> LAZY_INSTANCE = Suppliers.memoize(() -> {
        try {
            Class<?> classes = Class.forName("hungteen.immortal.common.impl.ImmortalAPIImpl");
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
         * 注册法术类型。 <br>
         * Only registered spells can be displayed on Spell Menu.
         * @return Registry interface.
         */
        Optional<IHTSimpleRegistry<ISpellType>> spellRegistry();

        /**
         * 注册宗门类型。 <br>
         * @return Registry interface.
         */
        Optional<IHTSimpleRegistry<ISectType>> sectRegistry();

        /**
         * 注册境界类型。 <br>
         * Register the realm type so that it can be stored as nbt.
         * @return Registry interface.
         */
        Optional<IHTSimpleRegistry<IRealmType>> realmRegistry();

        /**
         * 注册玩家Int数据类型。<br>
         * Stored in player capabilities.
         * @return Registry interface.
         */
        Optional<IHTSimpleRegistry<IRangeNumber<Integer>>> integerDataRegistry();

        /**
         * 获取生物的境界。
         * @param entity the entity that wants to know its realm type.
         * @return the realm type of the given entity.
         */
        IRealmType getEntityRealm(Entity entity);

        /**
         * 获取玩家灵气值。
         * @param player query player.
         * @return how many spiritual mana in player.
         */
        int getSpiritualMana(Player player);

        /**
         * 设置群系的灵气值。
         * @param biomeResourceKey the resource key of biome.
         * @param spiritualValue the spiritual value in the given biome.
         */
        void registerBiomeSpiritualValue(ResourceKey<Biome> biomeResourceKey, int spiritualValue);

        /**
         * 设置维度的灵气倍率。
         * @param levelResourceKey the resource key of level.
         * @param spiritualRatio the spiritual value ratio in the given level.
         */
        void registerLevelSpiritualRatio(ResourceKey<Level> levelResourceKey, float spiritualRatio);

        /**
         * 获取当前位置的灵气值。
         * @param level current level to be queried.
         * @param pos current position to be queried.
         * @return calculate the spiritual value at the given level and position.
         */
        int getSpiritualValue(Level level, BlockPos pos);

        /**
         * 设置炼丹材料的灵气值。
         * @param item the item need to be registered.
         * @param map the spiritual value map of the given item.
         */
        void registerElixirIngredient(Item item, Map<ISpiritualType, Integer> map);

        /**
         * 获取炼丹材料的灵气值。
         * @param item to be queried.
         * @return the spiritual value map of the given item.
         */
        Map<ISpiritualType, Integer> getElixirIngredient(Item item);

        /**
         * 获取所有炼丹材料。
         * @return all kinds of item that have a specific spiritual value map.
         */
        Set<Item> getElixirIngredients();

    }

}
