package hungteen.imm.common.cultivation;

import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.api.spell.TalismanSpell;
import hungteen.imm.common.cultivation.spell.basic.*;
import hungteen.imm.common.cultivation.spell.common.FlyWithItemSpell;
import hungteen.imm.common.cultivation.spell.common.PickupBlockSpell;
import hungteen.imm.common.cultivation.spell.common.PickupItemSpell;
import hungteen.imm.common.cultivation.spell.common.ThrowItemSpell;
import hungteen.imm.common.cultivation.spell.conscious.SpiritEyeSpell;
import hungteen.imm.common.cultivation.spell.earth.CrystalExplosionSpell;
import hungteen.imm.common.cultivation.spell.earth.CrystalHeartSpell;
import hungteen.imm.common.cultivation.spell.fire.BurningSpell;
import hungteen.imm.common.cultivation.spell.fire.IgnitionSpell;
import hungteen.imm.common.cultivation.spell.fire.LavaBreathingSpell;
import hungteen.imm.common.cultivation.spell.metal.CriticalHitSpell;
import hungteen.imm.common.cultivation.spell.metal.MetalMendingSpell;
import hungteen.imm.common.cultivation.spell.metal.SharpnessSpell;
import hungteen.imm.common.cultivation.spell.talisman.*;
import hungteen.imm.common.cultivation.spell.water.WaterBreathingSpell;
import hungteen.imm.common.cultivation.spell.wood.*;
import hungteen.imm.util.Util;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-09-29 14:26
 **/
public interface SpellTypes {

    HTCustomRegistry<SpellType> SPELL_TYPES = HTRegistryManager.custom(Util.prefix("spell_type"));

    /* 基本法术 - Basic Spell */

    SpellType MEDITATION = register(new MeditationSpell());
    SpellType DISPERSAL = register(new DispersalSpell());
    SpellType RELEASING = register(new ReleasingSpell());
    SpellType INTIMIDATION = register(new IntimidationSpell());

    /* 神识 - Consciousness */

    SpellType SPIRIT_EYES = register(new SpiritEyeSpell());

    /* 普通法术 - Common Spell */

    SpellType SPEED = register(new SpeedSpell());

    /* 御物术 - Object Controlling */

    SpellType PICKUP_ITEM = register(new PickupItemSpell());
    SpellType THROW_ITEM = register(new ThrowItemSpell());
    SpellType PICKUP_BLOCK = register(new PickupBlockSpell());
    SpellType FLY_WITH_ITEM = register(new FlyWithItemSpell());

    /* 金系法术 - Metal Spell */

    SpellType CRITICAL_HIT = register(new CriticalHitSpell());
    SpellType METAL_MENDING = register(new MetalMendingSpell());
    SpellType SHARPNESS = register(new SharpnessSpell());

    /* 木系法术 - Wood Spell */

    SpellType LEVITATION = register(new LevitationSpell());
    TalismanSpell TWISTING_VINE = registerTalisman(new TwistingVineSpell());
    TalismanSpell SPROUT = registerTalisman(new SproutSpell());
    SpellType WOOD_HEALING = register(new WoodHealingSpell());

    /* 水系法术 - Water Spell */

    SpellType WATER_BREATHING = register(new WaterBreathingSpell());
    TalismanSpell FALLING_ICE = registerTalisman(new FallingIceSpell());

    /* 火系法术 - Fire Spell */

    SpellType BURNING = register(new BurningSpell());
    SpellType LAVA_BREATHING = register(new LavaBreathingSpell());
    TalismanSpell FIREBALL = registerTalisman(new FireballSpell());
    SpellType IGNITION = register(new IgnitionSpell());

    /* 土系法术 - Earth Spell */

    //    public static final ISpellType EARTH_EVADING = initialize(new EarthEvadingSpell());
    SpellType CRYSTAL_EXPLOSION = register(new CrystalExplosionSpell());
    SpellType CRYSTAL_HEART = register(new CrystalHeartSpell());

    /* 业系法术 - Spirit Spell */

    SpellType WITHER = register(new WitherSpell());

    /* 雷系法术 - Lightning Spell */

    TalismanSpell LIGHTNING = registerTalisman(new LightningSpell());

    /* 元素精通 - Element Mastery */

    SpellType METAL_MASTERY = register(new ElementalMasterySpell(Element.METAL));
    SpellType WOOD_MASTERY = register(new ElementalMasterySpell(Element.WOOD));
    SpellType WATER_MASTERY = register(new ElementalMasterySpell(Element.WATER));
    SpellType FIRE_MASTERY = register(new ElementalMasterySpell(Element.FIRE));
    SpellType EARTH_MASTERY = register(new ElementalMasterySpell(Element.EARTH));
    SpellType SPIRIT_MASTERY = register(new ElementalMasterySpell(Element.SPIRIT));

    /* 多元素法术 */

    TalismanSpell EARTH_FANG = registerTalisman(new EarthFangTalisman());

    //    public static final ISpellType ADVANCE_CONSCIOUSNESS = new SpellType("advance_consciousness", 7, 50, 500,
//            lvl -> {
//                return lvl <= 2 ? RealmTypes.MEDITATION_STAGE5 :
//                        lvl <= 4 ? RealmTypes.MEDITATION_STAGE8 :
//                                lvl <= 6 ? RealmTypes.MEDITATION_STAGE10 :
//                                        RealmTypes.FOUNDATION_BEGIN;
//            }, List.of(), List.of()
//    );

    static HTCustomRegistry<SpellType> registry() {
        return SPELL_TYPES;
    }

    static SpellType register(SpellType spellType) {
        return registry().register(spellType.getLocation(), spellType);
    }

    static TalismanSpell registerTalisman(TalismanSpell spellType) {
        SpellType spell = registry().register(spellType.getLocation(), spellType);
        if(spell instanceof TalismanSpell talismanSpell){
            return talismanSpell;
        }
        throw new RuntimeException(
                "SpellType " + spellType.getLocation() + " is not a TalismanSpell, please check your code."
        );
    }

}
