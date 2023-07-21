package hungteen.imm.common;

import hungteen.htlib.HTLib;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.enums.ExperienceTypes;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.api.interfaces.IHasRealm;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
    private static final float DEFAULT_KILL_XP = 1;
    private static final Map<EntityType<?>, Float> killXpMap = new HashMap<>();
    private static final RealmNode ROOT = new RealmNode(RealmTypes.MORTALITY);

    /**
     * {@link hungteen.imm.ImmortalMod#setUp(FMLCommonSetupEvent)}
     */
    public static void init(){
        fillXpMap();
        updateRealmTree();
    }

    private static void fillXpMap(){
        addKillXp(IMMEntities.EMPTY_CULTIVATOR.get(), 10);
    }

    private static void updateRealmTree() {
        addRealmLine(ROOT, Arrays.asList(
                RealmTypes.SPIRITUAL_LEVEL_1,
                RealmTypes.SPIRITUAL_LEVEL_2,
                RealmTypes.SPIRITUAL_LEVEL_3
        ));
    }

    public static void onPlayerKillLiving(ServerPlayer player, LivingEntity living){
        final int count = player.getStats().getValue(Stats.ENTITY_KILLED, living.getType());
        if(count < MAX_VALID_KILL_COUNT){
            final float xp = getKillXp(living.getType()) * (1 - count * 1F / MAX_VALID_KILL_COUNT);
            PlayerUtil.addExperience(player, ExperienceTypes.FIGHTING, xp);
        }
    }

    public static void addKillXp(EntityType<?> type, float value){
        killXpMap.put(type, value);
    }

    public static float getKillXp(EntityType<?> type){
        return killXpMap.getOrDefault(type, DEFAULT_KILL_XP);
    }

    /**
     * Get Realm Stage of the specific entity.
     */
    public static RealmStages getRealmStage(Entity entity){
        if(entity instanceof Player player){
            return PlayerUtil.getPlayerRealmStage(player);
        } else if(entity instanceof IHasRealm realmEntity){
            return realmEntity.getRealmStage();
        }
        return RealmStages.PRELIMINARY;
    }

    public static float getStageRequiredCultivation(IRealmType realm, RealmStages stage){
        final RealmNode node = findRealmNode(realm);
        final float previousValue = node.hasPreviousNode() ? node.getPreviousRealm().requireCultivation() : 0;
        return stage.getPercent() * (realm.requireCultivation() - previousValue);
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
