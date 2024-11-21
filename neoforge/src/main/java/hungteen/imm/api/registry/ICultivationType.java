package hungteen.imm.api.registry;

import hungteen.htlib.api.registry.SimpleEntry;

/**
 * 修炼类型。
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-13 15:08
 **/
public interface ICultivationType extends SimpleEntry {

    /**
     * 能否使用附魔物品。
     * @return True if living of this type can use enchant items.
     */
    boolean canEnchant();

    /**
     * 是否利用灵气修行。
     * @return True if living of this type cultivates by spiritual.
     */
    boolean isSpiritual();
}
