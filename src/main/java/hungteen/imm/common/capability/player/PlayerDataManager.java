package hungteen.imm.common.capability.player;

import hungteen.htlib.api.interfaces.IPlayerDataManager;
import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.enums.ExperienceTypes;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.api.registry.*;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.impl.registry.PlayerRangeFloats;
import hungteen.imm.common.impl.registry.PlayerRangeIntegers;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.impl.registry.SectTypes;
import hungteen.imm.common.network.*;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.Constants;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.LevelUtil;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 15:02
 **/
public class PlayerDataManager implements IPlayerDataManager {

    private final Player player;
    private final HashSet<ISpiritualType> spiritualRoots = new HashSet<>(); // 灵根。
    private final HashMap<ISpellType, Integer> learnSpells = new HashMap<>(); // 学习的法术。
    private final HashMap<ISpellType, Long> spellCDs = new HashMap<>(); // 法术的冷却。
    private final ISpellType[] spellList = new ISpellType[Constants.SPELL_CIRCLE_SIZE]; // 法术轮盘。
    private final HashMap<ISectType, Float> sectRelations = new HashMap<>(); // 宗门关系。
    private final EnumMap<ExperienceTypes, Float> experienceMap = new EnumMap<>(ExperienceTypes.class); // 修为。
    private final HashMap<IRangeNumber<Integer>, Integer> integerMap = new HashMap<>(); // 其他整数数据。
    private final HashMap<IRangeNumber<Float>, Float> floatMap = new HashMap<>(); // 其他小数数据。
    private IRealmType realmType = RealmTypes.MORTALITY; // 当前境界。
    private RealmStages realmStage = RealmStages.PRELIMINARY; // 当前境界阶段。
    private ISpellType preparingSpell = null; // 当前选好的法术。
    private long nextRefreshTick;
    private boolean initialized = false;

    /* Caches */
    private RealmManager.RealmNode realmNode;

    public PlayerDataManager(Player player) {
        this.player = player;
        PlayerRangeIntegers.registry().getValues().forEach(data -> {
            integerMap.put(data, data.defaultData());
        });
        PlayerRangeFloats.registry().getValues().forEach(data -> {
            floatMap.put(data, data.defaultData());
        });
    }

    public void initialize(){
        if(! this.initialized){
            // 初始化玩家的灵根
            PlayerUtil.resetSpiritualRoots(player);
            this.initialized = true;
        }
    }

    @Override
    public void tick() {
        if (EntityHelper.isServer(player)) {
            if(EntityUtil.canManaIncrease(player)){
                final float value = getFloatData(PlayerRangeFloats.SPIRITUAL_MANA);
                final float fullValue = getFullManaValue();
                // can natural increasing.
                if(value < fullValue){
                    final float incValue = Math.min(fullValue - value, LevelUtil.getSpiritualRate(player.level(), player.blockPosition()));
                    this.addFloatData(PlayerRangeFloats.SPIRITUAL_MANA, incValue);
                }
            }
            //TODO 随时间更新Sect
        }
    }

