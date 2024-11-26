package hungteen.imm.api.cultivation;

import hungteen.htlib.api.registry.SimpleEntry;

/**
 * 修炼类型，为了支持扩展，不能是枚举类型。
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-13 15:08
 **/
public interface CultivationType extends SimpleEntry {

    /**
     * 是否利用灵气修行。
     * @return True if it requires Qi to cultivate.
     */
    boolean requireQi();

}
