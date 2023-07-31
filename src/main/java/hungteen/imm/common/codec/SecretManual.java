package hungteen.imm.common.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IManualContent;
import hungteen.imm.common.impl.manuals.ManualContents;
import hungteen.imm.common.impl.manuals.requirments.LearnRequirements;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 16:52
 */
public record SecretManual(ResourceLocation model, List<ManualEntry> entries, int textLine) {
    public static final Codec<SecretManual> CODEC = RecordCodecBuilder.<SecretManual>mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("entries", Util.prefix("secret_manual")).forGetter(SecretManual::model),
            ManualEntry.CODEC.listOf().fieldOf("entries").forGetter(SecretManual::entries),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("text_line", 0).forGetter(SecretManual::textLine)
    ).apply(instance, SecretManual::new)).codec();

    public record ManualEntry(Holder<ILearnRequirement> requirement, Holder<IManualContent> content) {
        public static final Codec<ManualEntry> CODEC = RecordCodecBuilder.<ManualEntry>mapCodec(instance -> instance.group(
                LearnRequirements.getCodec().fieldOf("requirement").forGetter(ManualEntry::requirement),
                ManualContents.getCodec().fieldOf("content").forGetter(ManualEntry::content)
        ).apply(instance, ManualEntry::new)).codec();

        public boolean canLearn(Level level, Player player){
            return requirement().get().check(level, player);
        }

        public void learn(Level level, Player player){
            content().get().learn(player);
            requirement().get().consume(level, player);
        }

        public List<Component> getContentInfo(){
            return content().get().getInfo();
        }
    }
}