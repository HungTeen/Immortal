package hungteen.imm.client;

import hungteen.htlib.util.helper.impl.ItemHelper;
import hungteen.imm.client.data.ClientData;
import hungteen.imm.common.blockentity.SmithingArtifactBlockEntity;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.item.artifact.WoodBowItem;
import hungteen.imm.common.item.talisman.TalismanItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-23 12:35
 **/
public class ClientHandler {

    private static final ItemPropertyFunction USED = (stack, level, entity, val) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
    private static final ItemPropertyFunction USING = (stack, level, entity, val) -> {
        if (entity == null) {
            return 0.0F;
        } else {
            return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
        }
    };

    public static void registerItemProperties(){
        ItemHelper.get().filterValues(TalismanItem.class::isInstance).forEach(talisman -> {
            ItemProperties.register(talisman, TalismanItem.ACTIVATED, USED);
            ItemProperties.register(talisman, TalismanItem.ACTIVATING, USING);
        });
        ItemProperties.register(IMMItems.WOOD_BOW.get(), WoodBowItem.PULL, USED);
        ItemProperties.register(IMMItems.WOOD_BOW.get(), WoodBowItem.PULLING, USING);
    }

    public static void registerScreen() {
//        MenuScreens.initialize(IMMMenus.CULTIVATOR_TRADE.get(), MerchantTradeScreen::new);
//        MenuScreens.initialize(IMMMenus.SPIRITUAL_FURNACE.get(), SpiritualFurnaceScreen::new);
//        MenuScreens.initialize(IMMMenus.ELIXIR_ROOM.get(), ElixirRoomScreen::new);
////        MenuScreens.initialize(ImmortalMenus.SMITHING_ARTIFACT.get(), SmithingArtifactScreen::new);
//        MenuScreens.initialize(IMMMenus.GOLEM_INVENTORY.get(), GolemInventoryScreen::new);
//        MenuScreens.initialize(IMMMenus.RUNE_CRAFT.get(), RuneCraftScreen::new);
//        MenuScreens.initialize(IMMMenus.RUNE_GATE.get(), RuneGateScreen::new);
//        MenuScreens.initialize(IMMMenus.RUNE_BIND.get(), RuneBindScreen::new);
    }

    /**
     * {@link ClientRegister#setUpClient(FMLClientSetupEvent)}
     */
    public static void registerCultivatorTypes() {
//        Arrays.stream(CultivatorTypes.values()).filter((cultivatorTypes -> cultivatorTypes.getProfileUUID().isPresent() && cultivatorTypes.getProfileName().isPresent())).forEach(value -> {
//            AtomicReference<GameProfile> profile = new AtomicReference<>(new GameProfile(value.getProfileUUID().get(), value.getProfileName().get()));
//
//            YggdrasilAuthenticationService yggdrasilauthenticationservice = new YggdrasilAuthenticationService(IMMClientProxy.MC.getProxy());
//            GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
//            GameProfileCache gameprofilecache = new GameProfileCache(gameprofilerepository, new File(IMMClientProxy.MC.gameDirectory, MinecraftServer.ANONYMOUS_PLAYER_PROFILE.getName()));
//            gameprofilecache.setExecutor(IMMClientProxy.MC);
//
//            ClientHandler.updateGameProfile(gameprofilecache, ClientUtil.mc().getMinecraftSessionService(), profile.get(), value::setGameProfile);
//        });
    }

//    /**
//     * Similar with {@link SkullBlockEntity#updateOwnerProfile()}
//     */
//    public static void updateGameProfile(GameProfileCache profileCache, MinecraftSessionService sessionService, @Nullable GameProfile gameProfile, Consumer<GameProfile> consumer) {
//        if (gameProfile != null && !StringUtil.isNullOrEmpty(gameProfile.getName()) && (!gameProfile.isComplete() || !gameProfile.getProperties().containsKey("textures")) && profileCache != null && sessionService != null) {
//            profileCache.getAsync(gameProfile.getName(), (gameProfile1) -> {
//                Util.backgroundExecutor().execute(() -> {
//                    Util.ifElse(gameProfile1, (gameProfile2) -> {
//                        Property property = Iterables.getFirst(gameProfile2.getProperties().get("textures"), null);
//                        if (property == null) {
//                            gameProfile2 = sessionService.fillProfileProperties(gameProfile2, true);
//                        }
//
//                        GameProfile gameprofile = gameProfile2;
//                        ClientProxy.MC.execute(() -> {
//                            profileCache.add(gameprofile);
//                            consumer.accept(gameprofile);
//                        });
//                    }, () -> {
//                        ClientProxy.MC.execute(() -> {
//                            consumer.accept(gameProfile);
//                        });
//                    });
//                });
//            });
//        } else {
//            consumer.accept(gameProfile);
//        }
//    }

    public static void startSmithing(ItemStack stack, SmithingArtifactBlockEntity blockEntity) {
        ClientData.StartSmithing = true;
        ClientData.SmithingDirection = true;
        ClientData.SmithingProgress = 0;
        ClientData.BestPointDisplayTick = 0;
//        ClientDatas.SmithingSpeedMultiple = blockEntity.getSmithingSpeedMultiple();
    }

    /**
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
