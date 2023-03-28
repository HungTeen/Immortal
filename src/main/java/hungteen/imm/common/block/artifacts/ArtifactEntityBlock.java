package hungteen.imm.common.block.artifacts;

import hungteen.htlib.common.block.entityblock.HTEntityBlock;
import hungteen.imm.api.interfaces.IArtifactBlock;
import hungteen.imm.api.registry.IArtifactType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/28 20:41
 */
public abstract class ArtifactEntityBlock extends HTEntityBlock implements IArtifactBlock {

    private final IArtifactType artifactType;

    protected ArtifactEntityBlock(BlockBehaviour.Properties properties, IArtifactType artifactType) {
        super(properties);
        this.artifactType = artifactType;
    }


    @Override
    public IArtifactType getArtifactType(BlockState state) {
        return this.artifactType;
    }

}
