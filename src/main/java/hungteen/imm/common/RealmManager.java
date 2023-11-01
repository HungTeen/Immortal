package hungteen.imm.common;

import hungteen.htlib.HTLib;
import hungteen.htlib.common.impl.raid.HTRaidComponents;
import hungteen.htlib.common.world.entity.DummyEntityManager;
import hungteen.htlib.util.WeightedList;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.enums.ExperienceTypes;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.api.interfaces.IHasRealm;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.capability.player.PlayerDataManager;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.impl.registry.CultivationTypes;
import hungteen.imm.common.impl.registry.PlayerRangeFloats;
import hungteen.imm.common.impl.registry.PlayerRangeIntegers;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.world.entity.trial.BreakThroughRaid;
import hungteen.imm.common.world.entity.trial.BreakThroughTrial;
import hungteen.imm.util.ItemUtil;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

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
    private static final RealmNode ROOT = new RealmNode(RealmTypes.MORTALITY);

    /**
     * {@link hungteen.imm.ImmortalMod#setUp(FMLCommonSetupEvent)}
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

    public static void breakThrough(ServerPlayer player, IRealmType realm, RealmStages stage){
        final IRealmType oldRealm = PlayerUtil.getPlayerRealm(player);
        final RealmStages oldStage = PlayerUtil.getPlayerRealmStage(player);
        if(oldRealm != realm){
            PlayerUtil.checkAndSetRealm(player, realm, stage, false);
        } else if(oldStage != stage){
            PlayerUtil.checkAndSetRealmStage(player, stage);
        }
    }

    public static void tryBreakThrough(ServerPlayer player){
        final IRealmType currentRealm = PlayerUtil.getPlayerRealm(player);
        final RealmStages currentStage = PlayerUtil.getPlayerRealmStage(player);
        final IRealmType nextRealm;
        final RealmStages nextStage;
        if(currentRealm == RealmTypes.MORTALITY || currentStage.canLevelUp()){
            final RealmNode currentNode = findRealmNode(currentRealm);
            final RealmNode nextNode = currentNode.next(CultivationTypes.SPIRITUAL);
            if(nextNode != null){
                nextRealm = nextNode.getRealm();
                nextStage = RealmStages.PRELIMINARY;
            } else {
                nextStage = currentStage;
                nextRealm = currentRealm;
                return;
            }
        } else {
            nextRealm = currentRealm;
            nextStage = RealmStages.next(currentStage);
        }
        if(player.level() instanceof ServerLevel serverLevel){
            final float difficulty = getTrialDifficulty(player, currentRealm, currentStage);
            WeightedList.create(breakThroughRaids(player.level()).stream().filter(raid -> {
                return raid.match(nextRealm, nextStage, difficulty);
            }).toList()).getRandomItem(player.getRandom()).ifPresentOrElse(raid -> {
                DummyEntityManager.addEntity(serverLevel, new BreakThroughTrial(serverLevel, player, difficulty, raid));
                PlayerUtil.addIntegerData(player, PlayerRangeIntegers.BREAK_THROUGH_TRIES, 1);
            }, () -> {
                breakThrough(player, nextRealm, nextStage);
            });
        }
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

    public static List<BreakThroughRaid> breakThroughRaids(Level level){
        return HTRaidComponents.registry().getValues(level).stream().filter(BreakThroughRaid.class::isInstance).map(BreakThroughRaid.class::cast).toList();
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

    public static IRealmType getDefaultRealm(EntityType<?> type){
        return DEFAULT_REALM_MAP.getOrDefault(type, RealmTypes.MORTALITY);
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
        final IRealmType realm = RealmManager.getEntityRealm(entity);
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
        final IRealmType realm1 = getEntityRealm(entity1);
        final IRealmType realm2 = getEntityRealm(entity2);
        return hasRealmGap(realm1, realm2) && compare(realm1, realm2);
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

    public static IRealmType getEntityRealm(Entity entity) {
        if(entity instanceof Player player){
            return PlayerUtil.getManagerResult(player, PlayerDataManager::getRealmType, RealmTypes.MORTALITY);
        }
        return entity instanceof IHasRealm ? ((IHasRealm) entity).getRealm() : getDefaultRealm(entity.getType());
    }

    public static ICultivationType getCultivationType(Entity entity) {
        return getEntityRealm(entity).getCultivationType();
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
        if(root == null) return null;
        if(root.realm == type) return root;
        for (RealmNode nextRealm : root.nextRealms) {
            final RealmNode node = seekRealm(nextRealm, type);
            if(node != null) return node;
        }
        return null;
    }

    public static MutableComponent getRealmInfo(IRealmType realm, @Nullable RealmStages stage){
        if(realm == RealmTypes.NOT_IN_REALM || realm == RealmTypes.MORTALITY || stage == null) return realm.getComponent();
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
                HTLib.getLogger().warn("Duplicate realm node : " + node.realm.getRegistryName());
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

}
