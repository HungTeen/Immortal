package hungteen.immortal.common.item.artifacts;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-19 22:31
 **/
public class HammerItem extends MeleeAttackItem {

    public HammerItem(int artifactLevel, float attackDamage) {
        this(artifactLevel, false, attackDamage, 0, 0);
    }

    public HammerItem(int artifactLevel, boolean isAncientArtifact, float attackDamage, float attackSpeed, float attackRange) {
        super(MeleeAttackTypes.HAMMER, artifactLevel, isAncientArtifact, attackDamage, attackSpeed, attackRange);
    }

}
