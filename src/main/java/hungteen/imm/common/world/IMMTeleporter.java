package hungteen.imm.common.world;

import hungteen.htlib.util.helper.MathHelper;
import hungteen.imm.common.block.IMMBlockPatterns;
import hungteen.imm.common.block.IMMBlocks;
import hungteen.imm.common.world.levelgen.IMMLevels;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-17 22:44
 **/
public class IMMTeleporter implements ITeleporter {

    public static final ITeleporter INSTANCE = new IMMTeleporter();

    @Nullable
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        final ResourceKey<Level> srcLevel = entity.level().dimension();
        final ResourceKey<Level> destLevel = destWorld.dimension();
        Vec3 destination = entity.position();
        if((srcLevel.equals(Level.OVERWORLD) && destLevel.equals(IMMLevels.EAST_WORLD)) ||
                (srcLevel.equals(IMMLevels.EAST_WORLD) && destLevel.equals(Level.OVERWORLD))){
            final BlockPattern.BlockPatternMatch match = IMMBlockPatterns.getTeleportPattern().blockPattern().find(destWorld, entity.blockPosition());
            if(match == null){
                buildTeleportAnchor(destWorld, entity.blockPosition());
            } else {
                destination = MathHelper.toVec3(match.getBlock(2, 0, 2).getPos());
            }
        }
        return new PortalInfo(destination, Vec3.ZERO, entity.getYRot(), entity.getXRot());
    }

    private static void buildTeleportAnchor(ServerLevel level, BlockPos pos){
        IMMBlockPatterns.getTeleportPattern().getBlockStates(pos, 2, 1, 2, true).forEach(pair -> {
            level.setBlock(pair.getFirst(), pair.getSecond(), 3);
        });
    }

    @Override
    public boolean isVanilla() {
        return false;
    }
}
