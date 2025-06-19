package hungteen.imm.common.cultivation.manual;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import hungteen.imm.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

/**
 * 成套的秘籍。
 * @author PangTeen
 * @program Immortal
 * @create 2023/7/17 16:52
 */
public record SecretManual(List<SecretScroll> scrolls, ResourceLocation model,
                           Component title, Optional<Component> category, Optional<Component> desc, boolean canSkipPage) {
    public static final Codec<SecretManual> CODEC = RecordCodecBuilder.<SecretManual>mapCodec(instance -> instance.group(
            SecretScroll.CODEC.listOf().fieldOf("scrolls").forGetter(SecretManual::scrolls),
            ResourceLocation.CODEC.optionalFieldOf("model", Util.prefix("secret_manual")).forGetter(SecretManual::model),
            ComponentSerialization.CODEC.fieldOf("title").forGetter(SecretManual::title),
            Codec.optionalField("category", ComponentSerialization.CODEC, true).forGetter(SecretManual::category),
            Codec.optionalField("desc", ComponentSerialization.CODEC, true).forGetter(SecretManual::desc),
            Codec.BOOL.optionalFieldOf("can_skip_page", true).forGetter(SecretManual::canSkipPage)
    ).apply(instance, SecretManual::new)).codec();

    public static final StreamCodec<RegistryFriendlyByteBuf, SecretManual> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

}