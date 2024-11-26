package hungteen.imm.common.cultivation;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.IMMConfigs;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.QiRootType;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;

import java.util.List;
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
            Set.of(Element.METAL),
            IMMConfigs::getMetalWeight,
            1,
            ColorHelper.METAL_ROOT,
            0,
            ChatFormatting.GOLD
    ));

    QiRootType WOOD = register(new QiRootTypeImpl(
            "wood",
            Set.of(Element.WOOD),
            IMMConfigs::getWoodWeight,
            2,
            ColorHelper.WOOD_ROOT,
            1,
            ChatFormatting.GREEN
    ));

    QiRootType WATER = register(new QiRootTypeImpl(
            "water",
            Set.of(Element.WATER),
            IMMConfigs::getWaterWeight,
            3,
            ColorHelper.WATER_ROOT,
            2,
            ChatFormatting.DARK_BLUE
    ));

    QiRootType FIRE = register(new QiRootTypeImpl(
            "fire",
            Set.of(Element.FIRE),
            IMMConfigs::getFireWeight,
            4,
            ColorHelper.FIRE_ROOT,
            3,
            ChatFormatting.RED
    ));

    QiRootType EARTH = register(new QiRootTypeImpl(
            "earth",
            Set.of(Element.EARTH),
            IMMConfigs::getEarthWeight,
            5,
            ColorHelper.EARTH_ROOT,
            4,
            ChatFormatting.YELLOW
    ));

    QiRootType SPIRIT = register(new QiRootTypeImpl(
            "spirit",
            Set.of(Element.SPIRIT),
            IMMConfigs::getSpiritWeight,
            6,
            ColorHelper.PURPLE.rgb(),
            5,
            ChatFormatting.DARK_PURPLE
    ));

    static MutableComponent getCategory() {
        return TipUtil.misc("spiritual_root");
    }

    static MutableComponent getSpiritualRoots(List<QiRootType> roots) {
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

    record QiRootTypeImpl(String name, Set<Element> elements, Supplier<Integer> weightSupplier, int priority,
                          int spiritualColor, int id, ChatFormatting textColor) implements QiRootType {

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
        public boolean isSpecialRoot() {
            return false;
        }

        @Override
        public Weight getWeight() {
            return Weight.of(this.weightSupplier.get());
        }


        @Override
        public int getSpiritualColor() {
            return this.spiritualColor;
        }

        @Override
        public Pair<Integer, Integer> getTexturePos() {
            return Pair.of(10 * id(), 0);
        }

        @Override
        public ResourceLocation getTexture() {
            return Util.get().guiTexture("elements");
        }

    }
}
