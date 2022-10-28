package hungteen.immortal.client;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import hungteen.htlib.client.render.entity.EmptyEffectRender;
import hungteen.htlib.util.BlockUtil;
import hungteen.immortal.client.gui.screen.ElixirFurnaceScreen;
import hungteen.immortal.client.gui.screen.GolemScreen;
import hungteen.immortal.client.gui.screen.SpiritualStoveScreen;
import hungteen.immortal.client.gui.tooltip.ClientElementToolTip;
import hungteen.immortal.client.model.ModelLayers;
import hungteen.immortal.client.model.entity.*;
import hungteen.immortal.client.render.entity.*;
import hungteen.immortal.common.block.ImmortalBlocks;
import hungteen.immortal.common.block.plants.GourdGrownBlock;
import hungteen.immortal.client.event.handler.OverlayHandler;
import hungteen.immortal.client.particle.ImmortalFlameParticle;
import hungteen.immortal.client.particle.ImmortalParticles;
import hungteen.immortal.common.entity.ImmortalEntities;
import hungteen.immortal.common.entity.human.Cultivator;
import hungteen.immortal.common.menu.ImmortalMenus;
import hungteen.immortal.common.menu.tooltip.ElementToolTip;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.util.StringUtil;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;
import java.io.File;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-02 14:59
 **/
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegister {

    @SubscribeEvent
    public static void setUpClient(FMLClientSetupEvent ev){
        OverlayHandler.registerOverlay();
        ClientHandler.registerCultivatorTypes();
        ev.enqueueWork(() -> {
            ImmortalKeyBinds.register();
            registerBlockRender();
            registerScreen();
            MinecraftForgeClient.registerTooltipComponentFactory(ElementToolTip.class, ClientElementToolTip::new);
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
    public static void registerFactories(ParticleFactoryRegisterEvent event) {
        ParticleEngine manager = Minecraft.getInstance().particleEngine;
        manager.register(ImmortalParticles.IMMORTAL_FLAME.get(), ImmortalFlameParticle.Factory::new) ;
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent ev){

    }

    public static void registerBlockRender(){
        ItemBlockRenderTypes.setRenderLayer(ImmortalBlocks.GOURD_STEM.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ImmortalBlocks.GOURD_ATTACHED_STEM.get(), RenderType.cutout());
        BlockUtil.getFilterBlocks(b -> b instanceof GourdGrownBlock).forEach(block -> {
            ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
        });

    }

    public static void registerScreen() {
        MenuScreens.register(ImmortalMenus.SPIRITUAL_STOVE.get(), SpiritualStoveScreen::new);
        MenuScreens.register(ImmortalMenus.ELIXIR_FURNACE.get(), ElixirFurnaceScreen::new);
        MenuScreens.register(ImmortalMenus.GOLEM_INVENTORY.get(), GolemScreen::new);
    }

}
