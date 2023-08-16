package hungteen.imm.common.impl.manuals;

import hungteen.htlib.api.interfaces.IHTCodecRegistry;
import hungteen.htlib.common.registry.HTCodecRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IManualContent;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.impl.manuals.requirments.LearnRequirements;
import hungteen.imm.common.impl.registry.CultivationTypes;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/24 15:09
 */
public class SecretManuals {

    private static final HTCodecRegistry<SecretManual> TUTORIALS = HTRegistryManager.create(Util.prefix("secret_manual"), () -> SecretManual.CODEC, () -> SecretManual.CODEC);

    public static void register(BootstapContext<SecretManual> context){
        final HolderGetter<ILearnRequirement> requirements = context.lookup(LearnRequirements.registry().getRegistryKey());
        final HolderGetter<IManualContent> contents = context.lookup(ManualContents.registry().getRegistryKey());
        final Holder<ILearnRequirement> spiritual = requirements.getOrThrow(LearnRequirements.cultivation(CultivationTypes.SPIRITUAL));
        register(context, contents, SpellTypes.MEDITATE, 1, builder -> {
            builder.require(spiritual);
        });
//        context.register(SPIRITUAL_BEGINNER_GUIDE, builder().entry(
//                new SecretManual.ManualEntry(
//                        requirements.getOrThrow(LearnRequirements.NO_REQUIREMENT),
//                        contents.getOrThrow(ManualContents.LEARN_RESTING)
//                )).build()
//        );
    }

    public static void register(BootstapContext<SecretManual> context, HolderGetter<IManualContent> contents, ISpellType spell, int level, Consumer<Builder> consumer){
        final Builder builder = builder(contents, spell, level);
        consumer.accept(builder);
        context.register(spellManual(spell, level), builder.build());
    }

    public static IHTCodecRegistry<SecretManual> registry(){
        return TUTORIALS;
    }

    public static Builder builder(HolderGetter<IManualContent> contents, ISpellType spell){
        return builder(contents, spell, 1);
    }

    public static Builder builder(HolderGetter<IManualContent> contents, ISpellType spell, int level){
        return builder(contents.getOrThrow(ManualContents.spellContent(spell, level)));
    }

    public static Builder builder(Holder<IManualContent> content){
        return new Builder(content);
    }

    public static ResourceKey<SecretManual> spellManual(ISpellType spell) {
        return spellManual(spell, 1);
    }

    public static ResourceKey<SecretManual> spellManual(ISpellType spell, int level) {
        return registry().createKey(StringHelper.suffix(spell.getLocation(), String.valueOf(level)));
    }

    public static ResourceKey<SecretManual> create(String name) {
        return registry().createKey(Util.prefix(name));
    }

    public static class Builder {

        private final Holder<IManualContent> content;
        private final List<Holder<ILearnRequirement>> requirements = new ArrayList<>();
        private ResourceLocation model = Util.prefix("secret_manual");

        public Builder(Holder<IManualContent> content) {
            this.content = content;
        }

        public Builder require(Holder<ILearnRequirement> requirement) {
            requirements.add(requirement);
            return this;
        }

        public Builder model(ResourceLocation model) {
            this.model = model;
            return this;
        }

        public SecretManual build() {
            return new SecretManual(requirements, content, model);
        }
    }
}
