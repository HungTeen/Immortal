package hungteen.immortal.common.impl;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.IRealmType;
import hungteen.immortal.api.registry.ISpellType;
import hungteen.immortal.api.registry.ISpiritualType;
import hungteen.immortal.utils.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 14:26
 **/
public class SpellTypes {

    private static final List<ISpellType> TYPES = new ArrayList<>();

//    public static final ISpellType RELEASING = new SpellType("releasing", 1, 10, 200,
//            lvl -> RealmTypes.MEDITATION_BEGINNER, List.of(), List.of()
//    );
//
//    public static final ISpellType RESTING = new SpellType("resting", 1, 0, 1200,
//            lvl -> RealmTypes.MEDITATION_BEGINNER, List.of(), List.of()
//    );
//
//    public static final ISpellType IGNITE = new SpellType("ignite", 1, 20, 200,
//            lvl -> RealmTypes.MEDITATION_STAGE3, List.of(), List.of()
//    );
//
//    public static final ISpellType WATER_BREATHING = new SpellType("water_breathing", 1, 200, 3600,
//            lvl -> RealmTypes.MEDITATION_STAGE3, List.of(SpiritualTypes.WATER), List.of()
//    );
//
//    public static final ISpellType ITEM_PICKING = new SpellType("item_picking", 1, 10, 10, 400,
//            lvl -> RealmTypes.MEDITATION_STAGE5, List.of(), List.of()
//    );
//
//    public static final ISpellType ADVANCE_CONSCIOUSNESS = new SpellType("advance_consciousness", 7, 50, 500,
//            lvl -> {
//                return lvl <= 2 ? RealmTypes.MEDITATION_STAGE5 :
//                        lvl <= 4 ? RealmTypes.MEDITATION_STAGE8 :
//                                lvl <= 6 ? RealmTypes.MEDITATION_STAGE10 :
//                                        RealmTypes.FOUNDATION_BEGIN;
//            }, List.of(), List.of()
//    );
//
//    public static final ISpellType FLY_WITH_SWORD = new SpellType("fly_with_sword", 1, 50, 3600,
//            lvl -> RealmTypes.MEDITATION_STAGE5, List.of(), List.of()
//    );
//
//    public static final ISpellType BLOCK_PICKING = new SpellType("block_picking", 1, 10, 50, 400,
//            lvl -> RealmTypes.MEDITATION_STAGE8, List.of(), List.of()
//    );

    public static class SpellType implements ISpellType {

        private final String name;
        private final int maxLevel;
        private final int startMana;
        private final int continueMana;
        private final int duration;
        private final boolean hasTutorialBook;
        private final Function<Integer, IRealmType> realmFunc;
        private final List<ISpiritualType> requireRoots;
        private final List<ISpellType> requireSpells;
        private final ResourceLocation resourceLocation;

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register(){
            ImmortalAPI.get().spellRegistry().ifPresent(l -> l.register(TYPES));
        }

        public SpellType(String name, int maxLevel, int startMana, int duration, Function<Integer, IRealmType> realmFunc, List<ISpiritualType> requireRoots, List<ISpellType> requireSpells) {
            this(name, maxLevel, startMana, 0, duration, realmFunc, requireRoots, requireSpells);
        }

        public SpellType(String name, int maxLevel, int startMana, int continueMana, int duration, Function<Integer, IRealmType> realmFunc, List<ISpiritualType> requireRoots, List<ISpellType> requireSpells) {
            this(name, maxLevel, startMana, continueMana, duration, true, realmFunc, requireRoots, requireSpells);
        }

        public SpellType(String name, int maxLevel, int startMana, int continueMana, int duration, boolean hasTutorialBook, Function<Integer, IRealmType> realmFunc, List<ISpiritualType> requireRoots, List<ISpellType> requireSpells) {
            this.name = name;
            this.maxLevel = maxLevel;
            this.startMana = startMana;
            this.continueMana = continueMana;
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
        public int getContinueMana() {
            return continueMana;
        }

        @Override
        public int getDuration() {
            return duration;
        }

        @Override
        public IRealmType requireRealm(int level) {
            return this.realmFunc.apply(level);
        }

        @Override
        public List<ISpiritualType> requireSpiritualRoots() {
            return requireRoots;
        }

        @Override
        public List<ISpellType> requirePreSpells() {
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
        public MutableComponent getComponent() {
            return Component.translatable("spell." + getModID() +"." + getName());
        }

    }
}
