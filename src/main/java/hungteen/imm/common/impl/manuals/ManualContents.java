package hungteen.imm.common.impl.manuals;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.IHTCodecRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.imm.api.registry.IManualContent;
import hungteen.imm.api.registry.IManualType;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:29
 */
public class ManualContents {

    private static final IHTCodecRegistry<IManualContent> TYPES = HTRegistryManager.create(Util.prefix("manual_content"), ManualContents::getDirectCodec, ManualContents::getDirectCodec);

    public static final ResourceKey<IManualContent> LEARN_SPELL = create("learn_spell");

    public static void register(BootstapContext<IManualContent> context){
    }

    public static Codec<IManualContent> getDirectCodec(){
        return ManualTypes.registry().byNameCodec().dispatch(IManualContent::getType, IManualType::codec);
    }

    public static Codec<Holder<IManualContent>> getCodec(){
        return registry().getHolderCodec(getDirectCodec());
    }

    public static IHTCodecRegistry<IManualContent> registry(){
        return TYPES;
    }

    public static ResourceKey<IManualContent> create(String name) {
        return registry().createKey(Util.prefix(name));
    }

}
