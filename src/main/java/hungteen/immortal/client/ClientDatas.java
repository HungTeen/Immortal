package hungteen.immortal.client;

import java.util.HashMap;
import java.util.Set;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-10 11:12
 **/
public class ClientDatas {

    public static final HashMap<String, Set<Integer>> FormationMap = new HashMap<>();

    /**
     * 是否展示了法术轮盘
     */
    public static boolean ShowSpellCircle = false;

    /**
     * 保存上一次法术轮盘选择的位置
     */
    public static int lastSelectedPosition = 0;

    public static boolean StartSmithing = false;
    public static float SmithingProgress = 0;
    public static float BestPointDisplayTick = 0;
    public static float SmithingSpeedMultiple = 1F;
    public static boolean SmithingDirection = true;

//    public static int LeftClickTick = 0;
//    public static int RightClickTick = 0;

    public static int ManaWarningTick = 0;
}
