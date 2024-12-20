package hungteen.imm.common.entity.human.cultivator;

import com.mojang.serialization.Dynamic;
import hungteen.imm.common.entity.human.HumanLikeEntity;
import hungteen.imm.common.entity.human.setting.trade.TradeEntry;
import hungteen.imm.common.entity.human.setting.trade.TradeOffer;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import hungteen.imm.common.item.IMMMapDecorationTypes;
import hungteen.imm.common.tag.IMMStructureTags;
import hungteen.imm.util.TipUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import java.util.List;

/**
 * 散修。
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/1 14:58
 */
public class WanderingCultivator extends Cultivator {

    public WanderingCultivator(EntityType<? extends HumanLikeEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected SimpleContainer createInventory() {
        return new SimpleContainer(12);
    }

    @Override
    public void fillSpecialTrade(TradeOffers offers, RandomSource random) {
        // 主世界则需要添加寻找残破小屋（传送门）的宝藏地图。
        if(level() instanceof ServerLevel serverLevel && serverLevel.dimension().equals(Level.OVERWORLD) && random.nextFloat() < 0.3F){
            final BlockPos dst = serverLevel.findNearestMapStructure(IMMStructureTags.TELEPORT_RUINS, blockPosition(), 100, true);
            if (dst != null) {
                ItemStack itemstack = MapItem.create(serverLevel, dst.getX(), dst.getZ(), (byte)2, true, true);
                MapItem.renderBiomePreviewMap(serverLevel, itemstack);
                MapItemSavedData.addTargetDecoration(itemstack, dst, "*", IMMMapDecorationTypes.TELEPORT_RUIN.holder());
                itemstack.set(DataComponents.CUSTOM_NAME, TipUtil.tooltip("teleport_ruin_map"));
                offers.add(new TradeOffer(new TradeEntry(
                        List.of(
                                new ItemStack(Items.EMERALD, 8),
                                new ItemStack(Items.COMPASS, 1)
                        ),
                        List.of(itemstack.copy()),
                        ConstantInt.of(1)
                ), random));
            }
        }
    }

    @Override
    public void updateBrain(ServerLevel level) {
        this.getBrain().tick(level, this);
        WanderingCultivatorAi.updateActivity(this);
    }

    @Override
    public Brain<WanderingCultivator> getBrain() {
        return (Brain<WanderingCultivator>)super.getBrain();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return WanderingCultivatorAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override
    protected Brain.Provider<WanderingCultivator> brainProvider() {
        return Brain.provider(getMemoryModules(), getSensorModules());
    }

}
