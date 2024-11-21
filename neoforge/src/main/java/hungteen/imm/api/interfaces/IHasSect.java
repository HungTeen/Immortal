package hungteen.imm.api.interfaces;

import hungteen.imm.api.registry.ISectType;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/5/20 9:55
 */
public interface IHasSect {

    /**
     * The sect everyone knows about it, maybe not real.
     */
    Optional<ISectType> getOuterSect();

    /**
     * The real sect in its heart.
     */
    default Optional<ISectType> getInnerSect(){
        return getOuterSect();
    }
}
