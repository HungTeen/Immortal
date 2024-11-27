package hungteen.imm.common.capability.player;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.registry.EnumEntry;
import hungteen.htlib.platform.HTLibPlatformAPI;
import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.api.registry.ISectType;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.advancement.trigger.PlayerLearnSpellTrigger;
import hungteen.imm.common.advancement.trigger.PlayerLearnSpellsTrigger;
import hungteen.imm.common.capability.HTPlayerData;
import hungteen.imm.common.impl.registry.SectTypes;
import hungteen.imm.common.network.MiscDataPacket;
import hungteen.imm.common.network.SectRelationPacket;
import hungteen.imm.common.network.SpellPacket;
import hungteen.imm.common.network.client.FloatDataPacket;
import hungteen.imm.common.network.client.IntegerDataPacket;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashMap;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-09-24 15:02
 **/
public class IMMPlayerData implements HTPlayerData {

    private Player player;
    private final PlayerMiscData miscData = new PlayerMiscData();
    private final CultivationData cultivationData;
    private final HashMap<ISpellType, Integer> learnSpells = new HashMap<>(); // 学习的法术。
    private final HashMap<ISpellType, Long> spellCDs = new HashMap<>(); // 法术的冷却。
    private final ISpellType[] spellList = new ISpellType[Constants.SPELL_CIRCLE_SIZE]; // 法术轮盘。
    private final HashMap<ISectType, Float> sectRelations = new HashMap<>(); // 宗门关系。
    private final EnumMap<IntegerData, Integer> integerMap = new EnumMap<>(IntegerData.class);
    private final EnumMap<FloatData, Float> floatMap = new EnumMap<>(FloatData.class);
    private ISpellType preparingSpell = null; // 当前选好的法术。
    private long nextRefreshTick;

    public IMMPlayerData(Player player) {
        this.player = player;
        this.cultivationData = new CultivationData(this);
        for (IntegerData data : IntegerData.values()) {
            integerMap.computeIfAbsent(data, k -> 0);
        }
        for (FloatData data : FloatData.values()) {
            floatMap.computeIfAbsent(data, k -> 0F);
        }
    }

    public IMMPlayerData() {
        this(null);
    }

