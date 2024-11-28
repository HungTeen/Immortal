package hungteen.imm.common.cultivation;

import hungteen.htlib.util.WeightedList;
import hungteen.htlib.util.helper.JavaHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.IMMConfigs;
import hungteen.imm.IMMInitializer;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.cultivation.*;
import hungteen.imm.api.interfaces.IArtifactBlock;
import hungteen.imm.api.interfaces.IArtifactItem;
import hungteen.imm.common.capability.player.CultivationData;
import hungteen.imm.common.capability.player.IMMPlayerData;
import hungteen.imm.common.cultivation.realm.RealmNode;
import hungteen.imm.common.effect.IMMEffects;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.common.tag.IMMItemTags;
import hungteen.imm.common.world.entity.trial.BreakThroughRaid;
import hungteen.imm.common.world.levelgen.spiritworld.SpiritWorldDimension;
import hungteen.imm.compat.minecraft.VanillaCultivationCompat;
import hungteen.imm.util.Constants;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-26 18:36
 **/
public class CultivationManager {

    public static final int MAX_VALID_KILL_COUNT = 5;
    public static final float DEFAULT_KILL_XP = 0.2F;

    static {
//        DAMAGE_REALM_MAP.putAll(Map.of(
//                IMMDamageTypeTags.IMM_REALM_LEVEL_1, RealmTypes.QI_REFINING,
//                IMMDamageTypeTags.IMM_REALM_LEVEL_2, RealmTypes.FOUNDATION,
//                IMMDamageTypeTags.IMM_REALM_LEVEL_3, RealmTypes.CORE_SHAPING
//        ));
    }

    /**
     * {@link IMMInitializer#setUp(FMLCommonSetupEvent)}
     */
    public static void init(){
        VanillaCultivationCompat.fillEntityMap();
        RealmNode.updateRealmTree();
    }

    /**
     * 玩家灵根的生成规则： <br>
     * 1. 首先依据概率选择是几个灵根（0 - 5）。 <br>
     * 2. 如果是1个灵根，那么依据权重在普通灵根和异灵根中选择一个。 <br>
     * 3. 否则依据权重在普通五行灵根中选择若干个。 <br>
     *
     * @return 随机生成的灵根。
     */
    public static List<QiRootType> getQiRoots(RandomSource random) {
        final double[] rootChances = {IMMConfigs.getNoRootChance(), IMMConfigs.getOneRootChance(), IMMConfigs.getTwoRootChance(), IMMConfigs.getThreeRootChance(), IMMConfigs.getFourRootChance()};
        return getQiRoots(random, rootChances);
    }

    /**
     * @param rootChances 每个灵根数量的概率，建议数组长度为 5。
     */
    public static List<QiRootType> getQiRoots(RandomSource random, double[] rootChances) {
        double chance = random.nextDouble();
        for (int i = 0; i < Math.max(Constants.MAX_ROOT_AMOUNT, rootChances.length); ++i) {
            if (chance < rootChances[i]) {
                return randomSpawnRoots(random, i);
            }
            chance -= rootChances[i];
        }
        return randomSpawnRoots(random, 5);
    }

    /**
     * 只有灵根数量为 1 时，才需要考虑变异灵根。
     * {@link #getQiRoots(RandomSource)}
     */
    private static List<QiRootType> randomSpawnRoots(RandomSource random, int rootCount) {
        final List<QiRootType> rootChosen = new ArrayList<>();
        if (rootCount == 1) {
            rootChosen.addAll(WeightedList.create(QiRootTypes.registry().getValues().stream().toList()).getRandomItems(random, 1, true));
        } else if (rootCount > 1) {
            rootChosen.addAll(WeightedList.create(QiRootTypes.registry().getValues().stream()
                    .filter(JavaHelper.not(QiRootType::isSpecialRoot))
                    .collect(Collectors.toList())
            ).getRandomItems(random, rootCount, true));
        }
        return rootChosen;
    }

    /**
     * 升级总共需要多少修为。
     */
    public static float getRequiredCultivation(Player player, RealmType realm){
        return realm.maxCultivation();
    }

    /**
     * 每一个 {@link ExperienceType} 需要多少修为。
     */
    public static float getEachCultivation(Player player, RealmType realm){
        return (float) realm.maxCultivation() / ExperienceType.values().length;
    }

