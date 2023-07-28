package hungteen.imm.common.spell.spells;

import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/28 11:09
 */
public abstract class SpellType implements ISpellType {

    private final String name;
    private final int maxLevel;
    private final int consumeMana;
    private final int prepareCD;
    private final int cooldown;
    private final boolean canTrigger;
    private final ResourceLocation resourceLocation;

    public SpellType(String name, SpellProperties properties) {
        this(name, properties.maxLevel, properties.consumeMana, properties.prepareCD, properties.cooldown, properties.canTrigger);
    }

    public SpellType(String name, int maxLevel, int consumeMana, int prepareCD, int cooldown, boolean canTrigger) {
        this.name = name;
        this.maxLevel = maxLevel;
        this.consumeMana = consumeMana;
        this.prepareCD = prepareCD;
        this.cooldown = cooldown;
        this.canTrigger = canTrigger;
        this.resourceLocation = Util.get().texture("spell/" + this.name);
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
    public int getPrepareCD() {
        return this.prepareCD;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public boolean canTrigger() {
        return canTrigger;
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
    public MutableComponent getComponent() {
        return Util.get().lang("spell", getName());
    }

    public static SpellProperties properties(){
        return new SpellProperties();
    }

    public static class SpellProperties {

        private int maxLevel = 1;
        private int consumeMana = 0;
        private int prepareCD = 0;
        private int cooldown = 0;
        private boolean canTrigger = true;

        public SpellProperties maxLevel(int maxLevel){
            this.maxLevel = maxLevel;
            return this;
        }

        public SpellProperties mana(int mana){
            this.consumeMana = mana;
            return this;
        }

        public SpellProperties pre(int cd){
            this.prepareCD = cd;
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

    }
}
