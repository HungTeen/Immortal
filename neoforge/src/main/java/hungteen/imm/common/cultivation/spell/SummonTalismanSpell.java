package hungteen.imm.common.cultivation.spell;

import hungteen.htlib.util.helper.MathHelper;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.TalismanSpell;
import hungteen.imm.common.entity.misc.talisman.RangeEffectTalismanEntity;
import hungteen.imm.util.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/28 11:09
 */
public abstract class SummonTalismanSpell extends SpellTypeImpl implements TalismanSpell {

    public SummonTalismanSpell(String name, SpellProperty property) {
        super(name, property);
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        if(context.level() instanceof ServerLevel serverLevel){
            EntityType<? extends RangeEffectTalismanEntity> entityType = getEntityType();
            Optional<? extends RangeEffectTalismanEntity> opt = EntityUtil.spawn(serverLevel, entityType, MathHelper.toVec3(context.owner().blockPosition()));
            if(opt.isPresent()){
                RangeEffectTalismanEntity talisman = opt.get();
                talisman.setOwner(context.owner());
                talisman.setSpellLevel(context.spellLevel());
                talisman.setCenter(context.owner().blockPosition());
                talisman.setMaxExistTick(talisman.calculateExistTick(context.scale()));
                return true;
            }
        }
        return super.checkActivate(context);
    }

    public abstract EntityType<? extends RangeEffectTalismanEntity> getEntityType();
}
