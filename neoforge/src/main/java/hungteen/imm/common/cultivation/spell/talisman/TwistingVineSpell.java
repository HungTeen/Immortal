package hungteen.imm.common.cultivation.spell.talisman;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.TalismanSpell;
import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.misc.TwistingVines;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.item.talisman.TalismanItem;
import hungteen.imm.util.EntityUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/18 21:30
 **/
public class TwistingVineSpell extends SpellTypeImpl implements TalismanSpell {

    public TwistingVineSpell() {
        super("twisting_vine", property(InscriptionTypes.JOSS_PAPER).cd(280).qi(35));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        EntityUtil.playSound(context.level(), context.owner(), SoundEvents.BONE_MEAL_USE);
        Optional<Vec3> targetOpt = TalismanItem.getTargetPosition(context.owner());
        if(targetOpt.isPresent()){
            Vec3 destination = targetOpt.get();
            TwistingVines vines = new TwistingVines(IMMEntities.TWISTING_VINES.get(), context.level());
            vines.setPos(destination);
            vines.setOwner(context.owner());
            vines.setVineHealth(500);
            context.level().addFreshEntity(vines);
            return true;
        } else {
            sendTip(context, NO_TARGET);
            return false;
        }
    }

    @Override
    public List<Element> requireElements() {
        return List.of(Element.WOOD);
    }

    @Override
    public ItemStack getTalismanItem(int level) {
        return new ItemStack(IMMItems.TWISTING_VINE_TALISMAN.get());
    }
}
