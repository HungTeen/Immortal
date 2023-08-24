package hungteen.imm.client;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.common.spell.SpellManager;
import hungteen.imm.common.blockentity.SmithingArtifactBlockEntity;
import hungteen.imm.common.entity.human.cultivator.CultivatorTypes;
import hungteen.imm.common.network.NetworkHandler;
import hungteen.imm.common.network.SpellPacket;
import hungteen.imm.util.Constants;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-23 12:35
 **/
public class ClientHandler {

    /**
     * {@link ClientRegister#setUpClient(FMLClientSetupEvent)}
     */
    public static void registerCultivatorTypes(){
        Arrays.stream(CultivatorTypes.values()).filter((cultivatorTypes -> cultivatorTypes.getProfileUUID().isPresent() && cultivatorTypes.getProfileName().isPresent())).forEach(value -> {
            AtomicReference<GameProfile> profile = new AtomicReference<>(new GameProfile(value.getProfileUUID().get(), value.getProfileName().get()));

            YggdrasilAuthenticationService yggdrasilauthenticationservice = new YggdrasilAuthenticationService(ClientProxy.MC.getProxy());
            GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
            GameProfileCache gameprofilecache = new GameProfileCache(gameprofilerepository, new File(ClientProxy.MC.gameDirectory, MinecraftServer.ANONYMOUS_PLAYER_PROFILE.getName()));
            gameprofilecache.setExecutor(ClientProxy.MC);

            ClientHandler.updateGameProfile(gameprofilecache, ClientProxy.MC.getMinecraftSessionService(), profile.get(), value::setGameProfile);
        });
    }

    /**
     * Similar with {@link net.minecraft.world.level.block.entity.SkullBlockEntity#updateGameprofile(GameProfile, Consumer)}
     */
    public static void updateGameProfile(GameProfileCache profileCache, MinecraftSessionService sessionService, @Nullable GameProfile gameProfile, Consumer<GameProfile> consumer) {
        if (gameProfile != null && !StringUtil.isNullOrEmpty(gameProfile.getName()) && (!gameProfile.isComplete() || !gameProfile.getProperties().containsKey("textures")) && profileCache != null && sessionService != null) {
            profileCache.getAsync(gameProfile.getName(), (gameProfile1) -> {
                Util.backgroundExecutor().execute(() -> {
                    Util.ifElse(gameProfile1, (gameProfile2) -> {
                        Property property = Iterables.getFirst(gameProfile2.getProperties().get("textures"), null);
                        if (property == null) {
                            gameProfile2 = sessionService.fillProfileProperties(gameProfile2, true);
                        }

                        GameProfile gameprofile = gameProfile2;
                        ClientProxy.MC.execute(() -> {
                            profileCache.add(gameprofile);
                            consumer.accept(gameprofile);
                        });
                    }, () -> {
                        ClientProxy.MC.execute(() -> {
                            consumer.accept(gameProfile);
                        });
                    });
                });
            });
        } else {
            consumer.accept(gameProfile);
        }
    }

    public static void startSmithing(ItemStack stack, SmithingArtifactBlockEntity blockEntity){
        ClientData.StartSmithing = true;
        ClientData.SmithingDirection = true;
        ClientData.SmithingProgress = 0;
        ClientData.BestPointDisplayTick = 0;
//        ClientDatas.SmithingSpeedMultiple = blockEntity.getSmithingSpeedMultiple();
    }

    /**
     * {@link hungteen.imm.client.event.ClientEvents#tick(TickEvent.ClientTickEvent)}
     */
    public static void onSmithing(){
//        if(ClientDatas.StartSmithing){
//            ++ ClientDatas.BestPointDisplayTick;
//            if(ClientDatas.BestPointDisplayTick >= (Constants.DISPLAY_BEST_SMITHING_POINT_CD << 1)){
//                ClientDatas.BestPointDisplayTick = 0;
//            }
//            ClientDatas.SmithingProgress += (ClientDatas.SmithingDirection ? 1 : -1 ) * ClientDatas.SmithingSpeedMultiple;
//            // change move direction.
//            if(ClientDatas.SmithingDirection && ClientDatas.SmithingProgress >= SmithingArtifactBlockEntity.MAX_PROGRESS_VALUE){
//                ClientDatas.SmithingDirection = false;
//            } else if(! ClientDatas.SmithingDirection && ClientDatas.SmithingProgress <= 0){
//                ClientDatas.SmithingDirection = true;
//            }
//        }
    }

