package hungteen.imm.common.impl.registry;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.ImmortalConfigs;
import hungteen.imm.ImmortalMod;
import hungteen.imm.api.ImmortalAPI;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.utils.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 16:05
 **/
public class SpiritualTypes {

    public static final int TEX_WIDTH = 9;
    private static final HTSimpleRegistry<ISpiritualType> SPIRITUAL_TYPES = HTRegistryManager.create(Util.prefix("spiritual_root"));
    private static final List<ISpiritualType> TYPES = new ArrayList<>();

    public static IHTSimpleRegistry<ISpiritualType> registry() {
        return SPIRITUAL_TYPES;
    }

    public static final ISpiritualType METAL = new SpiritualType("metal", true, ImmortalConfigs::getMetalWeight, 1, ColorHelper.METAL_ROOT, ChatFormatting.GOLD, Pair.of(0, 0));
    public static final ISpiritualType WOOD = new SpiritualType("wood", true, ImmortalConfigs::getWoodWeight, 2, ColorHelper.WOOD_ROOT, ChatFormatting.GREEN, Pair.of(10, 0));
    public static final ISpiritualType WATER = new SpiritualType("water", true, ImmortalConfigs::getWaterWeight, 3, ColorHelper.WATER_ROOT, ChatFormatting.DARK_BLUE, Pair.of(20, 0));
    public static final ISpiritualType FIRE = new SpiritualType("fire", true, ImmortalConfigs::getFireWeight, 4, ColorHelper.FIRE_ROOT, ChatFormatting.RED, Pair.of(30, 0));
    public static final ISpiritualType EARTH = new SpiritualType("earth", true, ImmortalConfigs::getEarthWeight, 5, ColorHelper.EARTH_ROOT, ChatFormatting.YELLOW, Pair.of(40, 0));

    public static final ISpiritualType WIND = new SpiritualType("wind", false, ImmortalConfigs::getWindWeight, 10, ColorHelper.WIND_ROOT, ChatFormatting.AQUA, Pair.of(50, 0));
    public static final ISpiritualType ELECTRIC = new SpiritualType("electric", false, ImmortalConfigs::getElectricWeight, 11, ColorHelper.ELECTRIC_ROOT, ChatFormatting.DARK_AQUA, Pair.of(60, 0));
    public static final ISpiritualType DRUG = new SpiritualType("drug", false, ImmortalConfigs::getDrugWeight, 12, ColorHelper.DRUG_ROOT, ChatFormatting.DARK_GREEN, Pair.of(70, 0));
    public static final ISpiritualType ICE = new SpiritualType("ice", false, ImmortalConfigs::getIceWeight, 13, ColorHelper.ICE_ROOT, ChatFormatting.BLUE, Pair.of(80, 0));
//    public static final ISpiritualRoot DEAD = new SpiritualRoot("dead", false, ImmortalConfigs.getIceWeight(), ChatFormatting.DARK_PURPLE);
//    public static final ISpiritualRoot BLOOD = new SpiritualRoot("blood", false, ImmortalConfigs.getIceWeight(), ChatFormatting.DARK_RED);
//    public static final ISpiritualRoot DARK = new SpiritualRoot("dark", false, ImmortalConfigs.getIceWeight(), ChatFormatting.BLACK);


    public record SpiritualType(String name, boolean isCommonRoot, Supplier<Integer> weightSupplier, int priority, int spiritualColor, ChatFormatting textColor, Pair<Integer, Integer> pair) implements ISpiritualType {

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register(){
            ImmortalAPI.get().spiritualRegistry().ifPresent(l -> l.register(TYPES));
        }

        public SpiritualType(String name, boolean isCommonRoot, Supplier<Integer> weightSupplier, int priority, int spiritualColor, ChatFormatting textColor, Pair<Integer, Integer> pair) {
            this.name = name;
            this.isCommonRoot = isCommonRoot;
            this.weightSupplier = weightSupplier;
            this.priority = priority;
            this.spiritualColor = spiritualColor;
            this.textColor = textColor;
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
            return Component.translatable("misc." + getModID() +".root." + getName()).withStyle(textColor);
        }

        @Override
        public Weight getWeight() {
            return Weight.of(this.weightSupplier.get());
        }

        @Override
        public int getSortPriority() {
            return this.priority;
        }

        @Override
        public int getSpiritualColor() {
            return this.spiritualColor;
        }

        @Override
        public Pair<Integer, Integer> getTexturePosition() {
            return pair;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return Util.prefix("textures/gui/spiritual_roots.png");
        }
    }
}
