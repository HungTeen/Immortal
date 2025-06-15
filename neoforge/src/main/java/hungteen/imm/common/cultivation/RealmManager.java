package hungteen.imm.common.cultivation;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.artifact.ArtifactBlock;
import hungteen.imm.api.artifact.ArtifactItem;
import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.api.cultivation.CultivationType;
import hungteen.imm.api.cultivation.ExperienceType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.entity.Cultivatable;
import hungteen.imm.common.IMMConfigs;
import hungteen.imm.common.capability.player.CultivationData;
import hungteen.imm.common.cultivation.realm.RealmNode;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.common.tag.IMMItemTags;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/30 15:13
 **/
public class RealmManager {

    private static final Map<EntityType<?>, RealmType> DEFAULT_REALM_MAP = new HashMap<>();
    private static final Map<TagKey<DamageType>, RealmType> DAMAGE_REALM_MAP = new HashMap<>();

    static {
//        DAMAGE_REALM_MAP.putAll(Map.of(
//                IMMDamageTypeTags.IMM_REALM_LEVEL_1, RealmTypes.QI_REFINING,
//                IMMDamageTypeTags.IMM_REALM_LEVEL_2, RealmTypes.FOUNDATION,
//                IMMDamageTypeTags.IMM_REALM_LEVEL_3, RealmTypes.CORE_SHAPING
//        ));
    }

    public static Optional<RealmType> getNextRealm(RealmType realmType, CultivationType cultivationType){
        Optional<RealmNode> nodeOpt = RealmNode.getNodeOpt(realmType);
        if(nodeOpt.isPresent()){
            Optional<RealmNode> nextNodeOpt = nodeOpt.get().next(cultivationType);
            if(nextNodeOpt.isPresent()){
                return Optional.of(nextNodeOpt.get().getRealm());
            }
        }
        return Optional.empty();
    }

    public static void getRealm(CompoundTag tag, String name, Consumer<RealmType> consumer){
        if(tag.contains(name)){
            IMMAPI.get().realmRegistry().flatMap(l -> l.byNameCodec().parse(NbtOps.INSTANCE, tag.get(name))
                    .result()).ifPresent(consumer);
        }
    }

    /**
     * 获得境界上的提升。
     */
    public static void realmLevelUp(LivingEntity living, RealmType newRealm){
        if(living instanceof Player player){
            PlayerUtil.setData(player, data -> data.getCultivationData().setRealmType(newRealm));
        }
        EntityUtil.playSound(living.level(), living, SoundEvents.PLAYER_LEVELUP);
    }

    /**
     * 尝试直接改变境界。
     *
     * @return 是否改变成功。
     */
    public static boolean checkAndSetRealm(Player player, RealmType realm, boolean force) {
        return PlayerUtil.getData(player, m -> {
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
        });
    }

    /**
     * 对于境界伤害的处理。<br>
     * 每跨一个大境界，伤害减免 50%，伤害增加 10%。
     */
    public static void realmAttackGap(LivingDamageEvent.Pre event) {
        final LivingEntity self = event.getEntity();
        final DamageSource source = event.getSource();
        final double amount = event.getNewDamage();
        double gapAmount = amount;
        int realmGap = 0;
        if (source.getEntity() != null) {
            // 伤害来源的境界差距。
            realmGap = getRealmGap(self, source.getEntity());
        } else {
            // TODO 伤害来源是环境。
//            gap = source.is(IMMDamageTypeTags.IGNORE_REALM) ? 0 : getRealmGap(getRealm(self), getDamageSourceRealm(source));
        }
        if(realmGap == 0){
            // 计算小境界差距。
            if (source.getEntity() != null) {
                float stageGap = getStageGap(self, source.getEntity());
                double multiplier = 1 + (realmAttackMultiplier(stageGap > 0 ? 1 : -1) - 1) * Math.abs(stageGap);
                gapAmount = amount * multiplier;
            }
        } else {
            gapAmount = amount * realmAttackMultiplier(realmGap);
        }
        // 理论上伤害不能低于0.1。
        event.setNewDamage((float) Math.max(Math.min(0.1F, amount), gapAmount));
    }

    public static double realmAttackMultiplier(int gap){
        if(gap > 0){
            return Math.pow(1 - IMMConfigs.realmSetting().realmReceiveDamageReduction.getAsDouble(), gap);
        } else if(gap < 0){
            return Math.pow(1 + IMMConfigs.realmSetting().realmDealDamageIncrease.getAsDouble(), -gap);
        }
        return 1.0;
    }

