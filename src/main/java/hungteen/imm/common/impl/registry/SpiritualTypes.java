package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.IMMConfigs;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.random.Weight;

import java.util.Set;
import java.util.function.Supplier;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 16:05
 **/
public class SpiritualTypes {

    public static final int TEX_WIDTH = 9;
    private static final HTSimpleRegistry<ISpiritualType> SPIRITUAL_TYPES = HTRegistryManager.createSimple(Util.prefix("spiritual_root"));

    public static IHTSimpleRegistry<ISpiritualType> registry() {
        return SPIRITUAL_TYPES;
    }

    public static final ISpiritualType METAL = register(new SpiritualType("metal", Set.of(Elements.METAL), IMMConfigs::getMetalWeight, 1, ColorHelper.METAL_ROOT, ChatFormatting.GOLD));
    public static final ISpiritualType WOOD = register(new SpiritualType("wood", Set.of(Elements.WOOD), IMMConfigs::getWoodWeight, 2, ColorHelper.WOOD_ROOT, ChatFormatting.GREEN));
    public static final ISpiritualType WATER = register(new SpiritualType("water", Set.of(Elements.WATER), IMMConfigs::getWaterWeight, 3, ColorHelper.WATER_ROOT, ChatFormatting.DARK_BLUE));
    public static final ISpiritualType FIRE = register(new SpiritualType("fire", Set.of(Elements.FIRE), IMMConfigs::getFireWeight, 4, ColorHelper.FIRE_ROOT, ChatFormatting.RED));
    public static final ISpiritualType EARTH = register(new SpiritualType("earth", Set.of(Elements.EARTH), IMMConfigs::getEarthWeight, 5, ColorHelper.EARTH_ROOT, ChatFormatting.YELLOW));
    public static final ISpiritualType SPIRIT = register(new SpiritualType("spirit", Set.of(Elements.SPIRIT), IMMConfigs::getEarthWeight, 5, ColorHelper.EARTH_ROOT, ChatFormatting.YELLOW));

    public static ISpiritualType register(ISpiritualType type) {
        return registry().register(type);
    }

    public record SpiritualType(String name, Set<Elements> elements, Supplier<Integer> weightSupplier, int priority,
                                int spiritualColor, ChatFormatting textColor) implements ISpiritualType {

        @Override
        public String getName() {
            return name();
        }

        @Override
        public String getModID() {
            return Util.id();
        }

        @Override
        public MutableComponent getComponent() {
            return Component.translatable("misc." + getModID() + ".root." + getName()).withStyle(textColor);
        }

        @Override
        public Set<Elements> getElements() {
            return elements();
        }

        @Override
        public Weight getWeight() {
            return Weight.of(this.weightSupplier.get());
        }


        @Override
        public int getSpiritualColor() {
            return this.spiritualColor;
        }

//        @Override
//        public Pair<Integer, Integer> getTexturePosition() {
//            return pair;
//        }
//
//        @Override
//        public ResourceLocation getResourceLocation() {
//            return Util.prefix("textures/gui/spiritual_roots.png");
//        }
    }
}
