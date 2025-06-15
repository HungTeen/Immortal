package hungteen.imm.common.impl.manuals;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.htlib.util.helper.PlayerHelper;
import hungteen.imm.api.cultivation.ExperienceType;
import hungteen.imm.api.spell.ILearnRequirement;
import hungteen.imm.api.spell.IScrollContent;
import hungteen.imm.common.cultivation.SpellManager;
import hungteen.imm.common.impl.manuals.requirments.RequirementTypes;
import hungteen.imm.util.PlayerUtil;
import hungteen.imm.util.TipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 卷轴注册项。
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 16:52
 */
public record SecretScroll(List<ILearnRequirement> requirements, IScrollContent content, Optional<Component> title) {
    public static final Codec<SecretScroll> CODEC = RecordCodecBuilder.<SecretScroll>mapCodec(instance -> instance.group(
            RequirementTypes.getCodec().listOf().optionalFieldOf("requirements", List.of()).forGetter(SecretScroll::requirements),
            ScrollTypes.getManualCodec().fieldOf("content").forGetter(SecretScroll::content),
            Codec.optionalField("title", ComponentSerialization.CODEC, true).forGetter(SecretScroll::title)
    ).apply(instance, SecretScroll::new)).codec();

    public boolean canLearn(Level level, Player player) {
        return content().canLearn(level, player) && requirements().stream().allMatch(l -> {
            final boolean result = l.check(level, player);
//            if (!result) {
//                PlayerHelper.sendTipTo(player, TipUtil.manual("requirement_not_fit").append(l.getRequirementInfo(player)).withStyle(ChatFormatting.RED));
//            }
            return result;
        });
    }

    public void learn(Level level, Player player) {
        content().learn(player);
        requirements().forEach(l -> l.consume(level, player));
        PlayerHelper.playClientSound(player, SoundEvents.ENCHANTMENT_TABLE_USE);
        PlayerUtil.addExperience(player, ExperienceType.SPELL, content().getLearnXp());
        PlayerUtil.setQiAmount(player, 0);
    }

    public List<Component> getRequirementInfo(Player player) {
        final List<Component> requirements = new ArrayList<>();
        requirements().forEach(l -> requirements.add(l.getRequirementInfo(player)));
        return requirements;
    }

    public MutableComponent getContentInfo() {
        return content().getInfo();
    }

    public Component getTitle(){
        if(content() instanceof LearnSpellScroll learnSpellScroll){
            return SpellManager.spellName(learnSpellScroll.spellType(), learnSpellScroll.level());
        }
        if(title().isPresent()){
            return title().get();
        }
        return TipUtil.UNKNOWN;
    }

    public Optional<ResourceLocation> getRenderLogo(){
        if(content() instanceof LearnSpellScroll learnSpellScroll){
            return Optional.ofNullable(learnSpellScroll.getIcon());
        }
        return Optional.empty();
    }

}