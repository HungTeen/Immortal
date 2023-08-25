package hungteen.imm.common.impl.manuals;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IManualContent;
import hungteen.imm.common.impl.manuals.requirments.LearnRequirements;
import hungteen.imm.common.impl.registry.PlayerRangeFloats;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 16:52
 */
public record SecretManual(List<Holder<ILearnRequirement>> requirements, Optional<Holder<IManualContent>> content, ResourceLocation model, Optional<MutableComponent> title) {
    public static final Codec<SecretManual> CODEC = RecordCodecBuilder.<SecretManual>mapCodec(instance -> instance.group(
            LearnRequirements.getCodec().listOf().optionalFieldOf("requirements", List.of()).forGetter(SecretManual::requirements),
            Codec.optionalField("content", ManualContents.getCodec()).forGetter(SecretManual::content),
            ResourceLocation.CODEC.optionalFieldOf("model", Util.prefix("secret_manual")).forGetter(SecretManual::model),
            Codec.optionalField("title", CodecHelper.componentCodec()).forGetter(SecretManual::title)
    ).apply(instance, SecretManual::new)).codec();

    public static final Codec<SecretManual> NETWORK_CODEC = RecordCodecBuilder.<SecretManual>mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("model", Util.prefix("secret_manual")).forGetter(SecretManual::model),
            Codec.optionalField("title", CodecHelper.componentCodec()).forGetter(SecretManual::title)
    ).apply(instance, (model, title) -> {
        return new SecretManual(List.of(), Optional.empty(), model, title);
    })).codec();

    public boolean canLearn(Level level, Player player){
        if(content().isPresent()) {
            return content().get().get().canLearn(level, player) && requirements().stream().map(Holder::get).allMatch(l -> {
                final boolean result = l.check(level, player);
                if (!result) {
                    PlayerHelper.sendTipTo(player, TipUtil.info("requirement_not_fit").append(l.getRequirementInfo(player)).withStyle(ChatFormatting.RED));
                }
                return result;
            });
        }
        return false;
    }

    public void learn(Level level, Player player){
        if(content().isPresent()) {
            content().get().get().learn(player);
            requirements().stream().map(Holder::get).forEach(l -> l.consume(level, player));
            PlayerHelper.playClientSound(player, SoundEvents.ENCHANTMENT_TABLE_USE);
            PlayerUtil.setFloatData(player, PlayerRangeFloats.SPIRITUAL_MANA, 0);
        }
    }

    public List<Component> getRequirementInfo(Player player){
        final List<Component> requirements = new ArrayList<>();
        requirements().stream().map(Holder::get).forEach(l -> requirements.add(l.getRequirementInfo(player)));
        return requirements;
    }

    public MutableComponent getManualTitle(){
        return content().isPresent() ? title().orElse(content().get().get().getManualTitle()) : Component.empty();
    }

    public MutableComponent getContentInfo(){
        return content().isPresent() ? content().get().get().getInfo() : Component.empty();
    }

}