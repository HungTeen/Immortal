package hungteen.imm.common.cultivation;

import hungteen.htlib.common.world.entity.DummyEntityManager;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.cultivation.ExperienceType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.IMMConfigs;
import hungteen.imm.common.capability.player.IMMPlayerData;
import hungteen.imm.common.entity.effect.IMMEffects;
import hungteen.imm.common.event.events.BreakThroughEvent;
import hungteen.imm.common.world.entity.trial.BreakThroughTrial;
import hungteen.imm.common.world.entity.trial.MortalityTrial;
import hungteen.imm.common.world.levelgen.spiritworld.SpiritWorldDimension;
import hungteen.imm.util.EventUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-26 18:36
 **/
public class CultivationManager {

    public static final int MAX_VALID_KILL_COUNT = 5;
    public static final float DEFAULT_KILL_XP = 0.2F;
    private static final Map<RealmType, BreakThroughTrial.Factory> BREAK_THROUGH_MAP = new HashMap<>();

    static {
        BREAK_THROUGH_MAP.put(RealmTypes.MORTALITY, MortalityTrial::new);
    }

    public static float getMaxExperience(Player player) {
        return CultivationManager.getMaxExperience(player, PlayerUtil.getPlayerRealm(player));
    }

    /**
     * 每一个 {@link ExperienceType} 需要多少修为，设置为总修为的 50%，也就是说不需要每个经验都拿满才能升级。
     */
    public static float getMaxExperience(Entity entity, RealmType realm){
        return realm.maxCultivation() * 0.4F;
    }

    /**
     * @return 生物当前的修为总和。
     */
    public static float getCultivation(Entity entity) {
        if(entity instanceof Player player){
            return PlayerUtil.getCultivation(player);
        }
        return 0;
    }

    /**
     * @return 当前境界升级总共需要多少修为。
     */
    public static float getMaxCultivation(Entity entity) {
        return RealmManager.getRealm(entity).maxCultivation();
    }

    /**
     * 修为达到了当前境界的升级门槛。
     */
    public static boolean reachThreshold(Player player) {
        return CultivationManager.hasThreshold(player) && getCultivation(player) >= getMaxCultivation(player);
    }

    /**
     * 当前存在门槛，不是每个境界的每个阶段都有门槛。
     */
    public static boolean hasThreshold(Entity entity){
        return RealmManager.getRealm(entity).hasThreshold();
    }

    /**
     * @return 能够跨越大境界（比如小圆满和大圆满都可以）。
     */
    public static boolean canLevelUp(Entity entity){
        return RealmManager.getRealm(entity).canLevelUp();
    }

    /**
     * 玩家在精神领域的修行界面点击突破按钮，玩家将触发突破考验。
     */
    public static void breakThroughStart(ServerPlayer player){
        final RealmType oldRealm = PlayerUtil.getPlayerRealm(player);
        BreakThroughTrial.Factory factory = BREAK_THROUGH_MAP.getOrDefault(oldRealm, null);
        if(factory != null){
            BreakThroughTrial trial = factory.create(player, oldRealm, getTrialDifficulty(player, oldRealm));
            if(EventUtil.post(new BreakThroughEvent.Start(player, trial))){
                return;
            }
            DummyEntityManager.addEntity(player.serverLevel(), trial);
            player.removeEffect(IMMEffects.BREAK_THROUGH.holder());
        }
    }

    public static void breakThroughSuccess(ServerPlayer player){
        BreakThroughTrial.getTrialFor(player).ifPresent(trial -> {
            EventUtil.post(new BreakThroughEvent.Success(player, trial.getRealmType()));
            PlayerHelper.playClientSound(player, SoundEvents.PLAYER_LEVELUP);
            // 更新到新的境界。
            RealmManager.getNextRealm(trial.getRealmType(), true).ifPresent(nextRealm -> {
                RealmManager.updateRealm(player, nextRealm);
            });
            trial.remove();
        });
    }

    public static void breakThroughFail(ServerPlayer player){
        BreakThroughTrial.getTrialFor(player).ifPresent(trial -> {
            EventUtil.post(new BreakThroughEvent.Failure(player, trial.getRealmType()));
            trial.remove();
        });
        clearBreakThroughProgress(player);
        float reductionPercent = 1 - (float) IMMConfigs.realmSettings().breakThroughFailReduction.getAsDouble();
        PlayerUtil.adjustExperience(player, ExperienceType.ELIXIR, reductionPercent);
        PlayerUtil.adjustExperience(player, ExperienceType.PERSONALITY, reductionPercent);

    }

