package hungteen.imm.common.cultivation;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.common.IMMConfigs;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-09-24 16:05
 **/
public interface QiRootTypes {

    HTCustomRegistry<QiRootType> SPIRITUAL_TYPES = HTRegistryManager.custom(Util.prefix("qi_root"));

    QiRootType METAL = register(new QiRootTypeImpl(
            "metal",
            Element.METAL,
            1,
            ColorHelper.METAL_ROOT,
            0,
            ChatFormatting.GOLD
    ));

    QiRootType WOOD = register(new QiRootTypeImpl(
            "wood",
            Element.WOOD,
            2,
            ColorHelper.WOOD_ROOT,
            1,
            ChatFormatting.GREEN
    ));

    QiRootType WATER = register(new QiRootTypeImpl(
            "water",
            Element.WATER,
            3,
            ColorHelper.WATER_ROOT,
            2,
            ChatFormatting.DARK_BLUE
    ));

    QiRootType FIRE = register(new QiRootTypeImpl(
            "fire",
            Element.FIRE,
            4,
            ColorHelper.FIRE_ROOT,
            3,
            ChatFormatting.RED
    ));

    QiRootType EARTH = register(new QiRootTypeImpl(
            "earth",
            Element.EARTH,
            5,
            ColorHelper.EARTH_ROOT,
            4,
            ChatFormatting.YELLOW
    ));

    QiRootType SPIRIT = register(new QiRootTypeImpl(
            "spirit",
            Element.SPIRIT,
            6,
            ColorHelper.PURPLE.rgb(),
            5,
            ChatFormatting.DARK_PURPLE
    ));

    QiRootType LIGHTNING = register(new QiRootTypeImpl(
            "lightning",
            Set.of(Element.METAL, Element.WOOD),
            8,
            ColorHelper.LIGHT_PURPLE.rgb(),
            0.5F,
            7,
            ChatFormatting.LIGHT_PURPLE
    ));

    QiRootType ICE = register(new QiRootTypeImpl(
            "ice",
            Set.of(Element.WATER),
            10,
            ColorHelper.DYE_CYAN.rgb(),
            0.5F,
            9,
            ChatFormatting.BLUE
    ));

    static MutableComponent getCategory() {
        return TipUtil.misc("spiritual_root");
    }

    static MutableComponent getRoots(List<QiRootType> roots) {
        if (!roots.isEmpty()) {
            final MutableComponent component = roots.get(0).getComponent();
            for (int i = 1; i < roots.size(); ++i) {
                component.append(Component.literal(", "));
                component.append(roots.get(i).getComponent());
            }
            return component;
        }
        return TipUtil.misc("no_spiritual_root");
    }

    static QiRootType register(QiRootType type) {
        return registry().register(type.getLocation(), type);
    }

    static HTCustomRegistry<QiRootType> registry() {
        return SPIRITUAL_TYPES;
    }

    /**
     * 仅支持单元素灵根，多元素灵根请另外继承。
     */
    record QiRootTypeImpl(String name, Set<Element> elements, int priority,
                          int spiritualColor, Supplier<Weight> weight, Float specialRootChance, int id, ChatFormatting textColor) implements QiRootType {

        public QiRootTypeImpl(
                String name,
                Element element,
                int priority,
                int spiritualColor,
                int id,
                ChatFormatting textColor) {
            this(name, Set.of(element), priority, spiritualColor, () -> Weight.of(IMMConfigs.getRootWeight(element)), null, id, textColor);
        }

        public QiRootTypeImpl(
                String name,
                Set<Element> elements,
                int priority,
                int spiritualColor,
                float specialRootChance,
                int id,
                ChatFormatting textColor) {
            this(name, elements, priority, spiritualColor, () -> Weight.of(0), specialRootChance, id, textColor);
        }

        @Override
        public String getModID() {
            return Util.id();
        }

        @Override
        public MutableComponent getComponent() {
            return Component.translatable("misc." + getModID() + ".root." + name()).withStyle(textColor);
        }

        @Override
        public Set<Element> getElements() {
            return elements();
        }

        @Override
        public boolean isCommonRoot() {
            return elements().size() == 1 && ! elements().contains(Element.SPIRIT);
        }

        @Override
        public Optional<Float> getSpecialRootChance() {
            return Optional.ofNullable(specialRootChance());
        }

        @Override
        public Weight getWeight() {
            return weight().get();
        }

        @Override
        public int getSpiritualColor() {
            return spiritualColor();
        }


        @Override
        public Pair<Integer, Integer> getTexturePos() {
            return Pair.of(10 * id(), 0);
        }

        @Override
        public ResourceLocation getTexture() {
            return Util.get().guiTexture("elements");
        }

        @Override
        public int getSortPriority() {
            return priority();
        }

    }
}
