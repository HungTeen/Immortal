package hungteen.imm.common.entity.misc.talisman;

import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.TalismanSpell;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.cultivation.SpellTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.level.Level;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/19 11:00
 **/
public class WoodHealingTalismanEntity extends RangeEffectTalismanEntity implements ItemSupplier {

    public WoodHealingTalismanEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public void tickTalisman() {
        super.tickTalisman();
        if(this.getExistTick() % 20 == 3){
            int range = 5;
            EntityHelper.getPredicateEntities(this, EntityHelper.getEntityAABB(this, range, range), LivingEntity.class, target ->{
                return target.equals(this.getOwner());
            }).forEach(living -> {
                living.addEffect(EffectHelper.viewEffect(MobEffects.REGENERATION, 100, 0));
                ElementManager.addElementAmount(living, Element.WOOD, true, 15);
            });
        }
    }

    @Override
    public int calculateExistTick(float scale) {
        return Mth.floor(400 * scale);
    }

    @Override
    public TalismanSpell getSpell() {
        return SpellTypes.SPROUT;
    }

}
