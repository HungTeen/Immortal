package hungteen.imm.client;

import hungteen.htlib.util.helper.impl.ItemHelper;
import hungteen.imm.client.data.ClientData;
import hungteen.imm.common.blockentity.SmithingArtifactBlockEntity;
import hungteen.imm.common.entity.human.cultivator.CultivatorType;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.item.artifact.WoodBowItem;
import hungteen.imm.common.item.talisman.TalismanItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.SkullBlockEntity;

import java.util.Arrays;

/**
 * @author HungTeen
 * @program Immortal
 * @create 2022-10-23 12:35
 **/
public class ClientHandler {

    private static final ItemPropertyFunction USED = (stack, level, entity, val) -> entity != null && entity.isUsingItem() && entity.getUseItem().equals(stack) ? 1.0F : 0.0F;
    private static final ItemPropertyFunction USING = (stack, level, entity, val) -> {
        if (entity == null) {
            return 0.0F;
        } else {
            return !entity.getUseItem().equals(stack) ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
        }
    };

    public static void registerItemProperties() {
        ItemHelper.get().filterValues(TalismanItem.class::isInstance).forEach(talisman -> {
            ItemProperties.register(talisman, TalismanItem.ACTIVATED, USED);
            ItemProperties.register(talisman, TalismanItem.ACTIVATING, USING);
        });
        ItemProperties.register(IMMItems.WOOD_BOW.get(), WoodBowItem.PULLED, USED);
        ItemProperties.register(IMMItems.WOOD_BOW.get(), WoodBowItem.PULLING, USING);
    }

    public static void registerCultivatorTypes() {
        Arrays.stream(CultivatorType.values())
                .filter((type -> ! type.isCommon()))
                .forEach(type -> {
                    if (type.getProfileUUID().isPresent()) {
                        SkullBlockEntity.fetchGameProfile(type.getProfileUUID().get()).thenAcceptAsync(opt -> {
                            opt.ifPresent(type::setGameProfile);
                        });
                    } else {
                        SkullBlockEntity.fetchGameProfile(type.getProfileName().get()).thenAcceptAsync(opt -> {
                            opt.ifPresent(type::setGameProfile);
                        });
                    }
                });
    }

    public static void startSmithing(ItemStack stack, SmithingArtifactBlockEntity blockEntity) {
        ClientData.StartSmithing = true;
        ClientData.SmithingDirection = true;
        ClientData.SmithingProgress = 0;
        ClientData.BestPointDisplayTick = 0;
//        ClientDatas.SmithingSpeedMultiple = blockEntity.getSmithingSpeedMultiple();
    }

    /**
     *
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
