package hungteen.imm.common.item.artifacts;

import hungteen.imm.api.cultivation.RealmType;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/3 16:16
 **/
public class WritingBrushItem extends ArtifactItem {

    public WritingBrushItem(Properties properties, RealmType realmType) {
        super(properties.stacksTo(1), realmType);
    }

}
