package hungteen.immortal.impl;

import com.mojang.datafixers.util.Pair;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.ImmortalConfigs;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.utils.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 16:05
 **/
public class SpiritualRoots {

    private static final List<ISpiritualRoot> TYPES = new ArrayList<>();

    public static final ISpiritualRoot METAL = new SpiritualRoot("metal", true, ImmortalConfigs.getMetalWeight(), ChatFormatting.GOLD, Pair.of(0, 0));
    public static final ISpiritualRoot WOOD = new SpiritualRoot("wood", true, ImmortalConfigs.getWoodWeight(), ChatFormatting.GREEN, Pair.of(10, 0));
    public static final ISpiritualRoot WATER = new SpiritualRoot("water", true, ImmortalConfigs.getWaterWeight(), ChatFormatting.DARK_BLUE, Pair.of(20, 0));
    public static final ISpiritualRoot FIRE = new SpiritualRoot("fire", true, ImmortalConfigs.getFireWeight(), ChatFormatting.RED, Pair.of(30, 0));
    public static final ISpiritualRoot EARTH = new SpiritualRoot("earth", true, ImmortalConfigs.getEarthWeight(), ChatFormatting.YELLOW, Pair.of(40, 0));

    public static final ISpiritualRoot WIND = new SpiritualRoot("wind", false, ImmortalConfigs.getWindWeight(), ChatFormatting.AQUA, Pair.of(0, 0));
    public static final ISpiritualRoot ELECTRIC = new SpiritualRoot("electric", false, ImmortalConfigs.getElectricWeight(), ChatFormatting.DARK_AQUA, Pair.of(0, 0));
    public static final ISpiritualRoot DRUG = new SpiritualRoot("drug", false, ImmortalConfigs.getDrugWeight(), ChatFormatting.DARK_GREEN, Pair.of(0, 0));
    public static final ISpiritualRoot ICE = new SpiritualRoot("ice", false, ImmortalConfigs.getIceWeight(), ChatFormatting.BLUE, Pair.of(0, 0));
//    public static final ISpiritualRoot DEAD = new SpiritualRoot("dead", false, ImmortalConfigs.getIceWeight(), ChatFormatting.DARK_PURPLE);
//    public static final ISpiritualRoot BLOOD = new SpiritualRoot("blood", false, ImmortalConfigs.getIceWeight(), ChatFormatting.DARK_RED);
//    public static final ISpiritualRoot DARK = new SpiritualRoot("dark", false, ImmortalConfigs.getIceWeight(), ChatFormatting.BLACK);


    public record SpiritualRoot(String name, boolean isCommonRoot, int weight, ChatFormatting color, Pair<Integer, Integer> pair) implements ISpiritualRoot {

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register(){
            TYPES.forEach(type -> ImmortalAPI.get().registerSpiritualRoot(type));
        }

        public SpiritualRoot(String name, boolean isCommonRoot, int weight, ChatFormatting color, Pair<Integer, Integer> pair) {
            this.name = name;
            this.isCommonRoot = isCommonRoot;
            this.weight = weight;
            this.color = color;
            this.pair = pair;
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
            return new TranslatableComponent("misc." + getModID() +".root." + getName()).withStyle(color);
        }

        @Override
        public int getWeight() {
            return weight;
        }

        @Override
        public Pair<Integer, Integer> getTexturePosition() {
            return pair;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return Util.prefix("textures/gui/icons.png");
        }
    }
}
