package hungteen.immortal.api;

import hungteen.immortal.api.interfaces.ISpiritualRoot;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 22:26
 **/
public class DummyAPI implements ImmortalAPI.IImmortalAPI {

    public static final ImmortalAPI.IImmortalAPI INSTANCE = new DummyAPI();

    @Override
    public void registerSpiritualRoot(ISpiritualRoot type) {
    }

    @Override
    public List<ISpiritualRoot> getSpiritualRoots() {
        return new ArrayList<>();
    }
}
