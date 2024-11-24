package hungteen.imm.common.world.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/22 18:54
 **/
public class SpiritRegionData extends SavedData {

    private static final String SPIRIT_REGION_FILE_ID = "spirit_region";
    private static final int[][] DIRECTIONS = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private static final int CHUNK_DISTANCE = 32 * 16;
    private static final int REGION_HEIGHT = 100;
    private static final int REGION_RADIUS = 20;
    private final ServerLevel level;
    private final Map<String, Vec3> spiritRegionMap = new HashMap<>();
    private int curX = 0;
    private int curY = 0;
    private int curDirection = 0;
    private int changeDirectionCnt = 0;
    private int stepCnt = 0;

    public SpiritRegionData(ServerLevel level) {
        this.level = level;
    }

    /**
     * 每个玩家的 UUID 会对应一个精神领域的逻辑坐标，这个逻辑坐标会在玩家进入精神领域时生成。
     *
     * @param level  精神世界。
     * @param player 玩家。
     * @return 玩家对应的精神领域的实际坐标。
     */
    public static Vec3 getPlayerSpiritRegionPosition(ServerLevel level, Player player) {
        SpiritRegionData regionData = get(level);
        return regionData.spiritRegionMap.computeIfAbsent(player.getStringUUID(), uuid -> regionData.calculateNextPosition());
    }

    /**
     * @return 每个逻辑坐标对应的实际坐标。
     */
    public Vec3 calculateNextPosition() {
        calculateNextLogicalPosition();
        return new Vec3(curX * CHUNK_DISTANCE, REGION_HEIGHT, curY * CHUNK_DISTANCE);
    }

    /**
     * 精神领域的生成类似于在网格上走螺旋线，每个点都是一个精神领域。<br>
     * 1. 起点的逻辑坐标为 (0, 0)。<br>
     * 2. 从起点开始，循环右下左上的方向，每次走的步数为 n / 2 + 1，n 为转弯次数。<br>
     * 3. 根据逻辑坐标最后再换算为实际坐标。<br>
     */
    public void calculateNextLogicalPosition() {
        if (stepCnt == 0) {
            changeDirection();
            stepCnt = getStepCountForNextDirection();
        }
        curX += DIRECTIONS[curDirection][0];
        curY += DIRECTIONS[curDirection][1];
        stepCnt--;
    }

    /**
     * 转换方向，只能顺时针转换。
     */
    public void changeDirection() {
        curDirection = (curDirection + 1) % DIRECTIONS.length;
        changeDirectionCnt++;
        this.setDirty();
    }

    /**
     * 获取下一个方向的步数。
     *
     * @return 下一个方向的步数。
     */
    public int getStepCountForNextDirection() {
        return (changeDirectionCnt / 2) + 1;
    }

    public static SpiritRegionData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(
                () -> new SpiritRegionData(level),
                (tag, provider) -> load(level, tag, provider),
                DataFixTypes.LEVEL
        ), SPIRIT_REGION_FILE_ID);
    }

    private static SpiritRegionData load(ServerLevel level, CompoundTag tag, HolderLookup.Provider provider) {
        final SpiritRegionData manager = new SpiritRegionData(level);
        if (tag.contains("CurX")) {
            manager.curX = tag.getInt("CurX");
        }
        if (tag.contains("CurY")) {
            manager.curY = tag.getInt("CurY");
        }
        if (tag.contains("CurDirection")) {
            manager.curDirection = tag.getInt("CurDirection");
        }
        if (tag.contains("ChangeDirectionCnt")) {
            manager.changeDirectionCnt = tag.getInt("ChangeDirectionCnt");
        }
        if (tag.contains("StepCnt")) {
            manager.stepCnt = tag.getInt("StepCnt");
        }
        if (tag.contains("SpiritRegionMap")) {
            CompoundTag spiritRegionMap = tag.getCompound("SpiritRegionMap");
            spiritRegionMap.getAllKeys().forEach(uuid -> {
                int[] intArray = spiritRegionMap.getIntArray(uuid);
                manager.spiritRegionMap.put(uuid, new Vec3(intArray[0], intArray[1], intArray[2]));
            });
        }
        return manager;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("CurX", curX);
        tag.putInt("CurY", curY);
        tag.putInt("CurDirection", curDirection);
        tag.putInt("ChangeDirectionCnt", changeDirectionCnt);
        tag.putInt("StepCnt", stepCnt);
        {
            CompoundTag spiritRegionMap = new CompoundTag();
            this.spiritRegionMap.forEach((uuid, vec3) -> {
                spiritRegionMap.putIntArray(uuid, new int[]{(int) vec3.x, (int) vec3.y, (int) vec3.z});
            });
            tag.put("SpiritRegionMap", spiritRegionMap);
        }
        return tag;
    }

}
