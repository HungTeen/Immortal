package hungteen.immortal.common.capability.player;

import hungteen.htlib.api.interfaces.IPlayerDataManager;
import hungteen.htlib.api.interfaces.IRangeNumber;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.IRealmType;
import hungteen.immortal.api.registry.ISpellType;
import hungteen.immortal.api.registry.ISpiritualType;
import hungteen.immortal.common.RealmManager;
import hungteen.immortal.common.impl.registry.SpellTypes;
import hungteen.immortal.common.network.IntegerDataPacket;
import hungteen.immortal.common.network.NetworkHandler;
import hungteen.immortal.common.network.SpellPacket;
import hungteen.immortal.common.network.StringDataPacket;
import hungteen.immortal.common.impl.PlayerRangeNumbers;
import hungteen.immortal.common.impl.RealmTypes;
import hungteen.immortal.utils.Constants;
import hungteen.immortal.utils.PlayerUtil;
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
    /* Store the end time of specific spells */
    private final HashMap<ISpellType, Long> activateSpells = new HashMap<>();
    private final HashMap<ISpellType, Long> spellCDs = new HashMap<>();
    private final ISpellType[] spellList = new ISpellType[Constants.SPELL_NUMS];
    private int selectedSpellPosition = 0;
    /* Cultivation */
    private IRealmType realmType = RealmTypes.MORTALITY;
    /* Misc sync integers */
    private final HashMap<IRangeNumber<Integer>, Integer> integerMap = new HashMap<>();
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
            this.activateSpells.forEach((spell, time) -> {
                nbt.putLong("activate_" + spell.getRegistryName(), time);
            });
            this.spellCDs.forEach((spell, time) -> {
                nbt.putLong("cooldown_" + spell.getRegistryName(), time);
            });
            tag.put("Spells", nbt);
        }
        {
            CompoundTag nbt = new CompoundTag();
            for (int i = 0; i < Constants.SPELL_NUMS; i++) {
                if (spellList[i] != null) {
                    nbt.putInt(spellList[i].getRegistryName(), i);
                }
            }
            nbt.putInt("SelectedSpellPosition", this.selectedSpellPosition);
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
            activateSpells.clear();
            spellCDs.clear();
            CompoundTag nbt = tag.getCompound("Spells");
            SpellTypes.registry().getValues().forEach(type -> {
                if (nbt.contains("learn_" + type.getRegistryName())) {
                    learnSpells.put(type, nbt.getInt("learn_" + type.getRegistryName()));
                }
                if (nbt.contains("activate_" + type.getRegistryName())) {
                    activateSpells.put(type, nbt.getLong("activate_" + type.getRegistryName()));
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
                    if (nbt.contains(type.getRegistryName())) {
                        if (nbt.contains(type.getRegistryName())) {
                            spellList[nbt.getInt(type.getRegistryName())] = type;
                        }
                    }
                });
            });
            if (nbt.contains("SelectedSpellPosition")) {
                this.selectedSpellPosition = nbt.getInt("SelectedSpellPosition");
            }
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
        for (int i = 0; i < Constants.SPELL_NUMS; i++) {
            if (this.spellList[i] != null) {
                this.sendSpellPacket(SpellPacket.SpellOptions.SET, this.spellList[i], i);
            }
        }
        this.activateSpells.forEach((spell, time) -> {
            this.sendSpellPacket(SpellPacket.SpellOptions.ACTIVATE, spell, time);
        });
        this.sendSpellPacket(SpellPacket.SpellOptions.SELECT, null, this.selectedSpellPosition);
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
        this.sendSpellPacket(SpellPacket.SpellOptions.SET, spell, pos);
    }

    public void removeSpellList(int pos, ISpellType spell) {
        this.spellList[pos] = null;
        this.sendSpellPacket(SpellPacket.SpellOptions.REMOVE, spell, pos);
    }

    public void activateSpell(@NotNull ISpellType spell, long num) {
        this.activateSpells.put(spell, num);
        this.sendSpellPacket(SpellPacket.SpellOptions.ACTIVATE, spell, num);
    }

    public void cooldownSpell(@NotNull ISpellType spell, long num) {
        this.spellCDs.put(spell, num);
        this.sendSpellPacket(SpellPacket.SpellOptions.COOLDOWN, spell, num);
    }

    public void selectSpell(long num) {
        this.selectedSpellPosition = Mth.clamp((int) num, 0, Constants.SPELL_NUM_EACH_PAGE - 1);
        this.sendSpellPacket(SpellPacket.SpellOptions.SELECT, null, num);
    }

    public ISpellType getSpellAt(int num) {
        return this.spellList[Mth.clamp(num, 0, Constants.SPELL_NUM_EACH_PAGE - 1)];
    }

    public boolean isSpellActivated(@NotNull ISpellType spell) {
        return this.activateSpells.containsKey(spell) && this.activateSpells.get(spell) > getGameTime();
    }

    public boolean isSpellOnCoolDown(@NotNull ISpellType spell) {
        return this.spellCDs.containsKey(spell) && this.spellCDs.get(spell) > getGameTime();
    }

    /**
     * 法术触发时哪怕没有冷却，也会有冷却。
     * @return cool down is the minimum value of spell duration and cool down.
     */
    public boolean activatedOrCooldown(@NotNull ISpellType spell) {
        return this.isSpellActivated(spell) || this.isSpellOnCoolDown(spell);
    }

    public double getSpellCDValue(@NotNull ISpellType spell) {
        return isSpellOnCoolDown(spell) ? (this.spellCDs.get(spell) - getGameTime()) * 1.0 / spell.getCooldown()
                : isSpellActivated(spell) ? (this.activateSpells.get(spell) - getGameTime()) * 1.0 / spell.getDuration() : 0;
    }

    public boolean hasLearnedSpell(@NotNull ISpellType spell, int level) {
        return getSpellLevel(spell) >= level;
    }

    public int getSpellLevel(@NotNull ISpellType spell) {
        return this.learnSpells.getOrDefault(spell, 0);
    }

    public int getSelectedSpellPosition() {
        return this.selectedSpellPosition;
    }

    @Nullable
    public ISpellType getSelectedSpell() {
        return this.spellList[this.selectedSpellPosition];
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
     *
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
     *
     * @return Natural increasing point.
     */
    public int getFullManaValue() {
        return Mth.clamp(getIntegerData(PlayerRangeNumbers.MAX_SPIRITUAL_MANA) + getRealmType().getBaseSpiritualValue(), 0, getRealmType().getSpiritualValueLimit());
    }

    /**
     * 第二层溢出条的极限。
     *
     * @return Explode if player has more mana than it.
     */
    public int getLimitManaValue() {
        return getRealmType().getSpiritualValueLimit();
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

    public long getGameTime() {
        return getPlayer().level.getGameTime();
    }

}
