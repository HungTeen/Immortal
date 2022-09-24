package hungteen.immortal.common.impl;

import com.google.common.collect.ImmutableList;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.ISpiritualRoot;
import hungteen.immortal.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 22:32
 **/
public class ImmortalAPIImpl implements ImmortalAPI.IImmortalAPI {

    private static final List<ISpiritualRoot> SPIRITUAL_ROOTS = new ArrayList<>();


    @Override
    public void registerSpiritualRoot(ISpiritualRoot type) {
        if(! SPIRITUAL_ROOTS.contains(type)){
            SPIRITUAL_ROOTS.add(type);
        } else{
            Util.warn("Spiritual Root Register : Duplicate Type !");
        }
    }

    @Override
    public List<ISpiritualRoot> getSpiritualRoots() {
        return Collections.unmodifiableList(SPIRITUAL_ROOTS);
    }
}
