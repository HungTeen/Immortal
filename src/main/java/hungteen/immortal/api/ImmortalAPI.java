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
         * 注册灵根类型。
         */
        void registerSpiritualRoot(ISpiritualRoot type);

        /**
         * 获取灵根类型。
         */
        List<ISpiritualRoot> getSpiritualRoots();

        /**
         * 注册法术类型。
         */
        void registerSpell(ISpell type);

        /**
         * 获取法术类型。
         * @return
         */
        Collection<ISpell> getSpells();

        /**
         * 获取法术。
         */
        Optional<ISpell> getSpell(String type);

        /**
         * 注册境界类型。
         */
        void registerRealm(IRealm type);

        /**
         * 获取境界类型。
         * @return
         */
        Collection<IRealm> getRealms();

        /**
         * 获取境界。
         */
        Optional<IRealm> getRealm(String type);

        /**
         * 获取生物的境界。
         */
        IRealm getEntityRealm(Entity entity);

        /**
         * 注册玩家Int数据类型。
         */
        void registerIntegerData(IRangeData<Integer> type);

        /**
         * 获取玩家Int数据类型。
         * @return
         */
        Collection<IRangeData<Integer>> getIntegerCollection();

        /**
         * 获取玩家Int数据类型。
         * @return
         */
        Optional<IRangeData<Integer>> getIntegerData(String type);

        /**
         * 获取玩家灵气值。
         */
        int getSpiritualMana(Player player);

        /**
         * 设置群系的灵气值。
         */
        void registerBiomeSpiritualValue(ResourceKey<Biome> biomeResourceKey, int spiritualValue);

        /**
         * 设置维度的灵气倍率。
         */
        void registerLevelSpiritualRatio(ResourceKey<Level> levelResourceKey, float spiritualRatio);

        /**
         * 获取当前位置的灵气值。
         */
        int getSpiritualValue(Level level, BlockPos pos);

        /**
         * 注册丹药类型。
         */
        void registerElixirType(IElixirType type);

        /**
         * 获取丹药类型。
         */
        List<IElixirType> getElixirTypes();

        /**
         * 设置炼丹材料的灵气值。
         */
        void registerElixirIngredient(Item item, Map<ISpiritualRoot, Integer> map);

        /**
         * 获取炼丹材料的灵气值。
         */
        Map<ISpiritualRoot, Integer> getElixirIngredient(Item item);

        /**
         * 获取所有炼丹材料。
         */
        Set<Item> getElixirIngredients();

    }

}
