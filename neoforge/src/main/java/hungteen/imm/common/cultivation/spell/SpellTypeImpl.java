package hungteen.imm.common.cultivation.spell;

import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.spell.InscriptionType;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.api.spell.SpellUsageCategory;
import hungteen.imm.common.IMMSounds;
import hungteen.imm.common.cultivation.InscriptionTypes;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import hungteen.imm.util.enums.SpellSortCategory;
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

    public static final String NO_EMPTY_HAND = "no_empty_hand";
    public static final String NO_TARGET = "no_target";
    public static final String TARGET_TOO_FAR_AWAY = "target_too_far_away";
    public static final String ONLY_VALID_FOR_LOW_REALM = "only_valid_for_low_realm";
    public static final String NO_ITEM_IN_HANDS = "no_item_in_hands";
    private static int id = 0;
    private final String name;
    private final int maxLevel;
    private final int consumeMana;
    private final int cooldown;
    private final boolean canTrigger;
    private final int priority;
    private final SpellUsageCategory category;
    private final InscriptionType inscriptionType;
    private final ResourceLocation resourceLocation;

    public SpellTypeImpl(String name, SpellProperty property) {
        this(name, property.maxLevel, property.consumeMana, property.cooldown, property.canTrigger, id ++, property.usageCategory, property.inscriptionType);
    }

    private SpellTypeImpl(String name, int maxLevel, int consumeMana, int cooldown, boolean canTrigger, int priority, SpellUsageCategory category, InscriptionType inscriptionType) {
        this.name = name;
        this.maxLevel = maxLevel;
        this.consumeMana = consumeMana;
        this.cooldown = cooldown;
        this.canTrigger = canTrigger;
        this.priority = priority;
        this.category = category;
        this.inscriptionType = inscriptionType;
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
    public int getConsumeQi() {
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
    public int getScreenPriority() {
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
    public InscriptionType getInscriptionType(int level) {
        return inscriptionType;
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
        return TipUtil.spell(name());
    }

    public static SpellProperty property(){
        return property(SpellUsageCategory.PLAYER_ONLY);
    }

    public static SpellProperty property(SpellUsageCategory category){
        return property(category, InscriptionTypes.NONE);
    }

    public static SpellProperty property(SpellUsageCategory category, InscriptionType inscriptionType){
        return new SpellProperty(category, inscriptionType);
    }

    public static class SpellProperty {

        private final SpellUsageCategory usageCategory;
        private final InscriptionType inscriptionType;
        private int maxLevel = 1;
        private int consumeMana = 0;
        private int cooldown = 0;
        private boolean canTrigger = true;
        private SpellSortCategory sortCategory = SpellSortCategory.MISC;

        public SpellProperty(SpellUsageCategory category, InscriptionType inscriptionType) {
            this.usageCategory = category;
            this.inscriptionType = inscriptionType;
        }

        public SpellProperty maxLevel(int maxLevel){
            this.maxLevel = maxLevel;
            return this;
        }

        public SpellProperty mana(int mana){
            this.consumeMana = mana;
            return this;
        }

        public SpellProperty cd(int cd){
            this.cooldown = cd;
            return this;
        }

        public SpellProperty notTrigger(){
            this.canTrigger = false;
            return this;
        }

        public SpellProperty sortCategory(SpellSortCategory sortCategory) {
            this.sortCategory = sortCategory;
            return this;
        }

    }
}
