package hungteen.imm.api.event;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.spell.Spell;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

import java.util.List;
import java.util.Set;

/**
 * 生物随机选择法术。
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/1 17:27
 **/
public class EntityRandomSpellEvent extends LivingEvent {

    private final List<Spell> learnedSpells;
    private final Set<Element> elements;
    private final RealmType realm;
    private final RandomSource random;

    public EntityRandomSpellEvent(LivingEntity living, List<Spell> learnedSpells, Set<Element> elements, RealmType realm, RandomSource random) {
        super(living);
        this.learnedSpells = learnedSpells;
        this.elements = elements;
        this.realm = realm;
        this.random = random;
    }

    public List<Spell> getLearnedSpells() {
        return learnedSpells;
    }

    public Set<Element> getElements() {
        return elements;
    }

    public RealmType getRealm() {
        return realm;
    }

    public RandomSource getRandom() {
        return random;
    }
}
