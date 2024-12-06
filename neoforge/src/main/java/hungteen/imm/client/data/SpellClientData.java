package hungteen.imm.client.data;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.htlib.util.helper.NetworkHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.common.IMMConfigs;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.client.IMMClientProxy;
import hungteen.imm.client.IMMKeyBinds;
import hungteen.imm.client.event.ClientEvents;
import hungteen.imm.client.util.ClientUtil;
import hungteen.imm.common.cultivation.SpellManager;
import hungteen.imm.common.network.server.ServerSpellPacket;
import hungteen.imm.util.Constants;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/4 21:57
 **/
public class SpellClientData {

    /**
     * 缓存上一次是否显示，用来记录当前是否变化。
     */
    public static boolean showSpellCircle = false;

    /**
     * 模拟鼠标的X值。
     */
    public static double spellMousePositionX = 0D;

    /**
     * 模拟鼠标的Y值。
     */
    public static double spellMousePositionY = 0D;

    /**
     * 保存上一次法术轮盘选择的位置，-1表示啥也没选。
     */
    public static int lastSelectedPosition = - 1;

    /**
     * 是否发送了冷却消息。
     */
    public static boolean sendOnCoolDown = false;

    /**
     * {@link ClientEvents#tick(ClientTickEvent.Post)}
     */
    public static void tick(@NotNull Player player) {
        // 不能使用轮盘时，强制关闭。
        if (showSpellCircle) {
            if (!SpellManager.canUseCircle(player)) {
                showSpellCircle = false;
            }
        }
        // 更新轮盘的改变。
        if (SpellManager.canUseCircle(player) && useDefaultCircle()) {
            if (showSpellCircle ^ IMMKeyBinds.displayingSpellCircle()) {
                switchSpellCircle(lastSelectedPosition);
            }
        }
        // 长按以检测法术释放。
        if (IMMClientProxy.MC.isWindowActive() && ClientUtil.screen() == null) {
            if (IMMKeyBinds.ACTIVATE_SPELL.isDown()) {
                final SpellType spell = PlayerUtil.getPreparingSpell(player);
                if (EntityHelper.isEntityValid(player) && spell != null) {
                    if (! PlayerUtil.isSpellOnCoolDown(player, spell)) {
                        NetworkHelper.sendToServer(new ServerSpellPacket(ServerSpellPacket.SpellOption.ACTIVATE));
                    } else {
                        // 冷却提醒。
                        if(! sendOnCoolDown){
                            PlayerHelper.sendTipTo(player, SpellManager.SPELL_ON_CD.withStyle(ChatFormatting.RED));
                            sendOnCoolDown = true;
                        }
                    }
                }
            } else {
                sendOnCoolDown = false;
            }
        }
    }

    /**
     * 模拟一个鼠标的偏移，在内圆到外圆之间才能有效选择法术。 <br>
     * {@link hungteen.imm.mixin.MixinMouseHandler}
     */
    public static void chooseByVector(double x, double y) {
        // 灵敏度。
        final double scale = 0.65D;
        spellMousePositionX += x * scale;
        spellMousePositionY -= y * scale;
        if (spellMousePositionX != 0 && spellMousePositionY != 0) {
            final double delta = Math.atan2(spellMousePositionY, spellMousePositionX);
            final double radius = Math.sqrt(spellMousePositionX * spellMousePositionX + spellMousePositionY * spellMousePositionY);
            final double maxRadius = 500;
            if (radius > maxRadius) {
                spellMousePositionX = maxRadius * Math.cos(delta);
                spellMousePositionY = maxRadius * Math.sin(delta);
            }
            if (radius > 200) {
                lastSelectedPosition = Mth.clamp((int) ((-delta * 4 / Math.PI + 2 + Constants.SPELL_CIRCLE_SIZE) % Constants.SPELL_CIRCLE_SIZE), 0, Constants.SPELL_CIRCLE_SIZE - 1);
            } else { // not effected.
                lastSelectedPosition = -1;
            }
        }
    }

    /**
     * 0表示需要同步客户端的配置文件。 <br>
     * 1为默认的轮盘操作： <br>
     * 根据鼠标偏移决定触发的法术，松开法术按钮即触发。 <br>
     * 2为滚轮式轮盘操作：<br>
     * 打开轮盘，滚轮选择，右击触发并关闭轮盘。 <br>
     */
    public static boolean useDefaultCircle() {
        // Check whether there need sync config file or not.
        final int mode = PlayerUtil.getSpellCircleMode(ClientUtil.player());
        if (mode == 0) {
            NetworkHelper.sendToServer(new ServerSpellPacket(ServerSpellPacket.SpellOption.SYNC_CIRCLE_OP));
            return IMMConfigs.defaultSpellCircle();
        }
        return mode == 1;
    }

    public static void changeCircleMode() {
        final int mode = PlayerUtil.getSpellCircleMode(ClientUtil.player());
        PlayerUtil.setSpellCircleMode(ClientUtil.player(), SpellManager.changeCircleMode(mode));
    }

    public static void switchSpellCircle(int selectedPos) {
        showSpellCircle = !showSpellCircle;
        // Open the spell circle.
        if (showSpellCircle) {
            if (useDefaultCircle()) {
                spellMousePositionX = 0;
                spellMousePositionY = 0;
                lastSelectedPosition = -1;
            } else {
                lastSelectedPosition = Mth.clamp(lastSelectedPosition, 0, Constants.SPELL_CIRCLE_SIZE - 1);
            }
        } else {
            SpellManager.selectSpellOnCircle(selectedPos);
        }
    }

    /**
     * 当鼠标或键盘输入时，进行轮盘操作。
     *
     * @param key 鼠标或键盘的key。
     * @return true if cancel event.
     */
    public static boolean checkSpellCircle(int key) {
        if (!useDefaultCircle() && SpellManager.canUseCircle(ClientUtil.player())) {
            // Switch display of spell circle.
            if (key == IMMKeyBinds.getKeyValue(IMMKeyBinds.SPELL_CIRCLE)) {
                switchSpellCircle(-1);
                return true;
            }
            // Right click to activate spell.
            if (showSpellCircle && key == InputConstants.MOUSE_BUTTON_RIGHT) {
                switchSpellCircle(lastSelectedPosition);
                return true;
            }
        }
        return false;
    }

    /**
     * 当鼠标或键盘输入时，进行轮盘操作。
     *
     * @param delta 滚轮移动方向。
     * @return true if cancel event.
     */
    public static boolean selectOnSpellCircle(double delta) {
        if (!useDefaultCircle() && SpellManager.canUseCircle(ClientUtil.player())) {
            // Scroll to switch select position.
            if (delta != 0.0 && ClientUtil.player() != null && showSpellCircle) {
                lastSelectedPosition = (lastSelectedPosition + (delta < 0 ? 1 : -1) + Constants.SPELL_CIRCLE_SIZE) % Constants.SPELL_CIRCLE_SIZE;
                return true;
            }
        }
        return false;
    }
    
}
