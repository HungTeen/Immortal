package hungteen.imm.common.entity.misc.talisman;

import hungteen.imm.api.spell.TalismanSpell;
import hungteen.imm.common.cultivation.SpellTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/19 11:00
 **/
public class SproutTalismanEntity extends RangeEffectTalismanEntity implements ItemSupplier {

    public SproutTalismanEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public void tickTalisman() {
        super.tickTalisman();
        if(this.getExistTick() % 10 == 0 && this.random.nextFloat() < 0.5){
            int range = 4;
            List<BlockPos.MutableBlockPos> list = new ArrayList<>();
            for(int i = - range; i <= range; ++ i){
                for(int j = - range; j <= range; ++ j){
                    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(this.getX() + i, this.getY(), this.getZ() + j);
                    while(pos.getY() >= Math.max(level().getMinBuildHeight(), getY() - range) && level().getBlockState(pos).isEmpty()){
                        pos.move(0, -1, 0);
                    }
                    if(pos.getY() >= level().getMinBuildHeight() && level().getBlockState(pos).getBlock() instanceof BonemealableBlock){
                        list.add(pos);
                    }
                }
            }
            Collections.shuffle(list);
            for(BlockPos.MutableBlockPos pos : list){
                if(applyOnPos(level(), pos)){
                    break;
                }
            }
        }
    }

    /**
     * {@link BoneMealItem#useOn(UseOnContext)}.
     */
    public boolean applyOnPos(Level level, BlockPos pos) {
        boolean success = false;
        if (BoneMealItem.applyBonemeal(ItemStack.EMPTY, level, pos, null)) {
            if (! level.isClientSide()) {
                level.levelEvent(1505, pos, 15);
            }
            success = true;
        } else {
            final BlockState blockstate = level.getBlockState(pos);
            final boolean flag = blockstate.isFaceSturdy(level, pos, Direction.DOWN);
            if (flag && BoneMealItem.growWaterPlant(ItemStack.EMPTY, level, pos.above(), Direction.UP)) {
                if (! level.isClientSide()) {
                    level.levelEvent(1505, pos.above(), 15);
                }
                success = true;
            }
        }
        return success;
    }

    @Override
    public int calculateExistTick(float scale) {
        return Mth.floor(600 * scale);
    }

    @Override
    public TalismanSpell getSpell() {
        return SpellTypes.SPROUT;
    }

}
