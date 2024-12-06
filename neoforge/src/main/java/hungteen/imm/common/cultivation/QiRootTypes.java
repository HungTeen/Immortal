package hungteen.imm.common.cultivation;

import com.mojang.datafixers.util.Pair;
import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.imm.common.IMMConfigs;
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
    record QiRootTypeImpl(String name, Element element, int priority,
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
            return Set.of(element());
        }

        @Override
        public boolean isCommonRoot() {
            return !(element() == Element.SPIRIT);
        }

        @Override
        public boolean isSpecialRoot() {
            return false;
        }

        @Override
        public Weight getWeight() {
            return Weight.of(IMMConfigs.getRootWeight(element()));
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
