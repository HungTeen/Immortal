package hungteen.imm.common.spell.spells;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/28 11:09
 */
public abstract class SpellType implements ISpellType {

    private final String name;
    private final int maxLevel;
    private final int consumeMana;
    private final int cooldown;
    private final boolean canTrigger;
    private final boolean playerOnly;
    private final ResourceLocation resourceLocation;

    public SpellType(String name, SpellProperties properties) {
        this(name, properties.maxLevel, properties.consumeMana, properties.cooldown, properties.canTrigger, properties.playerOnly);
    }

    public SpellType(String name, int maxLevel, int consumeMana, int cooldown, boolean canTrigger, boolean playerOnly) {
        this.name = name;
        this.maxLevel = maxLevel;
        this.consumeMana = consumeMana;
        this.cooldown = cooldown;
        this.canTrigger = canTrigger;
        this.playerOnly = playerOnly;
        this.resourceLocation = Util.get().texture("spell/" + this.name);
    }

    public void sendTip(LivingEntity owner, String msg){
        if(owner instanceof Player player){
            PlayerHelper.sendTipTo(player, TipUtil.info("spell." + msg).withStyle(ChatFormatting.RED));
        }
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }

    @Override
    public int getConsumeMana() {
        return this.consumeMana;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public float getLearnPoint(int level) {
        return 1;
    }

    @Override
    public boolean canTrigger() {
        return canTrigger;
    }

    @Override
    public boolean playerOnly() {
        return playerOnly;
    }

    @Override
    public ResourceLocation getSpellTexture() {
        return resourceLocation;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getModID() {
        return Util.id();
    }

    @Override
    public MutableComponent getSpellDesc(int level) {
        return TipUtil.spell(this, level);
    }

    @Override
    public MutableComponent getComponent() {
        return Util.get().lang("spell", getName());
    }

    public static SpellProperties properties(){
        return new SpellProperties();
    }

    public static class SpellProperties {

        private int maxLevel = 1;
        private int consumeMana = 0;
        private int cooldown = 0;
        private boolean canTrigger = true;
        private boolean playerOnly = false;

        public SpellProperties maxLevel(int maxLevel){
            this.maxLevel = maxLevel;
            return this;
        }

        public SpellProperties mana(int mana){
            this.consumeMana = mana;
            return this;
        }

        public SpellProperties cd(int cd){
            this.cooldown = cd;
            return this;
        }

        public SpellProperties notTrigger(){
            this.canTrigger = false;
            return this;
        }

        public SpellProperties onlyPlayer(){
            this.playerOnly = true;
            return this;
        }

    }
}
