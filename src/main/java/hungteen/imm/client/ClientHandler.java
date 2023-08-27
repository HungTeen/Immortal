package hungteen.imm.client;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import hungteen.imm.common.blockentity.SmithingArtifactBlockEntity;
import hungteen.imm.common.entity.human.cultivator.CultivatorTypes;
import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.GameProfileCache;
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
    public static void registerCultivatorTypes() {
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

    public static void startSmithing(ItemStack stack, SmithingArtifactBlockEntity blockEntity) {
        ClientData.StartSmithing = true;
        ClientData.SmithingDirection = true;
        ClientData.SmithingProgress = 0;
        ClientData.BestPointDisplayTick = 0;
//        ClientDatas.SmithingSpeedMultiple = blockEntity.getSmithingSpeedMultiple();
    }

    /**
     * {@link hungteen.imm.client.event.ClientEvents#tick(TickEvent.ClientTickEvent)}
     */
    public static void onSmithing() {
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

    public static void quitSmithing() {
        ClientData.StartSmithing = false;
        ClientData.SmithingDirection = true;
        ClientData.SmithingProgress = 0;
        ClientData.BestPointDisplayTick = 0;
        ClientData.SmithingSpeedMultiple = 1F;
    }

}
