package hungteen.imm.common.spell.spells;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.enums.SpellUsageCategories;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import hungteen.imm.util.enums.SpellSortCategories;
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

    private static int id = 0;
    private final String name;
    private final int maxLevel;
    private final int consumeMana;
    private final int cooldown;
    private final boolean canTrigger;
    private final boolean canPlaceOnCircle;
    private final int priority;
    private final SpellUsageCategories category;
    private final ResourceLocation resourceLocation;

    public SpellType(String name, SpellProperties properties) {
        this(name, properties.maxLevel, properties.consumeMana, properties.cooldown, properties.canTrigger, properties.canPlaceOnCircle, id ++, properties.usageCategory);
    }

    private SpellType(String name, int maxLevel, int consumeMana, int cooldown, boolean canTrigger, boolean canPlaceOnCircle, int priority, SpellUsageCategories category) {
        this.name = name;
        this.maxLevel = maxLevel;
        this.consumeMana = consumeMana;
        this.cooldown = cooldown;
        this.canTrigger = canTrigger;
        this.canPlaceOnCircle = canPlaceOnCircle;
        this.priority = priority;
        this.category = category;
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
    public boolean canPlaceOnCircle() {
        return canPlaceOnCircle;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int getModPriority() {
        return 100;
    }

    @Override
    public SpellUsageCategories getCategory() {
        return category;
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
        return properties(SpellUsageCategories.PLAYER_ONLY);
    }

    public static SpellProperties properties(SpellUsageCategories category){
        return new SpellProperties(category);
    }

    public static class SpellProperties {

        private final SpellUsageCategories usageCategory;
        private int maxLevel = 1;
        private int consumeMana = 0;
        private int cooldown = 0;
        private boolean canTrigger = true;
        private boolean canPlaceOnCircle = true;
        private SpellSortCategories sortCategory = SpellSortCategories.MISC;

        public SpellProperties(SpellUsageCategories category) {
            this.usageCategory = category;
        }

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

        public SpellProperties notOnCircle(){
            this.canPlaceOnCircle = false;
            return this.notTrigger();
        }

    }
}
