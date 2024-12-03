package hungteen.imm.common.cultivation.spell;

import net.minecraft.world.entity.Entity;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/3 22:45
 **/
public class SpellResult {

    private final boolean success;
    private Entity affectedTarget;

    public SpellResult(boolean success) {
        this.success = success;
    }

    public static SpellResult success(){
        return new SpellResult(true);
    }

    public static SpellResult fail(){
        return new SpellResult(false);
    }

    public SpellResult effectOn(Entity target){
        this.affectedTarget = target;
        return this;
    }

    public Optional<Entity> getAffectedTarget() {
        return Optional.ofNullable(affectedTarget);
    }

    public boolean isSuccess() {
        return success;
    }
}
