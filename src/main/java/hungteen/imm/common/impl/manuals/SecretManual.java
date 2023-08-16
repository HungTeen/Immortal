package hungteen.imm.common.impl.manuals;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IManualContent;
import hungteen.imm.common.impl.manuals.requirments.LearnRequirements;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 16:52
 */
public record SecretManual(List<Holder<ILearnRequirement>> requirements, Holder<IManualContent> content, ResourceLocation model) {
    public static final Codec<SecretManual> CODEC = RecordCodecBuilder.<SecretManual>mapCodec(instance -> instance.group(
            LearnRequirements.getCodec().listOf().optionalFieldOf("requirements", List.of()).forGetter(SecretManual::requirements),
            ManualContents.getCodec().fieldOf("content").forGetter(SecretManual::content),
            ResourceLocation.CODEC.optionalFieldOf("model", Util.prefix("secret_manual")).forGetter(SecretManual::model)
    ).apply(instance, SecretManual::new)).codec();

    public boolean canLearn(Level level, Player player){
        return requirements().stream().map(Holder::get).allMatch(l -> l.check(level, player));
    }

    public void learn(Level level, Player player){
        content().get().learn(player);
        requirements().stream().map(Holder::get).forEach(l -> l.consume(level, player));
    }

    public List<Component> getRequirementInfo(){
        final List<Component> requirements = new ArrayList<>();
        requirements().stream().map(Holder::get).forEach(l -> requirements.add(l.getRequirementInfo()));
        return requirements;
    }

    public List<Component> getContentInfo(){
        return content().get().getInfo();
    }

}