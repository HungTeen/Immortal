package hungteen.imm.common;

import hungteen.imm.api.interfaces.IArtifactBlock;
import hungteen.imm.api.interfaces.IArtifactItem;
import hungteen.imm.api.registry.IArtifactType;
import hungteen.imm.common.impl.ArtifactTypes;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.common.tag.IMMItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-11-16 14:25
 **/
public class ArtifactManager {

    public static boolean notCommon(IArtifactType type) {
        return type != ArtifactTypes.UNKNOWN && type != ArtifactTypes.COMMON_ITEM;
    }
    public static int getRealmValue(ItemStack stack){
        return getArtifactType(stack).getRealmValue();
    }

    public static int getRealmValue(BlockState state){
        return getArtifactType(state).getRealmValue();
    }

    public static IArtifactType getArtifactType(ItemStack stack){
        if(stack.is(IMMItemTags.COMMON_ARTIFACTS)) return ArtifactTypes.COMMON_ARTIFACT;
        if(stack.is(IMMItemTags.MODERATE_ARTIFACTS)) return ArtifactTypes.MODERATE_ARTIFACT;
        if(stack.is(IMMItemTags.ADVANCED_ARTIFACTS)) return ArtifactTypes.ADVANCED_ARTIFACT;
        if(stack.getItem() instanceof IArtifactItem artifactItem) return artifactItem.getArtifactType(stack);
        if(stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof IArtifactBlock artifactBlock) return artifactBlock.getArtifactType(stack);
        return ArtifactTypes.COMMON_ITEM;
    }

    public static IArtifactType getArtifactType(BlockState state){
        if(state.is(IMMBlockTags.COMMON_ARTIFACTS)) return ArtifactTypes.COMMON_ARTIFACT;
        if(state.is(IMMBlockTags.MODERATE_ARTIFACTS)) return ArtifactTypes.MODERATE_ARTIFACT;
        if(state.is(IMMBlockTags.ADVANCED_ARTIFACTS)) return ArtifactTypes.ADVANCED_ARTIFACT;
        if(state.getBlock() instanceof IArtifactBlock artifactBlock) return artifactBlock.getArtifactType(state);
        return ArtifactTypes.COMMON_ITEM;
    }

}
