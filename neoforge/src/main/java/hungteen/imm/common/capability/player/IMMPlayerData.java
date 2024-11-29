package hungteen.imm.common.capability.player;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.registry.EnumEntry;
import hungteen.htlib.platform.HTLibPlatformAPI;
import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.registry.ISectType;
import hungteen.imm.common.capability.HTPlayerData;
import hungteen.imm.common.impl.registry.SectTypes;
import hungteen.imm.common.network.MiscDataPacket;
import hungteen.imm.common.network.SectRelationPacket;
import hungteen.imm.common.network.client.FloatDataPacket;
import hungteen.imm.common.network.client.IntegerDataPacket;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;

import java.util.EnumMap;
import java.util.HashMap;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-09-24 15:02
 **/
public class IMMPlayerData implements HTPlayerData {

    private final Player player;
    private final CultivationData cultivationData;
    private final SpellData spellData;
    private final MiscData miscData = new MiscData();
    private final HashMap<ISectType, Float> sectRelations = new HashMap<>();
    private final EnumMap<IntegerData, Integer> integerMap = new EnumMap<>(IntegerData.class);
    private final EnumMap<FloatData, Float> floatMap = new EnumMap<>(FloatData.class);
    private long nextRefreshTick;

    public IMMPlayerData(Player player) {
        this.player = player;
        this.cultivationData = new CultivationData(this);
        this.spellData = new SpellData(this);
        for (IntegerData data : IntegerData.values()) {
            integerMap.computeIfAbsent(data, k -> 0);
        }
        for (FloatData data : FloatData.values()) {
            floatMap.computeIfAbsent(data, k -> 0F);
        }
    }

    public IMMPlayerData() {
        this(null);
        throw new RuntimeException("This constructor is not allowed to be called.");
    }

    public void tick() {
        if (EntityHelper.isServer(player)) {
//            // 突破检测。
//            if (this.getFloatData(FloatData.BREAK_THROUGH_PROGRESS) >= 1) {
//                if ((player.tickCount & 1) == 0) {
//                    player.addEffect(EffectHelper.viewEffect(IMMEffects.BREAK_THROUGH.holder(), 200, 0));
//                }
//            }
        }
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        {
            CompoundTag nbt = new CompoundTag();
            this.sectRelations.forEach((type, relation) -> {
                nbt.putFloat(type.getRegistryName(), relation);
            });
            tag.put("SectRelations", nbt);
        }
        {
            CompoundTag nbt = new CompoundTag();
            for(IntegerData data : IntegerData.values()){
                if(integerMap.containsKey(data)) {
                    nbt.putInt(data.getRegistryName(), integerMap.get(data));
                }
            }
            for (FloatData data : FloatData.values()) {
                if(floatMap.containsKey(data)) {
                    nbt.putFloat(data.getRegistryName(), floatMap.get(data));
                }
            }
            tag.put("PlayerRangeData", nbt);
        }
        {
            final CompoundTag nbt = new CompoundTag();
            nbt.putLong("NextRefreshTick", this.nextRefreshTick);
            tag.put("MiscData", nbt);
        }
        tag.put("PlayerCultureData", cultivationData.serializeNBT(provider));
        tag.put("PlayerSpellData", spellData.serializeNBT(provider));
        tag.put("PlayerMiscData", miscData.serializeNBT(provider));
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        if (tag.contains("SectRelations")) {
            final CompoundTag nbt = tag.getCompound("SectRelations");
            SectTypes.registry().getValues().forEach(type -> {
                if (nbt.contains(type.getRegistryName())) {
                    this.sectRelations.put(type, nbt.getFloat(type.getRegistryName()));
                }
            });
        }
        if (tag.contains("PlayerRangeData")) {
            integerMap.clear();
            floatMap.clear();
            final CompoundTag nbt = tag.getCompound("PlayerRangeData");
            for(IntegerData data : IntegerData.values()){
                if (nbt.contains(data.getRegistryName())) {
                    integerMap.put(data, nbt.getInt(data.getRegistryName()));
                }
            }
            for (FloatData data : FloatData.values()) {
                if (nbt.contains(data.getRegistryName())) {
                    this.floatMap.put(data, nbt.getFloat(data.getRegistryName()));
                }
            }
        }
        if (tag.contains("MiscData")) {
            CompoundTag nbt = tag.getCompound("MiscData");
            if (nbt.contains("NextRefreshTick")) {
                this.nextRefreshTick = nbt.getLong("NextRefreshTick");
            }
        }
        if(tag.contains("PlayerCultureData")){
            this.cultivationData.deserializeNBT(provider, tag.getCompound("PlayerCultureData"));
        }
        if(tag.contains("PlayerSpellData")){
            this.spellData.deserializeNBT(provider, tag.getCompound("PlayerSpellData"));
        }
        if (tag.contains("PlayerMiscData")) {
            this.miscData.deserializeNBT(provider, tag.getCompound("PlayerMiscData"));
        }
    }

    @Override
    public void initialize() {
        // 防止强制退出导致无法坐上坐垫。
        setIntegerData(IntegerData.IS_MEDITATING, 0);
        syncToClient();
    }

