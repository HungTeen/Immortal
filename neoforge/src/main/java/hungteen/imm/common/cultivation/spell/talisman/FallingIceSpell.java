package hungteen.imm.common.cultivation.spell.talisman;

import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.TalismanSpell;
import hungteen.imm.common.IMMSounds;
import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.misc.FallingIceEntity;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.item.talisman.TalismanItem;
import hungteen.imm.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/18 21:14
 **/
public class FallingIceSpell extends SpellTypeImpl implements TalismanSpell {

    public FallingIceSpell() {
        super("falling_ice", property(InscriptionTypes.JOSS_PAPER).cd(100).qi(30));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        EntityUtil.playSound(context.level(), context.owner(), IMMSounds.FALLING_ICE_FINISH.get());
        Optional<Vec3> targetOpt = TalismanItem.getTargetPosition(context.owner());
        if(targetOpt.isPresent()){
            Vec3 destination = targetOpt.get().add(0, 7, 0);
            FallingIceEntity iceEntity = new FallingIceEntity(IMMEntities.FALLING_ICE.get(), context.level());
            iceEntity.setPos(destination);
            iceEntity.setOwner(context.owner());
            iceEntity.setFloatTicks(30);
            context.level().addFreshEntity(iceEntity);
            return true;
        } else {
            sendTip(context, NO_TARGET);
            return false;
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity owner) {
        return 20;
    }

    @Override
    public List<Element> requireElements() {
        return List.of(Element.WATER);
    }

    @Override
    public ItemStack getTalismanItem(int level) {
        return new ItemStack(IMMItems.FALLING_ICE_TALISMAN.get());
    }
}

