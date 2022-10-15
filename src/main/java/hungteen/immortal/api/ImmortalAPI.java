package hungteen.immortal.api;

import com.google.common.base.Suppliers;
import hungteen.htlib.interfaces.IRangeData;
import hungteen.immortal.api.interfaces.IEffectRune;
import hungteen.immortal.api.interfaces.IGetterRune;
import hungteen.immortal.api.interfaces.ISpell;
import hungteen.immortal.api.interfaces.ISpiritualRoot;
import hungteen.immortal.utils.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
         * 注册效果符文类型。
         */
        void registerEffectRune(IEffectRune type);

        /**
         * 获取效果符文类型。
         */
        List<IEffectRune> getEffectRunes();

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
         * 注册取值符文类型。
         */
        void registerGetterRune(IGetterRune type);

        /**
         * 获取取值符文类型。
         */
        List<IGetterRune> getGetterRunes();

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

    }

}
