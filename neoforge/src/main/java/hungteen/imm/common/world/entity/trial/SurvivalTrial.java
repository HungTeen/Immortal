package hungteen.imm.common.world.entity.trial;

import hungteen.htlib.common.world.entity.DummyEntityType;
import hungteen.htlib.util.WeightedList;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.RandomHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/14 15:23
 **/
public abstract class SurvivalTrial extends BreakThroughTrial {

    private static final MutableComponent TITLE = TipUtil.info("trial.survival_title");
    protected int nextSpawnInterval = 50;
    protected int lastSpawnTick;

    public SurvivalTrial(DummyEntityType<?> dummyEntityType, ServerPlayer trialPlayer, RealmType realmType, float difficulty) {
        super(dummyEntityType, trialPlayer, realmType, difficulty);
    }

    public SurvivalTrial(DummyEntityType<?> dummyEntityType, Level level, CompoundTag trialTag) {
        super(dummyEntityType, level, trialTag);
    }

    @Override
    public void tickTrial(ServerPlayer player) {
        if (this.tickCount - this.lastSpawnTick >= this.nextSpawnInterval) {
            List<SpawnEntry> spawnEntries = getSpawnEntries();
            List<SpawnEntry> matchEntries = new ArrayList<>();
            for (var entry : spawnEntries) {
                // 难度不匹配或出场时间不对。
                if (entry.requireDifficulty() > getDifficulty() || entry.spawnProgress() > getTrialProgress()) {
                    continue;
                }
                int currentCount = EntityHelper.getPredicateEntities(player, EntityHelper.getEntityAABB(player, 50, 50), Entity.class, e -> {
                    return e.getType().equals(entry.type());
                }).size();
                if (currentCount < entry.maxCount()) {
                    matchEntries.add(entry);
                }
            }
            WeightedList<SpawnEntry> weightedList = WeightedList.create(matchEntries);
            Optional<SpawnEntry> entry = weightedList.getRandomItem(player.getRandom());
            if (entry.isPresent()) {
                BlockPos offset = null;
                int tryCnt = 5;
                while(tryCnt -- > 0){
                    offset = RandomHelper.circleArea(player.getRandom(), 5, getWidth()).offset(0, 1, 0);
                    Vec3 pos = MathHelper.toVec3(getBlockPosition().offset(offset));
                    // 距离玩家太近。
                    if(pos.distanceTo(player.position()) < 7){
                        continue;
                    }
                    Optional<? extends Entity> entityOpt = EntityUtil.spawn(player.serverLevel(), entry.get().type(), pos, true);
                    if(entityOpt.isPresent()){
                        addTrialEntity(entityOpt.get());
                        this.lastSpawnTick = this.tickCount;
                        this.nextSpawnInterval = getSpawnInterval().sample(player.getRandom());
                        return;
                    }
                }
            }
            this.nextSpawnInterval += 5;
        }
    }

    @Override
    protected void tickProgressBar() {
        super.tickProgressBar();
        int leftTick = getTrialLength() - tickCount;
        int leftMinute = leftTick / 1200;
        int leftSecond = leftTick % 1200 / 20;
        String formatTime = String.format("%02d:%02d", leftMinute, leftSecond);
        this.progressBar.setName(TITLE.append(" - " + formatTime));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("LastSpawnTick")) {
            lastSpawnTick = tag.getInt("LastSpawnTick");
        }
        if(tag.contains("NextSpawnInterval")) {
            nextSpawnInterval = tag.getInt("NextSpawnInterval");
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("LastSpawnTick", lastSpawnTick);
        tag.putInt("NextSpawnInterval", nextSpawnInterval);
        return super.save(tag);
    }

    @Override
    public ServerBossEvent createProgressBar() {
        return new ServerBossEvent(TITLE, BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    }

    public abstract IntProvider getSpawnInterval();

    public abstract List<SpawnEntry> getSpawnEntries();

    public record SpawnEntry(EntityType<?> type,
                             int weight,
                             int maxCount,
                             float spawnProgress,
                             int requireDifficulty) implements WeightedEntry {
        @Override
        public Weight getWeight() {
            return Weight.of(weight());
        }
    }
}
