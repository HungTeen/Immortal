package hungteen.imm.common.cultivation.spell.talisman;

import hungteen.htlib.util.helper.RandomHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.SpellCastContext;
import hungteen.imm.api.spell.TalismanSpell;
import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.common.cultivation.spell.SpellTypeImpl;
import hungteen.imm.common.item.IMMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/19 9:45
 **/
public class EarthFangTalisman extends SpellTypeImpl implements TalismanSpell {

    public EarthFangTalisman() {
        super("earth_fang", property(InscriptionTypes.JOSS_PAPER).cd(200).qi(40));
    }

    @Override
    public boolean checkActivate(SpellCastContext context) {
        int count = RandomHelper.getMinMax(context.owner().getRandom(), 5, 6);
        summonFangs(context.owner(), context.owner().getLookAngle(), count, 2.5);
        return true;
    }

    /**
     * @param owner 召唤者。
     * @param direction 召唤朝向。
     * @param count 尖牙的数量。
     * @param gap 每个尖牙之间的间隔。
     */
    public static void summonFangs(LivingEntity owner, Vec3 direction, int count, double gap) {
        float f = (float) Math.atan2(direction.z, direction.x);
        for (int i = 0; i < count; i++) {
            double dis = gap * (i + 1);
            int delay = i;
            createSpellEntity(owner, owner.getX() + Mth.cos(f) * dis, owner.getZ() + Mth.sin(f) * dis, f, delay);
        }
    }

    private static void createSpellEntity(LivingEntity owner, double x, double z, float yRot, int warmupDelay) {
        double minY = owner.getY() - 1;
        double maxY = owner.getY() + 2;
        BlockPos blockpos = BlockPos.containing(x, maxY, z);
        boolean flag = false;
        double d0 = 0.0;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = owner.level().getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(owner.level(), blockpos1, Direction.UP)) {
                if (!owner.level().isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = owner.level().getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(owner.level(), blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while (blockpos.getY() >= Mth.floor(minY) - 1);

        if (flag) {
            owner.level().addFreshEntity(new EvokerFangs(owner.level(), x, (double) blockpos.getY() + d0, z, yRot, warmupDelay, owner));
            owner.level().gameEvent(GameEvent.ENTITY_PLACE, new Vec3(x, (double) blockpos.getY() + d0, z), GameEvent.Context.of(owner));
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity owner) {
        return 50;
    }

    @Override
    public List<Element> requireElements() {
        return List.of(Element.METAL, Element.EARTH);
    }

    @Override
    public ItemStack getTalismanItem(int level) {
        return new ItemStack(IMMItems.EARTH_FANG_TALISMAN.get());
    }
}