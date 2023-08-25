package hungteen.imm.common.impl.manuals;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.interfaces.IHTCodecRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.api.registry.IManualContent;
import hungteen.imm.api.registry.IManualType;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/7/17 15:29
 */
public interface ManualContents {

    IHTCodecRegistry<IManualContent> TYPES = HTRegistryManager.create(Util.prefix("manual_content"), ManualContents::getDirectCodec);

    static void register(BootstapContext<IManualContent> context) {
//        SpellTypes.registry().getValues().forEach(spell -> {
//            for(int i = 1; i <= spell.getMaxLevel(); ++ i){
//                context.register(spellContent(spell, i), new LearnSpellManual(spell, i));
//            }
//        });
    }

    static Codec<IManualContent> getDirectCodec() {
        return ManualTypes.registry().byNameCodec().dispatch(IManualContent::getType, IManualType::codec);
    }

    static Codec<Holder<IManualContent>> getCodec() {
        return registry().getHolderCodec(getDirectCodec());
    }

    static IHTCodecRegistry<IManualContent> registry() {
        return TYPES;
    }

    static ResourceKey<IManualContent> spellContent(ISpellType spell, int level) {
        return registry().createKey(StringHelper.suffix(spell.getLocation(), String.valueOf(level)));
    }

    static ResourceKey<IManualContent> create(String name) {
        return registry().createKey(Util.prefix(name));
    }

}
