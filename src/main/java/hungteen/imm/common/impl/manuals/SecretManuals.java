package hungteen.imm.common.impl.manuals;

import hungteen.htlib.api.interfaces.IHTCodecRegistry;
import hungteen.htlib.common.registry.HTCodecRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.ILearnRequirement;
import hungteen.imm.api.registry.IManualContent;
import hungteen.imm.api.registry.ISpellType;
import hungteen.imm.common.impl.manuals.requirments.*;
import hungteen.imm.common.impl.registry.CultivationTypes;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.impl.registry.SpiritualTypes;
import hungteen.imm.common.spell.SpellTypes;
import hungteen.imm.common.spell.spells.ElementalMasterySpell;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/2/24 15:09
 */
public interface SecretManuals {

    HTCodecRegistry<SecretManual> TUTORIALS = HTRegistryManager.create(Util.prefix("secret_manual"), () -> SecretManual.CODEC, () -> SecretManual.NETWORK_CODEC);

    static void register(BootstapContext<SecretManual> context){
        final HolderGetter<ILearnRequirement> requirements = context.lookup(LearnRequirements.registry().getRegistryKey());
        final HolderGetter<IManualContent> contents = context.lookup(ManualContents.registry().getRegistryKey());
        final Holder<ILearnRequirement> spiritual = requirements.getOrThrow(LearnRequirements.cultivation(CultivationTypes.SPIRITUAL));
        final Holder<ILearnRequirement> spiritual_level_1 = requirements.getOrThrow(LearnRequirements.realm(RealmTypes.SPIRITUAL_LEVEL_1, true));
        final Holder<ILearnRequirement> spiritual_level_2 = requirements.getOrThrow(LearnRequirements.realm(RealmTypes.SPIRITUAL_LEVEL_2, true));
        final Holder<ILearnRequirement> spiritual_level_3 = requirements.getOrThrow(LearnRequirements.realm(RealmTypes.SPIRITUAL_LEVEL_3, true));
        register(context, SpellTypes.MEDITATE, 1, builder -> {
            builder.require(spiritual_level_1);
        });
        register(context, SpellTypes.DISPERSAL, 1, builder -> {
            builder.require(spiritual_level_1);
        });
        register(context, SpellTypes.RELEASING, 1, builder -> {
            builder.require(spiritual_level_1);
        });
        register(context, SpellTypes.INTIMIDATE, 1, builder -> {
            builder.require(spiritual_level_2);
        });
        register(context, SpellTypes.SPIRIT_EYES, 1, builder -> {
            builder.require(spiritual_level_1);
        });
        register(context, SpellTypes.SPIRIT_EYES, 2, builder -> {
            builder.require(spiritual_level_2);
        });
        register(context, SpellTypes.PICKUP_ITEM, 1, builder -> {
            builder.require(spiritual_level_1);
        });
        register(context, SpellTypes.PICKUP_ITEM, 2, builder -> {
            builder.require(spiritual_level_1);
        });
        register(context, SpellTypes.THROW_ITEM, 1, builder -> {
            builder.require(spiritual_level_2)
                    .require(Holder.direct(SpellRequirement.single(SpellTypes.PICKUP_ITEM, 1)));
        });
        register(context, SpellTypes.PICKUP_BLOCK, 1, builder -> {
            builder.require(spiritual_level_1);
        });
        register(context, SpellTypes.PICKUP_BLOCK, 2, builder -> {
            builder.require(spiritual_level_2);
        });
        register(context, SpellTypes.FLY_WITH_ITEM, 1, builder -> {
            builder.require(spiritual_level_2)
                    .require(Holder.direct(SpellRequirement.single(SpellTypes.PICKUP_ITEM, 1)));
        });
        register(context, SpellTypes.FLY_WITH_ITEM, 2, builder -> {
            builder.require(spiritual_level_3);
        });
//        register(context, SpellTypes.FLY_WITH_ITEM, 3, builder -> {
//            builder.require(spiritual_level_3);
//        });
        register(context, SpellTypes.SPROUT, 1, builder -> {
            builder.require(spiritual_level_2)
                    .require(Holder.direct(SpiritualRootRequirement.single(SpiritualTypes.WOOD)))
                    .require(Holder.direct(SpellRequirement.single(SpellTypes.RELEASING, 1)));
        });
        register(context, SpellTypes.WATER_BREATHE, 1, builder -> {
            builder.require(spiritual_level_2)
                    .require(Holder.direct(ElementRequirement.create(Elements.WATER)));
        });
        register(context, SpellTypes.BURNING, 1, builder -> {
            builder.require(spiritual_level_1)
                    .require(Holder.direct(ElementRequirement.create(Elements.FIRE)));
        });
        register(context, SpellTypes.BURNING, 2, builder -> {
            builder.require(spiritual_level_2)
                    .require(Holder.direct(ElementRequirement.create(Elements.FIRE)));
        });
        register(context, SpellTypes.LAVA_BREATHE, 1, builder -> {
            builder.require(spiritual_level_2)
                    .require(Holder.direct(ElementRequirement.create(Elements.FIRE)));
        });
        register(context, SpellTypes.IGNITION, 1, builder -> {
            builder.require(spiritual_level_2)
                    .require(Holder.direct(SpellRequirement.single(SpellTypes.FIRE_MASTERY, 1)));
        });
        for (Elements element : Elements.values()) {
            final ISpellType spell = ElementalMasterySpell.getSpell(element);
            register(context, spell, 1, builder -> {
                builder.require(spiritual_level_2)
                        .require(Holder.direct(new EMPRequirement(1)))
                        .require(Holder.direct(SpellRequirement.single(SpellTypes.RELEASING, 1)));
            });
            register(context, spell, 2, builder -> {
                builder.require(spiritual_level_2)
                        .require(Holder.direct(new EMPRequirement(2)))
                        .require(Holder.direct(SpellRequirement.single(SpellTypes.DISPERSAL, 1)));
            });
            register(context, spell, 3, builder -> {
                builder.require(spiritual_level_3)
                        .require(Holder.direct(new EMPRequirement(3)));
            });
        }
    }

    static void register(BootstapContext<SecretManual> context, ISpellType spell, int level, Consumer<Builder> consumer){
        if(level > 0 && level <= spell.getMaxLevel()){
            final Builder builder = builder(spell, level);
            if(level > 1){
                builder.require(Holder.direct(new SpellRequirement(List.of(com.mojang.datafixers.util.Pair.of(spell, level - 1)))));
            }
            consumer.accept(builder);
            context.register(spellManual(spell, level), builder.build());
        } else {
            Util.warn("Secret Manuals Warn : Invalid spell level !");
        }
    }

    static IHTCodecRegistry<SecretManual> registry(){
        return TUTORIALS;
    }

    static Builder builder(ISpellType spell){
        return builder(spell, 1);
    }

    static Builder builder(ISpellType spell, int level){
        return builder(Holder.direct(new LearnSpellManual(spell, level)));
    }

    static Builder builder(Holder<IManualContent> content){
        return new Builder(content);
    }

    static ResourceKey<SecretManual> spellManual(ISpellType spell) {
        return spellManual(spell, 1);
    }

    static ResourceKey<SecretManual> spellManual(ISpellType spell, int level) {
        return registry().createKey(StringHelper.suffix(spell.getLocation(), String.valueOf(level)));
    }

    static ResourceKey<SecretManual> create(String name) {
        return registry().createKey(Util.prefix(name));
    }

    class Builder {

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
            return new SecretManual(requirements, Optional.of(content), model, Optional.empty());
        }
    }
}
