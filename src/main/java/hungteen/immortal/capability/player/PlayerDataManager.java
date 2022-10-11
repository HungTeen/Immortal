package hungteen.immortal.capability.player;

import hungteen.htlib.util.Pair;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.ISpell;
import hungteen.immortal.api.interfaces.ISpiritualRoot;
import hungteen.immortal.event.handler.PlayerEventHandler;
import hungteen.immortal.network.NetworkHandler;
import hungteen.immortal.network.SpellPacket;
import hungteen.immortal.utils.Constants;
import hungteen.immortal.utils.PlayerUtil;
import net.minecraft.client.renderer.EffectInstance;
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
public class PlayerDataManager {

    private final Player player;
    private final HashSet<ISpiritualRoot> spiritualRoots = new HashSet<>();
    private final HashMap<ISpell, Integer> learnSpells = new HashMap<>();
    /* Store the end time of specific spells */
    private final HashMap<ISpell, Long> activateSpells = new HashMap<>();
    private ISpell[] spellList = new ISpell[Constants.SPELL_NUMS];
    private int selectedSpellPosition = 0;


    public PlayerDataManager(Player player){
        this.player = player;
        PlayerUtil.showPlayerSpiritualRoots(player);
    }

    public void tick(){
    }

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
                    nbt.putInt(spellList[i].getName(), i);
                }
            }
            nbt.putInt("SelectedSpellPosition", this.selectedSpellPosition);
            tag.put("SpellList", nbt);
        }

        return tag;
    }

    public void loadFromNBT(CompoundTag tag) {
        {
            spiritualRoots.clear();
            CompoundTag nbt = tag.getCompound("SpiritualRoots");
            for (ISpiritualRoot spiritualRoot : ImmortalAPI.get().getSpiritualRoots()){
                if(nbt.getBoolean(spiritualRoot.getName() + "_root")){
                    spiritualRoots.add(spiritualRoot);
                }
            }
        }
        {
            learnSpells.clear();
            CompoundTag nbt = tag.getCompound("LearnSpells");
            for (ISpell spell : ImmortalAPI.get().getSpells()){
                if(nbt.contains(spell.getName())){
                    learnSpells.put(spell, nbt.getInt(spell.getName()));
                }
            }
        }
        {
            activateSpells.clear();
            CompoundTag nbt = tag.getCompound("ActivateSpells");
            for (ISpell spell : ImmortalAPI.get().getSpells()){
                if(nbt.contains(spell.getName())){
                    activateSpells.put(spell, nbt.getLong(spell.getName()));
                }
            }
        }
        {
            CompoundTag nbt = tag.getCompound("SpellList");
            for (ISpell spell : ImmortalAPI.get().getSpells()){
                if(nbt.contains(spell.getName())){
                    spellList[nbt.getInt(spell.getName())] = spell;
                }
            }
            this.selectedSpellPosition = nbt.getInt("SelectedSpellPosition");
        }
    }

    /**
     * copy player data when clone event happen.
     */
    public void cloneFromExistingPlayerData(PlayerDataManager data, boolean died) {
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
    public void syncToClient() {
//        {// player resources.
//            for(Resources res : Resources.values()) {
//                this.sendResourcePacket(player, res);
//            }
//        }
        {
            this.learnSpells.forEach((spell, time) -> this.activateSpell(spell, time));
            for(int i = 0; i < Constants.SPELL_NUMS; i++) {
                if(this.spellList[i] != null) {
                    this.setSpellList(i, this.spellList[i]);
                }
            }
        }
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

    public void activateSpellAt(){
        Optional.ofNullable(this.spellList[this.selectedSpellPosition]).ifPresent(spell -> this.activateSpell(spell, getGameTime() + spell.getDuration()));
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

    public boolean learnedSpell(@NotNull ISpell spell, int level){
        return this.learnSpells.containsKey(spell) && this.learnSpells.get(spell) >= level;
    }

    public int getSelectedSpellPosition(){
        return this.selectedSpellPosition;
    }

    public void sendSpellPacket(SpellPacket.SpellOptions option, @Nullable ISpell spell, long num) {
        if (getPlayer() instanceof ServerPlayer) {
            NetworkHandler.sendToClient((ServerPlayer) getPlayer(), new SpellPacket(spell, option, num));
        }
    }


    public Player getPlayer(){
        return this.player;
    }

    public long getGameTime(){
        return getPlayer().level.getGameTime();
    }

}
