package hungteen.imm.client.event.handler;

import com.mojang.blaze3d.platform.InputConstants;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.IMMConfigs;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.client.ClientData;
import hungteen.imm.client.IMMClientProxy;
import hungteen.imm.client.ClientUtil;
import hungteen.imm.client.IMMKeyBinds;
import hungteen.imm.common.network.NetworkHandler;
import hungteen.imm.common.network.SpellPacket;
import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.util.Constants;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-27 11:16
 **/
public class SpellHandler {

    /**
     * {@link hungteen.imm.client.event.ClientEvents#tick(ClientTickEvent.Post)}
     */
    public static void tick(@NotNull Player player) {
        // 不能使用轮盘时，强制关闭。
        if (ClientData.ShowSpellCircle) {
            if (!SpellManager.canUseCircle(player)) {
                ClientData.ShowSpellCircle = false;
            }
        }
        // 更新轮盘的改变。
        if (SpellManager.canUseCircle(player) && useDefaultCircle()) {
            if (ClientData.ShowSpellCircle ^ IMMKeyBinds.displayingSpellCircle()) {
                switchSpellCircle(ClientData.LastSelectedPosition);
            }
        }
        // 长按以检测法术释放。
        if (IMMClientProxy.MC.isWindowActive() && ClientUtil.screen() == null) {
            if (IMMKeyBinds.ACTIVATE_SPELL.isDown()) {
                final ISpellType spell = PlayerUtil.getPreparingSpell(player);
                if (EntityHelper.isEntityValid(player) && spell != null) {
                    if (! PlayerUtil.isSpellOnCoolDown(player, spell)) {
                        NetworkHandler.sendToServer(new SpellPacket(SpellPacket.SpellOption.ACTIVATE));
                    } else {
                        // 冷却提醒。
                        if(! ClientData.sendOnCoolDown){
                            PlayerHelper.sendTipTo(player, SpellManager.SPELL_ON_CD.withStyle(ChatFormatting.RED));
                            ClientData.sendOnCoolDown = true;
                        }
                    }
                }
            } else {
                ClientData.sendOnCoolDown = false;
            }
        }
    }

    /**
     * 模拟一个鼠标的偏移，在内圆到外圆之间才能有效选择法术。 <br>
     * {@link hungteen.imm.mixin.MixinMouseHandler}
     */
    public static void chooseByVector(double x, double y) {
        final double scale = 0.65D; // 灵敏度。
        ClientData.SpellMousePositionX += x * scale;
        ClientData.SpellMousePositionY -= y * scale;
        if (ClientData.SpellMousePositionX != 0 && ClientData.SpellMousePositionY != 0) {
            final double delta = Math.atan2(ClientData.SpellMousePositionY, ClientData.SpellMousePositionX);
            final double radius = Math.sqrt(ClientData.SpellMousePositionX * ClientData.SpellMousePositionX + ClientData.SpellMousePositionY * ClientData.SpellMousePositionY);
            final double maxRadius = 500;
            if (radius > maxRadius) {
                ClientData.SpellMousePositionX = maxRadius * Math.cos(delta);
                ClientData.SpellMousePositionY = maxRadius * Math.sin(delta);
            }
            if (radius > 200) {
                ClientData.LastSelectedPosition = Mth.clamp((int) ((-delta * 4 / Math.PI + 2 + Constants.SPELL_CIRCLE_SIZE) % Constants.SPELL_CIRCLE_SIZE), 0, Constants.SPELL_CIRCLE_SIZE - 1);
            } else { // not effected.
                ClientData.LastSelectedPosition = -1;
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
            NetworkHandler.sendToServer(new SpellPacket(SpellPacket.SpellOption.SYNC_CIRCLE_OP));
            return IMMConfigs.defaultSpellCircle();
        }
        return mode == 1;
    }

    public static void changeCircleMode() {
        final int mode = PlayerUtil.getSpellCircleMode(ClientUtil.player());
        PlayerUtil.setSpellCircleMode(ClientUtil.player(), SpellManager.changeCircleMode(mode));
    }

    public static void switchSpellCircle(int selectedPos) {
        ClientData.ShowSpellCircle = !ClientData.ShowSpellCircle;
        // Open the spell circle.
        if (ClientData.ShowSpellCircle) {
            if (useDefaultCircle()) {
                ClientData.SpellMousePositionX = 0;
                ClientData.SpellMousePositionY = 0;
                ClientData.LastSelectedPosition = -1;
            } else {
                ClientData.LastSelectedPosition = Mth.clamp(ClientData.LastSelectedPosition, 0, Constants.SPELL_CIRCLE_SIZE - 1);
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
            if (ClientData.ShowSpellCircle && key == InputConstants.MOUSE_BUTTON_RIGHT) {
                switchSpellCircle(ClientData.LastSelectedPosition);
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
        if (!SpellHandler.useDefaultCircle() && SpellManager.canUseCircle(ClientUtil.player())) {
            // Scroll to switch select position.
            if (delta != 0.0 && ClientUtil.player() != null && ClientData.ShowSpellCircle) {
                ClientData.LastSelectedPosition = (ClientData.LastSelectedPosition + (delta < 0 ? 1 : -1) + Constants.SPELL_CIRCLE_SIZE) % Constants.SPELL_CIRCLE_SIZE;
                return true;
            }
        }
        return false;
    }

}
