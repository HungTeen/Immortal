package hungteen.imm.api.entity;

import hungteen.imm.util.interfaces.Trader;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.npc.Npc;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-21 18:33
 **/
public interface HumanLike extends Cultivatable, SpellCaster, SectMember, RangedAttackMob, InventoryCarrier, Npc, Trader {
}
