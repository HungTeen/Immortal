package hungteen.imm.common;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.IMMInitializer;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.enums.ExperienceTypes;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.api.interfaces.IArtifactBlock;
import hungteen.imm.api.interfaces.IArtifactItem;
import hungteen.imm.api.interfaces.IHasRealm;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.capability.player.PlayerDataManager;
import hungteen.imm.common.effect.IMMEffects;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.impl.registry.CultivationTypes;
import hungteen.imm.common.impl.registry.PlayerRangeFloats;
import hungteen.imm.common.impl.registry.PlayerRangeIntegers;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.common.tag.IMMDamageTypeTags;
import hungteen.imm.common.tag.IMMItemTags;
import hungteen.imm.common.world.entity.trial.BreakThroughRaid;
import hungteen.imm.util.ItemUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-26 18:36
 **/
public class RealmManager {

    public static final int MAX_VALID_KILL_COUNT = 5;
    private static final float DEFAULT_KILL_XP = 0.2F;
    private static final Map<EntityType<?>, IRealmType> DEFAULT_REALM_MAP = new HashMap<>();
    private static final Map<EntityType<?>, Float> KILL_XP_MAP = new HashMap<>();
    private static final Map<TagKey<DamageType>, IRealmType> DAMAGE_REALM_MAP = new HashMap<>();
    private static final RealmNode ROOT = new RealmNode(RealmTypes.MORTALITY);

    static {
        DAMAGE_REALM_MAP.putAll(Map.of(
                IMMDamageTypeTags.IMM_REALM_LEVEL_1, RealmTypes.SPIRITUAL_LEVEL_1,
                IMMDamageTypeTags.IMM_REALM_LEVEL_2, RealmTypes.SPIRITUAL_LEVEL_2,
                IMMDamageTypeTags.IMM_REALM_LEVEL_3, RealmTypes.SPIRITUAL_LEVEL_3
        ));
    }

    /**
     * {@link IMMInitializer#setUp(FMLCommonSetupEvent)}
     */
    public static void init(){
        fillEntityMap();
        updateRealmTree();
    }

    private static void fillEntityMap(){
        addKillXp(IMMEntities.EMPTY_CULTIVATOR.get(), 1);

        put(EntityType.ENDERMAN, RealmTypes.SPIRITUAL_LEVEL_1, 0.4F);
        put(EntityType.PIGLIN, RealmTypes.MONSTER_LEVEL_1, 0.5F);
        put(EntityType.BLAZE, RealmTypes.MONSTER_LEVEL_1, 0.5F);
        put(EntityType.HOGLIN, RealmTypes.MONSTER_LEVEL_2, 0.7F);
        put(EntityType.RAVAGER, RealmTypes.MONSTER_LEVEL_3, 1F);
    }

    private static void updateRealmTree() {
        addRealmLine(ROOT, Arrays.asList(
                RealmTypes.SPIRITUAL_LEVEL_1,
                RealmTypes.SPIRITUAL_LEVEL_2,
                RealmTypes.SPIRITUAL_LEVEL_3
        ));
    }

    public static Optional<RealmStatus> getNextRealmStatus(Player player){
        return getNextRealmStatus(PlayerUtil.getPlayerRealm(player), PlayerUtil.getPlayerRealmStage(player));
    }

    /**
     * 获取当前境界的下一个小境界。
     * @return 如果是empty，说明没有下一个境界了。
     */
    public static Optional<RealmStatus> getNextRealmStatus(IRealmType currentRealm, RealmStages currentStage){
        if(currentRealm == RealmTypes.MORTALITY || currentStage.canLevelUp()){
            // 可以突破到下一个大境界。 TODO 其他修为类型的突破。
            final RealmNode currentNode = findRealmNode(currentRealm);
            final RealmNode nextNode = currentNode.next(CultivationTypes.SPIRITUAL);
            if(nextNode != null){
                return Optional.of(status(nextNode.getRealm(), RealmStages.PRELIMINARY));
            }
            return Optional.empty();
        } else {
            // 只能突破到当前境界的下一个阶段。
            return Optional.of(status(currentRealm, RealmStages.next(currentStage)));
        }
    }

