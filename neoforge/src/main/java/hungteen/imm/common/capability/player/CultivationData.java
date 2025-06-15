package hungteen.imm.common.capability.player;

import hungteen.htlib.util.helper.CodecHelper;
import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.imm.api.cultivation.*;
import hungteen.imm.api.event.EntityRealmEvent;
import hungteen.imm.common.advancement.trigger.PlayerRealmChangeTrigger;
import hungteen.imm.common.capability.HTPlayerData;
import hungteen.imm.common.cultivation.*;
import hungteen.imm.common.cultivation.realm.RealmNode;
import hungteen.imm.common.network.client.ExperienceChangePacket;
import hungteen.imm.common.network.client.QiRootAndRealmPacket;
import hungteen.imm.common.world.IMMGameRules;
import hungteen.imm.util.EventUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.UnknownNullability;

import java.util.*;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/26 16:32
 **/
public class CultivationData implements HTPlayerData {

    private final IMMPlayerData playerData;
    private final HashSet<QiRootType> roots = new HashSet<>();
    private final EnumMap<ExperienceType, Float> experienceMap = new EnumMap<>(ExperienceType.class);
    private RealmType realmType = RealmTypes.MORTALITY;
    private CultivationType cultivationType = CultivationTypes.NONE;
    private RealmNode realmNodeCache;

