package hungteen.imm.common.spell;

import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.spell.spells.basic.*;
import hungteen.imm.common.spell.spells.common.FlyWithItemSpell;
import hungteen.imm.common.spell.spells.common.PickupBlockSpell;
import hungteen.imm.common.spell.spells.common.PickupItemSpell;
import hungteen.imm.common.spell.spells.common.ThrowItemSpell;
import hungteen.imm.common.spell.spells.conscious.SpiritEyeSpell;
import hungteen.imm.common.spell.spells.earth.CrystalExplosionSpell;
import hungteen.imm.common.spell.spells.earth.CrystalHeartSpell;
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
public interface SpellTypes {

    HTCustomRegistry<ISpellType> SPELL_TYPES = HTRegistryManager.custom(Util.prefix("spell_type"));

    /* 基本法术 - Basic Spell */

    ISpellType MEDITATION = register(new MeditationSpell());
    ISpellType DISPERSAL = register(new DispersalSpell());
    ISpellType RELEASING = register(new ReleasingSpell());
    ISpellType INTIMIDATION = register(new IntimidationSpell());

    /* 神识 - Consciousness */

    ISpellType SPIRIT_EYES = register(new SpiritEyeSpell());

    /* 御物术 - Object Controlling */

    ISpellType PICKUP_ITEM = register(new PickupItemSpell());
    ISpellType THROW_ITEM = register(new ThrowItemSpell());
    ISpellType PICKUP_BLOCK = register(new PickupBlockSpell());
    ISpellType FLY_WITH_ITEM = register(new FlyWithItemSpell());

    /* 金系法术 - Metal Spell */

    ISpellType CRITICAL_HIT = register(new CriticalHitSpell());
    ISpellType METAL_MENDING = register(new MetalMendingSpell());
    ISpellType SHARPNESS = register(new SharpnessSpell());

    /* 木系法术 - Wood Spell */

    ISpellType LEVITATION = register(new LevitationSpell());
    ISpellType SPROUT = register(new SproutSpell());
    ISpellType WITHER = register(new WitherSpell());
    ISpellType WOOD_HEALING = register(new WoodHealingSpell());

    /* 水系法术 - Water Spell */

    ISpellType WATER_BREATHING = register(new WaterBreathingSpell());

    /* 火系法术 - Fire Spell */

    ISpellType BURNING = register(new BurningSpell());
    ISpellType LAVA_BREATHING = register(new LavaBreathingSpell());
    ISpellType IGNITION = register(new IgnitionSpell());

    /* 土系法术 - Earth Spell */

//    public static final ISpellType EARTH_EVADING = initialize(new EarthEvadingSpell());
ISpellType CRYSTAL_EXPLOSION = register(new CrystalExplosionSpell());
    ISpellType CRYSTAL_HEART = register(new CrystalHeartSpell());

    /* 元素精通 - Element Mastery */

    ISpellType METAL_MASTERY = register(new ElementalMasterySpell(Elements.METAL));
    ISpellType WOOD_MASTERY = register(new ElementalMasterySpell(Elements.WOOD));
    ISpellType WATER_MASTERY = register(new ElementalMasterySpell(Elements.WATER));
    ISpellType FIRE_MASTERY = register(new ElementalMasterySpell(Elements.FIRE));
    ISpellType EARTH_MASTERY = register(new ElementalMasterySpell(Elements.EARTH));
    ISpellType SPIRIT_MASTERY = register(new ElementalMasterySpell(Elements.SPIRIT));

    //    public static final ISpellType ADVANCE_CONSCIOUSNESS = new SpellType("advance_consciousness", 7, 50, 500,
//            lvl -> {
//                return lvl <= 2 ? RealmTypes.MEDITATION_STAGE5 :
//                        lvl <= 4 ? RealmTypes.MEDITATION_STAGE8 :
//                                lvl <= 6 ? RealmTypes.MEDITATION_STAGE10 :
//                                        RealmTypes.FOUNDATION_BEGIN;
//            }, List.of(), List.of()
//    );

    static HTCustomRegistry<ISpellType> registry() {
        return SPELL_TYPES;
    }

    static ISpellType register(ISpellType spellType){
        return registry().register(spellType.getLocation(), spellType);
    }

}
