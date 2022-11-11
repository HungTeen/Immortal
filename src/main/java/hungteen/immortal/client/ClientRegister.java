package hungteen.immortal.client;

import hungteen.htlib.client.render.entity.EmptyEffectRender;
import hungteen.htlib.util.helper.ItemHelper;
import hungteen.immortal.client.event.OverlayEvents;
import hungteen.immortal.client.gui.screen.ElixirRoomScreen;
import hungteen.immortal.client.gui.screen.GolemScreen;
import hungteen.immortal.client.gui.screen.SpiritualFurnaceScreen;
import hungteen.immortal.client.gui.tooltip.ClientElementToolTip;
import hungteen.immortal.client.model.ModelLayers;
import hungteen.immortal.client.model.entity.*;
import hungteen.immortal.client.model.bake.ImmortalBakeModels;
import hungteen.immortal.client.particle.ImmortalFlameParticle;
import hungteen.immortal.client.particle.ImmortalParticles;
import hungteen.immortal.client.particle.SpiritualReleasingParticle;
import hungteen.immortal.client.render.entity.*;
import hungteen.immortal.common.entity.ImmortalEntities;
import hungteen.immortal.common.item.eixirs.ElixirItem;
import hungteen.immortal.common.menu.ImmortalMenus;
import hungteen.immortal.common.menu.tooltip.ElementToolTip;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-02 14:59
 **/
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegister {

    @SubscribeEvent
    public static void setUpClient(FMLClientSetupEvent ev){
        ClientHandler.registerCultivatorTypes();
        ev.enqueueWork(() -> {
            registerScreen();
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        /* misc entity */
        event.registerEntityRenderer(ImmortalEntities.FLYING_ITEM.get(), FlyingItemEntityRender::new);
        event.registerEntityRenderer(ImmortalEntities.SPIRITUAL_FLAME.get(), EmptyEffectRender::new);

        /* human */
        event.registerEntityRenderer(ImmortalEntities.CULTIVATOR.get(), CultivatorRender::new);
        /* creature */
        event.registerEntityRenderer(ImmortalEntities.GRASS_CARP.get(), GrassCarpRender::new);
        event.registerEntityRenderer(ImmortalEntities.SILK_WORM.get(), SilkWormRender::new);

        /* undead */
        event.registerEntityRenderer(ImmortalEntities.SPIRITUAL_ZOMBIE.get(), SpiritualZombieRender::new);

        /* golem */
        event.registerEntityRenderer(ImmortalEntities.IRON_GOLEM.get(), IronGolemRender::new);
    }

    /**
     * {@link LayerDefinitions}
     */
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        LayerDefinition INNER_ARMOR = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32);
        LayerDefinition OUTER_ARMOR = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32);

        /* human */
        event.registerLayerDefinition(ModelLayers.CULTIVATOR, () -> CultivatorModel.createBodyLayer(CubeDeformation.NONE, false));
        event.registerLayerDefinition(ModelLayers.CULTIVATOR_INNER_ARMOR, () -> INNER_ARMOR);
        event.registerLayerDefinition(ModelLayers.CULTIVATOR_OUTER_ARMOR, () -> OUTER_ARMOR);
        event.registerLayerDefinition(ModelLayers.CULTIVATOR_SLIM, () -> CultivatorModel.createBodyLayer(CubeDeformation.NONE, true));
        event.registerLayerDefinition(ModelLayers.CULTIVATOR_SLIM_INNER_ARMOR, () -> INNER_ARMOR);
        event.registerLayerDefinition(ModelLayers.CULTIVATOR_SLIM_OUTER_ARMOR, () -> OUTER_ARMOR);

        /* creature */
        event.registerLayerDefinition(ModelLayers.GRASS_CARP, GrassCarpModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayers.SILK_WORM, SilkWormModel::createBodyLayer);

        /* zombie entity */
        event.registerLayerDefinition(ModelLayers.SPIRITUAL_ZOMBIE, SpiritualZombieModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayers.SPIRITUAL_ZOMBIE_INNER_ARMOR, () -> INNER_ARMOR);
        event.registerLayerDefinition(ModelLayers.SPIRITUAL_ZOMBIE_OUTER_ARMOR, () -> OUTER_ARMOR);

        /* golem */
        event.registerLayerDefinition(ModelLayers.IRON_GOLEM, IronGolemModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerFactories(RegisterParticleProvidersEvent event) {
        event.register(ImmortalParticles.IMMORTAL_FLAME.get(), ImmortalFlameParticle.Factory::new) ;
        event.register(ImmortalParticles.SPIRITUAL_RELEASING.get(), SpiritualReleasingParticle.Factory::new) ;
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event){
        OverlayEvents.registerOverlay(event);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterKeyMappingsEvent event){
        ImmortalKeyBinds.register(event);
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event){
        ItemHelper.getFilterItems(ElixirItem.class::isInstance).stream().map(ElixirItem.class::cast).forEach(elixirItem -> {
            event.register((stack, id) -> elixirItem.getColor(id), elixirItem);
        });
    }

    @SubscribeEvent
    public static void registerTooltips(RegisterClientTooltipComponentFactoriesEvent event){
        event.register(ElementToolTip.class, ClientElementToolTip::new);
    }

    @SubscribeEvent
    public static void bakeModel(ModelEvent.BakingCompleted event) {
        ImmortalBakeModels.registerBakeModels(event);
    }

    @SubscribeEvent
    public static void bakeModel(ModelEvent.RegisterAdditional event) {
        ImmortalBakeModels.registerBakeModels(event);
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent ev){

    }

    public static void registerScreen() {
        MenuScreens.register(ImmortalMenus.SPIRITUAL_FURNACE.get(), SpiritualFurnaceScreen::new);
        MenuScreens.register(ImmortalMenus.ELIXIR_ROOM.get(), ElixirRoomScreen::new);
        MenuScreens.register(ImmortalMenus.GOLEM_INVENTORY.get(), GolemScreen::new);
    }

}
