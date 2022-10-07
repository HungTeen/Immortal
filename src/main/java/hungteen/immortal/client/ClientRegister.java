package hungteen.immortal.client;

import hungteen.htlib.util.BlockUtil;
import hungteen.immortal.block.ImmortalBlocks;
import hungteen.immortal.block.plants.GourdGrownBlock;
import hungteen.immortal.client.render.entity.FlyingItemEntityRender;
import hungteen.immortal.entity.FlyingItemEntity;
import hungteen.immortal.entity.ImmortalEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
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
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        /* misc entity */
        event.registerEntityRenderer(ImmortalEntities.FLYING_ITEM.get(), FlyingItemEntityRender::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        LayerDefinition INNER_ARMOR = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32);
        LayerDefinition OUTER_ARMOR = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32);
    }

    @SubscribeEvent
    public static void registerFactories(ParticleFactoryRegisterEvent event) {
        ParticleEngine manager = Minecraft.getInstance().particleEngine;
//        manager.register(PVZParticles.POTATO_EXPLOSION.get(), (sprite) -> new PotatoExplosionParticle.Provider(sprite));
    }

    @SubscribeEvent
    public static void setUpClient(FMLClientSetupEvent ev){
        ev.enqueueWork(() -> {
//            PVZKeyBinds.register();
//            PVZWoodType.register();
            registerBlockRender();
//            registerScreen();
        });
    }

    public static void registerBlockRender(){
        ItemBlockRenderTypes.setRenderLayer(ImmortalBlocks.GOURD_STEM.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ImmortalBlocks.GOURD_ATTACHED_STEM.get(), RenderType.cutout());
        BlockUtil.getFilterBlocks(b -> b instanceof GourdGrownBlock).forEach(block -> {
            ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
        });

    }

    public static void registerScreen() {
//        MenuScreens.register(PVZMenus.ESSENCE_ALTAR.get(), EssenceAltarScreen::new);
    }

}
