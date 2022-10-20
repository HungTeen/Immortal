package hungteen.immortal.client.model;

import hungteen.immortal.utils.Util;
import net.minecraft.client.model.geom.ModelLayerLocation;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-20 21:58
 **/
public class ModelLayers {
    /*
    Animals.
    */
    public static final ModelLayerLocation GRASS_CARP = register("grass_carp");

    /*
    Zombie Entities.
     */
    public static final ModelLayerLocation SPIRITUAL_ZOMBIE = register("spiritual_zombie");
    public static final ModelLayerLocation SPIRITUAL_ZOMBIE_INNER_ARMOR = registerInnerArmor("spiritual_zombie");
    public static final ModelLayerLocation SPIRITUAL_ZOMBIE_OUTER_ARMOR = registerOuterArmor("spiritual_zombie");

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
