package hungteen.imm.common.item.artifacts;

import hungteen.imm.api.registry.IRealmType;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/3 16:16
 **/
public class WritingBrushItem extends ArtifactItem {

    public WritingBrushItem(Properties properties, IRealmType realmType) {
        super(properties.stacksTo(1), realmType);
    }

}
