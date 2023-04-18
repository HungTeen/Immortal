package hungteen.imm.common.rune;

import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.MutableComponent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-17 22:37
 **/
public enum RuneCategories {

    /**
     * 切石机一样的合成台。
     */
    CRAFT,

    /**
     * 逻辑符文合成。
     */
    GATE,

    /**
     * 行为符文绑定。
     */
    BIND
    ;

    public MutableComponent getTitle(){
        return TipUtil.gui("rune_title." + this.toString().toLowerCase());
    }

    public MutableComponent getTabTitle(){
        return TipUtil.gui("rune_tab_title." + this.toString().toLowerCase());
    }
}