    /**
     * Find the realm node with the same realm type.
     */
    public static RealmNode findRealmNode(RealmType realmType){
        return RealmNode.seekRealm(realmType);
    }

    public static void getRealm(CompoundTag tag, String name, Consumer<RealmType> consumer){
        if(tag.contains(name)){
            IMMAPI.get().realmRegistry().flatMap(l -> l.byNameCodec().parse(NbtOps.INSTANCE, tag.get(name))
                    .result()).ifPresent(consumer);
        }
    }

    /**
     * 尝试直接改变境界。
     *
     * @return 是否改变成功。
     */
    public static boolean checkAndSetRealm(Player player, RealmType realm, boolean force) {
        // 自身修为达到了此境界的要求。
        return Boolean.TRUE.equals(PlayerUtil.getData(player, m -> {
            CultivationData cultivationData = m.getCultivationData();
            if (EntityHelper.isServer(player)) {
                // 自身修为达到了此境界的要求。
                if (cultivationData.getCultivation() >= realm.maxCultivation()) {
                    cultivationData.setRealmType(realm);
                } else {
                    if (force) {
                        final float requiredXp = (float) realm.maxCultivation() / ExperienceType.values().length;
                        for (ExperienceType type : ExperienceType.values()) {
                            cultivationData.setExperience(type, requiredXp);
                        }
                        cultivationData.setRealmType(realm);
                    }
                    return force;
                }
            } else {
                cultivationData.setRealmType(realm);
            }
            return true;
        }));
    }

    public static void breakThrough(ServerPlayer player, RealmType realm, RealmStage stage){
//        final RealmType oldRealm = PlayerUtil.getPlayerRealm(player);
//        final RealmStage oldStage = PlayerUtil.getPlayerRealmStage(player);
//        if(oldRealm != realm){
//            PlayerUtil.checkAndSetRealm(player, realm, stage, false);
//        } else if(oldStage != stage){
//            PlayerUtil.checkAndSetRealmStage(player, stage);
//        }
        player.playSound(SoundEvents.PLAYER_LEVELUP);
        player.removeEffect(IMMEffects.BREAK_THROUGH.holder());
    }

    /**
     * 玩家点击修行界面的突破按钮，将玩家传送到对应的精神领域的位置。
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
////                    breakThrough(player, nextRealm, nextStage);
////                });
//            }
//        });
    }

//    public static void realmAttackGap(LivingHurtEvent event){
//        final LivingEntity target = event.getEntity();
//        final DamageSource source = event.getSource();
//        final double amount = event.getAmount();
//        double gapAmount = event.getAmount();
//        if(source.getEntity() != null){
//            final int gap = getRealmGap(target, source.getEntity());
//            if(gap > 0) {
//                // 伤害减免。
//                gapAmount = amount * Math.pow(0.1, gap);
//            } else {
//                gapAmount = amount * Math.pow(1.1, - gap);
//            }
//        } else {
//            final int gap = source.is(IMMDamageTypeTags.IGNORE_REALM) ? 0 : getRealmGap(getRealm(target), getDamageSourceRealm(source));
//            if(gap > 0){
//                gapAmount = amount * Math.pow(0.2, gap);
//            }
//        }
//        // 理论上伤害不能低于0.1。
//        event.setAmount((float) Math.max(Math.min(0.1F, amount), gapAmount));
//    }

    public static List<BreakThroughRaid> breakThroughRaids(Level level){
//        return HTRaidComponents.registry().getValues(level).stream().filter(BreakThroughRaid.class::isInstance).map(BreakThroughRaid.class::cast).toList();
        return List.of();
    }

    public static void checkAndAddBreakThroughProgress(Player player, float progress){
//        if(cultivationEnough(player)){
//            PlayerUtil.addFloatData(player, PlayerRangeFloats.BREAK_THROUGH_PROGRESS, progress);
//        } else {
//            PlayerHelper.sendTipTo(player, TipUtil.info("cultivation_not_enough").withStyle(ChatFormatting.DARK_RED));
//        }
    }

//    public static boolean cultivationEnough(Player player){
//        final Optional<RealmStatus> nextRealmStatus = getNextRealmStatus(player);
//        return nextRealmStatus.isPresent() && PlayerUtil.getCultivation(player) >= RealmManager.getStageRequiredCultivation(nextRealmStatus.get().realm(), nextRealmStatus.get().stage());
//    }

    public static float getTrialDifficulty(Player player, RealmType realm, RealmStage stage){
        final float karma = Mth.clamp(KarmaManager.calculateKarma(player) + (player.getRandom().nextFloat() < 0.5F ? 0 : player.getRandom().nextInt(10)), 0, KarmaManager.MAX_KARMA_VALUE);
//        return karma + (realm == RealmTypes.MORTALITY ? 0 : (stage.canLevelUp() ? 100 : stage == RealmStage.PRELIMINARY ? 0 : stage == RealmStage.MIDTERM ? 30 : 60));
        return karma;
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
//        final RealmType realm = RealmManager.getRealm(entity);
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

    /**
     * 有大境界差距并且左边的大。
     */
    public static boolean hasRealmGapAndLarger(Entity entity1, Entity entity2) {
        final RealmType realm1 = getRealm(entity1);
        final RealmType realm2 = getRealm(entity2);
        return hasRealmGap(realm1, realm2) && compare(realm1, realm2);
    }

