package hungteen.imm.common.cultivation.reaction;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.cultivation.ElementReactions;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.List;

/**
 * 相生反应。
 *
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/28 22:29
 **/
public abstract class GenerationReaction extends ElementReactionImpl {

    private final Element ingredient;
    private final Element production;
    private final float ingredientAmount;
    private final float productionAmount;
    private final boolean spawnAmethyst;

    public GenerationReaction(String name, boolean once, Element main, float mainAmount, Element off, float percent) {
        this(name, once, 100, main, mainAmount, off, mainAmount * percent);
    }

    public GenerationReaction(String name, boolean once, int priority, Element main, float mainAmount, Element off, float productionAmount) {
        super(name, once, priority, List.of(new ElementEntry(main, true, mainAmount), new ElementEntry(off, false, 0F)));
        this.ingredient = main;
        this.production = off;
        this.ingredientAmount = mainAmount;
        this.productionAmount = productionAmount;
        this.spawnAmethyst = (main == Element.EARTH || off == Element.EARTH);
    }

    @Override
    public void doReaction(Entity entity, float scale) {
        if (this.spawnAmethyst && canSpawnAmethyst(entity)) {
            this.spawnAmethyst(entity).ifPresent(elementAmethyst -> {
                final float multiplier = scale * (this.once() ? 1 : 20);
                ElementManager.addElementAmount(elementAmethyst, this.ingredient, true, this.ingredientAmount * multiplier);
                ElementManager.addElementAmount(elementAmethyst, this.production, true, this.productionAmount * multiplier);
                entity.level().addFreshEntity(elementAmethyst);
            });
        }
        ElementManager.addElementAmount(entity, this.production, false, this.productionAmount * scale);
    }

    /**
     * 发生淬刃的被攻击者，会记录最大的受伤害值。
     */
    public static void markDamage(Entity entity, float damage) {
        // 发生淬刃的被攻击者，会记录最大的受伤害值。
        ElementManager.ifActiveReaction(entity, ElementReactions.QUENCH_BLADE, (reaction, scale) -> {
            ElementManager.setQuenchBladeDamage(entity, damage, true);
        }, () -> {
            ElementManager.setQuenchBladeDamage(entity, 0, true);
        });
    }

    public static void quenchBlade(LivingIncomingDamageEvent event, Entity attacker){
        ElementManager.ifActiveReaction(attacker, ElementReactions.QUENCH_BLADE, (reaction, scale) -> {
            final float damage = event.getOriginalAmount() + ElementManager.getQuenchBladeDamage(event.getSource().getEntity());
            event.setAmount(damage);
            ElementManager.addPercentElement(event.getEntity(), Element.WATER, false, scale * 0.5F);
        });
    }

}