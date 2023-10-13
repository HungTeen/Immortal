package hungteen.imm.client;

import hungteen.htlib.client.render.entity.EmptyEffectRender;
import hungteen.htlib.util.helper.registry.ItemHelper;
import hungteen.imm.client.gui.overlay.CommonOverlay;
import hungteen.imm.client.gui.overlay.ElementOverlay;
import hungteen.imm.client.gui.overlay.MeditationOverlay;
import hungteen.imm.client.gui.overlay.SpellOverlay;
import hungteen.imm.client.gui.screen.*;
import hungteen.imm.client.gui.screen.furnace.ElixirRoomScreen;
import hungteen.imm.client.gui.screen.furnace.SpiritualFurnaceScreen;
import hungteen.imm.client.gui.tooltip.ClientArtifactToolTip;
import hungteen.imm.client.gui.tooltip.ClientElementToolTip;
import hungteen.imm.client.model.IMMModelLayers;
import hungteen.imm.client.model.bake.IMMBakeModels;
import hungteen.imm.client.model.entity.*;
import hungteen.imm.client.model.entity.golem.CopperGolemModel;
import hungteen.imm.client.model.entity.golem.CreeperGolemModel;
import hungteen.imm.client.model.entity.golem.IronGolemModel;
import hungteen.imm.client.model.entity.golem.SnowGolemModel;
import hungteen.imm.client.model.entity.villager.PillagerModel;
import hungteen.imm.client.model.entity.villager.VillagerModel;
import hungteen.imm.client.particle.*;
import hungteen.imm.client.render.block.FurnaceBlockEntityRender;
import hungteen.imm.client.render.entity.creature.monster.BiFangRender;
import hungteen.imm.client.render.entity.misc.ElementAmethystRender;
import hungteen.imm.client.render.level.LevelRenderStages;
import hungteen.imm.client.render.entity.misc.FlyingItemEntityRender;
import hungteen.imm.client.render.entity.creature.monster.SharpStakeRender;
import hungteen.imm.client.render.entity.golem.CopperGolemRender;
import hungteen.imm.client.render.entity.golem.CreeperGolemRender;
import hungteen.imm.client.render.entity.golem.IronGolemRender;
import hungteen.imm.client.render.entity.golem.SnowGolemRender;
import hungteen.imm.client.render.entity.human.CommonVillagerRender;
import hungteen.imm.client.render.entity.human.CultivatorRender;
import hungteen.imm.client.render.entity.misc.ThrowingItemEntityRender;
import hungteen.imm.common.blockentity.IMMBlockEntities;
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
        event.registerEntityRenderer(IMMEntities.ELEMENT_AMETHYST.get(), ElementAmethystRender::new);
        event.registerEntityRenderer(IMMEntities.TELEPORT_FORMATION.get(), EmptyEffectRender::new);
        event.registerEntityRenderer(IMMEntities.SPIRITUAL_PEARL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(IMMEntities.FLYING_ITEM.get(), FlyingItemEntityRender::new);
        event.registerEntityRenderer(IMMEntities.THROWING_ITEM.get(), ThrowingItemEntityRender::new);
        event.registerEntityRenderer(IMMEntities.SPIRITUAL_FLAME.get(), EmptyEffectRender::new);

        /* human */
        event.registerEntityRenderer(IMMEntities.COMMON_VILLAGER.get(), CommonVillagerRender::new);
        event.registerEntityRenderer(IMMEntities.EMPTY_CULTIVATOR.get(), CultivatorRender::new);
        event.registerEntityRenderer(IMMEntities.SPIRITUAL_BEGINNER_CULTIVATOR.get(), CultivatorRender::new);

        /* creature */
//        event.registerEntityRenderer(ImmortalEntities.GRASS_CARP.get(), GrassCarpRender::new);
//        event.registerEntityRenderer(ImmortalEntities.SILK_WORM.get(), SilkWormRender::new);

        /* Monster */
        event.registerEntityRenderer(IMMEntities.SHARP_STAKE.get(), SharpStakeRender::new);
        event.registerEntityRenderer(IMMEntities.BI_FANG.get(), BiFangRender::new);

//        /* undead */
//        event.registerEntityRenderer(ImmortalEntities.SPIRITUAL_ZOMBIE.get(), SpiritualZombieRender::new);

        /* golem */
        event.registerEntityRenderer(IMMEntities.IRON_GOLEM.get(), IronGolemRender::new);
        event.registerEntityRenderer(IMMEntities.SNOW_GOLEM.get(), SnowGolemRender::new);
        event.registerEntityRenderer(IMMEntities.CREEPER_GOLEM.get(), CreeperGolemRender::new);
        event.registerEntityRenderer(IMMEntities.COPPER_GOLEM.get(), CopperGolemRender::new);

        /* block entity */
        event.registerBlockEntityRenderer(IMMBlockEntities.SPIRITUAL_FURNACE.get(), FurnaceBlockEntityRender::new);
    }

    /**
     * {@link LayerDefinitions}
     */
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        LayerDefinition INNER_ARMOR = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32);
        LayerDefinition OUTER_ARMOR = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32);

        /* Misc */
        event.registerLayerDefinition(IMMModelLayers.ELEMENT_AMETHYST, ElementAmethystModel::createBodyLayer);

        /* human */
        event.registerLayerDefinition(IMMModelLayers.VILLAGER, VillagerModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.PILLAGER, PillagerModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.CULTIVATOR, () -> CultivatorModel.createBodyLayer(CubeDeformation.NONE, false));
        event.registerLayerDefinition(IMMModelLayers.CULTIVATOR_INNER_ARMOR, () -> INNER_ARMOR);
        event.registerLayerDefinition(IMMModelLayers.CULTIVATOR_OUTER_ARMOR, () -> OUTER_ARMOR);
        event.registerLayerDefinition(IMMModelLayers.CULTIVATOR_SLIM, () -> CultivatorModel.createBodyLayer(CubeDeformation.NONE, true));
        event.registerLayerDefinition(IMMModelLayers.CULTIVATOR_SLIM_INNER_ARMOR, () -> INNER_ARMOR);
        event.registerLayerDefinition(IMMModelLayers.CULTIVATOR_SLIM_OUTER_ARMOR, () -> OUTER_ARMOR);

        /* creature */
        event.registerLayerDefinition(IMMModelLayers.GRASS_CARP, GrassCarpModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.SILK_WORM, SilkWormModel::createBodyLayer);

        /* Monster */
        event.registerLayerDefinition(IMMModelLayers.SHARP_STAKE, CubeModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.BI_FANG, BiFangModel::createBodyLayer);

        /* zombie entity */
        event.registerLayerDefinition(IMMModelLayers.SPIRITUAL_ZOMBIE, SpiritualZombieModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.SPIRITUAL_ZOMBIE_INNER_ARMOR, () -> INNER_ARMOR);
        event.registerLayerDefinition(IMMModelLayers.SPIRITUAL_ZOMBIE_OUTER_ARMOR, () -> OUTER_ARMOR);

        /* golem */
        event.registerLayerDefinition(IMMModelLayers.IRON_GOLEM, IronGolemModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.SNOW_GOLEM, SnowGolemModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.CREEPER_GOLEM, CreeperGolemModel::createBodyLayer);
        event.registerLayerDefinition(IMMModelLayers.COPPER_GOLEM, CopperGolemModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(IMMParticles.SPIRITUAL_MANA.get(), SpiritualManaParticle.Factory::new);
        event.registerSpriteSet(IMMParticles.METAL_ELEMENT.get(), MetalElementParticle.Factory::new);
        event.registerSpriteSet(IMMParticles.WOOD_ELEMENT.get(), WoodElementParticle.Factory::new);
        event.registerSpriteSet(IMMParticles.WATER_ELEMENT.get(), WaterElementParticle.Factory::new);
        event.registerSpriteSet(IMMParticles.FIRE_ELEMENT.get(), FireElementParticle.Factory::new);
        event.registerSpriteSet(IMMParticles.EARTH_ELEMENT.get(), EarthElementParticle.Factory::new);
        event.registerSpriteSet(IMMParticles.SPIRIT_ELEMENT.get(), SpiritElementParticle.Factory::new);
        event.registerSpriteSet(IMMParticles.SPIRITUAL_FLAME.get(), IMMFlameParticle.Factory::new);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event){
        event.registerAboveAll("spiritual_mana_bar", CommonOverlay.SPIRITUAL_MANA);
        event.registerAboveAll("spell_circle", SpellOverlay.SPELL_CIRCLE);
        event.registerAboveAll("prepared_spell", SpellOverlay.PREPARE_SPELL);
        event.registerAboveAll("elements", ElementOverlay.INSTANCE);
        event.registerAboveAll("meditation", MeditationOverlay.INSTANCE);
//        event.registerAboveAll("smithing_progress_bar", (gui, graphics, partialTick, screenWidth, screenHeight) -> {
//            if(RenderEventHandler.canRenderOverlay()){
//                RenderEventHandler.renderSmithingBar(graphics, screenHeight, screenWidth);
//            }
//        });
    }

    @SubscribeEvent
    public static void registerKeyBinds(RegisterKeyMappingsEvent event){
        IMMKeyBinds.register(event);
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
    public static void bakeModel(ModelEvent.ModifyBakingResult event) {
        IMMBakeModels.registerBakeModels(event);
    }

    @SubscribeEvent
    public static void bakeModel(ModelEvent.RegisterAdditional event) {
        IMMBakeModels.registerBakeModels(event);
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent ev){

    }

    public static void registerScreen() {
        MenuScreens.register(IMMMenus.CULTIVATOR_TRADE.get(), MerchantTradeScreen::new);
        MenuScreens.register(IMMMenus.SPIRITUAL_FURNACE.get(), SpiritualFurnaceScreen::new);
        MenuScreens.register(IMMMenus.ELIXIR_ROOM.get(), ElixirRoomScreen::new);
//        MenuScreens.register(ImmortalMenus.SMITHING_ARTIFACT.get(), SmithingArtifactScreen::new);
        MenuScreens.register(IMMMenus.GOLEM_INVENTORY.get(), GolemInventoryScreen::new);
        MenuScreens.register(IMMMenus.RUNE_CRAFT.get(), RuneCraftScreen::new);
        MenuScreens.register(IMMMenus.RUNE_GATE.get(), RuneGateScreen::new);
        MenuScreens.register(IMMMenus.RUNE_BIND.get(), RuneBindScreen::new);
    }

    public static void registerItemProperties(){
        ItemHelper.get().filterValues(TalismanItem.class::isInstance).forEach(talisman -> {
            ItemProperties.register(talisman, TalismanItem.ACTIVATE_PROPERTY, (stack, level, entity, val) -> {
                return TalismanItem.isActivated(stack) ? 1F : 0F;
            });
        });
    }
}
