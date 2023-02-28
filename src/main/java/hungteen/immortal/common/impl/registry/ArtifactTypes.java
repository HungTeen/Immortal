package hungteen.immortal.common.impl.registry;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.IArtifactType;
import hungteen.immortal.api.registry.ISpiritualType;
import hungteen.immortal.utils.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/28 19:15
 */
public enum ArtifactTypes implements IArtifactType{

    /**
     * 体系之外，不予评价。
     */
    EMPTY(0, ChatFormatting.WHITE),

    /**
     * 凡物。
     */
    COMMON_ITEM(0, ChatFormatting.WHITE),

    /**
     * 普通法器。
     */
    COMMON_ARTIFACT(50, ChatFormatting.GREEN),

    /**
     * 中等法器。
     */
    MODERATE_ARTIFACT(100, ChatFormatting.GREEN),

    /**
     * 高级法器。
     */
    ADVANCED_ARTIFACT(150, ChatFormatting.BLUE),
    ;

    private int value;
    private final ChatFormatting formatting;

    ArtifactTypes(int value, ChatFormatting formatting) {
        this.value = value;
        this.formatting = formatting;
    }

    @Override
    public int getRealmValue() {
        return value;
    }

    @Override
    public MutableComponent getComponent() {
        return Component.translatable("misc." + getModID() + ".artifact_type." + getName()).withStyle(this.formatting);
    }

    @Override
    public String getName() {
        return toString().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getModID() {
        return Util.id();
    }
}
