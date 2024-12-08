package hungteen.imm.common.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import hungteen.imm.common.entity.human.HumanLikeEntity;
import hungteen.imm.util.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-12-06 21:46
 **/
public class UseTalisman extends Behavior<HumanLikeEntity> {

    private Predicate<ItemStack> itemPredicate;

    public UseTalisman(Predicate<ItemStack> itemPredicate) {
        super(ImmutableMap.of(), 20);
        this.itemPredicate = itemPredicate;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, HumanLikeEntity entity, long time) {
        return EntityUtil.isMainHolding(entity, itemPredicate);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, HumanLikeEntity entity) {
        return ! EntityUtil.isMainHolding(entity, itemPredicate);
    }

    @Override
    protected void start(ServerLevel level, HumanLikeEntity entity, long time) {
        entity.switchInventory(EquipmentSlot.MAINHAND, itemPredicate);
    }

    @Override
    protected void tick(ServerLevel level, HumanLikeEntity owner, long gameTime) {

    }


}
