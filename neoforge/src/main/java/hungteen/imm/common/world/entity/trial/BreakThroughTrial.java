package hungteen.imm.common.world.entity.trial;

import hungteen.htlib.common.world.entity.DummyEntity;
import hungteen.htlib.common.world.entity.DummyEntityManager;
import hungteen.htlib.common.world.entity.DummyEntityType;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.LevelUtil;
import hungteen.imm.util.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-08-01 17:09
 **/
public abstract class BreakThroughTrial extends DummyEntity {

    private final RealmType realmType;
    private final float difficulty;
    private final ServerBossEvent progressBar;
    private ServerPlayer trialPlayer;
    private UUID uuid;
    private int tickCount;

    public BreakThroughTrial(DummyEntityType<?> dummyEntityType, ServerPlayer trialPlayer, RealmType realmType, float difficulty) {
        super(dummyEntityType, trialPlayer.serverLevel(), trialPlayer.position());
        this.realmType = realmType;
        this.difficulty = difficulty;
        this.trialPlayer = trialPlayer;
        this.uuid = trialPlayer.getUUID();
        this.progressBar = createProgressBar();
    }

    public BreakThroughTrial(DummyEntityType<?> dummyEntityType, Level level, CompoundTag trialTag) {
        super(dummyEntityType, level, trialTag);
        this.progressBar = createProgressBar();
        this.realmType = CodecHelper.parse(RealmTypes.registry().byNameCodec(), trialTag.get("RealmType"))
                .resultOrPartial(msg -> IMMAPI.logger().error(msg))
                .orElseThrow();
        this.difficulty = trialTag.getFloat("Difficulty");
    }

    public static Optional<BreakThroughTrial> getTrialFor(ServerPlayer player){
        List<BreakThroughTrial> list = getTrialsFor(player);
        if(list.size() > 1){
            IMMAPI.logger().error("Player {} has more than one trial, system removed all of them !", player.getName());
            list.forEach(BreakThroughTrial::remove);
        } else if(list.size() == 1){
            return Optional.of(list.getFirst());
        }
        return Optional.empty();
    }

    public static List<BreakThroughTrial> getTrialsFor(ServerPlayer player){
        return DummyEntityManager.getDummyEntities(player.serverLevel()).stream()
                .filter(dummyEntity -> {
                    if (dummyEntity instanceof BreakThroughTrial trial) {
                        return player.equals(trial.getTrialPlayer());
                    }
                    return false;
                }).map(BreakThroughTrial.class::cast).toList();

    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("TrialPlayer")) {
            this.uuid = tag.getUUID("TrialPlayer");
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        NBTUtil.write(tag, RealmTypes.registry().byNameCodec(), "RealmType", this.realmType);
        tag.putFloat("Difficulty", this.difficulty);
        if (this.trialPlayer != null) {
            tag.putUUID("TrialPlayer", this.trialPlayer.getUUID());
        }
        return super.save(tag);
    }

    @Override
    public void tick() {
        if(this.getTrialPlayer() == null || this.getLevel().getDifficulty() == Difficulty.PEACEFUL){
            this.remove();
        }
        ++ this.tickCount;
        if(! this.isRemoved()){
            tickTrial(this.getTrialPlayer());
            if(this.tickCount >= getTrialLength()){
                this.finishTrial(this.getTrialPlayer());
                this.remove();
            }
        }
    }

    protected void tickProgressBar() {
        this.progressBar.setProgress(this.tickCount * 1.0F / this.getTrialLength());
    }

    public abstract void tickTrial(ServerPlayer player);

    public abstract ServerBossEvent createProgressBar();

    public void finishTrial(ServerPlayer player){

    }

    @Override
    public void remove() {
        super.remove();
        this.clearAllSpawnedEntities();
    }

    public void clearAllSpawnedEntities(){
        AABB aabb = MathHelper.getAABB(this.getPosition(), 60, 60);
        LevelUtil.getEntities(this.getLevel(), LivingEntity.class, aabb, e -> {
            return EntityUtil.getData(e).getTrialId() == this.getEntityID();
        }).forEach(target -> {
            target.discard();
        });
    }

    /**
     * 给召唤的实体添加试炼数据。
     */
    public void addTrialData(Entity entity){
        EntityUtil.setData(entity, data -> {
            data.setTrialId(this.getEntityID());
        });
    }

    @Nullable
    public ServerPlayer getTrialPlayer() {
        if (this.trialPlayer == null && this.uuid != null && this.getLevel() instanceof ServerLevel serverLevel) {
            if (serverLevel.getPlayerByUUID(this.uuid) instanceof ServerPlayer serverPlayer) {
                this.trialPlayer = serverPlayer;
            }
        }
        return this.trialPlayer;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public abstract int getTrialLength();

    public RealmType getRealmType() {
        return realmType;
    }

    @FunctionalInterface
    public interface Factory {

        BreakThroughTrial create(ServerPlayer trialPlayer, RealmType realmType, float difficulty);
    }
}
