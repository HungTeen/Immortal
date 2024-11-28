package hungteen.imm.api.cultivation;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/19 15:45
 */
public interface IHasQi {

    /**
     * @return 灵力含量。
     */
    float getQiAmount();

    /**
     * @return 自身最大灵力含量。
     */
    float getMaxQiAmount();

    /**
     * @param amount 灵力增加量。
     */
    void addQiAmount(float amount);

    /**
     * @return 灵力是否已满。
     */
    default boolean isQiFull(){
        return getQiAmount() >= getMaxQiAmount();
    }

}
