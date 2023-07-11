package hungteen.imm.client;

import hungteen.htlib.client.render.entity.EmptyEffectRender;
import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.client.event.OverlayEvents;
import hungteen.imm.client.gui.screen.*;
import hungteen.imm.client.gui.tooltip.ClientArtifactToolTip;
import hungteen.imm.client.gui.tooltip.ClientElementToolTip;
import hungteen.imm.client.model.ModelLayers;
import hungteen.imm.client.model.bake.ImmortalBakeModels;
import hungteen.imm.client.model.entity.*;
import hungteen.imm.client.model.entity.golem.CopperGolemModel;
import hungteen.imm.client.model.entity.golem.CreeperGolemModel;
import hungteen.imm.client.model.entity.golem.IronGolemModel;
import hungteen.imm.client.model.entity.golem.SnowGolemModel;
import hungteen.imm.client.particle.IMMFlameParticle;
import hungteen.imm.client.particle.IMMParticles;
import hungteen.imm.client.particle.SpiritParticle;
import hungteen.imm.client.particle.SpiritualManaParticle;
import hungteen.imm.client.render.LevelRenderStages;
import hungteen.imm.client.render.entity.FlyingItemEntityRender;
import hungteen.imm.client.render.entity.creature.monster.SharpStakeRender;
import hungteen.imm.client.render.entity.golem.CopperGolemRender;
import hungteen.imm.client.render.entity.golem.CreeperGolemRender;
import hungteen.imm.client.render.entity.golem.IronGolemRender;
import hungteen.imm.client.render.entity.golem.SnowGolemRender;
import hungteen.imm.client.render.entity.human.CommonVillagerRender;
import hungteen.imm.client.render.entity.human.CultivatorRender;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.item.elixirs.ElixirItem;
import hungteen.imm.common.item.talismans.TalismanItem;
import hungteen.imm.common.menu.IMMMenus;
import hungteen.imm.common.menu.tooltip.ArtifactToolTip;
import hungteen.imm.common.menu.tooltip.ElementToolTip;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
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
            registerItemProperties();
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        /* misc entity */
        event.registerEntityRenderer(IMMEntities.TELEPORT_FORMATION.get(), EmptyEffectRender::new);
        event.registerEntityRenderer(IMMEntities.SPIRITUAL_PEARL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(IMMEntities.FLYING_ITEM.get(), FlyingItemEntityRender::new);
//        event.registerEntityRenderer(ImmortalEntities.SPIRITUAL_FLAME.get(), EmptyEffectRender::new);

        /* human */
        event.registerEntityRenderer(IMMEntities.COMMON_VILLAGER.get(), CommonVillagerRender::new);
        event.registerEntityRenderer(IMMEntities.EMPTY_CULTIVATOR.get(), CultivatorRender::new);
        event.registerEntityRenderer(IMMEntities.SPIRITUAL_CULTIVATOR.get(), CultivatorRender::new);

        /* creature */
//        event.registerEntityRenderer(ImmortalEntities.GRASS_CARP.get(), GrassCarpRender::new);
//        event.registerEntityRenderer(ImmortalEntities.SILK_WORM.get(), SilkWormRender::new);

        /* Monster */
        event.registerEntityRenderer(IMMEntities.SHARP_STAKE.get(), SharpStakeRender::new);

//        /* undead */
//        event.registerEntityRenderer(ImmortalEntities.SPIRITUAL_ZOMBIE.get(), SpiritualZombieRender::new);

        /* golem */
        event.registerEntityRenderer(IMMEntities.IRON_GOLEM.get(), IronGolemRender::new);
        event.registerEntityRenderer(IMMEntities.SNOW_GOLEM.get(), SnowGolemRender::new);
        event.registerEntityRenderer(IMMEntities.CREEPER_GOLEM.get(), CreeperGolemRender::new);
        event.registerEntityRenderer(IMMEntities.COPPER_GOLEM.get(), CopperGolemRender::new);
    }

    /**
     * {@link LayerDefinitions}
     */
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        LayerDefinition INNER_ARMOR = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32);
        LayerDefinition OUTER_ARMOR = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32);

        /* human */
        event.registerLayerDefinition(ModelLayers.VILLAGER, VillagerModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayers.PILLAGER, PillagerModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayers.CULTIVATOR, () -> CultivatorModel.createBodyLayer(CubeDeformation.NONE, false));
        event.registerLayerDefinition(ModelLayers.CULTIVATOR_INNER_ARMOR, () -> INNER_ARMOR);
        event.registerLayerDefinition(ModelLayers.CULTIVATOR_OUTER_ARMOR, () -> OUTER_ARMOR);
        event.registerLayerDefinition(ModelLayers.CULTIVATOR_SLIM, () -> CultivatorModel.createBodyLayer(CubeDeformation.NONE, true));
        event.registerLayerDefinition(ModelLayers.CULTIVATOR_SLIM_INNER_ARMOR, () -> INNER_ARMOR);
        event.registerLayerDefinition(ModelLayers.CULTIVATOR_SLIM_OUTER_ARMOR, () -> OUTER_ARMOR);

        /* creature */
        event.registerLayerDefinition(ModelLayers.GRASS_CARP, GrassCarpModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayers.SILK_WORM, SilkWormModel::createBodyLayer);

        /* Monster */
        event.registerLayerDefinition(ModelLayers.SHARP_STAKE, EmptyModel::createBodyLayer);

        /* zombie entity */
        event.registerLayerDefinition(ModelLayers.SPIRITUAL_ZOMBIE, SpiritualZombieModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayers.SPIRITUAL_ZOMBIE_INNER_ARMOR, () -> INNER_ARMOR);
        event.registerLayerDefinition(ModelLayers.SPIRITUAL_ZOMBIE_OUTER_ARMOR, () -> OUTER_ARMOR);

        /* golem */
        event.registerLayerDefinition(ModelLayers.IRON_GOLEM, IronGolemModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayers.SNOW_GOLEM, SnowGolemModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayers.CREEPER_GOLEM, CreeperGolemModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayers.COPPER_GOLEM, CopperGolemModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(IMMParticles.IMMORTAL_FLAME.get(), IMMFlameParticle.Factory::new);
        event.registerSpriteSet(IMMParticles.SPIRITUAL_MANA.get(), SpiritualManaParticle.Factory::new);
        event.registerSpriteSet(IMMParticles.SPIRIT.get(), SpiritParticle.Factory::new);
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
        ItemHelper.get().filterValues(ElixirItem.class::isInstance).stream().map(ElixirItem.class::cast).forEach(elixirItem -> {
            event.register((stack, id) -> elixirItem.getColor(id), elixirItem);
        });
    }

    @SubscribeEvent
    public static void registerTooltips(RegisterClientTooltipComponentFactoriesEvent event){
        event.register(ElementToolTip.class, ClientElementToolTip::new);
        event.register(ArtifactToolTip.class, ClientArtifactToolTip::new);
    }

    @SubscribeEvent
    public static void registerLevelRenderStages(RenderLevelStageEvent.RegisterStageEvent event){
        LevelRenderStages.init(event);
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
        MenuScreens.register(IMMMenus.CULTIVATOR_TRADE.get(), MerchantTradeScreen::new);
        MenuScreens.register(IMMMenus.SPIRITUAL_FURNACE.get(), SpiritualFurnaceScreen::new);
//        MenuScreens.register(ImmortalMenus.ELIXIR_ROOM.get(), ElixirRoomScreen::new);
//        MenuScreens.register(ImmortalMenus.SMITHING_ARTIFACT.get(), SmithingArtifactScreen::new);
        MenuScreens.register(IMMMenus.GOLEM_INVENTORY.get(), GolemInventoryScreen::new);
        MenuScreens.register(IMMMenus.RUNE_CRAFT.get(), RuneCraftScreen::new);
        MenuScreens.register(IMMMenus.RUNE_GATE.get(), RuneGateScreen::new);
        MenuScreens.register(IMMMenus.RUNE_BIND.get(), RuneBindScreen::new);
    }

    public static void registerItemProperties(){
        ItemHelper.get().filterValues(TalismanItem.class::isInstance).forEach(talisman -> {
            ItemProperties.register(talisman, TalismanItem.ACTIVATE_PROPERTY, (stack, level, entity, val) -> {
                return TalismanItem.isActivated(stack) ? 0F : 1F;
            });
        });
    }
}
