package hungteen.immortal.impl;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.IRealm;
import hungteen.immortal.api.interfaces.ISpell;
import hungteen.immortal.api.interfaces.ISpiritualRoot;
import hungteen.immortal.utils.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 14:26
 **/
public class Spells {

    private static final List<ISpell> TYPES = new ArrayList<>();
    private static final ResourceLocation SPELLS = Util.prefix("textures/gui/overlay/spells.png");

    public static final ISpell RELEASING = new Spell("releasing", 1, 10, 200,
            lvl -> Realms.MEDITATION_STAGE1, List.of(), List.of()
    );

    public static final ISpell RESTING = new Spell("resting", 1, 0, 1200,
            lvl -> Realms.MEDITATION_STAGE1, List.of(), List.of()
    );

    public static final ISpell IGNITE = new Spell("ignite", 1, 20, 200,
            lvl -> Realms.MEDITATION_STAGE3, List.of(), List.of()
    );

    public static final ISpell WATER_BREATHING = new Spell("water_breathing", 1, 200, 3600,
            lvl -> Realms.MEDITATION_STAGE3, List.of(SpiritualRoots.WATER), List.of()
    );

    public static final ISpell ITEM_PICKING = new Spell("item_picking", 2, 10, 400,
            lvl -> Realms.MEDITATION_STAGE5, List.of(), List.of()
    );

    public static final ISpell ADVANCE_CONSCIOUSNESS = new Spell("advance_consciousness", 7, 50, 500,
            lvl -> {
                return lvl <= 2 ? Realms.MEDITATION_STAGE5 :
                        lvl <= 4 ? Realms.MEDITATION_STAGE8 :
                                lvl <= 6 ? Realms.MEDITATION_STAGE10 :
                                        Realms.FOUNDATION_BEGIN;
            }, Arrays.asList(), Arrays.asList()
    );

    public static final ISpell FLY_WITH_SWORD = new Spell("fly_with_sword", 1, 50, 3600,
            lvl -> Realms.MEDITATION_STAGE5, Arrays.asList(), Arrays.asList()
    );

    public static class Spell implements ISpell {

        private final String name;
        private final int maxLevel;
        private final int startMana;
        private final int duration;
        private final boolean hasTutorialBook;
        private final Function<Integer, IRealm> realmFunc;
        private final List<ISpiritualRoot> requireRoots;
        private final List<ISpell> requireSpells;
        private final ResourceLocation resourceLocation;

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register(){
            TYPES.forEach(type -> ImmortalAPI.get().registerSpell(type));
        }

        public Spell(String name, int maxLevel, int startMana, int duration, Function<Integer, IRealm> realmFunc, List<ISpiritualRoot> requireRoots, List<ISpell> requireSpells) {
            this(name, maxLevel, startMana, duration, true, realmFunc, requireRoots, requireSpells);
        }

        public Spell(String name, int maxLevel, int startMana, int duration, boolean hasTutorialBook, Function<Integer, IRealm> realmFunc, List<ISpiritualRoot> requireRoots, List<ISpell> requireSpells) {
            this.name = name;
            this.maxLevel = maxLevel;
            this.startMana = startMana;
            this.duration = duration;
            this.hasTutorialBook = hasTutorialBook;
            this.realmFunc = realmFunc;
            this.requireRoots = requireRoots;
            this.requireSpells = requireSpells;
            this.resourceLocation = Util.prefix("textures/spell/" + this.name + ".png");
            TYPES.add(this);
        }

        @Override
        public int getMaxLevel() {
            return this.maxLevel;
        }

        @Override
        public int getStartMana() {
            return this.startMana;
        }

        @Override
        public int getDuration() {
            return duration;
        }

        @Override
        public IRealm requireRealm(int level) {
            return this.realmFunc.apply(level);
        }

        @Override
        public List<ISpiritualRoot> requireSpiritualRoots() {
            return requireRoots;
        }

        @Override
        public List<ISpell> requirePreSpells() {
            return requireSpells;
        }

        @Override
        public ResourceLocation getSpellTexture() {
            return resourceLocation;
        }

        @Override
        public boolean hasTutorialBook() {
            return hasTutorialBook;
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
        public Component getComponent() {
            return new TranslatableComponent("misc." + getModID() +".spell." + getName());
        }

    }
}
