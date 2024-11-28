package hungteen.imm.common.capability.player;

import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.common.advancement.trigger.PlayerLearnSpellTrigger;
import hungteen.imm.common.advancement.trigger.PlayerLearnSpellsTrigger;
import hungteen.imm.common.capability.HTPlayerData;
import hungteen.imm.common.cultivation.SpellTypes;
import hungteen.imm.common.network.client.ClientSpellPacket;
import hungteen.imm.util.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/21 21:01
 **/
public class SpellData implements HTPlayerData {

    private final IMMPlayerData playerData;
    private final HashMap<SpellType, Integer> learnSpells = new HashMap<>();
    private final HashMap<SpellType, Long> spellCDs = new HashMap<>();
    private final SpellType[] spellList = new SpellType[Constants.SPELL_CIRCLE_SIZE];
    private SpellType preparingSpell = null;

    public SpellData(IMMPlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
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
        if (this.preparingSpell != null) {
            tag.putString("PreparingSpell", this.preparingSpell.getRegistryName());
        }
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
        if (tag.contains("PreparingSpell")) {
            SpellTypes.registry().getValue(tag.getString("PreparingSpell")).ifPresent(l -> this.preparingSpell = l);
        }
    }

    @Override
    public void initialize() {

    }

    @Override
    public void syncToClient() {
        this.learnSpells.forEach((spell, level) -> {
            this.sendSpellPacket(ClientSpellPacket.SpellOption.LEARN, spell, level);
        });
        for (int i = 0; i < Constants.SPELL_CIRCLE_SIZE; i++) {
            if (this.spellList[i] != null) {
                this.sendSpellPacket(ClientSpellPacket.SpellOption.SET_SPELL_ON_CIRCLE, this.spellList[i], i);
            }
        }
        if (this.preparingSpell != null) {
            this.setPreparingSpell(this.preparingSpell);
        }
    }

    /**
     * @param spell 要学习的法术。
     * @param level 学习的等级，0 则表示遗忘。
     */
    public void learnSpell(SpellType spell, int level) {
        final int lvl = Mth.clamp(level, 0, spell.getMaxLevel());
        if(lvl == 0){
            this.learnSpells.remove(spell);
        } else {
            this.learnSpells.put(spell, lvl);
        }
        if (playerData.getPlayer() instanceof ServerPlayer serverPlayer) {
            PlayerLearnSpellTrigger.INSTANCE.trigger(serverPlayer, spell, level);
            PlayerLearnSpellsTrigger.INSTANCE.trigger(serverPlayer, (int) this.learnSpells.values().stream().filter(l -> l > 0).count());
        }
        this.sendSpellPacket(ClientSpellPacket.SpellOption.LEARN, spell, lvl);
    }

    public void forgetSpell(SpellType spell) {
        this.learnSpell(spell, 0);
    }

    public void setSpellAt(int pos, SpellType spell) {
        this.spellList[pos] = spell;
        this.sendSpellPacket(ClientSpellPacket.SpellOption.SET_SPELL_ON_CIRCLE, spell, pos);
    }

    public void removeSpellAt(int pos) {
        this.spellList[pos] = null;
        this.sendSpellPacket(ClientSpellPacket.SpellOption.REMOVE_SPELL_ON_CIRCLE, SpellTypes.MEDITATION, pos);
    }

    public SpellType getSpellAt(int pos) {
        return this.spellList[Mth.clamp(pos, 0, Constants.SPELL_CIRCLE_SIZE - 1)];
    }

    public boolean isSpellOnCircle(SpellType spell) {
        for (SpellType spellType : this.spellList) {
            if (spell == spellType) {
                return true;
            }
        }
        return false;
    }

    public void cooldownSpell(@NotNull SpellType spell, long num) {
        this.spellCDs.put(spell, num);
        this.sendSpellPacket(ClientSpellPacket.SpellOption.COOL_DOWN, spell, num);
    }

    public boolean isSpellOnCoolDown(@NotNull SpellType spell) {
        return this.spellCDs.containsKey(spell) && this.spellCDs.get(spell) > playerData.getGameTime();
    }

    public float getSpellCoolDown(@NotNull SpellType spell) {
        return isSpellOnCoolDown(spell) ? (this.spellCDs.get(spell) - playerData.getGameTime()) * 1.0F / spell.getCooldown() : 0;
    }

    public boolean hasLearnedSpell(@NotNull SpellType spell, int level) {
        return getSpellLevel(spell) >= level;
    }

    public int getSpellLevel(@NotNull SpellType spell) {
        return this.learnSpells.getOrDefault(spell, 0);
    }

    public void sendSpellPacket(ClientSpellPacket.SpellOption option, SpellType spell, long num) {
        if (playerData.getPlayer() instanceof ServerPlayer serverPlayer) {
            ClientSpellPacket packet = spell == null ? new ClientSpellPacket(option) : new ClientSpellPacket(spell, option, num);
            NetworkHelper.sendToClient(serverPlayer, packet);
        }
    }

    @Nullable
    public SpellType getPreparingSpell() {
        return preparingSpell;
    }

    public void setPreparingSpell(@Nullable SpellType spell) {
        this.preparingSpell = spell;
        if (spell != null) {
            this.sendSpellPacket(ClientSpellPacket.SpellOption.PREPARING_SPELL, spell, 0);
        } else {
            this.sendSpellPacket(ClientSpellPacket.SpellOption.CLEAR_PREPARING_SPELL, null, 0);
        }
    }

    @Override
    public boolean isServer() {
        return playerData.isServer();
    }
}
