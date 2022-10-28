package hungteen.immortal.impl;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.IElixirType;
import hungteen.immortal.api.registry.IRealm;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.utils.Util;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Rarity;

import java.util.*;
import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-28 21:38
 **/
public class ElixirTypes {

    private static final List<IElixirType> TYPES = new ArrayList<>();

    public static final IElixirType FIVE_FLOWERS = new ElixirType("five_flowers", Rarity.COMMON, immortal(), new HashMap<>(Map.of(
            SpiritualRoots.METAL, 5,
            SpiritualRoots.WOOD, 5,
            SpiritualRoots.WATER, 5,
            SpiritualRoots.FIRE, 5,
            SpiritualRoots.EARTH, 5
    )));
    public static final IElixirType ANTIDOTE = new ElixirType("antidote", Rarity.COMMON, immortal(), new HashMap<>(Map.of(
            SpiritualRoots.WOOD, 5,
            SpiritualRoots.WATER, 5,
            SpiritualRoots.FIRE, 5,
            SpiritualRoots.EARTH, 5,
            SpiritualRoots.DRUG, 10
    )));


    public record ElixirType(String name, Rarity rarity, Function<IRealm, Optional<Boolean>> checker, Map<ISpiritualRoot, Integer> map) implements IElixirType {

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register(){
            TYPES.forEach(type -> ImmortalAPI.get().registerElixirType(type));
        }

        public ElixirType(String name, Rarity rarity, Function<IRealm, Optional<Boolean>> checker, Map<ISpiritualRoot, Integer> map) {
            this.name = name;
            this.rarity = rarity;
            this.checker = checker;
            this.map = map;
            TYPES.add(this);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getModID() {
            return Util.id();
        }

        @Override
        public MutableComponent getComponent() {
            return new TranslatableComponent("elixir." + getModID() +"." + getName());
        }

        @Override
        public Rarity getElixirRarity() {
            return rarity;
        }

        @Override
        public Function<IRealm, Optional<Boolean>> realmChecker() {
            return checker;
        }

        @Override
        public Map<ISpiritualRoot, Integer> requireSpiritualMap() {
            return map;
        }

    }

    private static Function<IRealm, Optional<Boolean>> immortal(){
        return largeThan(Realms.MEDITATION_STAGE1);
    }

    private static Function<IRealm, Optional<Boolean>> lessThan(IRealm base){
        return realm -> {
            if(realm.getRealmValue() <= base.getRealmValue()){
                return Optional.of(true);
            }
            return Optional.empty();
        };
    }

    private static Function<IRealm, Optional<Boolean>> largeThan(IRealm base){
        return realm -> {
            if(realm.getRealmValue() >= base.getRealmValue()){
                return Optional.of(true);
            }
            return Optional.of(false);
        };
    }

}
