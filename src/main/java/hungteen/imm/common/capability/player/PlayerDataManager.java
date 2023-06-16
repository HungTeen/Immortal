package hungteen.imm.common.capability.player;

import hungteen.htlib.api.interfaces.IPlayerDataManager;
import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.api.registry.ISectType;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.impl.registry.PlayerRangeFloats;
import hungteen.imm.common.impl.registry.SectTypes;
import hungteen.imm.common.network.*;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.common.impl.registry.PlayerRangeIntegers;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.util.Constants;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 15:02
 **/
public class PlayerDataManager implements IPlayerDataManager {

    private final Player player;
    private final HashSet<ISpiritualType> spiritualRoots = new HashSet<>();
    private final HashMap<ISpellType, Integer> learnSpells = new HashMap<>();
    private final HashMap<ISpellType, Long> spellCDs = new HashMap<>(); // store the end time of specific spells.
    private final ISpellType[] spellList = new ISpellType[Constants.SPELL_CIRCLE_SIZE]; // active spells.
    private final HashSet<ISpellType> spellSet = new HashSet<>(); // passive spells.
    private final HashMap<ISectType, Float> sectRelations = new HashMap<>();
    private IRealmType realmType = RealmTypes.MORTALITY;
    private final HashMap<IRangeNumber<Integer>, Integer> integerMap = new HashMap<>(); // misc sync integers.
    private final HashMap<IRangeNumber<Float>, Float> floatMap = new HashMap<>(); // misc sync floats.
    private long nextRefreshTick;
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
        // 初始化玩家的灵根
        PlayerUtil.resetSpiritualRoots(player);
    }

    @Override
    public void tick() {
        if (!player.level.isClientSide) {
            //TODO 灵气自然恢复
//            if(player.level.getGameTime() % Constants.SPIRITUAL_ABSORB_TIME == 0){
//                final int value = getIntegerData(PlayerRangeNumbers.SPIRITUAL_MANA);
//                final int fullValue = getFullManaValue();
//                final int limitValue = getLimitManaValue();
//                // can natural increasing.
//                if(value < fullValue){
//                    final int incValue = Math.min(fullValue - value, ImmortalAPI.get().getSpiritualValue(player.level, player.blockPosition()));
//                    this.addIntegerData(PlayerRangeNumbers.SPIRITUAL_MANA, incValue);
//                } else if(value > fullValue){
//                    if(value > limitValue){
//                        // TODO 爆体而亡 ？
//                    } else {
//                        final int decValue = Math.min(Math.max(1, (value - fullValue) / 10), value - fullValue);
//                        this.addIntegerData(PlayerRangeNumbers.SPIRITUAL_MANA, - decValue);
//                    }
//                }
//            }
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
            for (int i = 0; i < Constants.SPELL_CIRCLE_SIZE; i++) {
                if (spellList[i] != null) {
                    nbt.putInt("active_" + spellList[i].getRegistryName(), i);
                }
            }
            this.spellSet.forEach(spell -> {
                nbt.putBoolean("passive_" + spell.getRegistryName(), true);
            });
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
            nbt.putLong("NextRefreshTick", this.nextRefreshTick);
            nbt.putString("PlayerRealmType", this.realmType.getRegistryName());
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
            IMMAPI.get().spellRegistry().ifPresent(l -> {
                l.getValues().forEach(type -> {
                    if (nbt.contains("active_" + type.getRegistryName())) {
                        spellList[nbt.getInt(type.getRegistryName())] = type;
                    }
                    if (nbt.contains("passive_" + type.getRegistryName())) {
                        spellSet.add(type);
                    }
                });
            });
        }
        if (tag.contains("SectRelations")){
            final CompoundTag nbt = tag.getCompound("SectRelations");
            SectTypes.registry().getValues().forEach(type -> {
                if(nbt.contains(type.getRegistryName())){
                    this.sectRelations.put(type, nbt.getFloat(type.getRegistryName()));
                }
            });
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
            if(nbt.contains("NextRefreshTick")){
                this.nextRefreshTick = nbt.getLong("NextRefreshTick");
            }
            if (nbt.contains("PlayerRealmType")) {
                IMMAPI.get().realmRegistry().flatMap(l -> l.getValue(nbt.getString("PlayerRealmType"))).ifPresent(r -> this.realmType = r);
            }
        }
    }

    /**
     * copy player data when clone event happen.
     */
    @Override
    public void cloneFromExistingPlayerData(IPlayerDataManager data, boolean died) {
        this.loadFromNBT(data.saveToNBT());
    }

    /**
     * sync data to client side.
     */
    @Override
    public void syncToClient() {
        this.learnSpells.forEach((spell, level) -> {
            this.sendSpellPacket(SpellPacket.SpellOptions.LEARN, spell, level);
        });
        for (int i = 0; i < Constants.SPELL_CIRCLE_SIZE; i++) {
            if (this.spellList[i] != null) {
                this.sendSpellPacket(SpellPacket.SpellOptions.SET_POS_ON_CIRCLE, this.spellList[i], i);
            }
        }
        this.integerMap.forEach(this::sendIntegerDataPacket);
        this.floatMap.forEach(this::sendFloatDataPacket);
    }

    /* SpiritualRoots related methods */

    public void addSpiritualRoot(ISpiritualType spiritualRoot) {
        this.spiritualRoots.add(spiritualRoot);
    }

    public void removeSpiritualRoot(ISpiritualType spiritualRoot) {
        this.spiritualRoots.remove(spiritualRoot);
    }

    public void clearSpiritualRoot() {
        this.spiritualRoots.clear();
    }

    public boolean hasSpiritualRoot(ISpiritualType spiritualRoot) {
        return this.spiritualRoots.contains(spiritualRoot);
    }

    public int getSpiritualRootCount() {
        return this.spiritualRoots.size();
    }

    public List<ISpiritualType> getSpiritualRoots() {
        return this.spiritualRoots.stream().toList();
    }

    /* Spell related methods */

    public void learnSpell(ISpellType spell, int level) {
        this.learnSpells.put(spell, Mth.clamp(level, 0, spell.getMaxLevel()));
        this.sendSpellPacket(SpellPacket.SpellOptions.LEARN, spell, level);
    }

    public void forgetSpell(ISpellType spell) {
        this.learnSpell(spell, 0);
    }

    public void learnAllSpells(int level) {
        this.learnSpells.forEach((spell, time) -> learnSpell(spell, level));
    }

    public void forgetAllSpells() {
        this.learnSpells.forEach((spell, time) -> forgetSpell(spell));
    }

    public void setSpellList(int pos, ISpellType spell) {
        this.spellList[pos] = spell;
        this.sendSpellPacket(SpellPacket.SpellOptions.SET_POS_ON_CIRCLE, spell, pos);
    }

    public void removeSpellList(int pos, ISpellType spell) {
        this.spellList[pos] = null;
        this.sendSpellPacket(SpellPacket.SpellOptions.REMOVE_POS_ON_CIRCLE, spell, pos);
    }

    /**
     * Check whether the spell set out of bound of limit.
     */
    private void checkSpellSet(){
        if(this.spellSet.size() > getSpellSetLimit()){
            List<ISpellType> list = this.spellSet.stream().limit(getSpellSetLimit()).toList();
            this.clearSpellSet();
            list.forEach(this::addSpellSet);
        }
    }

    public void addSpellSet(ISpellType spell){
        if(! this.isClientSide()) checkSpellSet();
        if(! this.spellSet.contains(spell) && this.spellSet.size() < getSpellSetLimit()){
            this.spellSet.add(spell);
            this.sendSpellPacket(SpellPacket.SpellOptions.ADD_SET, spell, 0);
        }
    }

    public void removeSpellSet(ISpellType spell){
        if(! this.isClientSide()) checkSpellSet();
        if(this.spellSet.contains(spell)){
            this.spellSet.remove(spell);
            this.sendSpellPacket(SpellPacket.SpellOptions.REMOVE_SET, spell, 0);
        }
    }

    public void clearSpellSet(){
        this.spellSet.clear();
        this.sendSpellPacket(SpellPacket.SpellOptions.CLEAR_SET, null, 0);
    }

    public void cooldownSpell(@NotNull ISpellType spell, long num) {
        this.spellCDs.put(spell, num);
        this.sendSpellPacket(SpellPacket.SpellOptions.COOL_DOWN, spell, num);
    }

    public ISpellType getSpellAt(int num) {
        return this.spellList[Mth.clamp(num, 0, Constants.SPELL_CIRCLE_SIZE - 1)];
    }

    public boolean isSpellOnCoolDown(@NotNull ISpellType spell) {
        return this.spellCDs.containsKey(spell) && this.spellCDs.get(spell) > getGameTime();
    }

    public double getSpellCoolDown(@NotNull ISpellType spell) {
        return isSpellOnCoolDown(spell) ? (this.spellCDs.get(spell) - getGameTime()) * 1.0 / spell.getCooldown() : 0;
    }

    public boolean hasLearnedSpell(@NotNull ISpellType spell, int level) {
        return getSpellLevel(spell) >= level;
    }

    public int getSpellLevel(@NotNull ISpellType spell) {
        return this.learnSpells.getOrDefault(spell, 0);
    }

    public boolean hasPassiveSpell(@NotNull ISpellType spell, int level) {
        return this.hasLearnedSpell(spell, level) && this.hasPassiveSpell(spell);
    }

    public boolean hasPassiveSpell(@NotNull ISpellType spell) {
        return this.spellSet.contains(spell);
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

    /* Player Number Data Related Methods */

    public int getIntegerData(IRangeNumber<Integer> rangeData) {
        return integerMap.getOrDefault(rangeData, rangeData.defaultData());
    }

    public float getFloatData(IRangeNumber<Float> rangeData) {
        return floatMap.getOrDefault(rangeData, rangeData.defaultData());
    }

    public void setIntegerData(IRangeNumber<Integer> rangeData, int value) {
        setIntegerData(rangeData, value, false);
    }

    public void setFloatData(IRangeNumber<Float> rangeData, float value) {
        setFloatData(rangeData, value, false);
    }

    /**
     * Directly set value.
     * @param ignore 防止死循环。
     */
    public void setIntegerData(IRangeNumber<Integer> rangeData, int value, boolean ignore) {
        final int result = Mth.clamp(value, rangeData.getMinData(), rangeData.getMaxData());
        integerMap.put(rangeData, result);
        sendIntegerDataPacket(rangeData, result);
    }

    /**
     * Directly set value.
     * @param ignore 防止死循环。
     */
    public void setFloatData(IRangeNumber<Float> rangeData, float value, boolean ignore) {
        final float result = Mth.clamp(value, rangeData.getMinData(), rangeData.getMaxData());
        if (!ignore && PlayerRangeFloats.CULTIVATION.equals(rangeData)) {
            updateCultivation(value - getFloatData(PlayerRangeFloats.CULTIVATION));
        } else {
            floatMap.put(rangeData, result);
            sendFloatDataPacket(rangeData, result);
        }
    }

    public void addIntegerData(IRangeNumber<Integer> rangeData, int value) {
        setIntegerData(rangeData, getIntegerData(rangeData) + value);
    }

    public void addFloatData(IRangeNumber<Float> rangeData, float value) {
        if (PlayerRangeFloats.CULTIVATION.equals(rangeData)) {
            updateCultivation(value);
        } else {
            setFloatData(rangeData, getFloatData(rangeData) + value);
        }
    }

    protected void updateCultivation(float value) {
        float oldValue = getFloatData(PlayerRangeFloats.CULTIVATION);
        if (value < 0) {
            while (oldValue + value < 0 && getRealmNode().hasPreviousNode()) {
                value += oldValue;
                levelDown();
                oldValue = getRealmType().requireCultivation(); // 上一级所需的修为。
            }
            this.setFloatData(PlayerRangeFloats.CULTIVATION, oldValue + value, true);
        } else if (value > 0) {
            RealmManager.RealmNode next = getRealmNode().next(getRealmType().getCultivationType());
            // 1. Cultivation reach, 2. no threshold, 3. has next.
            while (oldValue + value >= getRealmType().requireCultivation() && !getRealmType().hasThreshold() && next != null) {
                value -= getRealmType().requireCultivation() - oldValue;
                oldValue = 0;
                this.setRealmType(next.getRealm());
                getRealmNode(true);
                next = getRealmNode().next(getRealmType().getCultivationType());
            }
            this.setFloatData(PlayerRangeFloats.CULTIVATION, oldValue + value, true);
        }
        if (getFloatData(PlayerRangeFloats.SPIRITUAL_MANA) > getLimitManaValue()) {
            this.setFloatData(PlayerRangeFloats.SPIRITUAL_MANA, getLimitManaValue());
        }
    }

    protected void levelDown() {
        if (getRealmNode().hasPreviousNode()) {
            this.setRealmType(getRealmNode().getPreviousRealm());
            getRealmNode(true);
        }
    }

    /**
     * 第一层法力条的极限。
     * @return Natural increasing point.
     */
    public float getFullManaValue() {
        return Mth.clamp(getFloatData(PlayerRangeFloats.MAX_SPIRITUAL_MANA) + getRealmType().getBaseSpiritualValue(), 0, getRealmType().getSpiritualValueLimit());
    }

    /**
     * 第二层溢出条的极限。
     * @return Explode if player has more mana than it.
     */
    public float getLimitManaValue() {
        return getRealmType().getSpiritualValueLimit();
    }

    public int getSpellSetLimit(){
        return getIntegerData(PlayerRangeIntegers.PASSIVE_SPELL_COUNT_LIMIT);
    }

    /**
     * 待客户端配置文件更新该值。
     */
    public boolean requireSyncCircle(){
        return getIntegerData(PlayerRangeIntegers.DEFAULT_SPELL_CIRCLE) == 0;
    }

    public boolean useDefaultCircle(){
        return getIntegerData(PlayerRangeIntegers.DEFAULT_SPELL_CIRCLE) == 1;
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
        this.sendStringDataPacket(StringDataPacket.Types.REALM, realmType.getRegistryName());
    }

    public void sendStringDataPacket(StringDataPacket.Types type, String data) {
        if (getPlayer() instanceof ServerPlayer) {
            NetworkHandler.sendToClient((ServerPlayer) getPlayer(), new StringDataPacket(type, data));
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
        return getPlayer().level.isClientSide();
    }

    public long getGameTime() {
        return getPlayer().level.getGameTime();
    }

}
