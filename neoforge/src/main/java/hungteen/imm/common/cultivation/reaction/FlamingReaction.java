package hungteen.imm.common.cultivation.reaction;

import hungteen.htlib.util.helper.JavaHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.misc.damage.IMMDamageSources;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

/**
 * (强木 + 火)为基本相生反应，但是(弱木 + 火)也能发生相生反应！
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/28 22:32
 **/
public class FlamingReaction extends GenerationReaction {

    private final boolean robustReaction;

    public FlamingReaction(String name, boolean once, int priority, boolean robustReaction, float woodCostAmount, float percent) {
        super(name, once, priority, Element.WOOD, woodCostAmount, Element.FIRE, percent * woodCostAmount);
        this.robustReaction = robustReaction;
    }

    @Override
    protected float getMatchAmount(Entity entity, ElementEntry entry) {
        if(entry.element() == Element.WOOD && ! this.robustReaction){
            return ElementManager.getElementAmount(entity, Element.WOOD, false);
        }
        return super.getMatchAmount(entity, entry);
    }

    @Override
    public void doReaction(Entity entity, float scale) {
        super.doReaction(entity, scale);
        if(! this.robustReaction && entity.level().getRandom().nextFloat() < 0.1F){
            entity.setRemainingFireTicks(Math.min(2, (int)(scale * 5)) * 20);
            entity.hurt(IMMDamageSources.elementReaction(entity), scale * 2);
        }
        if (entity.level() instanceof ServerLevel level && level.getRandom().nextFloat() < 0.3F) {
            ParticleHelper.sendParticles(level, ParticleTypes.FLAME, entity.getX(), entity.getY(0.5F), entity.getZ(), 5, 0.1F);
        }
        EntityHelper.getPredicateEntities(entity, EntityHelper.getEntityAABB(entity, 4, 3), Entity.class, JavaHelper::alwaysTrue).forEach(target -> {
            if (target.level().getRandom().nextFloat() < 0.1F) {
                ElementManager.addElementAmount(target, Element.FIRE, false, scale * 5);
            }
        });
    }
}
