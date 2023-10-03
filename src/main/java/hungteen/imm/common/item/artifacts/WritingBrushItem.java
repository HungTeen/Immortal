package hungteen.imm.common.item.artifacts;

import hungteen.imm.api.registry.IArtifactType;

/**
 * @program: Immortal
 * @author: PangTeen
 * @create: 2023/10/3 16:16
 **/
public class WritingBrushItem extends ArtifactItem {

    public WritingBrushItem(Properties properties, IArtifactType artifactType) {
        super(properties.stacksTo(1), artifactType);
    }

}
