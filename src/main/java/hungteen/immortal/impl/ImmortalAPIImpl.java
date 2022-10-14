package hungteen.immortal.impl;

import hungteen.htlib.interfaces.IRangeData;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.IEffectRune;
import hungteen.immortal.api.interfaces.IGetterRune;
import hungteen.immortal.api.interfaces.ISpell;
import hungteen.immortal.api.interfaces.ISpiritualRoot;
import hungteen.immortal.utils.Util;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 22:32
 **/
public class ImmortalAPIImpl implements ImmortalAPI.IImmortalAPI {

    private static final List<ISpiritualRoot> SPIRITUAL_ROOTS = new ArrayList<>();
    private static final List<ISpell> SPELLS = new ArrayList<>();
    private static final List<IEffectRune> EFFECT_RUNES = new ArrayList<>();
    private static final List<IGetterRune> GETTER_RUNES = new ArrayList<>();
    private static final HashMap<String, IRangeData<Integer>> INTEGER_MAP = new HashMap();


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

    @Override
    public void registerSpell(ISpell type) {
        if(! SPELLS.contains(type)){
            SPELLS.add(type);
        } else{
            Util.warn("Spell Register : Duplicate Type !");
        }
    }

    @Override
    public List<ISpell> getSpells() {
        return Collections.unmodifiableList(SPELLS);
    }

    @Override
    public void registerEffectRune(IEffectRune type) {
        if(! EFFECT_RUNES.contains(type)){
            EFFECT_RUNES.add(type);
        } else{
            Util.warn("Effect Rune Register : Duplicate Type !");
        }
    }

    @Override
    public List<IEffectRune> getEffectRunes() {
        return Collections.unmodifiableList(EFFECT_RUNES);
    }

    @Override
    public void registerIntegerData(IRangeData<Integer> type) {
        if(! INTEGER_MAP.containsKey(type)){
            INTEGER_MAP.put(type.getRegistryName(), type);
        } else{
            Util.warn("Integer Data Register : Duplicate Type !");
        }
    }

    @Override
    public Collection<IRangeData<Integer>> getIntegerCollection() {
        return Collections.unmodifiableCollection(INTEGER_MAP.values());
    }

    @Override
    public Optional<IRangeData<Integer>> getIntegerData(String type) {
        return Optional.ofNullable(INTEGER_MAP.get(type));
    }

    @Override
    public void registerGetterRune(IGetterRune type) {
        if(! GETTER_RUNES.contains(type)){
            GETTER_RUNES.add(type);
        } else{
            Util.warn("Getter Rune Register : Duplicate Type !");
        }
    }

    @Override
    public List<IGetterRune> getGetterRunes() {
        return Collections.unmodifiableList(GETTER_RUNES);
    }

}
