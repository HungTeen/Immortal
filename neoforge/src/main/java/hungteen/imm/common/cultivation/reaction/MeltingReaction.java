package hungteen.imm.common.cultivation.reaction;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/**
 * （火 + 金）相克反应。
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/28 22:33
 **/
public class MeltingReaction extends InhibitionReaction {

    private final float waterAmount;

    public MeltingReaction(String name, boolean once, float waterAmount, Element main, float mainAmount, Element off, float offAmount) {
        super(name, once, main, mainAmount, off, offAmount);
        this.waterAmount = waterAmount;
    }

    @Override
    public float match(Entity entity) {
        final float scale = super.match(entity);
        if(!ElementManager.hasElement(entity, Element.WATER, false)){
            return scale;
        } else if(entity.level().getRandom().nextFloat() < 0.05F) {
            final float amount = Math.min(ElementManager.getAmount(entity, Element.WATER, false), this.waterAmount * scale);
            return Math.min(scale, amount / this.waterAmount) / 5;
        }
        return 0F;
    }

    @Override
    public void doReaction(Entity entity, float scale) {
        super.doReaction(entity, scale);
        float seconds = 4 * scale;
        float range = 1.5F * scale;
        if(ElementManager.hasElement(entity, Element.FIRE, true)){
            seconds *= 1.5F;
        }
        if(ElementManager.hasElement(entity, Element.METAL, true)){
            range += Math.sqrt(scale + 1) / 2;
        }

        final boolean hasWater = ElementManager.hasElement(entity, Element.WATER, false);
        // 水介入反应导致爆炸被抑制。
        if(hasWater){
            seconds *= 0.5F;
            range *= 0.6F;
        }
        entity.setRemainingFireTicks(((int) seconds + 2) * 20);
        entity.level().explode(null, entity.getX(), entity.getY(), entity.getZ(), range, Level.ExplosionInteraction.NONE);
        // 爆炸后再附着元素。
        if(hasWater){
            EntityUtil.forRange(entity, LivingEntity.class, range * 2, range, EntityHelper::isEntityValid, (target, factor) -> {
                ElementManager.addElementAmount(target, Element.WATER, false, scale * 10F * factor);
            });
        }
    }

    @Override
    public void consume(Entity entity, float scale) {
        super.consume(entity, scale);
        if(ElementManager.hasElement(entity, Element.WATER, false)){
            ElementManager.consumeAmount(entity, Element.WATER, false, this.waterAmount * scale);
        }
    }
}
