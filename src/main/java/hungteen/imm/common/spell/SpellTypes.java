package hungteen.imm.common.spell;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.spell.spells.*;
import hungteen.imm.util.Util;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 14:26
 **/
public class SpellTypes {

    private static final HTSimpleRegistry<ISpellType> SPELL_TYPES = HTRegistryManager.createSimple(Util.prefix("spell_type"));

    public static final ISpellType MEDITATE = register(new MeditateSpell());
    public static final ISpellType PICKUP_ITEM = register(new PickupItemSpell());
    public static final ISpellType THROW_ITEM = register(new ThrowItemSpell());
    public static final ISpellType PICKUP_BLOCK = register(new PickupBlockSpell());
    public static final ISpellType FLY_WITH_ITEM = register(new FlyWithItemSpell());
    public static final ISpellType RELEASING = register(new ReleasingSpell());
    public static final ISpellType INTIMIDATE = register(new IntimidateSpell());
    public static final ISpellType WATER_BREATHE = register(new WaterBreatheSpell());
    public static final ISpellType LAVA_BREATHE = register(new LavaBreatheSpell());
    public static final ISpellType SPROUT = register(new SproutSpell());
    public static final ISpellType SPIRIT_EYES = register(new SpiritEyeSpell());
    public static final ISpellType METAL_MASTERY = register(new ElementalMasterySpell(Elements.METAL));
    public static final ISpellType WOOD_MASTERY = register(new ElementalMasterySpell(Elements.WOOD));
    public static final ISpellType WATER_MASTERY = register(new ElementalMasterySpell(Elements.WATER));
    public static final ISpellType FIRE_MASTERY = register(new ElementalMasterySpell(Elements.FIRE));
    public static final ISpellType EARTH_MASTERY = register(new ElementalMasterySpell(Elements.EARTH));
    public static final ISpellType SPIRIT_MASTERY = register(new ElementalMasterySpell(Elements.SPIRIT));

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

    public static IHTSimpleRegistry<ISpellType> registry() {
        return SPELL_TYPES;
    }

    public static ISpellType register(ISpellType spellType){
        return registry().register(spellType);
    }

}
