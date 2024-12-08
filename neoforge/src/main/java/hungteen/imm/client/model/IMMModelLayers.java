package hungteen.imm.client.model;

import hungteen.imm.util.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-20 21:58
 **/
public interface IMMModelLayers {

    LayerDefinition INNER_ARMOR = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32);
    LayerDefinition OUTER_ARMOR = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32);

    /* Misc */

    ModelLayerLocation ELEMENT_CRYSTAL = create("element_crystal");
    ModelLayerLocation TORNADO = create("tornado");

    /* Human */

    ModelLayerLocation VILLAGER = create("villager");
    ModelLayerLocation PILLAGER = create("pillager");

    /* Creature */

    ModelLayerLocation GRASS_CARP = create("grass_carp");
    ModelLayerLocation SILK_WORM = create("silk_worm");

    /* Monster */

    ModelLayerLocation SHARP_STAKE = create("sharp_stake");
    ModelLayerLocation BI_FANG = create("bi_fang");

    /* Spirit */

    ModelLayerLocation METAL_SPIRIT = create("metal_spirit");
    ModelLayerLocation WOOD_SPIRIT = create("wood_spirit");
    ModelLayerLocation WATER_SPIRIT = create("water_spirit");
    ModelLayerLocation FIRE_SPIRIT = create("fire_spirit");
    ModelLayerLocation EARTH_SPIRIT = create("earth_spirit");

    /* Golem */

    ModelLayerLocation IRON_GOLEM = create("iron_golem");
    ModelLayerLocation SNOW_GOLEM = create("snow_golem");
    ModelLayerLocation CREEPER_GOLEM = create("creeper_golem");
    ModelLayerLocation COPPER_GOLEM = create("copper_golem");

    static LayerDefinition innerArmor() {
        return INNER_ARMOR;
    }

    static LayerDefinition outerArmor() {
        return OUTER_ARMOR;
    }

    static ModelLayerLocation create(String name) {
        return createLocation(name, "main");
    }

    static ModelLayerLocation createInnerArmor(String name) {
        return createLocation(name, "inner_armor");
    }

    static ModelLayerLocation createOuterArmor(String name) {
        return createLocation(name, "outer_armor");
    }

    static ModelLayerLocation createLocation(String name, String type) {
        return new ModelLayerLocation(Util.prefix(name), type);
    }
}
