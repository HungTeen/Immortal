package hungteen.immortal.common.impl.registry;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.registry.ISpellType;
import hungteen.immortal.api.registry.ISpiritualType;
import hungteen.immortal.utils.Util;
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

    public static final ISpellType INSPIRATION = new SpellType("inspiration", 1, 0, 0, 10, 1200, List.of());

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

    public static final ISpellType ITEM_PICKING = new SpellType("item_picking", 1, 10, 10, 400, 600, List.of());
    public static final ISpellType ITEM_STEALING = new SpellType("item_stealing", 1, 10, 10, 400, 1800, List.of());
    public static final ISpellType BLOCK_PICKING = new SpellType("block_picking", 2, 10, 10, 400, 600, List.of());
    public static final ISpellType FLY_WITH_SWORD = new SpellType("fly_with_sword", 3, 50, 30, 200, 2400, List.of());

    //    public static final ISpellType ADVANCE_CONSCIOUSNESS = new SpellType("advance_consciousness", 7, 50, 500,
//            lvl -> {
//                return lvl <= 2 ? RealmTypes.MEDITATION_STAGE5 :
//                        lvl <= 4 ? RealmTypes.MEDITATION_STAGE8 :
//                                lvl <= 6 ? RealmTypes.MEDITATION_STAGE10 :
//                                        RealmTypes.FOUNDATION_BEGIN;
//            }, List.of(), List.of()
//    );





    public static class SpellType implements ISpellType {

        private final String name;
        private final int maxLevel;
        private final int startMana;
        private final int continueMana;
        private final int duration;
        private final int cooldown;
        private final List<ISpiritualType> requireRoots;
        private final ResourceLocation resourceLocation;

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register(){
            ImmortalAPI.get().spellRegistry().ifPresent(l -> l.register(TYPES));
        }

        public SpellType(String name, int maxLevel, int startMana, int continueMana, int duration, int cooldown, List<ISpiritualType> requireRoots) {
            this.name = name;
            this.maxLevel = maxLevel;
            this.startMana = startMana;
            this.continueMana = continueMana;
            this.duration = duration;
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
}