    public static int getRealmGap(Entity entity1, Entity entity2) {
        return getRealmGap(getRealm(entity1), getRealm(entity2));
    }

    public static int getRealmGap(RealmType realm1, RealmType realm2) {
        return realm1.getRealmValue() / 100 - realm2.getRealmValue() / 100;
    }

    /**
     * 有大境界差距。
     */
    public static boolean hasRealmGap(RealmType realm1, RealmType realm2) {
        return getRealmGap(realm1, realm2) != 0;
    }

    public static boolean compare(RealmType realm1, RealmType realm2) {
        return realm1.getRealmValue() > realm2.getRealmValue();
    }

    public static RealmType getRealm(Entity entity) {
        if(entity instanceof ICultivatable realmEntity){
            return realmEntity.getRealm();
        } else if(entity instanceof LivingEntity){
            if(entity instanceof Player player){
                return PlayerUtil.getPlayerRealm(player);
            }
            return VanillaCultivationCompat.getDefaultRealm(entity.getType(), RealmTypes.MORTALITY);
        } else {
            return VanillaCultivationCompat.getDefaultRealm(entity.getType(), RealmTypes.NOT_IN_REALM);
        }
    }

    public static RealmType getRealm(ItemStack stack){
        if(stack.is(IMMItemTags.COMMON_ARTIFACTS)) {
            return RealmTypes.COMMON_ARTIFACT;
        } else if(stack.is(IMMItemTags.MODERATE_ARTIFACTS)) {
            return RealmTypes.MODERATE_ARTIFACT;
        } else if(stack.is(IMMItemTags.ADVANCED_ARTIFACTS)) {
            return RealmTypes.ADVANCED_ARTIFACT;
        } else if(stack.getItem() instanceof IArtifactItem artifactItem) {
            return artifactItem.getArtifactRealm(stack);
        } else if(stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof IArtifactBlock artifactBlock) {
            return artifactBlock.getArtifactRealm(stack);
        }
        return RealmTypes.NOT_IN_REALM;
    }

    public static RealmType getRealm(BlockState state){
        if(state.is(IMMBlockTags.COMMON_ARTIFACTS)) {
            return RealmTypes.COMMON_ARTIFACT;
        } else if(state.is(IMMBlockTags.MODERATE_ARTIFACTS)) {
            return RealmTypes.MODERATE_ARTIFACT;
        } else if(state.is(IMMBlockTags.ADVANCED_ARTIFACTS)) {
            return RealmTypes.ADVANCED_ARTIFACT;
        } else if(state.getBlock() instanceof IArtifactBlock artifactBlock) {
            return artifactBlock.getRealm(state);
        }
        return RealmTypes.NOT_IN_REALM;
    }

    public static boolean notCommon(RealmType type) {
        return type != RealmTypes.NOT_IN_REALM && type != RealmTypes.MORTALITY;
    }

    public static CultivationType getCultivationType(Entity entity) {
        return getRealm(entity).getCultivationType();
    }

    /**
     * 是否可能有灵根，不可能有灵根就不显示。
     */
    public static boolean mayHaveRoots(Entity entity){
        return entity instanceof LivingEntity;
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
