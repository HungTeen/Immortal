package hungteen.imm.common.cultivation;

import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.artifact.ArtifactBlock;
import hungteen.imm.api.artifact.ArtifactItem;
import hungteen.imm.api.artifact.ArtifactRank;
import hungteen.imm.api.cultivation.ExperienceType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.entity.Cultivatable;
import hungteen.imm.common.IMMConfigs;
import hungteen.imm.common.capability.player.CultivationData;
import hungteen.imm.common.cultivation.realm.RealmNode;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.common.tag.IMMDamageTypeTags;
import hungteen.imm.common.tag.IMMItemTags;
import hungteen.imm.compat.minecraft.VanillaCultivationCompat;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.Optional;
import java.util.function.Consumer;

import static hungteen.imm.compat.minecraft.VanillaCultivationCompat.getDamageSourceRealm;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/30 15:13
 **/
public class RealmManager {

    static {
//        DAMAGE_REALM_MAP.putAll(Map.of(
//                IMMDamageTypeTags.IMM_REALM_LEVEL_1, RealmTypes.QI_REFINING,
//                IMMDamageTypeTags.IMM_REALM_LEVEL_2, RealmTypes.FOUNDATION,
//                IMMDamageTypeTags.IMM_REALM_LEVEL_3, RealmTypes.CORE_SHAPING
//        ));
    }

    /**
     * Find the realm node with the same realm type.
     */
    public static RealmNode findRealmNode(RealmType realmType){
        return RealmNode.seekRealm(realmType);
    }

    public static Optional<RealmType> getNextRealm(RealmType realmType, boolean hasThreshold){
        RealmNode realmNode = findRealmNode(realmType);
        return realmNode.next().stream()
                .filter(node -> node.getRealm().hasThreshold() == hasThreshold)
                .map(RealmNode::getRealm)
                .findAny();
    }

    public static void getRealm(CompoundTag tag, String name, Consumer<RealmType> consumer){
        if(tag.contains(name)){
            IMMAPI.get().realmRegistry().flatMap(l -> l.byNameCodec().parse(NbtOps.INSTANCE, tag.get(name))
                    .result()).ifPresent(consumer);
        }
    }

    public static void updateRealm(LivingEntity living, RealmType newRealm){
        if(living instanceof Player player){
            PlayerUtil.setData(player, data -> data.getCultivationData().setRealmType(newRealm));
        }
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
        final double amount = event.getOriginalDamage();
        double gapAmount = amount;
        int gap = 0;
        if (source.getEntity() != null) {
            // 伤害来源的境界差距。
            gap = getRealmGap(self, source.getEntity());
        } else {
            // 伤害来源是环境。
            gap = source.is(IMMDamageTypeTags.IGNORE_REALM) ? 0 : getRealmGap(getRealm(self), getDamageSourceRealm(source));
        }
        if (gap > 0) {
            // 伤害减免。
            gapAmount = amount * Math.pow(1 - IMMConfigs.realmSettings().realmReceiveDamageReduction.getAsDouble(), gap);
        } else if(gap < 0){
            gapAmount = amount * Math.pow(1 + IMMConfigs.realmSettings().realmDealDamageIncrease.getAsDouble(), -gap);
        }
        // 理论上伤害不能低于0.1。
        event.setNewDamage((float) Math.max(Math.min(0.1F, amount), gapAmount));
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

    /**
     * @return 正数表示左边境界大。
     */
    public static int getRealmGap(RealmType realm1, RealmType realm2) {
        return realm1.getRealmValue() / 100 - realm2.getRealmValue() / 100;
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
            return VanillaCultivationCompat.getDefaultRealm(entity.getType(), RealmTypes.MORTALITY);
        } else {
            return VanillaCultivationCompat.getDefaultRealm(entity.getType(), RealmTypes.NOT_IN_REALM);
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
