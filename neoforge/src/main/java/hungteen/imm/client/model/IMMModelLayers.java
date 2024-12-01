package hungteen.imm.client.model;

import hungteen.imm.util.Util;
import net.minecraft.client.model.geom.ModelLayerLocation;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-20 21:58
 **/
public class IMMModelLayers {

    /* Misc */

    public static final ModelLayerLocation ELEMENT_CRYSTAL = register("element_crystal");
    public static final ModelLayerLocation TORNADO = register("tornado");

    /* Human */

    public static final ModelLayerLocation CULTIVATOR = register("cultivator");
    public static final ModelLayerLocation CULTIVATOR_INNER_ARMOR = registerInnerArmor("cultivator");
    public static final ModelLayerLocation CULTIVATOR_OUTER_ARMOR = registerOuterArmor("cultivator");
    public static final ModelLayerLocation CULTIVATOR_SLIM = register("cultivator_slim");
    public static final ModelLayerLocation CULTIVATOR_SLIM_INNER_ARMOR = registerInnerArmor("cultivator_slim");
    public static final ModelLayerLocation CULTIVATOR_SLIM_OUTER_ARMOR = registerOuterArmor("cultivator_slim");
    public static final ModelLayerLocation VILLAGER = register("villager");
    public static final ModelLayerLocation PILLAGER = register("pillager");

    /* Creature */

    public static final ModelLayerLocation GRASS_CARP = register("grass_carp");
    public static final ModelLayerLocation SILK_WORM = register("silk_worm");

    /* Monster */

    public static final ModelLayerLocation SHARP_STAKE = register("sharp_stake");
    public static final ModelLayerLocation BI_FANG = register("bi_fang");

    /* Spirit */

    public static final ModelLayerLocation METAL_SPIRIT = register("metal_spirit");
    public static final ModelLayerLocation WOOD_SPIRIT = register("wood_spirit");
    public static final ModelLayerLocation WATER_SPIRIT = register("water_spirit");
    public static final ModelLayerLocation FIRE_SPIRIT = register("fire_spirit");
    public static final ModelLayerLocation EARTH_SPIRIT = register("earth_spirit");

    /* Zombie */

    public static final ModelLayerLocation QI_ZOMBIE = register("spiritual_zombie");
    public static final ModelLayerLocation QI_ZOMBIE_INNER_ARMOR = registerInnerArmor("spiritual_zombie");
    public static final ModelLayerLocation QI_ZOMBIE_OUTER_ARMOR = registerOuterArmor("spiritual_zombie");

    /* Golem */

    public static final ModelLayerLocation IRON_GOLEM = register("iron_golem");
    public static final ModelLayerLocation SNOW_GOLEM = register("snow_golem");
    public static final ModelLayerLocation CREEPER_GOLEM = register("creeper_golem");
    public static final ModelLayerLocation COPPER_GOLEM = register("copper_golem");

    private static ModelLayerLocation register(String name) {
        return createLocation(name, "main");
    }

    private static ModelLayerLocation registerInnerArmor(String name) {
        return createLocation(name, "inner_armor");
    }

    private static ModelLayerLocation registerOuterArmor(String name) {
        return createLocation(name, "outer_armor");
    }

    private static ModelLayerLocation createLocation(String name, String type) {
        return new ModelLayerLocation(Util.prefix(name), type);
    }
}
