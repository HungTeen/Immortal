package hungteen.immortal.common.capability.player;

import hungteen.htlib.interfaces.IPlayerDataManager;
import hungteen.htlib.interfaces.IRangeData;
import hungteen.htlib.util.Pair;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.ICultivationType;
import hungteen.immortal.api.registry.IRealm;
import hungteen.immortal.api.registry.ISpell;
import hungteen.immortal.api.registry.ISpiritualRoot;
import hungteen.immortal.common.RealmManager;
import hungteen.immortal.common.event.handler.PlayerEventHandler;
import hungteen.immortal.common.network.StringDataPacket;
import hungteen.immortal.impl.CultivationTypes;
import hungteen.immortal.impl.PlayerDatas;
import hungteen.immortal.impl.Realms;
import hungteen.immortal.common.network.IntegerDataPacket;
import hungteen.immortal.common.network.NetworkHandler;
import hungteen.immortal.common.network.SpellPacket;
import hungteen.immortal.utils.Constants;
import hungteen.immortal.utils.PlayerUtil;
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
    private final HashSet<ISpiritualRoot> spiritualRoots = new HashSet<>();
    private final HashMap<ISpell, Integer> learnSpells = new HashMap<>();
    /* Store the end time of specific spells */
    private final HashMap<ISpell, Long> activateSpells = new HashMap<>();
    private ISpell[] spellList = new ISpell[Constants.SPELL_NUMS];
    private int selectedSpellPosition = 0;
    /* Cultivation */
    private ICultivationType cultivationType = CultivationTypes.SPIRITUAL;
    private IRealm realm = Realms.MORTALITY;
    private final HashMap<IRangeData<Integer>, Integer> integerMap = new HashMap<>();

    public PlayerDataManager(Player player){
        this.player = player;
        ImmortalAPI.get().getIntegerCollection().forEach(data -> {
            integerMap.put(data, data.defaultData());
        });
        PlayerUtil.showPlayerSpiritualRoots(player);
    }

    @Override
    public void tick(){
        if(! player.level.isClientSide){
            if(player.level.getGameTime() % Constants.SPIRITUAL_ABSORB_TIME == 0){
                final int value = getIntegerData(PlayerDatas.SPIRITUAL_MANA);
                final int fullValue = getFullManaValue();
                final int limitValue = getLimitManaValue();
                // can natural increasing.
                if(value < fullValue){
                    final int incValue = Math.min(fullValue - value, ImmortalAPI.get().getSpiritualValue(player.level, player.blockPosition()));
                    this.addIntegerData(PlayerDatas.SPIRITUAL_MANA, incValue);
                } else if(value > fullValue){
                    if(value > limitValue){
                        // TODO 爆体而亡 ？
                    } else {
                        final int decValue = Math.min(Math.max(1, (value - fullValue) / 10), value - fullValue);
                        this.addIntegerData(PlayerDatas.SPIRITUAL_MANA, - decValue);
                    }
                }
            }
        }
    }

    @Override
    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        {
            CompoundTag nbt = new CompoundTag();
            for (ISpiritualRoot spiritualRoot : ImmortalAPI.get().getSpiritualRoots()){
                nbt.putBoolean(spiritualRoot.getName() + "_root", spiritualRoots.contains(spiritualRoot));
            }
            tag.put("SpiritualRoots", nbt);
        }
        {
            CompoundTag nbt = new CompoundTag();
            this.learnSpells.forEach((p1, p2) -> {
                nbt.putInt(p1.getName(), p2);
            });
            tag.put("LearnSpells", nbt);
        }
        {
            CompoundTag nbt = new CompoundTag();
            this.activateSpells.forEach((p1, p2) -> {
                nbt.putLong(p1.getName(), p2);
            });
            tag.put("ActivateSpells", nbt);
        }
        {
            CompoundTag nbt = new CompoundTag();
            for(int i = 0; i < Constants.SPELL_NUMS; i++){
                if(spellList[i] != null){
                    nbt.putInt(spellList[i].getRegistryName(), i);
                }
            }
            nbt.putInt("SelectedSpellPosition", this.selectedSpellPosition);
            tag.put("SpellList", nbt);
        }
        {
            CompoundTag nbt = new CompoundTag();
            ImmortalAPI.get().getIntegerCollection().forEach(data -> {
                nbt.putInt(data.getRegistryName(), integerMap.getOrDefault(data, data.defaultData()));
            });
            tag.put("PlayerRangeData", nbt);
        }
        {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("PlayerRealm", realm.getRegistryName());
            tag.put("Misc", nbt);
        }
        return tag;
    }

    @Override
    public void loadFromNBT(CompoundTag tag) {
        {
            spiritualRoots.clear();
            CompoundTag nbt = tag.getCompound("SpiritualRoots");
            for (ISpiritualRoot spiritualRoot : ImmortalAPI.get().getSpiritualRoots()){
                if(nbt.getBoolean(spiritualRoot.getRegistryName() + "_root")){
                    spiritualRoots.add(spiritualRoot);
                }
            }
        }
        {
            learnSpells.clear();
            CompoundTag nbt = tag.getCompound("LearnSpells");
            for (ISpell spell : ImmortalAPI.get().getSpells()){
                if(nbt.contains(spell.getRegistryName())){
                    learnSpells.put(spell, nbt.getInt(spell.getRegistryName()));
                }
            }
        }
        {
            activateSpells.clear();
            CompoundTag nbt = tag.getCompound("ActivateSpells");
            for (ISpell spell : ImmortalAPI.get().getSpells()){
                if(nbt.contains(spell.getRegistryName())){
                    activateSpells.put(spell, nbt.getLong(spell.getRegistryName()));
                }
            }
        }
        {
            CompoundTag nbt = tag.getCompound("SpellList");
            for (ISpell spell : ImmortalAPI.get().getSpells()){
                if(nbt.contains(spell.getRegistryName())){
                    spellList[nbt.getInt(spell.getRegistryName())] = spell;
                }
            }
            this.selectedSpellPosition = nbt.getInt("SelectedSpellPosition");
        }
        {
            CompoundTag nbt = tag.getCompound("PlayerRangeData");
            ImmortalAPI.get().getIntegerCollection().forEach(data -> {
                if(nbt.contains(data.getRegistryName())){
                    integerMap.put(data, nbt.getInt(data.getRegistryName()));
                }
            });
        }
        {
            CompoundTag nbt = tag.getCompound("Misc");
            if(nbt.contains("PlayerRealm")){
                realm = ImmortalAPI.get().getRealm(nbt.getString("PlayerRealm")).orElse(Realms.MORTALITY);
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
     * run when player join world.
     * {@link PlayerEventHandler#onPlayerLogin(Player)}
     */
    public void init() {
        this.syncBounds();
        this.syncToClient();
    }

    /**
     * avoid render out of bound.
     * {@link #init()}
     */
    private void syncBounds() {
//        {// player resources.
//            for(Resources res : Resources.values()) {
//                this.addResource(res, 0);
//            }
//        }
    }

    /**
     * sync data to client side.
     * {@link #init()}
     */
    @Override
    public void syncToClient() {
        this.learnSpells.forEach((spell, level) -> {
            this.sendSpellPacket(SpellPacket.SpellOptions.LEARN, spell, level);
        });
        for(int i = 0; i < Constants.SPELL_NUMS; i++) {
            if(this.spellList[i] != null) {
                this.sendSpellPacket(SpellPacket.SpellOptions.SET, this.spellList[i], i);
            }
        }
        this.activateSpells.forEach((spell, time) -> {
            this.sendSpellPacket(SpellPacket.SpellOptions.ACTIVATE, spell, time);
        });
        this.sendSpellPacket(SpellPacket.SpellOptions.SELECT, null, this.selectedSpellPosition);
        ImmortalAPI.get().getIntegerCollection().forEach(data -> {
            this.sendIntegerDataPacket(data, this.getIntegerData(data));
        });
    }

    /* SpiritualRoots related methods */

    public void addSpiritualRoot(ISpiritualRoot spiritualRoot) {
        this.spiritualRoots.add(spiritualRoot);
    }

    public void removeSpiritualRoot(ISpiritualRoot spiritualRoot) {
        this.spiritualRoots.remove(spiritualRoot);
    }

    public void clearSpiritualRoot() {
        this.spiritualRoots.clear();
    }

    public boolean hasSpiritualRoot(ISpiritualRoot spiritualRoot) {
        return this.spiritualRoots.contains(spiritualRoot);
    }

    public int getSpiritualRootCount() {
        return this.spiritualRoots.size();
    }

    public List<ISpiritualRoot> getSpiritualRoots() {
        return Collections.unmodifiableList(this.spiritualRoots.stream().toList());
    }

    /* Spell related methods */

    public void learnSpell(ISpell spell, int level) {
        this.learnSpells.put(spell, Mth.clamp(level, 0, spell.getMaxLevel()));
        this.activateSpells.put(spell, getPlayer().level.getGameTime());
        this.sendSpellPacket(SpellPacket.SpellOptions.LEARN, spell, level);
    }

    public void forgetSpell(ISpell spell) {
        this.learnSpells.put(spell, 0);
        this.sendSpellPacket(SpellPacket.SpellOptions.FORGET, spell, 0);
    }

    public void learnAllSpells() {
        this.learnSpells.forEach((spell, time) -> learnSpell(spell, spell.getMaxLevel()));
    }

    public void forgetAllSpells() {
        this.learnSpells.forEach((spell, time) -> forgetSpell(spell));
    }

    public void setSpellList(int pos, ISpell spell){
        this.spellList[pos] = spell;
        this.sendSpellPacket(SpellPacket.SpellOptions.SET, spell, pos);
    }

    public void removeSpellList(int pos, ISpell spell){
        this.spellList[pos] = null;
        this.sendSpellPacket(SpellPacket.SpellOptions.REMOVE, spell, pos);
    }

    public void activateSpell(@NotNull ISpell spell, long num){
        this.activateSpells.put(spell, num);
        this.sendSpellPacket(SpellPacket.SpellOptions.ACTIVATE, spell, num);
    }

    public void selectSpell(long num){
        this.selectedSpellPosition = Mth.clamp((int) num, 0, Constants.SPELL_NUM_EACH_PAGE - 1);
        this.sendSpellPacket(SpellPacket.SpellOptions.SELECT, null, num);
    }

    public ISpell getSpellAt(int num){
        return this.spellList[Mth.clamp(num, 0, Constants.SPELL_NUM_EACH_PAGE - 1)];
    }

    public void nextSpell(long num){
        this.selectedSpellPosition = (int) (this.selectedSpellPosition + num + Constants.SPELL_NUM_EACH_PAGE) % Constants.SPELL_NUM_EACH_PAGE;
        this.sendSpellPacket(SpellPacket.SpellOptions.NEXT, null, num);
    }

    public boolean isSpellActivated(@NotNull ISpell spell){
        return this.activateSpells.containsKey(spell) && this.activateSpells.get(spell) > getGameTime();
    }

    public double getSpellCDValue(@NotNull ISpell spell){
        return isSpellActivated(spell) ? (this.activateSpells.get(spell) - getGameTime()) * 1.0 / spell.getDuration() : 0;
    }

    public boolean learnedSpell(@NotNull ISpell spell, int level){
        return this.learnSpells.containsKey(spell) && this.learnSpells.get(spell) >= level;
    }

    public int getSpellLearnLevel(@NotNull ISpell spell){
        return this.learnSpells.getOrDefault(spell, 0);
    }

    public int getSelectedSpellPosition(){
        return this.selectedSpellPosition;
    }

    @Nullable
    public ISpell getSelectedSpell(){
        return this.spellList[this.selectedSpellPosition];
    }

    public void sendSpellPacket(SpellPacket.SpellOptions option, @Nullable ISpell spell, long num) {
        if (getPlayer() instanceof ServerPlayer) {
            NetworkHandler.sendToClient((ServerPlayer) getPlayer(), new SpellPacket(spell, option, num));
        }
    }

    /* Integer related methods */

    public int getIntegerData(IRangeData<Integer> rangeData){
        return integerMap.getOrDefault(rangeData, rangeData.defaultData());
    }

    public void setIntegerData(IRangeData<Integer> rangeData, int value){
        int result = Mth.clamp(value, rangeData.getMinData(), rangeData.getMaxData());
        if(PlayerDatas.CULTIVATION.equals(rangeData)){
            Pair<IRealm, Integer> pair = RealmManager.updateRealm(getCultivationType(), getRealm(), value);
            result = pair.getSecond();
            if(getRealm() != pair.getFirst()){
                this.setRealm(pair.getFirst());
            }
            final int mana = getIntegerData(PlayerDatas.SPIRITUAL_MANA);
            if(mana > getLimitManaValue()){
                this.setIntegerData(PlayerDatas.SPIRITUAL_MANA, getLimitManaValue());
            }
        }
        integerMap.put(rangeData, result);
        sendIntegerDataPacket(rangeData, result);
    }

    public void addIntegerData(IRangeData<Integer> rangeData, int value){
        setIntegerData(rangeData, getIntegerData(rangeData) + value);
    }

    /**
     * natural increasing point.
     */
    public int getFullManaValue(){
        return Mth.clamp(getIntegerData(PlayerDatas.MAX_SPIRITUAL_MANA) + getRealm().getBaseSpiritualValue(), 0, getIntegerData(PlayerDatas.CULTIVATION));
    }

    /**
     * explode if player has more mana than it.
     */
    public int getLimitManaValue(){
        return getIntegerData(PlayerDatas.CULTIVATION);
    }

    public void sendIntegerDataPacket(IRangeData<Integer> rangeData, int value) {
        if (getPlayer() instanceof ServerPlayer) {
            NetworkHandler.sendToClient((ServerPlayer) getPlayer(), new IntegerDataPacket(rangeData, value));
        }
    }

    /* Misc methods */

    public void setRealm(IRealm realm) {
        this.realm = realm;
        this.sendStringDataPacket(StringDataPacket.Types.REALM, realm.getRegistryName());
    }

    public IRealm getRealm() {
        return this.realm;
    }

    public ICultivationType getCultivationType() {
        return this.cultivationType;
    }

    public void sendStringDataPacket(StringDataPacket.Types type, String data) {
        if (getPlayer() instanceof ServerPlayer) {
            NetworkHandler.sendToClient((ServerPlayer) getPlayer(), new StringDataPacket(type, data));
        }
    }

    @Override
    public Player getPlayer(){
        return this.player;
    }

    public long getGameTime(){
        return getPlayer().level.getGameTime();
    }

}
