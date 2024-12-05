package hungteen.imm.common.item.artifact;

import hungteen.imm.api.artifact.ArtifactRank;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/3 16:16
 **/
public class WritingBrushItem extends ArtifactItemImpl {

    public WritingBrushItem(Properties properties, ArtifactRank realmType) {
        super(properties.stacksTo(1), realmType);
    }

}
