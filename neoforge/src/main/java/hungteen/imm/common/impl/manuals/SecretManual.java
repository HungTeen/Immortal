package hungteen.imm.common.impl.manuals;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.helper.CodecHelper;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.cultivation.ExperienceType;
import hungteen.imm.api.spell.ILearnRequirement;
import hungteen.imm.api.spell.IManualContent;
import hungteen.imm.common.impl.manuals.requirments.RequirementTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.ChatFormatting;
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
 * @create 2023/7/17 16:52
 */
public record SecretManual(List<ILearnRequirement> requirements, IManualContent content, ResourceLocation model,
                           Optional<MutableComponent> title) {
    public static final Codec<SecretManual> CODEC = RecordCodecBuilder.<SecretManual>mapCodec(instance -> instance.group(
            RequirementTypes.getCodec().listOf().optionalFieldOf("requirements", List.of()).forGetter(SecretManual::requirements),
            ManualTypes.getManualCodec().fieldOf("content").forGetter(SecretManual::content),
            ResourceLocation.CODEC.optionalFieldOf("model", Util.prefix("secret_manual")).forGetter(SecretManual::model),
            Codec.optionalField("title", CodecHelper.componentCodec(), true).forGetter(SecretManual::title)
    ).apply(instance, SecretManual::new)).codec();

    public boolean canLearn(Level level, Player player) {
        return content().canLearn(level, player) && requirements().stream().allMatch(l -> {
            final boolean result = l.check(level, player);
            if (!result) {
                PlayerHelper.sendTipTo(player, TipUtil.info("requirement_not_fit").append(l.getRequirementInfo(player)).withStyle(ChatFormatting.RED));
            }
            return result;
        });
    }

    public void learn(Level level, Player player) {
        content().learn(player);
        requirements().forEach(l -> l.consume(level, player));
        PlayerHelper.playClientSound(player, SoundEvents.ENCHANTMENT_TABLE_USE);
        PlayerUtil.addExperience(player, ExperienceType.SPELL, 5);
        PlayerUtil.setQiAmount(player, 0);
    }

    public List<Component> getRequirementInfo(Player player) {
        final List<Component> requirements = new ArrayList<>();
        requirements().forEach(l -> requirements.add(l.getRequirementInfo(player)));
        return requirements;
    }

    public MutableComponent getManualTitle() {
        return title().orElse(content().getManualTitle());
    }

    public MutableComponent getContentInfo() {
        return content().getInfo();
    }

    public Optional<ResourceLocation> getTexture(){
        if(content() instanceof LearnSpellManual learnSpellManual) return Optional.of(learnSpellManual.getTexture());
        return Optional.empty();
    }

}