    /**
     * 更新生物的境界属性。
     */
    public static void updateRealmAttributes(LivingEntity living, RealmType oldRealm, RealmType newRealm) {
        // 移除旧的属性。
        oldRealm.getAttributeModifiers().forEach((holder, modifier) -> {
            AttributeInstance instance = living.getAttribute(holder);
            if (instance != null) {
                instance.removeModifier(modifier);
            }
        });
        // 添加新的属性。
        newRealm.getAttributeModifiers().forEach((holder, modifier) -> {
            AttributeInstance instance = living.getAttribute(holder);
            if (instance != null) {
                instance.addOrReplacePermanentModifier(modifier);
            }
        });
    }

    public static void addDefaultRealm(EntityType<?> type, RealmType realm){
        DEFAULT_REALM_MAP.put(type, realm);
    }

    public static RealmType getDefaultRealm(EntityType<?> type, RealmType defaultRealm){
        return DEFAULT_REALM_MAP.getOrDefault(type, defaultRealm);
    }

    public static RealmType getDamageSourceRealm(DamageSource source){
        for (Map.Entry<TagKey<DamageType>, RealmType> entry : DAMAGE_REALM_MAP.entrySet()) {
            if(source.is(entry.getKey())){
                return entry.getValue();
            }
        }
        return RealmTypes.NOT_IN_REALM;
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

    public static float getStageGap(Entity entity1, Entity entity2) {
        return getStageGap(getRealm(entity1), getRealm(entity2));
    }

    /**
     * 计算大境界差距。
     * @return 正数表示左边境界大。
     */
    public static int getRealmGap(RealmType realm1, RealmType realm2) {
        return realm1.getRealmValue() / 100 - realm2.getRealmValue() / 100;
    }

    public static float getStageGap(RealmType realm1, RealmType realm2) {
        if(hasRealmGap(realm1, realm2)){
            return getRealmGap(realm1, realm2);
        }
        return (realm1.getRealmValue() - realm2.getRealmValue()) * 1F / 100;
    }

    /**
     * 有大境界差距。
     */
    public static boolean hasRealmGap(RealmType realm1, RealmType realm2) {
        return getRealmGap(realm1, realm2) != 0;
    }

    /**
     * @return 左边的境界大返回 true。
     */
    public static boolean compare(RealmType realm1, RealmType realm2) {
        return realm1.getRealmValue() > realm2.getRealmValue();
    }

    public static RealmType getRealm(Entity entity) {
        if (entity instanceof Cultivatable realmEntity) {
            return realmEntity.getRealm();
        } else if (entity instanceof LivingEntity) {
            if (entity instanceof Player player) {
                return PlayerUtil.getPlayerRealm(player);
            }
            return getDefaultRealm(entity.getType(), RealmTypes.MORTALITY);
        } else {
            return getDefaultRealm(entity.getType(), RealmTypes.NOT_IN_REALM);
        }
    }

    public static ArtifactRank getRank(ItemStack stack) {
        if (stack.is(IMMItemTags.COMMON_ARTIFACTS)) {
            return ArtifactRank.COMMON;
        } else if (stack.is(IMMItemTags.MODERATE_ARTIFACTS)) {
            return ArtifactRank.MODERATE;
        } else if (stack.is(IMMItemTags.ADVANCED_ARTIFACTS)) {
            return ArtifactRank.ADVANCED;
        } else if (stack.getItem() instanceof ArtifactItem artifactItem) {
            return artifactItem.getArtifactRealm(stack);
        }
        return ArtifactRank.UNKNOWN;
    }

    public static ArtifactRank getRank(BlockState state) {
        if (state.is(IMMBlockTags.COMMON_ARTIFACTS)) {
            return ArtifactRank.COMMON;
        } else if (state.is(IMMBlockTags.MODERATE_ARTIFACTS)) {
            return ArtifactRank.MODERATE;
        } else if (state.is(IMMBlockTags.ADVANCED_ARTIFACTS)) {
            return ArtifactRank.ADVANCED;
        } else if (state.getBlock() instanceof ArtifactBlock artifactBlock) {
            return artifactBlock.getRealm(state);
        }
        return ArtifactRank.UNKNOWN;
    }

    public static boolean notCommon(ArtifactRank type) {
        return type != ArtifactRank.UNKNOWN;
    }
}
