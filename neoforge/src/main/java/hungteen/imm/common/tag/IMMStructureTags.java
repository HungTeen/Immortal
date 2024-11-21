package hungteen.imm.common.tag;

import hungteen.htlib.util.helper.impl.StructureHelper;
import hungteen.imm.util.Util;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/28 9:37
 */
public interface IMMStructureTags {

    TagKey<Structure> TELEPORT_RUINS = tag("teleport_ruins");

    private static TagKey<Structure> tag(String name){
        return StructureHelper.get().tag(Util.prefix(name));
    }

}
