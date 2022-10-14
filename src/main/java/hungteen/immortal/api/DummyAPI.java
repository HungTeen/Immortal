package hungteen.immortal.api;

import hungteen.htlib.interfaces.IRangeData;
import hungteen.immortal.api.interfaces.IEffectRune;
import hungteen.immortal.api.interfaces.IGetterRune;
import hungteen.immortal.api.interfaces.ISpell;
import hungteen.immortal.api.interfaces.ISpiritualRoot;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
        return List.of();
    }

    @Override
    public void registerSpell(ISpell type) {

    }

    @Override
    public List<ISpell> getSpells() {
        return List.of();
    }

    @Override
    public void registerEffectRune(IEffectRune type) {

    }

    @Override
    public List<IEffectRune> getEffectRunes() {
        return List.of();
    }

    @Override
    public void registerIntegerData(IRangeData<Integer> type) {

    }

    @Override
    public Collection<IRangeData<Integer>> getIntegerCollection() {
        return List.of();
    }

    @Override
    public Optional<IRangeData<Integer>> getIntegerData(String type) {
        return Optional.empty();
    }

    @Override
    public void registerGetterRune(IGetterRune type) {

    }

    @Override
    public List<IGetterRune> getGetterRunes() {
        return List.of();
    }
}
