package hungteen.imm.common.impl;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.api.registry.IArtifactType;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

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
    UNKNOWN(0, StringHelper.colorStyle(ColorHelper.WHITE)),

    /**
     * 凡物。
     */
    COMMON_ITEM(40, StringHelper.colorStyle(ColorHelper.WHITE)),

    /**
     * 普通法器。
     */
    COMMON_ARTIFACT(120, StringHelper.colorStyle(ColorHelper.GREEN)),

    /**
     * 中等法器。
     */
    MODERATE_ARTIFACT(225, StringHelper.colorStyle(ColorHelper.GREEN)),

    /**
     * 高级法器。
     */
    ADVANCED_ARTIFACT(325, StringHelper.colorStyle(ColorHelper.BLUE)),
    ;

    private int value;
    private final Style style;

    ArtifactTypes(int value, Style style) {
        this.value = value;
        this.style = style;
    }

    @Override
    public int getRealmValue() {
        return value;
    }

    @Override
    public MutableComponent getComponent() {
        return TipUtil.misc("artifact_type." + getName()).withStyle(this.style);
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
