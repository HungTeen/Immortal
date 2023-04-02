package hungteen.imm.common.item.runes;

import hungteen.imm.common.rune.sensor.ISensorRune;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-04-02 22:36
 **/
public class SensorRuneItem extends RuneItem {

    private final ISensorRune sensorRune;

    public SensorRuneItem(ISensorRune sensorRune) {
        this.sensorRune = sensorRune;
    }

    public ISensorRune getSensorRune() {
        return sensorRune;
    }
}