    /**
     * {@link hungteen.imm.client.event.ClientEvents#tick(TickEvent.ClientTickEvent)}
     */
    public static void tickSpellCircle(){
        // 不能使用轮盘时，强制关闭。
        if(ClientData.ShowSpellCircle){
            if(! SpellManager.canUseCircle(ClientUtil.player())){
                ClientData.ShowSpellCircle = false;
            }
        }
        // update change.
        if(useDefaultCircle()){
            if(ClientData.ShowSpellCircle ^ IMMKeyBinds.displayingSpellCircle()){
                // Close spell circle and activate spell.
                if(ClientData.ShowSpellCircle){
                    SpellManager.selectSpellOnCircle(ClientData.lastSelectedPosition);
                }
                switchSpellCircle();
            }
        }
    }

    /**
     * 模拟一个鼠标的偏移，在内圆到外圆之间才能有效选择法术。 <br>
     * {@link hungteen.imm.mixin.MixinMouseHandler}
     */
    public static void chooseByVector(double x, double y){
        final double scale = 0.65D; // 灵敏度。
        ClientData.SpellMousePositionX += x * scale;
        ClientData.SpellMousePositionY -= y * scale;
        if(ClientData.SpellMousePositionX != 0 && ClientData.SpellMousePositionY != 0) {
            final double delta = Math.atan2(ClientData.SpellMousePositionY, ClientData.SpellMousePositionX);
            final double radius = Math.sqrt(ClientData.SpellMousePositionX * ClientData.SpellMousePositionX + ClientData.SpellMousePositionY * ClientData.SpellMousePositionY);
            final double maxRadius = 500;
            if (radius > maxRadius) {
                ClientData.SpellMousePositionX = maxRadius * Math.cos(delta);
                ClientData.SpellMousePositionY = maxRadius * Math.sin(delta);
            }
            if (radius > 200) {
                ClientData.lastSelectedPosition = Mth.clamp((int) ((-delta * 4 / Math.PI + 2 + Constants.SPELL_CIRCLE_SIZE) % Constants.SPELL_CIRCLE_SIZE), 0, Constants.SPELL_CIRCLE_SIZE - 1);
            } else { // not effected.
                ClientData.lastSelectedPosition = -1;
            }
        }
    }

    public static void quitSmithing(){
        ClientData.StartSmithing = false;
        ClientData.SmithingDirection = true;
        ClientData.SmithingProgress = 0;
        ClientData.BestPointDisplayTick = 0;
        ClientData.SmithingSpeedMultiple = 1F;
    }

    /**
     * 默认的轮盘操作： <br>
     * 根据鼠标偏移决定触发的法术，松开法术按钮即触发。 <br>
     * 滚轮式轮盘操作：<br>
     * 打开轮盘，滚轮选择，右击触发并关闭轮盘。 <br>
     */
    public static boolean useDefaultCircle(){
        return PlayerUtil.useDefaultCircle(ClientProxy.MC.player);
    }

    public static void switchSpellCircle(){
        ClientData.ShowSpellCircle = ! ClientData.ShowSpellCircle;
        // Open the spell circle.
        if(ClientData.ShowSpellCircle){
            // check whether there need sync config file or not.
            PlayerHelper.getClientPlayer().ifPresent(player -> {
                if(PlayerUtil.requireSyncCircle(player)){
                    NetworkHandler.sendToServer(new SpellPacket(null, SpellPacket.SpellOptions.SELECT_ON_CIRCLE, ClientData.lastSelectedPosition));
                }
            });
            ClientData.SpellMousePositionX = 0;
            ClientData.SpellMousePositionY = 0;
            ClientData.lastSelectedPosition = useDefaultCircle() ? -1 : 0;
        }
    }
}
