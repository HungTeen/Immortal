package hungteen.immortal.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.immortal.common.entity.human.HumanEntity;
import hungteen.immortal.common.tag.ImmortalItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.item.Items;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-12-06 21:46
 **/
public class SwitchMeleeAttackItem extends Behavior<HumanEntity> {

    public SwitchMeleeAttackItem() {
        super(ImmutableMap.of(), 20);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, HumanEntity entity, long time) {
        return true;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, HumanEntity entity) {
        return true;
    }

    @Override
    protected void start(ServerLevel level, HumanEntity entity, long time) {
        entity.switchInventory(stack -> stack.is(Items.DIAMOND_SWORD), InteractionHand.MAIN_HAND);
        entity.switchInventory(stack -> stack.is(ImmortalItemTags.MELEE_ATTACK_ITEMS), InteractionHand.MAIN_HAND);
    }

    @Override
    protected void tick(ServerLevel level, HumanEntity entity, long time) {
    }

    @Override
    protected void stop(ServerLevel level, HumanEntity entity, long time) {
    }

}
