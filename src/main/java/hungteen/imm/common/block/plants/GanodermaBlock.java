package hungteen.imm.common.block.plants;

import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.util.RandomUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-07-13 22:09
 **/
public class GanodermaBlock extends SpiritualPlantBlock {

    private static final VoxelShape AABB = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 7.0D, 12.0D);

    public GanodermaBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.BROWN_MUSHROOM).lightLevel(state -> 8));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        if (source.nextInt(5) == 0) {
            final Vec3 offset = RandomUtil.vec3Range(source, 0.2, 0.4);
            ParticleHelper.spawnRandomSpeedParticle(level, IMMParticles.SPIRIT.get(), MathHelper.toVec3(pos).add(offset),0.05F);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return AABB;
    }

}
