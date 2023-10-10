package hungteen.imm.common.spell;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.spell.spells.basic.*;
import hungteen.imm.common.spell.spells.common.FlyWithItemSpell;
import hungteen.imm.common.spell.spells.common.PickupBlockSpell;
import hungteen.imm.common.spell.spells.common.PickupItemSpell;
import hungteen.imm.common.spell.spells.common.ThrowItemSpell;
import hungteen.imm.common.spell.spells.conscious.SpiritEyeSpell;
import hungteen.imm.common.spell.spells.earth.AmethystExplosionSpell;
import hungteen.imm.common.spell.spells.earth.AmethystHeartSpell;
import hungteen.imm.common.spell.spells.earth.EarthEvadingSpell;
import hungteen.imm.common.spell.spells.fire.BurningSpell;
import hungteen.imm.common.spell.spells.fire.IgnitionSpell;
import hungteen.imm.common.spell.spells.fire.LavaBreathingSpell;
import hungteen.imm.common.spell.spells.metal.CriticalHitSpell;
import hungteen.imm.common.spell.spells.metal.MetalMendingSpell;
import hungteen.imm.common.spell.spells.metal.SharpnessSpell;
import hungteen.imm.common.spell.spells.water.WaterBreathingSpell;
import hungteen.imm.common.spell.spells.wood.LevitationSpell;
import hungteen.imm.common.spell.spells.wood.SproutSpell;
import hungteen.imm.common.spell.spells.wood.WitherSpell;
import hungteen.imm.common.spell.spells.wood.WoodHealingSpell;
import hungteen.imm.util.Util;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-29 14:26
 **/
public class SpellTypes {

    private static final HTSimpleRegistry<ISpellType> SPELL_TYPES = HTRegistryManager.createSimple(Util.prefix("spell_type"));

    /* 基本法术 - Basic Spell */

    public static final ISpellType MEDITATION = register(new MeditationSpell());
    public static final ISpellType DISPERSAL = register(new DispersalSpell());
    public static final ISpellType RELEASING = register(new ReleasingSpell());
    public static final ISpellType INTIMIDATE = register(new IntimidateSpell());

    /* 神识 - Consciousness */

    public static final ISpellType SPIRIT_EYES = register(new SpiritEyeSpell());

    /* 御物术 - Object Controlling */

    public static final ISpellType PICKUP_ITEM = register(new PickupItemSpell());
    public static final ISpellType THROW_ITEM = register(new ThrowItemSpell());
    public static final ISpellType PICKUP_BLOCK = register(new PickupBlockSpell());
    public static final ISpellType FLY_WITH_ITEM = register(new FlyWithItemSpell());

    /* 金系法术 - Metal Spell */

    public static final ISpellType CRITICAL_HIT = register(new CriticalHitSpell());
    public static final ISpellType METAL_MENDING = register(new MetalMendingSpell());
    public static final ISpellType SHARPNESS = register(new SharpnessSpell());

    /* 木系法术 - Wood Spell */

    public static final ISpellType LEVITATION = register(new LevitationSpell());
    public static final ISpellType SPROUT = register(new SproutSpell());
    public static final ISpellType WITHER = register(new WitherSpell());
    public static final ISpellType WOOD_HEALING = register(new WoodHealingSpell());

    /* 水系法术 - Water Spell */

    public static final ISpellType WATER_BREATHING = register(new WaterBreathingSpell());

    /* 火系法术 - Fire Spell */

    public static final ISpellType BURNING = register(new BurningSpell());
    public static final ISpellType LAVA_BREATHING = register(new LavaBreathingSpell());
    public static final ISpellType IGNITION = register(new IgnitionSpell());

    /* 土系法术 - Earth Spell */

    public static final ISpellType EARTH_EVADING = register(new EarthEvadingSpell());
    public static final ISpellType AMETHYST_EXPLOSION = register(new AmethystExplosionSpell());
    public static final ISpellType AMETHYST_HEART = register(new AmethystHeartSpell());

    /* 元素精通 - Element Mastery */

    public static final ISpellType METAL_MASTERY = register(new ElementalMasterySpell(Elements.METAL));
    public static final ISpellType WOOD_MASTERY = register(new ElementalMasterySpell(Elements.WOOD));
    public static final ISpellType WATER_MASTERY = register(new ElementalMasterySpell(Elements.WATER));
    public static final ISpellType FIRE_MASTERY = register(new ElementalMasterySpell(Elements.FIRE));
    public static final ISpellType EARTH_MASTERY = register(new ElementalMasterySpell(Elements.EARTH));
    public static final ISpellType SPIRIT_MASTERY = register(new ElementalMasterySpell(Elements.SPIRIT));

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
