package hungteen.imm.common.impl.manuals;

import hungteen.htlib.api.registry.HTCodecRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.ILearnRequirement;
import hungteen.imm.api.spell.IManualContent;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.common.cultivation.CultivationTypes;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.cultivation.SpellTypes;
import hungteen.imm.common.cultivation.spell.basic.ElementalMasterySpell;
import hungteen.imm.common.impl.manuals.requirments.CultivationTypeRequirement;
import hungteen.imm.common.impl.manuals.requirments.ElementRequirement;
import hungteen.imm.common.impl.manuals.requirments.RealmRequirement;
import hungteen.imm.common.impl.manuals.requirments.SpellRequirement;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/2/24 15:09
 */
public interface SecretManuals {

    HTCodecRegistry<SecretManual> TUTORIALS = HTRegistryManager.codec(Util.prefix("secret_manual"), () -> SecretManual.CODEC, () -> SecretManual.CODEC);

    static void register(BootstrapContext<SecretManual> context){
        final ILearnRequirement spiritual = CultivationTypeRequirement.create(CultivationTypes.SPIRITUAL);
        final ILearnRequirement spiritual_level_1 = RealmRequirement.create(RealmTypes.QI_REFINING.first(), true);
        final ILearnRequirement spiritual_level_2 = RealmRequirement.create(RealmTypes.FOUNDATION.pre(), true);
        final ILearnRequirement spiritual_level_3 = RealmRequirement.create(RealmTypes.CORE_SHAPING.pre(), true);

        /* 基本法术 */
        register(context, SpellTypes.MEDITATION, 1, builder -> {
            builder.require(spiritual_level_1);
        });
        register(context, SpellTypes.DISPERSAL, 1, builder -> {
            builder.require(spiritual_level_1);
        });
        register(context, SpellTypes.RELEASING, 1, builder -> {
            builder.require(spiritual_level_1);
        });
        register(context, SpellTypes.INTIMIDATION, 1, builder -> {
            builder.require(spiritual_level_2);
        });

        /* 神识 */
        register(context, SpellTypes.SPIRIT_EYES, 1, builder -> {
            builder.require(spiritual_level_1);
        });
        register(context, SpellTypes.SPIRIT_EYES, 2, builder -> {
            builder.require(spiritual_level_2);
        });

        /* 御物术 */
        register(context, SpellTypes.PICKUP_ITEM, 1, builder -> {
            builder.require(spiritual_level_1);
        });
        register(context, SpellTypes.PICKUP_ITEM, 2, builder -> {
            builder.require(spiritual_level_1);
        });
        register(context, SpellTypes.THROW_ITEM, 1, builder -> {
            builder.require(spiritual_level_2)
                    .require(SpellRequirement.single(SpellTypes.PICKUP_ITEM, 1));
        });
        register(context, SpellTypes.PICKUP_BLOCK, 1, builder -> {
            builder.require(spiritual_level_1);
        });
        register(context, SpellTypes.PICKUP_BLOCK, 2, builder -> {
            builder.require(spiritual_level_2);
        });
        register(context, SpellTypes.FLY_WITH_ITEM, 1, builder -> {
            builder.require(spiritual_level_2)
                    .require(SpellRequirement.single(SpellTypes.PICKUP_ITEM, 1));
        });
        register(context, SpellTypes.FLY_WITH_ITEM, 2, builder -> {
            builder.require(spiritual_level_3);
        });
//        initialize(context, SpellTypes.FLY_WITH_ITEM, 3, builder -> {
//            builder.require(spiritual_level_3);
//        });

        /* 金系法术 */
        register(context, SpellTypes.CRITICAL_HIT, 1, builder -> {
            builder.require(spiritual_level_1)
                    .require(ElementRequirement.create(Element.METAL));
        });
        register(context, SpellTypes.METAL_MENDING, 1, builder -> {
            builder.require(spiritual_level_2)
                    .require(ElementRequirement.create(Element.METAL));
        });
        register(context, SpellTypes.SHARPNESS, 1, builder -> {
            builder.require(spiritual_level_3)
                    .require(SpellRequirement.single(SpellTypes.METAL_MASTERY, 3));
        });

        /* 木系法术 */
        register(context, SpellTypes.LEVITATION, 1, builder -> {
            builder.require(spiritual_level_1)
                    .require(ElementRequirement.create(Element.WOOD));
        });
        register(context, SpellTypes.SPROUT, 1, builder -> {
            builder.require(spiritual_level_2)
                    .require(ElementRequirement.create(Element.WOOD))
                    .require(SpellRequirement.single(SpellTypes.RELEASING, 1));
        });
        register(context, SpellTypes.WITHER, 1, builder -> {
            builder.require(spiritual_level_2)
                    .require(ElementRequirement.create(Element.WOOD))
                    .require(SpellRequirement.single(SpellTypes.RELEASING, 1));
        });
        register(context, SpellTypes.WOOD_HEALING, 1, builder -> {
            builder.require(spiritual_level_3)
                    .require(SpellRequirement.single(SpellTypes.WOOD_MASTERY, 3));
        });

        /* 水系法术 */
        register(context, SpellTypes.WATER_BREATHING, 1, builder -> {
            builder.require(spiritual_level_2)
                    .require(ElementRequirement.create(Element.WATER));
        });

        /* 火系法术 */
        register(context, SpellTypes.BURNING, 1, builder -> {
            builder.require(spiritual_level_1)
                    .require(ElementRequirement.create(Element.FIRE));
        });
        register(context, SpellTypes.BURNING, 2, builder -> {
            builder.require(spiritual_level_2)
                    .require(ElementRequirement.create(Element.FIRE));
        });
        register(context, SpellTypes.LAVA_BREATHING, 1, builder -> {
            builder.require(spiritual_level_2)
                    .require(ElementRequirement.create(Element.FIRE));
        });
        register(context, SpellTypes.IGNITION, 1, builder -> {
            builder.require(spiritual_level_2)
                    .require(SpellRequirement.single(SpellTypes.FIRE_MASTERY, 1));
        });

        /* 土系法术 */
//        initialize(context, SpellTypes.EARTH_EVADING, 1, builder -> {
//            builder.require(spiritual_level_2)
//                    .require(ElementRequirement.create(Elements.EARTH));
//        });
        register(context, SpellTypes.CRYSTAL_EXPLOSION, 1, builder -> {
            builder.require(spiritual_level_3)
                    .require(SpellRequirement.single(SpellTypes.EARTH_MASTERY, 3));
        });
        register(context, SpellTypes.CRYSTAL_HEART, 1, builder -> {
            builder.require(spiritual_level_3)
                    .require(SpellRequirement.single(SpellTypes.EARTH_MASTERY, 3));
        });

        /* 元素精通 */
        for (Element element : Element.values()) {
            final SpellType spell = ElementalMasterySpell.getSpell(element);
            register(context, spell, 1, builder -> {
                builder.require(spiritual_level_2)
//                        .require(new EMPRequirement(1))
                        .require(SpellRequirement.single(SpellTypes.RELEASING, 1));
            });
            register(context, spell, 2, builder -> {
                builder.require(spiritual_level_2)
//                        .require(new EMPRequirement(2))
                        .require(SpellRequirement.single(SpellTypes.DISPERSAL, 1));
            });
            register(context, spell, 3, builder -> {
                builder.require(spiritual_level_3)
//                        .require(new EMPRequirement(3))
                        ;
            });
        }
    }