    public static float getTrialDifficulty(Player player, RealmType realm){
        // TODO 设置试炼难度。
//        final float karma = Mth.clamp(KarmaManager.calculateKarma(player) + (player.getRandom().nextFloat() < 0.5F ? 0 : player.getRandom().nextInt(10)), 0, KarmaManager.MAX_KARMA_VALUE);
//        return karma + (realm == RealmTypes.MORTALITY ? 0 : (stage.canLevelUp() ? 100 : stage == RealmStage.PRELIMINARY ? 0 : stage == RealmStage.MIDTERM ? 30 : 60));
        return 0;
    }

    /**
     * 玩家点击修行界面的冥想按钮，将玩家传送到对应的精神领域的位置。
     */
    public static void meditate(ServerPlayer player){
        SpiritWorldDimension.teleportToSpiritRegion(player.serverLevel(), player);
//        final RealmStage currentStage = PlayerUtil.getPlayerRealmStage(player);
//        getNextRealmStatus(currentRealm, currentStage).ifPresent(status -> {
//            final RealmType nextRealm = status.realm();
//            final RealmStage nextStage = status.stage();
//            if(player.level() instanceof ServerLevel serverLevel){
//                final float difficulty = getTrialDifficulty(player, currentRealm, currentStage);
////                WeightedList.create(breakThroughRaids(player.level()).stream().filter(raid -> {
////                    return raid.match(nextRealm, nextStage, difficulty);
////                }).toList()).getRandomItem(player.getRandom()).ifPresentOrElse(raid -> {
//////                    DummyEntityManager.addEntity(serverLevel, new BreakThroughTrial(serverLevel, player, difficulty, raid));
////                    PlayerUtil.addIntegerData(player, PlayerRangeIntegers.BREAK_THROUGH_TRIES, 1);
////                }, () -> {
////                    breakThroughStart(player, nextRealm, nextStage);
////                });
//            }
//        });
    }

    /**
     * 增加突破进度。
     */
    public static void addBreakThroughProgress(Player player, float progress){
        if(reachThreshold(player)){
            PlayerUtil.addFloatData(player, IMMPlayerData.FloatData.BREAK_THROUGH_PROGRESS, progress);
        } else {
            PlayerHelper.sendTipTo(player, TipUtil.info("cultivation_not_enough").withStyle(ChatFormatting.DARK_RED));
        }
    }

    public static void clearBreakThroughProgress(Player player){
        PlayerUtil.setFloatData(player, IMMPlayerData.FloatData.BREAK_THROUGH_PROGRESS, 0);
    }

    public static void onPlayerKillLiving(ServerPlayer player, LivingEntity living){
        final int count = player.getStats().getValue(Stats.ENTITY_KILLED, living.getType());
        if(count < MAX_VALID_KILL_COUNT){
//            final float xp = getKillXp(living.getType()) * (1 - count * 1F / MAX_VALID_KILL_COUNT);
//            PlayerUtil.addExperience(player, ExperienceTypes.FIGHTING, xp);
        }
    }

    public static boolean canBreakThrough(Player player){
        return PlayerUtil.getFloatData(player, IMMPlayerData.FloatData.BREAK_THROUGH_PROGRESS) >= 1;
    }

    public static float getBreakThroughProgress(Player player){
        return PlayerUtil.getFloatData(player, IMMPlayerData.FloatData.BREAK_THROUGH_PROGRESS);
    }

    public static int getConsciousness(Entity entity){
//        final RealmType realm = RealmManager.getRank(entity);
//        if(entity instanceof Player player){
//            final int consciousness = PlayerUtil.getIntegerData(player, PlayerRangeIntegers.CONSCIOUSNESS);
//            return (realm.getBaseConsciousness() + consciousness);
//        }
//        return realm.getBaseConsciousness();
        return 0;
    }

    public static double getSpiritRange(Entity entity){
        return getConsciousness(entity) / 10D;
    }

    public static void addElixir(Player player, float amount){
        PlayerUtil.addExperience(player, ExperienceType.ELIXIR, amount);
    }

    public static void addSpell(Player player, float amount){
        PlayerUtil.addExperience(player, ExperienceType.SPELL, amount);
    }

    public static MutableComponent getExperienceComponent(){
        return TipUtil.misc("xp_type");
    }

    public static MutableComponent getExperienceComponent(ExperienceType type){
        return TipUtil.misc("xp_type." + type.name().toLowerCase());
    }

    public static MutableComponent getCultivation(){
        return TipUtil.misc("cultivation");
    }

}