    public void tick() {
        if (EntityHelper.isServer(player)) {
            // 灵气自然增长。
//            if (EntityUtil.canManaIncrease(player)) {
//                final float incValue = LevelUtil.getSpiritualRate(player.level(), player.blockPosition());
//                this.addFloatData(FloatData.QI_AMOUNT, incValue);
//            }
//            // 突破检测。
//            if (this.getFloatData(FloatData.BREAK_THROUGH_PROGRESS) >= 1) {
//                if ((player.tickCount & 1) == 0) {
//                    player.addEffect(EffectHelper.viewEffect(IMMEffects.BREAK_THROUGH.holder(), 200, 0));
//                }
//            }
            //TODO 随时间更新Sect
        }
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        {
            CompoundTag nbt = new CompoundTag();
            this.learnSpells.forEach((spell, level) -> {
                nbt.putInt("learn_" + spell.getRegistryName(), level);
            });
            this.spellCDs.forEach((spell, time) -> {
                nbt.putLong("cooldown_" + spell.getRegistryName(), time);
            });
            tag.put("Spells", nbt);
        }
        {
            CompoundTag nbt = new CompoundTag();
            for (int i = 0; i < Constants.SPELL_CIRCLE_SIZE; i++) {
                if (spellList[i] != null) {
                    nbt.putString("active_" + i, spellList[i].getRegistryName());
                }
            }
            tag.put("SpellList", nbt);
        }
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
            if (this.preparingSpell != null) {
                nbt.putString("PreparingSpell", this.preparingSpell.getRegistryName());
            }
            tag.put("MiscData", nbt);
        }
        tag.put("PlayerCultureData", cultivationData.serializeNBT(provider));
        tag.put("PlayerMiscData", miscData.serializeNBT(provider));
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        if (tag.contains("Spells")) {
            learnSpells.clear();
            spellCDs.clear();
            final CompoundTag nbt = tag.getCompound("Spells");
            SpellTypes.registry().getValues().forEach(type -> {
                if (nbt.contains("learn_" + type.getRegistryName())) {
                    learnSpells.put(type, nbt.getInt("learn_" + type.getRegistryName()));
                }
                if (nbt.contains("cooldown_" + type.getRegistryName())) {
                    spellCDs.put(type, nbt.getLong("cooldown_" + type.getRegistryName()));
                }
            });
        }
        if (tag.contains("SpellList")) {
            final CompoundTag nbt = tag.getCompound("SpellList");
            for (int i = 0; i < this.spellList.length; ++i) {
                if (nbt.contains("active_" + i)) {
                    final int pos = i;
                    SpellTypes.registry().getValue(nbt.getString("active_" + i)).ifPresent(type -> {
                        this.spellList[pos] = type;
                    });
                }
            }
        }
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
            if (nbt.contains("PreparingSpell")) {
                SpellTypes.registry().getValue(nbt.getString("PreparingSpell")).ifPresent(l -> this.preparingSpell = l);
            }
        }
        if(tag.contains("PlayerCultureData")){
            this.cultivationData.deserializeNBT(provider, tag.getCompound("PlayerCultureData"));
        }
        if (tag.contains("PlayerMiscData")) {
            this.miscData.deserializeNBT(provider, tag.getCompound("PlayerMiscData"));
        }
    }

    @Override
    public void initialize() {
        // 防止强制退出导致无法坐上坐垫。
        setIntegerData(IntegerData.IS_MEDITATING, 0);
    }

    /**
     * sync data to client side.
     */
    public void syncToClient() {
        this.cultivationData.syncToClient();
        this.miscData.syncToClient();
        this.learnSpells.forEach((spell, level) -> {
            this.sendSpellPacket(SpellPacket.SpellOption.LEARN, spell, level);
        });
        for (int i = 0; i < Constants.SPELL_CIRCLE_SIZE; i++) {
            if (this.spellList[i] != null) {
                this.sendSpellPacket(SpellPacket.SpellOption.SET_SPELL_ON_CIRCLE, this.spellList[i], i);
            }
        }
        for(IntegerData data : IntegerData.values()){
            this.sendIntegerDataPacket(data, this.getIntegerData(data));
        }
        for(FloatData data : FloatData.values()){
            this.sendFloatDataPacket(data, this.getFloatData(data));
        }

        if (this.preparingSpell != null) {
            this.setPreparingSpell(this.preparingSpell);
        }
    }

    @Override
    public boolean isServer() {
        return player instanceof ServerPlayer;
    }

    /* Spell related methods */

    public void learnSpell(ISpellType spell, int level) {
        final int lvl = Mth.clamp(level, 0, spell.getMaxLevel());
        this.learnSpells.put(spell, lvl);
        if (player instanceof ServerPlayer serverPlayer) {
            PlayerLearnSpellTrigger.INSTANCE.trigger(serverPlayer, spell, level);
            PlayerLearnSpellsTrigger.INSTANCE.trigger(serverPlayer, (int) this.learnSpells.values().stream().filter(l -> l > 0).count());
        }
        this.sendSpellPacket(SpellPacket.SpellOption.LEARN, spell, lvl);
    }

    public void forgetSpell(ISpellType spell) {
        this.learnSpell(spell, 0);
    }

    public void forgetAllSpells() {
        this.learnSpells.forEach((spell, time) -> forgetSpell(spell));
    }

    public void setSpellAt(int pos, ISpellType spell) {
        this.spellList[pos] = spell;
        this.sendSpellPacket(SpellPacket.SpellOption.SET_SPELL_ON_CIRCLE, spell, pos);
    }

    public void removeSpellAt(int pos) {
        this.spellList[pos] = null;
        this.sendSpellPacket(SpellPacket.SpellOption.REMOVE_SPELL_ON_CIRCLE, SpellTypes.MEDITATION, pos);
    }

    public ISpellType getSpellAt(int pos) {
        return this.spellList[Mth.clamp(pos, 0, Constants.SPELL_CIRCLE_SIZE - 1)];
    }

    public boolean isSpellOnCircle(ISpellType spell) {
        for (ISpellType spellType : this.spellList) {
            if (spell == spellType) {
                return true;
            }
        }
        return false;
    }

    public void cooldownSpell(@NotNull ISpellType spell, long num) {
        this.spellCDs.put(spell, num);
        this.sendSpellPacket(SpellPacket.SpellOption.COOL_DOWN, spell, num);
    }

    public boolean isSpellOnCoolDown(@NotNull ISpellType spell) {
        return this.spellCDs.containsKey(spell) && this.spellCDs.get(spell) > getGameTime();
    }

    public float getSpellCoolDown(@NotNull ISpellType spell) {
        return isSpellOnCoolDown(spell) ? (this.spellCDs.get(spell) - getGameTime()) * 1.0F / spell.getCooldown() : 0;
    }

    public boolean hasLearnedSpell(@NotNull ISpellType spell, int level) {
        return getSpellLevel(spell) >= level;
    }

    public int getSpellLevel(@NotNull ISpellType spell) {
        return this.learnSpells.getOrDefault(spell, 0);
    }

    public void sendSpellPacket(SpellPacket.SpellOption option, @Nullable ISpellType spell, long num) {
        if (getPlayer() instanceof ServerPlayer) {
            NetworkHelper.sendToClient((ServerPlayer) getPlayer(), new SpellPacket(spell, option, num));
        }
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

    public PlayerMiscData getMiscData() {
        return miscData;
    }

    @Nullable
    public ISpellType getPreparingSpell() {
        return preparingSpell;
    }

    public void setPreparingSpell(@Nullable ISpellType spell) {
        this.preparingSpell = spell;
        if (spell != null) {
            this.sendMiscDataPacket(MiscDataPacket.Types.PREPARING_SPELL, spell.getRegistryName());
        } else {
            this.sendMiscDataPacket(MiscDataPacket.Types.CLEAR_PREPARING_SPELL);
        }
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
        return getPlayer().level().isClientSide();
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