package hungteen.immortal.client;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.immortal.common.spell.SpellManager;
import hungteen.immortal.common.blockentity.SmithingArtifactBlockEntity;
import hungteen.immortal.common.entity.human.cultivator.CultivatorTypes;
import hungteen.immortal.common.network.NetworkHandler;
import hungteen.immortal.common.network.SpellPacket;
import hungteen.immortal.utils.Constants;
import hungteen.immortal.utils.PlayerUtil;
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
        ClientDatas.StartSmithing = true;
        ClientDatas.SmithingDirection = true;
        ClientDatas.SmithingProgress = 0;
        ClientDatas.BestPointDisplayTick = 0;
//        ClientDatas.SmithingSpeedMultiple = blockEntity.getSmithingSpeedMultiple();
    }

    /**
     * {@link hungteen.immortal.client.event.ClientEvents#tick(TickEvent.ClientTickEvent)}
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
     * {@link hungteen.immortal.client.event.ClientEvents#tick(TickEvent.ClientTickEvent)}
     */
    public static void tickSpellCircle(){
        // update change.
        if(useDefaultCircle()){
            if(ClientDatas.ShowSpellCircle ^ ImmortalKeyBinds.displayingSpellCircle()){
                // Close spell circle and activate spell.
                if(ClientDatas.ShowSpellCircle){
                    SpellManager.activateAt(ClientDatas.lastSelectedPosition);
                }
                switchSpellCircle();
            }
        }
    }

    /**
     * 模拟一个鼠标的偏移，在内圆到外圆之间才能有效选择法术。 <br>
     * {@link hungteen.immortal.mixin.MixinMouseHandler}
     */
    public static void chooseByVector(double x, double y){
        final double scale = 0.65D; // 灵敏度。
        ClientDatas.SpellMousePositionX += x * scale;
        ClientDatas.SpellMousePositionY -= y * scale;
        if(ClientDatas.SpellMousePositionX != 0 && ClientDatas.SpellMousePositionY != 0) {
            final double delta = Math.atan2(ClientDatas.SpellMousePositionY, ClientDatas.SpellMousePositionX);
            final double radius = Math.sqrt(ClientDatas.SpellMousePositionX * ClientDatas.SpellMousePositionX + ClientDatas.SpellMousePositionY * ClientDatas.SpellMousePositionY);
            final double maxRadius = 500;
            if (radius > maxRadius) {
                ClientDatas.SpellMousePositionX = maxRadius * Math.cos(delta);
                ClientDatas.SpellMousePositionY = maxRadius * Math.sin(delta);
            }
            if (radius > 200) {
                ClientDatas.lastSelectedPosition = Mth.clamp((int) ((-delta * 4 / Math.PI + 2 + Constants.SPELL_CIRCLE_SIZE) % Constants.SPELL_CIRCLE_SIZE), 0, Constants.SPELL_CIRCLE_SIZE - 1);
            } else { // not effected.
                ClientDatas.lastSelectedPosition = -1;
            }
        }
    }

    public static void quitSmithing(){
        ClientDatas.StartSmithing = false;
        ClientDatas.SmithingDirection = true;
        ClientDatas.SmithingProgress = 0;
        ClientDatas.BestPointDisplayTick = 0;
        ClientDatas.SmithingSpeedMultiple = 1F;
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
        ClientDatas.ShowSpellCircle = ! ClientDatas.ShowSpellCircle;
        // Open the spell circle.
        if(ClientDatas.ShowSpellCircle){
            // check whether there need sync config file or not.
            if(PlayerUtil.requireSyncCircle(PlayerHelper.getClientPlayer())){
                NetworkHandler.sendToServer(new SpellPacket(null, SpellPacket.SpellOptions.ACTIVATE, ClientDatas.lastSelectedPosition));
            }
            ClientDatas.SpellMousePositionX = 0;
            ClientDatas.SpellMousePositionY = 0;
            ClientDatas.lastSelectedPosition = useDefaultCircle() ? -1 : 0;
        }
    }
}
