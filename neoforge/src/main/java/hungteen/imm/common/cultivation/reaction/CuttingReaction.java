package hungteen.imm.common.cultivation.reaction;

import hungteen.imm.api.cultivation.Element;

/**
 * (强木 + 火)为基本相生反应，但是(弱木 + 火)也能发生相生反应！
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/28 22:34
 **/
public class CuttingReaction extends InhibitionReaction {

    private final float waterAmount;

    public CuttingReaction(String name, boolean once, float waterPercent, Element main, float mainAmount, Element off, float percent) {
        super(name, once, main, mainAmount, off, percent);
        this.waterAmount = waterPercent * mainAmount;
    }

    public float getWaterAmount() {
        return waterAmount;
    }
}
