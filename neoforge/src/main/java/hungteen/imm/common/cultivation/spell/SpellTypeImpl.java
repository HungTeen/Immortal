package hungteen.imm.common.cultivation.spell;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.misc.IMMSounds;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import hungteen.imm.util.enums.SpellSortCategories;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/28 11:09
 */
public abstract class SpellTypeImpl implements SpellType {

    private static int id = 0;
    private final String name;
    private final int maxLevel;
    private final int consumeMana;
    private final int cooldown;
    private final boolean canTrigger;
    private final boolean canPlaceOnCircle;
    private final int priority;
    private final SpellUsageCategory category;
    private final ResourceLocation resourceLocation;

    public SpellTypeImpl(String name, SpellProperties properties) {
        this(name, properties.maxLevel, properties.consumeMana, properties.cooldown, properties.canTrigger, properties.canPlaceOnCircle, id ++, properties.usageCategory);
    }

    private SpellTypeImpl(String name, int maxLevel, int consumeMana, int cooldown, boolean canTrigger, boolean canPlaceOnCircle, int priority, SpellUsageCategory category) {
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

    public static void sendTip(LivingEntity owner, String msg){
        if(owner instanceof Player player){
            PlayerHelper.sendTipTo(player, TipUtil.info("spell." + msg).withStyle(ChatFormatting.RED));
        }
    }

    public static void playSound(Entity entity, SoundEvent sound){
        entity.playSound(sound);
    }

    public static void playClientSound(Entity entity, SoundEvent sound){
        if(entity instanceof Player player){
            PlayerHelper.playClientSound(player, sound);
        }
    }

    public static void playSoundAround(Entity entity, SoundEvent sound){
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, entity.getSoundSource(), 1.0F, 1.0F);
    }

    @Override
    public Optional<SoundEvent> getTriggerSound() {
        return Optional.of(IMMSounds.RELEASING.get());
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
    public SpellUsageCategory getCategory() {
        return category;
    }

    @Override
    public ResourceLocation getSpellTexture() {
        return resourceLocation;
    }

    @Override
    public String name() {
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
        return properties(SpellUsageCategory.PLAYER_ONLY);
    }

    public static SpellProperties properties(SpellUsageCategory category){
        return new SpellProperties(category);
    }

    public static class SpellProperties {

        private final SpellUsageCategory usageCategory;
        private int maxLevel = 1;
        private int consumeMana = 0;
        private int cooldown = 0;
        private boolean canTrigger = true;
        private boolean canPlaceOnCircle = true;
        private SpellSortCategories sortCategory = SpellSortCategories.MISC;

        public SpellProperties(SpellUsageCategory category) {
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
