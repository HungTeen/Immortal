package hungteen.imm.client;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-10 11:12
 **/
public class ClientData {

    /* 轮盘相关 */

    public static boolean ShowSpellCircle = false; // 缓存上一次是否显示，用来记录当前是否变化。
    public static double SpellMousePositionX = 0D; // 模拟鼠标的X值。
    public static double SpellMousePositionY = 0D; // 模拟鼠标的Y值。
    public static int LastSelectedPosition = - 1; // 保存上一次法术轮盘选择的位置，-1表示啥也没选。

    /* 杂项 */

    public static boolean isDebugMode = false; // 是否开启Debug模式。

    public static boolean StartSmithing = false;
    public static float SmithingProgress = 0;
    public static float BestPointDisplayTick = 0;
    public static float SmithingSpeedMultiple = 1F;
    public static boolean SmithingDirection = true;

//    public static int LeftClickTick = 0;
//    public static int RightClickTick = 0;

    public static int ManaWarningTick = 0;
}
