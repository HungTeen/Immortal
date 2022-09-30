package hungteen.immortal.impl;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.ImmortalAPI;
import hungteen.immortal.api.interfaces.ISpell;
import hungteen.immortal.api.interfaces.ISpiritualRoot;
import hungteen.immortal.utils.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 14:26
 **/
public class Spells {

    private static final List<ISpell> TYPES = new ArrayList<>();

    public static final ISpell ITEM_CONTROLLING = new Spell(
            "item_controlling", Arrays.asList(), Arrays.asList(), 0, 10, 100
    );

    public static final ISpell BLOCK_CONTROLLING = new Spell(
            "block_controlling", Arrays.asList(), Arrays.asList(ITEM_CONTROLLING), 0, 40, 100
    );

    public static final ISpell ENTITY_CONTROLLING = new Spell(
            "entity_controlling", Arrays.asList(), Arrays.asList(BLOCK_CONTROLLING), 0, 100, 100
    );

    public static final ISpell FLY_WITH_SWORD = new Spell(
            "fly_with_sword", Arrays.asList(), Arrays.asList(ENTITY_CONTROLLING), 0, 20, 200
    );


    public record Spell(String name, List<ISpiritualRoot> requireRoots, List<ISpell> requireSpells, int realm, int cost, int duration) implements ISpell {

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register(){
            TYPES.forEach(type -> ImmortalAPI.get().registerSpell(type));
        }

        public Spell(String name, List<ISpiritualRoot> requireRoots, List<ISpell> requireSpells, int realm, int cost, int duration) {
            this.name = name;
            this.requireRoots = requireRoots;
            this.requireSpells = requireSpells;
            this.realm = realm;
            this.cost = cost;
            this.duration = duration;
        }

        @Override
        public List<ISpiritualRoot> requireSpiritualRoots() {
            return requireRoots;
        }

        @Override
        public int requireRealm() {
            return realm;
        }

        @Override
        public List<ISpell> requirePreSpells() {
            return requireSpells;
        }

        @Override
        public int getCostEnergy() {
            return cost;
        }

        @Override
        public int getDuration() {
            return duration;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Component getComponent() {
            return new TranslatableComponent("misc." + Util.id() +".spell." + getName());
        }

    }
}
