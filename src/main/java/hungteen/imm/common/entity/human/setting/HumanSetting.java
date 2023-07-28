package hungteen.imm.common.entity.human.setting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.IInventoryLootType;
import hungteen.imm.common.entity.human.HumanEntity;
import hungteen.imm.common.entity.human.setting.trade.TradeOffer;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import hungteen.imm.common.entity.human.setting.trade.TradeSetting;
import hungteen.imm.common.event.events.HumanFillTradeEvent;
import hungteen.imm.common.impl.registry.InventoryLootTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.Container;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-06-08 23:16
 **/
public record HumanSetting(IInventoryLootType type, int weight, List<LootSetting> lootSettings, Optional<TradeSetting> tradeSetting) implements WeightedEntry {
    public static final Codec<HumanSetting> CODEC = RecordCodecBuilder.<HumanSetting>mapCodec(instance -> instance.group(
            InventoryLootTypes.registry().byNameCodec().fieldOf("type").forGetter(HumanSetting::type),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("weight", 0).forGetter(HumanSetting::weight),
            LootSetting.CODEC.listOf().fieldOf("loot_settings").forGetter(HumanSetting::lootSettings),
            Codec.optionalField("trade_setting", TradeSetting.CODEC).forGetter(HumanSetting::tradeSetting)
    ).apply(instance, HumanSetting::new)).codec();

    @Override
    public Weight getWeight() {
        return Weight.of(weight());
    }

    public void fillInventory(Container container, RandomSource random){
        final int len = Math.min(container.getContainerSize(), lootSettings().size());
        for(int i = 0; i < len; ++ i){
            container.setItem(i, lootSettings().get(i).getItem(random));
        }
    }

    public void fillTrade(HumanEntity entity, RandomSource random){
        tradeSetting().ifPresent(setting -> {
            final TradeOffers offers = new TradeOffers();
            setting.sampleTradeEntries(random).forEach(entry -> {
                offers.add(new TradeOffer(entry, random));
            });
            entity.fillSpecialTrade(offers, random);
            MinecraftForge.EVENT_BUS.post(new HumanFillTradeEvent(entity, offers));
            entity.setTradeOffers(offers);
        });
    }
}
