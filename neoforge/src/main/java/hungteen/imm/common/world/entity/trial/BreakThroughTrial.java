package hungteen.imm.common.world.entity.trial;

import hungteen.htlib.common.world.entity.DummyEntity;
import hungteen.htlib.common.world.entity.DummyEntityManager;
import hungteen.htlib.common.world.entity.DummyEntityType;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.htlib.util.helper.impl.ParticleHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.cultivation.ExperienceType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.common.IMMConfigs;
import hungteen.imm.common.cultivation.CultivationManager;
import hungteen.imm.common.cultivation.RealmManager;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.event.events.BreakThroughEvent;
import hungteen.imm.util.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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
    protected int tickCount;
    protected int searchCooldown;

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
        if(tag.contains("TickCount")){
            this.tickCount = tag.getInt("TickCount");
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        NBTUtil.write(tag, RealmTypes.registry().byNameCodec(), "RealmType", this.realmType);
        tag.putFloat("Difficulty", this.difficulty);
        if (this.trialPlayer != null) {
            tag.putUUID("TrialPlayer", this.trialPlayer.getUUID());
        }
        tag.putInt("TickCount", this.tickCount);
        return super.save(tag);
    }

    @Override
    public void tick() {
        // 如果玩家不在周围
        if(this.getTrialPlayer() == null){
            // 减少搜索次数。
            if(searchCooldown -- > 0){
                return;
            }
            ServerPlayer player = getLevel().getServer().getPlayerList().getPlayer(this.uuid);
            if(player == null){
                // 玩家可能退出游戏了，此时需要暂停试炼。
                searchCooldown = 60;
                return;
            } else {
                // 玩家逃跑了。
                this.punishTrial(player);
                this.remove();
            }
        }
        ++ this.tickCount;
        if(! this.isRemoved()){
            tickTrial(this.getTrialPlayer());
            tickProgressBar();
            if(this.tickCount >= getTrialLength()){
                this.finishTrial(this.getTrialPlayer());
                this.remove();
            }
        }
    }

    public float getTrialProgress(){
        return this.tickCount * 1.0F / this.getTrialLength();
    }

    protected void tickProgressBar() {
        this.progressBar.setProgress(getTrialProgress());
        this.progressBar.addPlayer(trialPlayer);
    }

    public abstract void tickTrial(ServerPlayer player);

    public abstract ServerBossEvent createProgressBar();

    /**
     * 玩家顺利完成试炼。
     * @param player 试炼玩家。
     */
    public void finishTrial(ServerPlayer player){
        player.playSound(SoundEvents.PLAYER_LEVELUP);
        EventUtil.post(new BreakThroughEvent.Success(player, getRealmType()));
        PlayerHelper.playClientSound(player, SoundEvents.PLAYER_LEVELUP);
        // 更新到新的境界。
        RealmManager.getNextRealm(getRealmType(), PlayerUtil.getCultivationDirection(player)).ifPresent(nextRealm -> {
            RealmManager.updateRealm(player, nextRealm);
        });
//        // 传送回现实世界。
//        SpiritWorldDimension.teleportBackFromSpiritRegion(player.serverLevel(), player);
    }

    /**
     * 玩家在试炼中失败（比如死亡）。
     * @param player 试炼玩家。
     */
    public void punishTrial(ServerPlayer player){
        EventUtil.post(new BreakThroughEvent.Failure(player, getRealmType()));
        CultivationManager.clearBreakThroughProgress(player);
        float reductionPercent = 1 - (float) IMMConfigs.realmSettings().breakThroughFailReduction.getAsDouble();
        PlayerUtil.adjustExperience(player, ExperienceType.ELIXIR, reductionPercent);
        PlayerUtil.adjustExperience(player, ExperienceType.PERSONALITY, reductionPercent);
    }

    @Override
    public void remove() {
        super.remove();
        this.progressBar.removeAllPlayers();
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
    public void addTrialEntity(Entity entity){
        EntityUtil.setData(entity, data -> {
            data.setTrialId(this.getEntityID());
        });
        if(entity instanceof Mob mob) {
            mob.setPersistenceRequired();
            mob.setTarget(trialPlayer);
        }
        LevelUtil.playSound(getLevel(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.NEUTRAL, entity.position());
        ParticleHelper.sendParticles(trialPlayer.serverLevel(), IMMParticles.SPIRIT_ELEMENT.get(), entity.getX(), entity.getY(), entity.getZ(), 10, 0.5D, 0D, 0.5D, 0);
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

    /**
     * @return 晋升难度。
     */
    public float getDifficulty() {
        return difficulty;
    }

    public abstract int getTrialLength();

    public RealmType getRealmType() {
        return realmType;
    }

    @Override
    public boolean renderBorder() {
        return false;
    }

    @Override
    public boolean hasCollision() {
        return false;
    }

    @FunctionalInterface
    public interface Factory {

        BreakThroughTrial create(ServerPlayer trialPlayer, RealmType realmType, float difficulty);
    }
}
