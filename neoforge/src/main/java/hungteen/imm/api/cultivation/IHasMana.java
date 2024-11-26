package hungteen.imm.api.cultivation;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/19 15:45
 */
public interface IHasMana {

    float getMana();

    float getMaxMana();

    void addMana(float amount);

    default boolean isManaFull(){
        return getMana() >= getMaxMana();
    }

}