    public CultivationData(IMMPlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        {
            CompoundTag nbt = new CompoundTag();
            roots.forEach(type -> {
                nbt.putBoolean(type.getRegistryName(), true);
            });
            tag.put("SpiritualRoots", nbt);
        }
        {
            CompoundTag nbt = new CompoundTag();
            this.experienceMap.forEach((type, value) -> {
                nbt.putFloat(type.toString(), value);
            });
            tag.put("ExperienceMap", nbt);
        }
        CodecHelper.encodeNbt(RealmTypes.registry().byNameCodec(), this.realmType)
                .resultOrPartial()
                .ifPresent(nbt -> tag.put("PlayerRealmType", nbt));
        CodecHelper.encodeNbt(CultivationTypes.registry().byNameCodec(), this.cultivationType)
                .resultOrPartial()
                .ifPresent(nbt -> tag.put("NextCultivationType", nbt));
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt.contains("SpiritualRoots")) {
            CompoundTag spiritualRoots = nbt.getCompound("SpiritualRoots");
            roots.clear();
            spiritualRoots.getAllKeys().forEach(key -> {
                QiRootTypes.registry().getValue(key).ifPresent(roots::add);
            });
        }
        if (nbt.contains("ExperienceMap")) {
            CompoundTag experienceMap = nbt.getCompound("ExperienceMap");
            this.experienceMap.clear();
            experienceMap.getAllKeys().forEach(key -> {
                ExperienceType type = ExperienceType.valueOf(key);
                this.experienceMap.put(type, experienceMap.getFloat(key));
            });
        }
        if (nbt.contains("PlayerRealmType")) {
            CodecHelper.parse(RealmTypes.registry().byNameCodec(), nbt.get("PlayerRealmType"))
                    .resultOrPartial()
                    .ifPresent(realmType -> this.realmType = realmType);
        }
        if (nbt.contains("NextCultivationType")) {
            CodecHelper.parse(CultivationTypes.registry().byNameCodec(), nbt.get("NextCultivationType"))
                    .resultOrPartial()
                    .ifPresent(cultivationType -> this.cultivationType = cultivationType);
        }
    }

    @Override
    public void initialize() {
        if(IMMGameRules.randomQiRoots(playerData.getPlayer().level())) {
            PlayerUtil.setRoots(playerData.getPlayer(), QiManager.getQiRoots(playerData.getPlayer().getRandom()));
        }
    }

    @Override
    public void syncToClient() {
        sendExperienceUpdatePacket();
        sendRootAndRealmUpdatePacket();
    }

    @Override
    public boolean isServer() {
        return playerData.isServer();
    }

    /* Spiritual Roots related methods */

    public void addRoot(QiRootType spiritualRoot) {
        this.roots.add(spiritualRoot);
        this.sendRootAndRealmUpdatePacket();
    }

    public void removeRoot(QiRootType spiritualRoot) {
        this.roots.remove(spiritualRoot);
        this.sendRootAndRealmUpdatePacket();
    }

    public void clearSpiritualRoot() {
        this.roots.clear();
        this.sendRootAndRealmUpdatePacket();
    }

    public int getSpiritualRootCount() {
        return this.roots.size();
    }

    public boolean hasRoot(QiRootType root) {
        return this.roots.contains(root);
    }

    public List<QiRootType> getRoots() {
        return this.roots.stream().toList();
    }

    /* Experience Related Methods */

    public void setExperience(ExperienceType type, float value) {
        this.experienceMap.put(type, value);
        if (isServer()) {
            // 没有门槛时，自动进到下一个阶段。
            Optional<RealmType> nextRealm = RealmManager.getNextRealm(this.realmType, this.cultivationType);
            while (CultivationManager.canFreeLevelUp(playerData.getPlayer()) && nextRealm.isPresent()) {
                RealmManager.realmLevelUp(playerData.getPlayer(), nextRealm.get());
                nextRealm = RealmManager.getNextRealm(this.realmType, this.cultivationType);
            }
        }
        this.sendExperienceUpdatePacket(type, value);
    }

    public void addExperience(ExperienceType type, float value) {
        this.setExperience(type, this.getExperience(type) + value);
    }

    public float getExperience(ExperienceType type) {
        return this.experienceMap.getOrDefault(type, 0F);
    }

    public float getCultivation() {
        return Arrays.stream(ExperienceType.values())
                .map(xp -> Math.min(CultivationManager.getMaxExperience(playerData.getPlayer(), this.realmType), getExperience(xp)))
                .reduce(0F, Float::sum);
    }

    public void sendExperienceUpdatePacket(ExperienceType type, float value) {
        if (playerData.getPlayer() instanceof ServerPlayer serverPlayer) {
            NetworkHelper.sendToClient(serverPlayer, new ExperienceChangePacket(Map.of(type, value)));
        }
    }

    public void sendExperienceUpdatePacket() {
        if (playerData.getPlayer() instanceof ServerPlayer serverPlayer) {
            NetworkHelper.sendToClient(serverPlayer, new ExperienceChangePacket(experienceMap));
        }
    }

    /* Realm Related Methods */

    public RealmType getRealmType() {
        return this.realmType;
    }

    public void setRealmType(RealmType realmType) {
        if (this.realmType != realmType) {
            EntityRealmEvent.Pre preEvent = new EntityRealmEvent.Pre(playerData.getPlayer(), this.realmType, realmType);
            if (EventUtil.post(preEvent)) {
                return;
            }
            realmType = preEvent.getAfterRealm();
            // trigger realm change.
            if (playerData.getPlayer() instanceof ServerPlayer serverPlayer && this.realmType != realmType) {
                PlayerRealmChangeTrigger.INSTANCE.trigger(serverPlayer, realmType);
            }
            // 突破次数重设。
            this.playerData.setIntegerData(IMMPlayerData.IntegerData.BREAK_THROUGH_TRIES, 0);
            // 突破进度重设。
            this.playerData.setFloatData(IMMPlayerData.FloatData.BREAK_THROUGH_PROGRESS, 0F);
            // 更新玩家属性。
            RealmManager.updateRealmAttributes(playerData.getPlayer(), this.realmType, realmType);
            this.realmType = realmType;
            // Update realm node manually.
            this.getRealmNode(true);
            this.sendRootAndRealmUpdatePacket();
            EventUtil.post(new EntityRealmEvent.Post(playerData.getPlayer(), this.realmType, realmType));
        }

    }

    /**
     * 目测应该不需要同步到客户端。
     */
    public void setCultivationType(CultivationType cultivationType) {
        this.cultivationType = cultivationType;
    }

    /**
     * @return 指明下一个境界的晋升方向。
     */
    public CultivationType getCultivationType() {
        return cultivationType;
    }

    public void sendRootAndRealmUpdatePacket() {
        if (playerData.getPlayer() instanceof ServerPlayer serverPlayer) {
            NetworkHelper.sendToClient(serverPlayer, new QiRootAndRealmPacket(getRoots(), getRealmType()));
        }
    }

    public RealmNode getRealmNode() {
        return getRealmNode(false);
    }

    public RealmNode getRealmNode(boolean update) {
        if (this.realmNodeCache == null || update) {
            RealmNode.getNodeOpt(this.realmType).ifPresent(node -> {
                this.realmNodeCache = node;
            });
        }
        return this.realmNodeCache;
    }

}
