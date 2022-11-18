package hungteen.immortal.impl;

import hungteen.immortal.ImmortalMod;
import hungteen.immortal.api.registry.ISpell;
import hungteen.immortal.api.registry.ISpellBook;
import hungteen.immortal.utils.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 14:26
 **/
public class SpellBooks {

    private static final List<ISpellBook> TYPES = new ArrayList<>();
//
//    public static final ISpellBook CONTROLLING_OBJECTS = new SpellBook("controlling_objects", List.of(
//            Spells.
//    ));

    public static class SpellBook implements ISpellBook {

        private final String name;
        private final List<ISpell> spells;

        /**
         * {@link ImmortalMod#coreRegister()}
         */
        public static void register(){
//            TYPES.forEach(type -> ImmortalAPI.get().registerSpellBook(type));
        }

        public SpellBook(String name, List<ISpell> spells) {
            this.name = name;
            this.spells = spells;
            TYPES.add(this);
        }


        @Override
        public List<ISpell> getSpells() {
            return Collections.unmodifiableList(this.spells);
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
            return Component.translatable("spell." + getModID() +".book." + getName());
        }

    }
}
