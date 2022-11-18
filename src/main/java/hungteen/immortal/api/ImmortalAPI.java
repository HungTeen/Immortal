package hungteen.immortal.api;

import com.google.common.base.Suppliers;
import hungteen.htlib.interfaces.IRangeData;
import hungteen.immortal.api.registry.*;
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
            Class<?> classes = Class.forName("hungteen.immortal.impl.ImmortalAPIImpl");
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
     * a dummy one {@link DummyAPI} and a implemented one {@link } <br>
     * all implement code are below impl package.
     */
    public interface IImmortalAPI {

        /**
         * 注册灵根类型。<br>
         * Only registered roots can participate in human's randomly choosing roots when born.
         * @param type the spiritual root instance to be registered.
         */
        void registerSpiritualRoot(ISpiritualRoot type);

        /**
         * 获取所有灵根类型。
         * @return all kinds of spiritual roots registered.
         */
        List<ISpiritualRoot> getSpiritualRoots();

        /**
         * 根据名字获取灵根。
         * @param type the name of spiritual root.
         * @return spiritual type with the given name.
         */
        Optional<ISpiritualRoot> getSpiritualRoot(String type);

        /**
         * 注册法术类型。 <br>
         * Only registered spells can be displayed on Spell Menu.
         * @param type the spell type to be registered.
         */
        void registerSpell(ISpell type);

        /**
         * 获取所有法术类型。
         * @return all kinds of spell types registered.
         */
        Collection<ISpell> getSpells();

        /**
         * 根据名字获取法术。
         * @param type the name of spell.
         * @return the spell type with the given name.
         */
        Optional<ISpell> getSpell(String type);

//        /**
//         * 注册功法秘籍类型。 <br>
//         * Register book types that can be added as a book item in game.
//         * @param type the spell book to be registered.
//         */
//        void registerSpellBook(ISpellBook type);
//
//        /**
//         * 获取所有功法秘籍类型。
//         * @return all kinds of spell book registered.
//         */
//        Collection<ISpellBook> getSpellBooks();
//
//        /**
//         * 根据名字获取功法秘籍。
//         * @param type the name of spell book.
//         * @return the book type with the given name.
//         */
//        Optional<ISpellBook> getSpellBook(String type);

        /**
         * 注册境界类型。
         * Register the realm type so that it can be stored as nbt.
         * @param type the realm type to be registered.
         */
        void registerRealm(IRealm type);

        /**
         * 获取境界类型。
         * @return all kinds of realm types registered.
         */
        Collection<IRealm> getRealms();

        /**
         * 根据名字获取境界。
         * @param type the name of realm type.
         * @return the realm type with the given name.
         */
        Optional<IRealm> getRealm(String type);

        /**
         * 获取生物的境界。
         * @param entity the entity that wants to know its realm type.
         * @return the realm type of the given entity.
         */
        IRealm getEntityRealm(Entity entity);

        /**
         * 注册玩家Int数据类型。
         * @param type register a type to store in player capability.
         */
        void registerIntegerData(IRangeData<Integer> type);

        /**
         * 获取玩家Int数据类型。
         * @return all kinds of integer data registered.
         */
        Collection<IRangeData<Integer>> getIntegerCollection();

        /**
         * 根据名字获取玩家Int数据类型。
         * @param type the name of integer data.
         * @return the corresponding integer data with the given name.
         */
        Optional<IRangeData<Integer>> getIntegerData(String type);

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
        void registerElixirIngredient(Item item, Map<ISpiritualRoot, Integer> map);

        /**
         * 获取炼丹材料的灵气值。
         * @param item to be queried.
         * @return the spiritual value map of the given item.
         */
        Map<ISpiritualRoot, Integer> getElixirIngredient(Item item);

        /**
         * 获取所有炼丹材料。
         * @return all kinds of item that have a specific spiritual value map.
         */
        Set<Item> getElixirIngredients();

    }

}
