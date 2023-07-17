package hungteen.imm.common.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IManualContent;
import hungteen.imm.common.impl.manuals.ManualContents;
import hungteen.imm.common.impl.manuals.requirments.LearnRequirements;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 16:52
 */
public record SecretManual(int treasureWeight, int tradeWeight, List<ManualEntry> entries) {
    public static final Codec<SecretManual> CODEC = RecordCodecBuilder.<SecretManual>mapCodec(instance -> instance.group(
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("treasure_weight", 0).forGetter(SecretManual::treasureWeight),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("trade_weight", 0).forGetter(SecretManual::tradeWeight),
            ManualEntry.CODEC.listOf().fieldOf("entries").forGetter(SecretManual::entries)
    ).apply(instance, SecretManual::new)).codec();

    public record ManualEntry(Holder<ILearnRequirement> requirement, Holder<IManualContent> content) {
        public static final Codec<ManualEntry> CODEC = RecordCodecBuilder.<ManualEntry>mapCodec(instance -> instance.group(
                LearnRequirements.getCodec().fieldOf("requirement").forGetter(ManualEntry::requirement),
                ManualContents.getCodec().fieldOf("content").forGetter(ManualEntry::content)
        ).apply(instance, ManualEntry::new)).codec();

        public List<Component> getManualInfo(){
            return List.of();
        }
    }
}