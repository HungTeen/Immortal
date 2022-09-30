package hungteen.immortal.api;

import com.google.common.base.Suppliers;
import hungteen.immortal.api.interfaces.ISpell;
import hungteen.immortal.api.interfaces.ISpiritualRoot;
import hungteen.immortal.utils.Util;

import java.lang.reflect.Constructor;
import java.util.List;
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
         */
        List<ISpell> getSpells();
    }

}
