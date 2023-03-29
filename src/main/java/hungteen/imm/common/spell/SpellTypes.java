package hungteen.imm.common.spell;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.ImmortalMod;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.api.registry.ISpiritualType;
import hungteen.imm.common.spell.spells.*;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 14:26
 **/
public class SpellTypes {

    private static final HTSimpleRegistry<ISpellType> SPELL_TYPES = HTRegistryManager.create(Util.prefix("spell_type"));
    private static final List<ISpellType> TYPES = new ArrayList<>();

    public static IHTSimpleRegistry<ISpellType> registry() {
        return SPELL_TYPES;
    }

    public static final ISpellType INSPIRATION = new InspireSpell(properties().maxLevel(1).mana(0).cd(2000));
    public static final ISpellType RESTING = new RestSpell(properties().maxLevel(1).mana(0).cd(600));
    public static final ISpellType ITEM_PICKING = new PickUpItemSpell(properties().maxLevel(2).mana(5).cd(200));
    public static final ISpellType ITEM_STEALING = new StealItemSpell(properties().maxLevel(1).mana(50).cd(1800));
    public static final ISpellType BLOCK_PICKING = new StealItemSpell(properties().maxLevel(2).mana(10).cd(400));
    public static final ISpellType FLY_WITH_ITEM = new FlyWithItemSpell(properties().maxLevel(3).mana(20).cd(200));

    //    public static final ISpellType IGNITE = new SpellType("ignite", 1, 20, 200,
//            lvl -> RealmTypes.MEDITATION_STAGE3, List.of(), List.of()
//    );
//
//    public static final ISpellType WATER_BREATHING = new SpellType("water_breathing", 1, 200, 3600,
//            lvl -> RealmTypes.MEDITATION_STAGE3, List.of(SpiritualTypes.WATER), List.of()
//    );

    //    public static final ISpellType ADVANCE_CONSCIOUSNESS = new SpellType("advance_consciousness", 7, 50, 500,
//            lvl -> {
//                return lvl <= 2 ? RealmTypes.MEDITATION_STAGE5 :
//                        lvl <= 4 ? RealmTypes.MEDITATION_STAGE8 :
//                                lvl <= 6 ? RealmTypes.MEDITATION_STAGE10 :
//                                        RealmTypes.FOUNDATION_BEGIN;
//            }, List.of(), List.of()
//    );

    public static abstract class SpellType implements ISpellType {

        private final String name;
        private final int maxLevel;
        private final int consumeMana;
        private final int cooldown;
        private final List<ISpiritualType> requireRoots;
        private final ResourceLocation resourceLocation;

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register(){
            IMMAPI.get().spellRegistry().ifPresent(l -> l.register(TYPES));
        }

        public SpellType(String name, SpellProperties properties) {
           this(name, properties.maxLevel, properties.consumeMana, properties.cooldown, properties.requireRoots);
        }

        public SpellType(String name, int maxLevel, int consumeMana, int cooldown, List<ISpiritualType> requireRoots) {
            this.name = name;
            this.maxLevel = maxLevel;
            this.consumeMana = consumeMana;
            this.cooldown = cooldown;
            this.requireRoots = requireRoots;
            this.resourceLocation = Util.prefix("textures/spell/" + this.name + ".png");
            TYPES.add(this);
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
        public List<ISpiritualType> requireSpiritualRoots() {
            return requireRoots;
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
            return Component.translatable("spell." + getModID() +"." + getName());
        }

    }

    public static SpellProperties properties(){
        return new SpellProperties();
    }

    public static class SpellProperties {

        private int maxLevel = 1;
        private int consumeMana = 0;
        private int cooldown = 0;
        private final List<ISpiritualType> requireRoots = new ArrayList<>();

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

        public SpellProperties root(ISpiritualType root){
            this.requireRoots.add(root);
            return this;
        }

    }
}