    public static void breakThrough(ServerPlayer player, IRealmType realm, RealmStages stage){
        final IRealmType oldRealm = PlayerUtil.getPlayerRealm(player);
        final RealmStages oldStage = PlayerUtil.getPlayerRealmStage(player);
        if(oldRealm != realm){
            PlayerUtil.checkAndSetRealm(player, realm, stage, false);
        } else if(oldStage != stage){
            PlayerUtil.checkAndSetRealmStage(player, stage);
        }
        player.playSound(SoundEvents.PLAYER_LEVELUP);
        player.removeEffect(IMMEffects.BREAK_THROUGH.holder());
    }

    public static void tryBreakThrough(ServerPlayer player){
        final IRealmType currentRealm = PlayerUtil.getPlayerRealm(player);
        final RealmStages currentStage = PlayerUtil.getPlayerRealmStage(player);
        getNextRealmStatus(currentRealm, currentStage).ifPresent(status -> {
            final IRealmType nextRealm = status.realm();
            final RealmStages nextStage = status.stage();
            if(player.level() instanceof ServerLevel serverLevel){
                final float difficulty = getTrialDifficulty(player, currentRealm, currentStage);
//                WeightedList.create(breakThroughRaids(player.level()).stream().filter(raid -> {
//                    return raid.match(nextRealm, nextStage, difficulty);
//                }).toList()).getRandomItem(player.getRandom()).ifPresentOrElse(raid -> {
////                    DummyEntityManager.addEntity(serverLevel, new BreakThroughTrial(serverLevel, player, difficulty, raid));
//                    PlayerUtil.addIntegerData(player, PlayerRangeIntegers.BREAK_THROUGH_TRIES, 1);
//                }, () -> {
//                    breakThrough(player, nextRealm, nextStage);
//                });
            }
        });
    }

