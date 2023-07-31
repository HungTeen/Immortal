package hungteen.imm.common.impl.manuals;

import hungteen.htlib.api.interfaces.IHTCodecRegistry;
import hungteen.htlib.common.registry.HTCodecRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IManualContent;
import hungteen.imm.common.codec.SecretManual;
import hungteen.imm.common.impl.manuals.requirments.LearnRequirements;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/24 15:09
 */
public class SecretManuals {

    private static final HTCodecRegistry<SecretManual> TUTORIALS = HTRegistryManager.create(Util.prefix("secret_manual"), () -> SecretManual.CODEC, () -> SecretManual.CODEC);

    public static final ResourceKey<SecretManual> SPIRITUAL_BEGINNER_GUIDE = create("spiritual_beginner_guide");

    public static void register(BootstapContext<SecretManual> context){
        final HolderGetter<ILearnRequirement> requirements = context.lookup(LearnRequirements.registry().getRegistryKey());
        final HolderGetter<IManualContent> contents = context.lookup(ManualContents.registry().getRegistryKey());
        context.register(SPIRITUAL_BEGINNER_GUIDE, builder().entry(
                new SecretManual.ManualEntry(
                        requirements.getOrThrow(LearnRequirements.NO_REQUIREMENT),
                        contents.getOrThrow(ManualContents.LEARN_RESTING)
                )).build()
        );
    }

    public static IHTCodecRegistry<SecretManual> registry(){
        return TUTORIALS;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static ResourceKey<SecretManual> create(String name) {
        return registry().createKey(Util.prefix(name));
    }

    public static class Builder {

        private ResourceLocation model = Util.prefix("secret_manual");
        private final List<SecretManual.ManualEntry> entries = new ArrayList<>();
        private int textLine = 0;

        public Builder model(ResourceLocation model) {
            this.model = model;
            return this;
        }

        public Builder entry(SecretManual.ManualEntry entry) {
            this.entries.add(entry);
            return this;
        }

        public Builder line(int textLine) {
            this.textLine = textLine;
            return this;
        }

        public SecretManual build() {
            return new SecretManual(model, entries, textLine);
        }
    }
}
