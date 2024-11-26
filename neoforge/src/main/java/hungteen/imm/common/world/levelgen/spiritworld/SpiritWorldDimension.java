package hungteen.imm.common.world.levelgen.spiritworld;

import hungteen.htlib.util.helper.MathHelper;
import hungteen.imm.common.world.data.SpiritRegionData;
import hungteen.imm.common.world.levelgen.IMMLevels;
import hungteen.imm.common.world.structure.structures.SpiritPagoda;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/24 15:11
 **/
public class SpiritWorldDimension {

    public static void teleportToSpiritRegion(ServerLevel level, Player player) {
        if (!level.dimension().equals(IMMLevels.SPIRIT_WORLD)) {
            Vec3 spiritRegionPos = SpiritRegionData.getPlayerSpiritRegionPosition(level, player);
            Vec3 centerPos = MathHelper.toVec3(MathHelper.toBlockPos(spiritRegionPos));
            ServerLevel spiritWorld = level.getServer().getLevel(IMMLevels.SPIRIT_WORLD);
            if (spiritWorld != null) {
                PlayerUtil.setData(player, data -> {
                    data.getMiscData().setLastPosBeforeSpiritWorld(level.dimension(), player.position());
                });
                player.changeDimension(new DimensionTransition(spiritWorld, centerPos, Vec3.ZERO, 0, 0, false, DimensionTransition.PLAY_PORTAL_SOUND));
                BlockPos pos = MathHelper.toBlockPos(spiritRegionPos);
                List<BlockPos> anchorPos = SpiritPagoda.placeSpiritPagoda(spiritWorld, pos);
                BlockPos curAnchorPos = getPlayerLevelAnchorPos(anchorPos, player, pos);

                // teleport to above the anchor position.
                player.teleportTo(player.getX(), curAnchorPos.getY() + 1, player.getZ());
            }
        }
    }

    public static BlockPos getPlayerLevelAnchorPos(List<BlockPos> anchorPos, Player player, BlockPos center){
        BlockPos playerPos = new BlockPos(center.getX(), SpiritPagoda.BASE_HEIGHT + 1, center.getZ());
        if(! anchorPos.isEmpty()){
            // TODO according to player realm level to choose the anchor position.
            int idx = Math.clamp(6, 0, anchorPos.size() - 1);
            playerPos = anchorPos.get(idx);
        }
        return playerPos;
    }

}
