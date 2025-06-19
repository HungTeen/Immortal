package hungteen.imm.common.cultivation.spell.talisman;

import hungteen.htlib.util.helper.MathHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.TalismanSpell;
import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.common.cultivation.QiRootTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.common.item.IMMItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/18 20:57
 **/
public class LightningSpell extends SpellTypeImpl implements TalismanSpell {

    public LightningSpell() {
        super("lightning", property(InscriptionTypes.JOSS_PAPER).cd(180).qi(50));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        Vec3 destination = null;
        if(context.targetOpt().isPresent()){
            destination = context.target().position();
        } else if(context.targetPosOpt().isPresent()){
            destination = MathHelper.toVec3(context.targetPos());
        }
        if(destination != null) {
            LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, context.level());
            bolt.setPos(destination);
            if (context.owner() instanceof ServerPlayer serverPlayer) {
                bolt.setCause(serverPlayer);
            }
            bolt.setDamage(6);
            context.level().addFreshEntity(bolt);
            return true;
        }
        sendTip(context, NO_TARGET);
        return false;
    }

    @Override
    public List<Element> requireElements() {
        return QiRootTypes.LIGHTNING.getElements().stream().toList();
    }

    @Override
    public ItemStack getTalismanItem(int level) {
        return new ItemStack(IMMItems.LIGHTNING_TALISMAN.get());
    }
}
