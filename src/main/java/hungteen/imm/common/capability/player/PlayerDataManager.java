package hungteen.imm.common.capability.player;

import hungteen.htlib.api.interfaces.IPlayerDataManager;
import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.imm.api.ImmortalAPI;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.RealmManager;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.common.network.IntegerDataPacket;
import hungteen.imm.common.network.NetworkHandler;
import hungteen.imm.common.network.SpellPacket;
import hungteen.imm.common.network.StringDataPacket;
import hungteen.imm.common.impl.registry.PlayerRangeNumbers;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.utils.Constants;
import hungteen.imm.utils.PlayerUtil;
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
    private IRealmType realmType = RealmTypes.MORTALITY;
    private final HashMap<IRangeNumber<Integer>, Integer> integerMap = new HashMap<>(); // misc sync integers.
    /* Caches */
    private RealmManager.RealmNode realmNode;

    public PlayerDataManager(Player player) {
        this.player = player;
        if (ImmortalAPI.get().integerDataRegistry().isPresent()) {
            ImmortalAPI.get().integerDataRegistry().get().getValues().forEach(data -> {
                integerMap.put(data, data.defaultData());
            });
        }
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
        }
    }

    @Override
    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        {
            CompoundTag nbt = new CompoundTag();
            ImmortalAPI.get().spiritualRegistry().ifPresent(l -> {
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
            ImmortalAPI.get().integerDataRegistry().ifPresent(l -> {
                l.getValues().forEach(data -> {
                    nbt.putInt(data.getRegistryName(), integerMap.getOrDefault(data, data.defaultData()));
                });
            });
            tag.put("PlayerRangeData", nbt);
        }
        {
            final CompoundTag nbt = new CompoundTag();
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
            ImmortalAPI.get().spiritualRegistry().ifPresent(l -> {
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
            CompoundTag nbt = tag.getCompound("Spells");
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
            CompoundTag nbt = tag.getCompound("SpellList");
            ImmortalAPI.get().spellRegistry().ifPresent(l -> {
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
        if (tag.contains("PlayerRangeData")) {
            integerMap.clear();
            CompoundTag nbt = tag.getCompound("PlayerRangeData");
            ImmortalAPI.get().integerDataRegistry().ifPresent(l -> {
                l.getValues().forEach(type -> {
                    if (nbt.contains(type.getRegistryName())) {
                        integerMap.put(type, nbt.getInt(type.getRegistryName()));
                    }
                });
            });
        }
        if (tag.contains("MiscData")) {
            CompoundTag nbt = tag.getCompound("MiscData");
            if (nbt.contains("PlayerRealmType")) {
                ImmortalAPI.get().realmRegistry().flatMap(l -> l.getValue(nbt.getString("PlayerRealmType"))).ifPresent(r -> this.realmType = r);
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

    /* Integer related methods */

    public int getIntegerData(IRangeNumber<Integer> rangeData) {
        return integerMap.getOrDefault(rangeData, rangeData.defaultData());
    }

    public void setIntegerData(IRangeNumber<Integer> rangeData, int value) {
        setIntegerData(rangeData, value, false);
    }

    /**
     * Directly set value.
     * @param ignore 防止死循环。
     */
    public void setIntegerData(IRangeNumber<Integer> rangeData, int value, boolean ignore) {
        int result = Mth.clamp(value, rangeData.getMinData(), rangeData.getMaxData());
        if (!ignore && PlayerRangeNumbers.CULTIVATION.equals(rangeData)) {
            updateCultivation(value - getIntegerData(PlayerRangeNumbers.CULTIVATION));
        } else {
            integerMap.put(rangeData, result);
            sendIntegerDataPacket(rangeData, result);
        }
    }

    public void addIntegerData(IRangeNumber<Integer> rangeData, int value) {
        if (PlayerRangeNumbers.CULTIVATION.equals(rangeData)) {
            updateCultivation(value);
        } else {
            setIntegerData(rangeData, getIntegerData(rangeData) + value);
        }
    }

    protected void updateCultivation(int value) {
        int oldValue = getIntegerData(PlayerRangeNumbers.CULTIVATION);
        if (value < 0) {
            while (oldValue + value < 0 && getRealmNode().hasPreviousNode()) {
                value += oldValue;
                levelDown();
                oldValue = getRealmType().requireCultivation(); // 上一级所需的修为。
            }
            this.setIntegerData(PlayerRangeNumbers.CULTIVATION, oldValue + value, true);
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
            this.setIntegerData(PlayerRangeNumbers.CULTIVATION, oldValue + value, true);
        }
        if (getIntegerData(PlayerRangeNumbers.SPIRITUAL_MANA) > getLimitManaValue()) {
            this.setIntegerData(PlayerRangeNumbers.SPIRITUAL_MANA, getLimitManaValue());
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
    public int getFullManaValue() {
        return Mth.clamp(getIntegerData(PlayerRangeNumbers.MAX_SPIRITUAL_MANA) + getRealmType().getBaseSpiritualValue(), 0, getRealmType().getSpiritualValueLimit());
    }

    /**
     * 第二层溢出条的极限。
     * @return Explode if player has more mana than it.
     */
    public int getLimitManaValue() {
        return getRealmType().getSpiritualValueLimit();
    }

    public int getSpellSetLimit(){
        return getIntegerData(PlayerRangeNumbers.PASSIVE_SPELL_COUNT_LIMIT);
    }

    /**
     * 待客户端配置文件更新该值。
     */
    public boolean requireSyncCircle(){
        return getIntegerData(PlayerRangeNumbers.DEFAULT_SPELL_CIRCLE) == 0;
    }

    public boolean useDefaultCircle(){
        return getIntegerData(PlayerRangeNumbers.DEFAULT_SPELL_CIRCLE) == 1;
    }

    public void sendIntegerDataPacket(IRangeNumber<Integer> rangeData, int value) {
        if (getPlayer() instanceof ServerPlayer) {
            NetworkHandler.sendToClient((ServerPlayer) getPlayer(), new IntegerDataPacket(rangeData, value));
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
