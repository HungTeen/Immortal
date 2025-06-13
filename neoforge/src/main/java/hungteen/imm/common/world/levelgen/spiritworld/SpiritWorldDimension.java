package hungteen.imm.common.world.levelgen.spiritworld;

import hungteen.htlib.common.entity.SeatEntity;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.block.IMMPoiTypes;
import hungteen.imm.common.capability.player.MiscData;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.entity.human.cultivator.RealityPlayer;
import hungteen.imm.common.world.data.SpiritRegionData;
import hungteen.imm.common.world.levelgen.IMMLevels;
import hungteen.imm.common.world.structure.structures.SpiritPagoda;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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
        if (!isSpiritWorld(level)) {
            final RealmType currentRealm = PlayerUtil.getPlayerRealm(player);
            final int realmRegionLevel = currentRealm.getRealmRegionLevel();
            Vec3 spiritRegionPos = SpiritRegionData.getPlayerSpiritRegionPosition(level, player);
            Vec3 centerPos = MathHelper.toVec3(MathHelper.toBlockPos(spiritRegionPos));
            ServerLevel spiritWorld = level.getServer().getLevel(IMMLevels.SPIRIT_WORLD);
            if (spiritWorld != null) {
                // 保存传送前的位置。
                PlayerUtil.setData(player, data -> {
                    data.getMiscData().setLastPosBeforeSpiritWorld(level.dimension(), player.position());
                });
                // 获取传送前的坐垫。
                BlockState cushion = null;
                if (player.getVehicle() instanceof SeatEntity seat) {
                    cushion = level.getBlockState(seat.blockPosition());
                    PlayerUtil.quitResting(player);
                    // 召唤一个玩家替身在原处。
                    EntityUtil.create(level, IMMEntities.REALITY_PLAYER.get(), seat.blockPosition(), MobSpawnType.EVENT).ifPresent(realityPlayer -> {
                        realityPlayer.startRiding(seat);
                        realityPlayer.setOwner(player);
                        level.addFreshEntity(realityPlayer);
                    });
                }

                // 玩家传送到对应的精神领域。
                player.changeDimension(new DimensionTransition(spiritWorld, centerPos, Vec3.ZERO, 0, 0, false, DimensionTransition.PLAY_PORTAL_SOUND));
                BlockPos pos = MathHelper.toBlockPos(spiritRegionPos);
                List<BlockPos> anchorPos = SpiritPagoda.placeSpiritPagoda(spiritWorld, pos);
                BlockPos curAnchorPos = getPlayerLevelAnchorPos(anchorPos, pos, realmRegionLevel);
                // 传送到对应层。
                player.teleportTo(player.getX(), curAnchorPos.getY() + 1, player.getZ());
                if (cushion != null) {
                    spiritWorld.setBlock(player.blockPosition(), cushion, 3);
                    PlayerUtil.sitToMeditate(player, player.blockPosition(), 0.5F, true);
                }
            }
        }
    }

    public static void teleportBackFromSpiritRegion(ServerLevel level, ServerPlayer player) {
        if (isSpiritWorld(level)) {
            // 玩家传送回现实世界。
            MiscData.IMMPosition position = PlayerUtil.getData(player, data -> data.getMiscData().getLastPosBeforeSpiritWorld());
            if (position == null) {
                position = new MiscData.IMMPosition(Level.OVERWORLD, MathHelper.toVec3(level.getLevelData().getSpawnPos()));
            }
            ServerLevel backLevel = level.getServer().getLevel(position.level());
            if (backLevel == null) {
                backLevel = level.getServer().overworld();
            }
            player.changeDimension(new DimensionTransition(
                    backLevel,
                    position.pos(),
                    Vec3.ZERO,
                    0,
                    0,
                    false,
                    DimensionTransition.PLAY_PORTAL_SOUND)
            );
            // 删除之前的假人和坐垫。
            EntityHelper.getPredicateEntities(player, EntityHelper.getEntityAABB(player, 5, 5), RealityPlayer.class, target -> {
                if (target.getPlayerOpt().isPresent()) {
                    return target.getPlayerOpt().get().equals(player);
                }
                return false;
            }).forEach(l -> {
                if (l.getVehicle() != null) {
                    l.getVehicle().discard();
                }
                l.discard();
            });
            // 玩家重新坐上坐垫。
            backLevel.getPoiManager().getInRange(
                    poi -> poi.is(IMMPoiTypes.CUSHION.getRegistryName()),
                    player.blockPosition(),
                    3,
                    PoiManager.Occupancy.HAS_SPACE
            ).min((p1, p2) -> {
                return (int) (p1.getPos().distSqr(player.blockPosition()) - p2.getPos().distSqr(player.blockPosition()));
            }).ifPresent(poi -> {
                BlockPos pos = poi.getPos();
                PlayerUtil.sitToMeditate(player, pos, 0.5F, true);
            });
        }
    }

    public static BlockPos getPlayerLevelAnchorPos(List<BlockPos> anchorPos, BlockPos center, int realmRegionLevel) {
        BlockPos playerPos = new BlockPos(center.getX(), SpiritPagoda.BASE_HEIGHT + 1, center.getZ());
        if (!anchorPos.isEmpty()) {
            int idx = Math.clamp(realmRegionLevel, 1, anchorPos.size()) - 1;
            playerPos = anchorPos.get(idx);
        }
        return playerPos;
    }

    public static boolean isSpiritWorld(Level level) {
        return level.dimension().equals(IMMLevels.SPIRIT_WORLD);
    }

}