    static void register(BootstrapContext<SecretManual> context, SpellType spell, int level, Consumer<Builder> consumer){
        if(level > 0 && level <= spell.getMaxLevel()){
            final Builder builder = builder(spell, level);
            if(level > 1){
                builder.require(new SpellRequirement(List.of(com.mojang.datafixers.util.Pair.of(spell, level - 1))));
            }
            consumer.accept(builder);
            context.register(spellManual(spell, level), builder.build());
        } else {
            Util.warn("Secret Manuals Warn : Invalid spell level !");
        }
    }

    static HTCodecRegistry<SecretManual> registry(){
        return TUTORIALS;
    }

    static Builder builder(SpellType spell){
        return builder(spell, 1);
    }

    static Builder builder(SpellType spell, int level){
        return builder(new LearnSpellManual(spell, level));
    }

    static Builder builder(IManualContent content){
        return new Builder(content);
    }

    static ResourceKey<SecretManual> spellManual(SpellType spell) {
        return spellManual(spell, 1);
    }

    static ResourceKey<SecretManual> spellManual(SpellType spell, int level) {
        return registry().createKey(StringHelper.suffix(spell.getLocation(), String.valueOf(level)));
    }

    static ResourceKey<SecretManual> create(String name) {
        return registry().createKey(Util.prefix(name));
    }

    class Builder {

        private final IManualContent content;
        private final List<ILearnRequirement> requirements = new ArrayList<>();
        private ResourceLocation model = Util.prefix("secret_manual");

        public Builder(IManualContent content) {
            this.content = content;
        }

        public Builder require(ILearnRequirement requirement) {
            requirements.add(requirement);
            return this;
        }

        public Builder model(ResourceLocation model) {
            this.model = model;
            return this;
        }

        public SecretManual build() {
            return new SecretManual(requirements, content, model, Optional.empty());
        }
    }
}
