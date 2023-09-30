package hungteen.imm.api.interfaces;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/19 15:45
 */
public interface IHasMana {

    float getMana();

    float getMaxMana();

    void addMana(float amount);

    default boolean isManaFull(){
        return getMana() >= getMaxMana();
    }

}
