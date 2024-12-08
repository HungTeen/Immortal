package hungteen.imm.common.entity.human.setting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.common.entity.human.HumanLikeEntity;
import hungteen.imm.common.entity.human.setting.loot.SlotSetting;
import hungteen.imm.common.entity.human.setting.trade.TradeOffer;
import hungteen.imm.common.entity.human.setting.trade.TradeOffers;
import hungteen.imm.common.entity.human.setting.trade.TradeSetting;
import hungteen.imm.common.event.events.HumanFillTradeEvent;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.Container;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;
import java.util.Optional;

/**
 * 将人类实体的部分功能数据包化，比如背包、交易。
 * @program Immortal
 * @author HungTeen
 * @create 2023-06-08 23:16
 * @param predicateSetting 该配置可以用于哪些人类。
 * @param weight 权重，可以为 0（表示不可用）。
 * @param slotSettings 背包配置。
 * @param tradeSetting 交易配置。
 * @param spellSettings 法术配置。
 **/
public record HumanSetting(HumanPredicateSetting predicateSetting, int weight, List<SlotSetting> slotSettings, Optional<TradeSetting> tradeSetting, List<SpellSetting> spellSettings) implements WeightedEntry {
    public static final Codec<HumanSetting> CODEC = RecordCodecBuilder.<HumanSetting>mapCodec(instance -> instance.group(
            HumanPredicateSetting.CODEC.fieldOf("predict_setting").forGetter(HumanSetting::predicateSetting),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("weight", 1).forGetter(HumanSetting::weight),
            SlotSetting.CODEC.listOf().optionalFieldOf("slot_settings", List.of()).forGetter(HumanSetting::slotSettings),
            Codec.optionalField("trade_setting", TradeSetting.CODEC, true).forGetter(HumanSetting::tradeSetting),
            SpellSetting.CODEC.listOf().optionalFieldOf("spell_settings", List.of()).forGetter(HumanSetting::spellSettings)
    ).apply(instance, HumanSetting::new)).codec();

    public static final Codec<HumanSetting> NETWORK_CODEC = RecordCodecBuilder.<HumanSetting>mapCodec(instance -> instance.group(
            HumanPredicateSetting.CODEC.fieldOf("predict_setting").forGetter(HumanSetting::predicateSetting)
    ).apply(instance, (setting) -> {
        return new HumanSetting(setting, 1, List.of(), Optional.empty(), List.of());
    })).codec();

    @Override
    public Weight getWeight() {
        return Weight.of(weight());
    }

    public void updateHuman(HumanLikeEntity entity){
        entity.setHumanSetting(this);
        fillInventory(entity);
        fillTrades(entity);
        fillSpells(entity);
    }

    /**
     * 填充背包。
     */
    public void fillInventory(HumanLikeEntity entity){
        final Container container = entity.getInventory();
        container.clearContent();
        final int len = Math.min(container.getContainerSize(), slotSettings().size());
        for(int i = 0; i < len; ++ i){
            container.setItem(i, slotSettings().get(i).getItem(entity.registryAccess(), entity.getRandom()));
        }
    }

    /**
     * 填充交易。
     */
    public void fillTrades(HumanLikeEntity entity){
        tradeSetting().ifPresent(setting -> {
            final TradeOffers offers = new TradeOffers();
            setting.sampleTradeEntries(entity.getRandom()).forEach(entry -> {
                offers.add(new TradeOffer(entry, entity.getRandom()));
            });
            entity.fillSpecialTrade(offers, entity.getRandom());
            NeoForge.EVENT_BUS.post(new HumanFillTradeEvent(entity, offers));
            entity.setTradeOffers(offers);
        });
    }

    public void fillSpells(HumanLikeEntity entity){
        entity.clearLearnedSpells();
        spellSettings().forEach(spellSetting -> {
            spellSetting.getSpell(entity.getRandom()).ifPresent(spell -> {
                entity.learnSpell(spell.spell(), spell.level());
            });
        });
    }
}