    /**
     * 不能使用附魔的修行者，强制弹出附魔物品。
     */
    public static void limitEnchantments(LivingEntity living){
        if((living.tickCount & 1) == 0 && ! RealmManager.getCultivationType(living).canEnchant()){
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                final ItemStack slotItem = living.getItemBySlot(slot);
                if(ItemUtil.hasEnchantment(slotItem)){
                    final ItemStack copyItem = slotItem.copy();
                    living.setItemSlot(slot, ItemStack.EMPTY);
                    ItemEntity itemEntity = living.spawnAtLocation(copyItem);
                    if(itemEntity != null){
                        itemEntity.setPickUpDelay(50);
                    }
                    if(living instanceof Player player){
                        PlayerHelper.sendTipTo(player, TipUtil.info("spiritual_conflict_with_enchant").withStyle(ChatFormatting.RED));
                    }
                }
            }
        }
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
        if(cultivationEnough(player)){
            PlayerUtil.addFloatData(player, PlayerRangeFloats.BREAK_THROUGH_PROGRESS, progress);
        } else {
            PlayerHelper.sendTipTo(player, TipUtil.info("cultivation_not_enough").withStyle(ChatFormatting.DARK_RED));
        }
    }

    public static boolean cultivationEnough(Player player){
        final Optional<RealmStatus> nextRealmStatus = getNextRealmStatus(player);
        return nextRealmStatus.isPresent() && PlayerUtil.getCultivation(player) >= RealmManager.getStageRequiredCultivation(nextRealmStatus.get().realm(), nextRealmStatus.get().stage());
    }

    public static float getTrialDifficulty(Player player, IRealmType realm, RealmStages stage){
        final float karma = Mth.clamp(KarmaManager.calculateKarma(player) + (player.getRandom().nextFloat() < 0.5F ? 0 : player.getRandom().nextInt(10)), 0, KarmaManager.MAX_KARMA_VALUE);
        return karma + (realm == RealmTypes.MORTALITY ? 0 : (stage.canLevelUp() ? 100 : stage == RealmStages.PRELIMINARY ? 0 : stage == RealmStages.MIDTERM ? 30 : 60));
    }

    public static void onPlayerKillLiving(ServerPlayer player, LivingEntity living){
        final int count = player.getStats().getValue(Stats.ENTITY_KILLED, living.getType());
        if(count < MAX_VALID_KILL_COUNT){
            final float xp = getKillXp(living.getType()) * (1 - count * 1F / MAX_VALID_KILL_COUNT);
            PlayerUtil.addExperience(player, ExperienceTypes.FIGHTING, xp);
        }
    }

    public static void put(EntityType<?> type, IRealmType realm, float xp){
        addDefaultRealm(type, realm);
        addKillXp(type, xp);
    }

    public static void addDefaultRealm(EntityType<?> type, IRealmType realm){
        DEFAULT_REALM_MAP.put(type, realm);
    }

    public static IRealmType getDefaultRealm(EntityType<?> type, IRealmType defaultRealm){
        return DEFAULT_REALM_MAP.getOrDefault(type, defaultRealm);
    }

    public static IRealmType getDamageSourceRealm(DamageSource source){
        for (Map.Entry<TagKey<DamageType>, IRealmType> entry : DAMAGE_REALM_MAP.entrySet()) {
            if(source.is(entry.getKey())){
                return entry.getValue();
            }
        }
        return RealmTypes.NOT_IN_REALM;
    }

    public static void addKillXp(EntityType<?> type, float value){
        KILL_XP_MAP.put(type, value);
    }

    public static float getKillXp(EntityType<?> type){
        return KILL_XP_MAP.getOrDefault(type, DEFAULT_KILL_XP);
    }

    public static boolean canBreakThrough(Player player){
        return PlayerUtil.getFloatData(player, PlayerRangeFloats.BREAK_THROUGH_PROGRESS) >= 1;
    }

    public static float getBreakThroughProgress(Player player){
        return PlayerUtil.getFloatData(player, PlayerRangeFloats.BREAK_THROUGH_PROGRESS);
    }

    public static int getConsciousness(Entity entity){
        final IRealmType realm = RealmManager.getRealm(entity);
        if(entity instanceof Player player){
            final int consciousness = PlayerUtil.getIntegerData(player, PlayerRangeIntegers.CONSCIOUSNESS);
            return (realm.getBaseConsciousness() + consciousness);
        }
        return realm.getBaseConsciousness();
    }

    public static double getSpiritRange(Entity entity){
        return getConsciousness(entity) / 10D;
    }

    /**
     * Get Realm Stage of the specific entity.
     */
    public static Optional<RealmStages> getRealmStage(Entity entity){
        if(entity instanceof Player player){
            return Optional.ofNullable(PlayerUtil.getPlayerRealmStage(player));
        } else if(entity instanceof IHasRealm realmEntity){
            return realmEntity.getRealmStageOpt();
        }
        return Optional.empty();
    }

    /**
     * 有大境界差距并且左边的大。
     */
    public static boolean hasRealmGapAndLarger(Entity entity1, Entity entity2) {
        final IRealmType realm1 = getRealm(entity1);
        final IRealmType realm2 = getRealm(entity2);
        return hasRealmGap(realm1, realm2) && compare(realm1, realm2);
    }

    public static int getRealmGap(Entity entity1, Entity entity2) {
        return getRealmGap(getRealm(entity1), getRealm(entity2));
    }

    public static int getRealmGap(IRealmType realm1, IRealmType realm2) {
        return realm1.getRealmValue() / 100 - realm2.getRealmValue() / 100;
    }

    /**
     * 有大境界差距。
     */
    public static boolean hasRealmGap(IRealmType realm1, IRealmType realm2) {
        return getRealmGap(realm1, realm2) != 0;
    }

    public static boolean compare(IRealmType realm1, IRealmType realm2) {
        return realm1.getRealmValue() > realm2.getRealmValue();
    }

    public static IRealmType getRealm(Entity entity) {
        if(entity instanceof IHasRealm realmEntity){
            return realmEntity.getRealm();
        } else if(entity instanceof LivingEntity){
            if(entity instanceof Player player){
                return PlayerUtil.getManagerResult(player, PlayerDataManager::getRealmType, RealmTypes.MORTALITY);
            }
            return getDefaultRealm(entity.getType(), RealmTypes.MORTALITY);
        } else {
            return getDefaultRealm(entity.getType(), RealmTypes.NOT_IN_REALM);
        }
    }

    public static IRealmType getRealm(ItemStack stack){
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

    public static IRealmType getRealm(BlockState state){
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

    public static boolean notCommon(IRealmType type) {
        return type != RealmTypes.NOT_IN_REALM && type != RealmTypes.MORTALITY;
    }

    public static ICultivationType getCultivationType(Entity entity) {
        return getRealm(entity).getCultivationType();
    }

    public static float getStageRequiredCultivation(IRealmType realm, RealmStages stage){
        final RealmNode node = findRealmNode(realm);
        final float previousValue = node.hasPreviousNode() ? node.getPreviousRealm().maxCultivation() : 0;
        return stage.getPercent() * (realm.maxCultivation() - previousValue) + previousValue;
    }

    public static float getEachCultivation(IRealmType realm){
        return (float) realm.maxCultivation() / ExperienceTypes.values().length;
    }

    /**
     * Find the realm node with the same realm type.
     */
    public static RealmNode findRealmNode(IRealmType realmType){
        return Objects.requireNonNullElse(seekRealm(ROOT, realmType), ROOT);
    }

    public static void getRealm(CompoundTag tag, String name, Consumer<IRealmType> consumer){
        if(tag.contains(name)){
            IMMAPI.get().realmRegistry().flatMap(l -> l.byNameCodec().parse(NbtOps.INSTANCE, tag.get(name))
                    .result()).ifPresent(consumer);
        }
    }

    /**
     * Add a hierarchy list.
     */
    public static void addRealmLine(RealmNode root, List<IRealmType> realmTypes){
        RealmNode tmp = root;
        for(int i = 0; i < realmTypes.size(); ++ i){
            RealmNode now = new RealmNode(realmTypes.get(i), tmp);
            tmp.add(now);
            tmp = now;
        }
    }

    @Nullable
    private static RealmNode seekRealm(RealmNode root, IRealmType type){
        if(root == null) {
            return null;
        } else if(root.realm == type) {
            return root;
        }
        for (RealmNode nextRealm : root.nextRealms) {
            final RealmNode node = seekRealm(nextRealm, type);
            if(node != null) {
                return node;
            }
        }
        return null;
    }

    public static MutableComponent getRealmInfo(IRealmType realm, @Nullable RealmStages stage){
        if(realm == RealmTypes.NOT_IN_REALM || realm == RealmTypes.MORTALITY || stage == null) {
            return realm.getComponent();
        }
        return realm.getComponent().append("-").append(getStageComponent(stage));
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

    public static MutableComponent getExperienceComponent(ExperienceTypes type){
        return TipUtil.misc("xp_type." + type.name().toLowerCase());
    }

    public static MutableComponent getStageComponent(){
        return TipUtil.misc("realm_stage");
    }

    public static MutableComponent getStageComponent(RealmStages stage){
        return TipUtil.misc("realm_stage." + stage.name().toLowerCase());
    }

    public static MutableComponent getCultivation(){
        return TipUtil.misc("cultivation");
    }

    public static RealmStatus status(IRealmType realm, RealmStages stage){
        return new RealmStatus(realm, stage);
    }

    /**
     * A tree node.
     */
    public static class RealmNode {

        private final IRealmType realm;
        private RealmNode prevRealm;

        private final HashSet<RealmNode> nextRealms = new HashSet<>();

        private RealmNode(@NotNull IRealmType realm){
            this(realm, null);
        }

        private RealmNode(@NotNull IRealmType realm, @Nullable RealmNode prevRealm) {
            this.realm = realm;
            this.prevRealm = prevRealm;
        }

        @Nullable
        public RealmNode next(){
            return next(realm.getCultivationType());
        }

        @Nullable
        public RealmNode next(ICultivationType type){
            return nextRealms.stream().filter(l -> l.realm.getCultivationType() == type).findAny().orElse(null);
        }

        public void add(RealmNode node){
            if(nextRealms.stream().anyMatch(l -> l.realm == node.realm)){
                IMMAPI.logger().warn("Duplicate realm node : {}", node.realm.getRegistryName());
            } else {
                nextRealms.add(node);
            }
        }

        public boolean hasPreviousNode(){
            return this.prevRealm != null;
        }

        public IRealmType getRealm(){
            return this.realm;
        }

        public IRealmType getPreviousRealm(){
            return this.prevRealm.realm;
        }

    }

    public record RealmStatus(IRealmType realm, RealmStages stage){

    }

}