    @Override
    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        {
            CompoundTag nbt = new CompoundTag();
            IMMAPI.get().spiritualRegistry().ifPresent(l -> {
                l.getValues().forEach(type -> {
                    nbt.putBoolean(type.getRegistryName() + "_root", spiritualRoots.contains(type));
                });
            });
            tag.put("SpiritualRoots", nbt);
        }
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
            for (int i = 0; i < Constants.SPELL_CIRCLE_SIZE; i ++) {
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
            this.experienceMap.forEach((type, value) -> {
                nbt.putFloat(type.toString(), value);
            });
            tag.put("ExperienceMap", nbt);
        }
        {
            CompoundTag nbt = new CompoundTag();
            PlayerRangeIntegers.registry().getValues().forEach(data -> {
                nbt.putInt(data.getRegistryName(), integerMap.getOrDefault(data, data.defaultData()));
            });
            PlayerRangeFloats.registry().getValues().forEach(data -> {
                nbt.putFloat(data.getRegistryName(), floatMap.getOrDefault(data, data.defaultData()));
            });
            tag.put("PlayerRangeData", nbt);
        }
        {
            final CompoundTag nbt = new CompoundTag();
            nbt.putBoolean("Initialized", this.initialized);
            nbt.putLong("NextRefreshTick", this.nextRefreshTick);
            nbt.putString("PlayerRealmType", this.realmType.getRegistryName());
            nbt.putInt("PlayerRealmStage", this.realmStage.ordinal());
            if(this.preparingSpell != null){
                nbt.putString("PreparingSpell", this.preparingSpell.getRegistryName());
            }
            tag.put("MiscData", nbt);
        }
        return tag;
    }

    @Override
    public void loadFromNBT(CompoundTag tag) {
        if (tag.contains("SpiritualRoots")) {
            spiritualRoots.clear();
            final CompoundTag nbt = tag.getCompound("SpiritualRoots");
            IMMAPI.get().spiritualRegistry().ifPresent(l -> {
                l.getValues().forEach(type -> {
                    if (nbt.getBoolean(type.getRegistryName() + "_root")) {
                        spiritualRoots.add(type);
                    }
                });
            });
        }
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
            for(int i = 0; i < this.spellList.length; ++ i){
                if(nbt.contains("active_" + i)){
                    final int pos = i;
                    SpellTypes.registry().getValue(nbt.getString("active_" + i)).ifPresent(type -> {
                        this.spellList[pos] = type;
                    });
                }
            }
        }
        if (tag.contains("SectRelations")){
            final CompoundTag nbt = tag.getCompound("SectRelations");
            SectTypes.registry().getValues().forEach(type -> {
                if(nbt.contains(type.getRegistryName())){
                    this.sectRelations.put(type, nbt.getFloat(type.getRegistryName()));
                }
            });
        }
        if(tag.contains("ExperienceMap")){
            final CompoundTag nbt = tag.getCompound("ExperienceMap");
            for (ExperienceTypes type : ExperienceTypes.values()) {
                if(nbt.contains(type.toString())){
                    this.experienceMap.put(type, nbt.getFloat(type.toString()));
                }
            }
        }
        if (tag.contains("PlayerRangeData")) {
            integerMap.clear();
            floatMap.clear();
            final CompoundTag nbt = tag.getCompound("PlayerRangeData");
            PlayerRangeIntegers.registry().getValues().forEach(type -> {
                if (nbt.contains(type.getRegistryName())) {
                    integerMap.put(type, nbt.getInt(type.getRegistryName()));
                }
            });
            PlayerRangeFloats.registry().getValues().forEach(type -> {
                if (nbt.contains(type.getRegistryName())) {
                    floatMap.put(type, nbt.getFloat(type.getRegistryName()));
                }
            });
        }
        if (tag.contains("MiscData")) {
            CompoundTag nbt = tag.getCompound("MiscData");
            if(nbt.contains("Initialized")){
                this.initialized = nbt.getBoolean("Initialized");
            }
            if(nbt.contains("NextRefreshTick")){
                this.nextRefreshTick = nbt.getLong("NextRefreshTick");
            }
            if (nbt.contains("PlayerRealmType")) {
                RealmTypes.registry().getValue(nbt.getString("PlayerRealmType")).ifPresent(r -> this.realmType = r);
            }
            if(nbt.contains("PlayerRealmStage")){
                this.realmStage = RealmStages.values()[nbt.getInt("PlayerRealmStage")];
            }
            if(nbt.contains("PreparingSpell")){
                SpellTypes.registry().getValue(nbt.getString("PreparingSpell")).ifPresent(l -> this.preparingSpell = l);
            }
        }
    }

    /**
     * copy player data when clone event happen.
     */
    @Override
    public void cloneFromExistingPlayerData(IPlayerDataManager data, boolean died) {
        this.loadFromNBT(data.saveToNBT());
        this.syncToClient();
    }

    /**
     * sync data to client side.
     */
    @Override
    public void syncToClient() {
        this.sendMiscDataPacket(MiscDataPacket.Types.CLEAR_ROOT);
        this.spiritualRoots.forEach(l -> {
            this.sendMiscDataPacket(MiscDataPacket.Types.ADD_ROOT, l.getRegistryName());
        });
        this.learnSpells.forEach((spell, level) -> {
            this.sendSpellPacket(SpellPacket.SpellOptions.LEARN, spell, level);
        });
        for (int i = 0; i < Constants.SPELL_CIRCLE_SIZE; i++) {
            if (this.spellList[i] != null) {
                this.sendSpellPacket(SpellPacket.SpellOptions.SET_SPELL_ON_CIRCLE, this.spellList[i], i);
            }
        }
        this.integerMap.forEach(this::sendIntegerDataPacket);
        this.floatMap.forEach(this::sendFloatDataPacket);
        Arrays.stream(ExperienceTypes.values()).forEach(type -> this.addExperience(type, 0));
        this.setRealmType(this.realmType);
        if(this.preparingSpell != null) {
            this.setPreparingSpell(this.preparingSpell);
        }
    }

    /* Spiritual Roots related methods */

    public void addSpiritualRoot(ISpiritualType spiritualRoot) {
        this.spiritualRoots.add(spiritualRoot);
        this.sendMiscDataPacket(MiscDataPacket.Types.ADD_ROOT, spiritualRoot.getRegistryName());
    }

    public void removeSpiritualRoot(ISpiritualType spiritualRoot) {
        this.spiritualRoots.remove(spiritualRoot);
        this.sendMiscDataPacket(MiscDataPacket.Types.REMOVE_ROOT, spiritualRoot.getRegistryName());
    }

    public void clearSpiritualRoot() {
        this.spiritualRoots.clear();
        this.sendMiscDataPacket(MiscDataPacket.Types.CLEAR_ROOT);
    }

    public boolean hasSpiritualRoot(ISpiritualType spiritualRoot) {
        return this.spiritualRoots.contains(spiritualRoot);
    }

    public int getSpiritualRootCount() {
        return this.spiritualRoots.size();
    }

    public boolean hasRoot(ISpiritualType root){
        return this.spiritualRoots.contains(root);
    }

    public List<ISpiritualType> getSpiritualRoots() {
        return this.spiritualRoots.stream().toList();
    }

    /* Spell related methods */

    public void learnSpell(ISpellType spell, int level) {
        final int lvl = Mth.clamp(level, 0, spell.getMaxLevel());
        this.learnSpells.put(spell, lvl);
        this.sendSpellPacket(SpellPacket.SpellOptions.LEARN, spell, lvl);
    }

    public void forgetSpell(ISpellType spell) {
        this.learnSpell(spell, 0);
    }

    public void forgetAllSpells() {
        this.learnSpells.forEach((spell, time) -> forgetSpell(spell));
    }

    public void setSpellAt(int pos, ISpellType spell) {
        this.spellList[pos] = spell;
        this.sendSpellPacket(SpellPacket.SpellOptions.SET_SPELL_ON_CIRCLE, spell, pos);
    }

    public void removeSpellAt(int pos) {
        this.spellList[pos] = null;
        this.sendSpellPacket(SpellPacket.SpellOptions.REMOVE_SPELL_ON_CIRCLE, SpellTypes.MEDITATION, pos);
    }

    public ISpellType getSpellAt(int pos) {
        return this.spellList[Mth.clamp(pos, 0, Constants.SPELL_CIRCLE_SIZE - 1)];
    }

    public void cooldownSpell(@NotNull ISpellType spell, long num) {
        this.spellCDs.put(spell, num);
        this.sendSpellPacket(SpellPacket.SpellOptions.COOL_DOWN, spell, num);
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

    public void sendSpellPacket(SpellPacket.SpellOptions option, @Nullable ISpellType spell, long num) {
        if (getPlayer() instanceof ServerPlayer) {
            NetworkHandler.sendToClient((ServerPlayer) getPlayer(), new SpellPacket(spell, option, num));
        }
    }

    /* Sect Related Methods */

    public float getSectRelation(ISectType sectType){
        return this.sectRelations.getOrDefault(sectType, 0F);
    }

    public void setSectRelation(ISectType sectType, float value){
        this.sectRelations.put(sectType, value);
        this.sendSectPacket(sectType, value);
    }

    public void addSectRelation(ISectType sectType, float value){
        this.setSectRelation(sectType, this.getSectRelation(sectType) + value);
    }

    public void sendSectPacket(ISectType sect, float value) {
        if (getPlayer() instanceof ServerPlayer p) {
            NetworkHandler.sendToClient(p, new SectRelationPacket(sect, value));
        }
    }

    /* Experience Related Methods */

    public void setExperience(ExperienceTypes type, float value) {
        this.experienceMap.put(type, value);
        if(EntityHelper.isServer(getPlayer())){
            final RealmStages currentStage = this.getRealmStage();
            // 没有门槛时，自动进到下一个阶段。
            if(! currentStage.hasThreshold() && currentStage.hasNextStage()){
                final RealmStages nextStage = RealmStages.next(currentStage);
                if(this.getCultivation() >= RealmManager.getStageRequiredCultivation(this.getRealmType(), nextStage)){
                    this.setRealmStage(nextStage);
                }
            }
        }
        this.sendMiscDataPacket(MiscDataPacket.Types.EXPERIENCE, type.toString(), value);
    }

    public void addExperience(ExperienceTypes type, float value) {
        this.setExperience(type, this.getExperience(type) + value);
    }

    public float getExperience(ExperienceTypes type){
        return this.experienceMap.getOrDefault(type, 0F);
    }

    public float getCultivation(){
        return Arrays.stream(ExperienceTypes.values()).map(xp -> Math.min(RealmManager.getEachCultivation(this.realmType), getExperience(xp))).reduce(0F, Float::sum);
    }

    /* Player Number Data Related Methods */

    public int getIntegerData(IRangeNumber<Integer> rangeData) {
        return integerMap.getOrDefault(rangeData, rangeData.defaultData());
    }

    public float getFloatData(IRangeNumber<Float> rangeData) {
        return floatMap.getOrDefault(rangeData, rangeData.defaultData());
    }

    public void setIntegerData(IRangeNumber<Integer> rangeData, int value) {
        value = Mth.clamp(value, rangeData.getMinData(), rangeData.getMaxData());
        integerMap.put(rangeData, value);
        sendIntegerDataPacket(rangeData, value);
    }

    public void setFloatData(IRangeNumber<Float> rangeData, float value) {
        if(rangeData == PlayerRangeFloats.SPIRITUAL_MANA){
            value = Mth.clamp(value, rangeData.getMinData(), getFullManaValue());
        } else {
            value = Mth.clamp(value, rangeData.getMinData(), rangeData.getMaxData());
        }
        floatMap.put(rangeData, value);
        sendFloatDataPacket(rangeData, value);
    }

    public void addIntegerData(IRangeNumber<Integer> rangeData, int value) {
        setIntegerData(rangeData, getIntegerData(rangeData) + value);
    }

    public void addFloatData(IRangeNumber<Float> rangeData, float value) {
        setFloatData(rangeData, getFloatData(rangeData) + value);
    }

    /**
     * 灵力条的极限。
     * @return Natural increasing point.
     */
    public float getFullManaValue() {
        return getFloatData(PlayerRangeFloats.MAX_SPIRITUAL_MANA) + getRealmType().getSpiritualValue();
    }

    /**
     * 待客户端配置文件更新该值。
     */
    public boolean requireSyncCircle(){
        return getIntegerData(PlayerRangeIntegers.SPELL_CIRCLE_MODE) == 0;
    }

    public boolean useDefaultCircle(){
        return getIntegerData(PlayerRangeIntegers.SPELL_CIRCLE_MODE) == 1;
    }

    public void sendIntegerDataPacket(IRangeNumber<Integer> rangeData, int value) {
        if (getPlayer() instanceof ServerPlayer) {
            NetworkHandler.sendToClient((ServerPlayer) getPlayer(), new IntegerDataPacket(rangeData, value));
        }
    }

    public void sendFloatDataPacket(IRangeNumber<Float> rangeData, float value) {
        if (getPlayer() instanceof ServerPlayer) {
            NetworkHandler.sendToClient((ServerPlayer) getPlayer(), new FloatDataPacket(rangeData, value));
        }
    }

    /* Misc methods */

    public IRealmType getRealmType() {
        return this.realmType;
    }

    public void setRealmType(IRealmType realmType) {
        this.realmType = realmType;
        this.getRealmNode(true); // Update realm node manually.
        this.sendMiscDataPacket(MiscDataPacket.Types.REALM, realmType.getRegistryName());
    }

    public RealmStages getRealmStage(){
        return this.realmStage;
    }

    public void setRealmStage(RealmStages stage){
        if(this.realmStage != stage){
            this.realmStage = stage;
            // 突破次数重设。
            this.setIntegerData(PlayerRangeIntegers.BREAK_THROUGH_TRIES, 0);
            // 突破进度重设。
            this.setFloatData(PlayerRangeFloats.BREAK_THROUGH_PROGRESS, 0F);
            this.sendMiscDataPacket(MiscDataPacket.Types.REALM_STAGE, this.realmStage.toString());
        }
    }

    public ICultivationType getCultivationType() {
        return this.realmType.getCultivationType();
    }

    @Nullable
    public ISpellType getPreparingSpell() {
        return preparingSpell;
    }

    public void setPreparingSpell(@Nullable ISpellType spell){
        this.preparingSpell = spell;
        if(spell != null){
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
            NetworkHandler.sendToClient((ServerPlayer) getPlayer(), new MiscDataPacket(type, data, value));
        }
    }

    public RealmManager.RealmNode getRealmNode() {
        return getRealmNode(false);
    }

    public RealmManager.RealmNode getRealmNode(boolean update) {
        if (this.realmNode == null || update) {
            this.realmNode = RealmManager.findRealmNode(this.realmType);
        }
        return this.realmNode;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    public boolean isClientSide(){
        return getPlayer().level().isClientSide();
    }

    public long getGameTime() {
        return getPlayer().level().getGameTime();
    }

}
