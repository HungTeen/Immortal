package hungteen.imm.api.interfaces;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/19 15:45
 */
public interface IHasMana {

    float getMana();

    void addMana(float amount);

    boolean isManaFull();

}
