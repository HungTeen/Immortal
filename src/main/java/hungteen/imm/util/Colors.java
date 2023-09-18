package hungteen.imm.util;

import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.records.HTColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-21 12:34
 **/
public class Colors {

    public static final int ZOMBIE_AQUA = 44975;
    public static final int ZOMBIE_SKIN = 7969893;

    public static final int SPIRITUAL_MANA = 3336407;

    public static final int WORD = 5716524;

    public static ColorMixer mixer(){
        return new ColorMixer();
    }

    public static final class ColorMixer{

        List<Float> redColors = new ArrayList<>();
        List<Float> greenColors = new ArrayList<>();
        List<Float> blueColors = new ArrayList<>();

        public ColorMixer(){

        }

        public ColorMixer add(HTColor color){
            return add(color.red(), color.green(), color.blue());
        }

        public ColorMixer add(float red, float green, float blue){
            this.redColors.add(red);
            this.greenColors.add(green);
            this.blueColors.add(blue);
            return this;
        }

        public HTColor mix(){
            final float red = this.redColors.stream().reduce(0F, Float::sum);
            final float green = this.greenColors.stream().reduce(0F, Float::sum);
            final float blue = this.blueColors.stream().reduce(0F, Float::sum);
            final int size = this.redColors.size();
            return ColorHelper.create(new float[]{red / size, green / size, blue / size});
        }
    }

}
