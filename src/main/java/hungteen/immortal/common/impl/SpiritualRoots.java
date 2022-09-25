package hungteen.immortal.common.impl;

import hungteen.immortal.Immortal;
import hungteen.immortal.ModConfigs;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.ISpiritualRoot;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 16:05
 **/
public class SpiritualRoots {

    private static final List<ISpiritualRoot> TYPES = new ArrayList<>();

    public static final ISpiritualRoot METAL = new SpiritualRoot("metal", true, ModConfigs.getMetalWeight());
    public static final ISpiritualRoot WOOD = new SpiritualRoot("wood", true, ModConfigs.getWoodWeight());
    public static final ISpiritualRoot WATER = new SpiritualRoot("water", true, ModConfigs.getWaterWeight());
    public static final ISpiritualRoot FIRE = new SpiritualRoot("fire", true, ModConfigs.getFireWeight());
    public static final ISpiritualRoot EARTH = new SpiritualRoot("earth", true, ModConfigs.getEarthWeight());

    public static final ISpiritualRoot WIND = new SpiritualRoot("wind", false, ModConfigs.getWindWeight());
    public static final ISpiritualRoot ELECTRIC = new SpiritualRoot("electric", false, ModConfigs.getElectricWeight());
    public static final ISpiritualRoot DRUG = new SpiritualRoot("drug", false, ModConfigs.getDrugWeight());
    public static final ISpiritualRoot ICE = new SpiritualRoot("ice", false, ModConfigs.getIceWeight());


    public record SpiritualRoot(String name, boolean isCommonRoot, int weight) implements ISpiritualRoot {

        /**
         * {@link Immortal#coreRegister()}
         */
        public static void register(){
            TYPES.forEach(type -> ImmortalAPI.get().registerSpiritualRoot(type));
        }

        public SpiritualRoot(String name, boolean isCommonRoot, int weight) {
            this.name = name;
            this.isCommonRoot = isCommonRoot;
            this.weight = weight;
            TYPES.add(this);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int getWeight() {
            return weight;
        }
    }
}