    /**
     * sync data to client side.
     */
    @Override
    public void syncToClient() {
        this.cultivationData.syncToClient();
        this.spellData.syncToClient();
        this.miscData.syncToClient();
        for(IntegerData data : IntegerData.values()){
            this.sendIntegerDataPacket(data, this.getIntegerData(data));
        }
        for(FloatData data : FloatData.values()){
            this.sendFloatDataPacket(data, this.getFloatData(data));
        }
    }

    @Override
    public boolean isServer() {
        return player instanceof ServerPlayer;
    }

    /* Sect Related Methods */

    public float getSectRelation(ISectType sectType) {
        return this.sectRelations.getOrDefault(sectType, 0F);
    }

    public void setSectRelation(ISectType sectType, float value) {
        this.sectRelations.put(sectType, value);
        this.sendSectPacket(sectType, value);
    }

    public void addSectRelation(ISectType sectType, float value) {
        this.setSectRelation(sectType, this.getSectRelation(sectType) + value);
    }

    public void sendSectPacket(ISectType sect, float value) {
        if (getPlayer() instanceof ServerPlayer p) {
            NetworkHelper.sendToClient(p, new SectRelationPacket(sect, value));
        }
    }

    /* Player Number Data Related Methods */

    public int getIntegerData(IntegerData rangeData) {
        return integerMap.computeIfAbsent(rangeData, k -> 0);
    }

    public float getFloatData(FloatData rangeData) {
        return floatMap.computeIfAbsent(rangeData, k -> 0F);
    }

    public void setIntegerData(IntegerData rangeData, int value) {
        integerMap.put(rangeData, value);
        sendIntegerDataPacket(rangeData, value);
    }

    public void setFloatData(FloatData rangeData, float value) {
        if (rangeData == FloatData.QI_AMOUNT) {
            value = Mth.clamp(value, 0, PlayerUtil.getMaxQi(player));
        }
        floatMap.put(rangeData, value);
        sendFloatDataPacket(rangeData, value);
    }

    public void addIntegerData(IntegerData rangeData, int value) {
        setIntegerData(rangeData, getIntegerData(rangeData) + value);
    }

    public void addFloatData(FloatData rangeData, float value) {
        setFloatData(rangeData, getFloatData(rangeData) + value);
    }

    public void sendIntegerDataPacket(IntegerData rangeData, int value) {
        if (getPlayer() instanceof ServerPlayer serverPlayer) {
            HTLibPlatformAPI.get().sendToClient(serverPlayer, new IntegerDataPacket(rangeData, value));
        }
    }

    public void sendFloatDataPacket(FloatData data, float value) {
        if (getPlayer() instanceof ServerPlayer serverPlayer) {
            NetworkHelper.sendToClient(serverPlayer, new FloatDataPacket(data, value));
        }
    }

    /* Misc methods */

    public CultivationData getCultivationData() {
        return cultivationData;
    }

    public SpellData getSpellData() {
        return spellData;
    }

    public MiscData getMiscData() {
        return miscData;
    }

    public void sendMiscDataPacket(MiscDataPacket.Types type) {
        sendMiscDataPacket(type, "");
    }

    public void sendMiscDataPacket(MiscDataPacket.Types type, String data) {
        sendMiscDataPacket(type, data, 0F);
    }

    public void sendMiscDataPacket(MiscDataPacket.Types type, String data, float value) {
        if (getPlayer() instanceof ServerPlayer) {
//            NetworkHelper.sendToClient((ServerPlayer) getPlayer(), new MiscDataPacket(type, data, value));
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isClientSide() {
        return ! isServer();
    }

    public long getGameTime() {
        return getPlayer().level().getGameTime();
    }

    public enum FloatData implements EnumEntry {

        QI_AMOUNT,

        BREAK_THROUGH_PROGRESS,

        /**
         * 精神力 or 神识。
         */
        CONSCIOUSNESS,

        /**
         * 业障。
         */
        KARMA,

        ;

        public static final Codec<FloatData> CODEC = StringRepresentable.fromEnum(FloatData::values);

        @Override
        public String getModID() {
            return Util.id();
        }

        @Override
        public MutableComponent getComponent() {
            return TipUtil.misc("player_data." + getName());
        }
    }

    public enum IntegerData implements EnumEntry {

        /**
         * 元素精通点。
         */
        ELEMENTAL_MASTERY_POINTS,

        /**
         * 是否正在打坐，0表示没有，1表示正在打坐。
         */
        IS_MEDITATING,

        /**
         * 是否开启默认轮盘，0代表需要客户端配置文件更新选项，1表示默认，2表示滚轮
         */
        SPELL_CIRCLE_MODE,

        /**
         * 突破尝试次数。
         */
        BREAK_THROUGH_TRIES,

        /**
         * 是否知道自身灵根。
         */
        KNOW_SPIRITUAL_ROOTS

        ;

        public static final Codec<IntegerData> CODEC = StringRepresentable.fromEnum(IntegerData::values);

        @Override
        public String getModID() {
            return Util.id();
        }

        @Override
        public MutableComponent getComponent() {
            return TipUtil.misc("player_data." + getName());
        }
    }

}