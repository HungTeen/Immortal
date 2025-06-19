package hungteen.imm.common.cultivation.impl;

import com.mojang.serialization.Codec;
import hungteen.htlib.api.registry.HTCodecRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.util.helper.StringHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.LearnRequirement;
import hungteen.imm.api.spell.ScrollContent;
import hungteen.imm.api.spell.Spell;
import hungteen.imm.api.spell.SpellType;
import hungteen.imm.common.cultivation.CultivationTypes;
import hungteen.imm.common.cultivation.ElementManager;
import hungteen.imm.common.cultivation.RealmTypes;
import hungteen.imm.common.cultivation.SpellTypes;
import hungteen.imm.common.cultivation.manual.LearnSpellScroll;
import hungteen.imm.common.cultivation.manual.SecretManual;
import hungteen.imm.common.cultivation.manual.SecretScroll;
import hungteen.imm.common.cultivation.spell.basic.ElementalMasterySpell;
import hungteen.imm.common.cultivation.manual.requirement.CultivationTypeRequirement;
import hungteen.imm.common.cultivation.manual.requirement.ElementRequirement;
import hungteen.imm.common.cultivation.manual.requirement.RealmRequirement;
import hungteen.imm.common.cultivation.manual.requirement.SpellRequirement;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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

    static void register(BootstrapContext<SecretManual> context) {
        final LearnRequirement spiritual = CultivationTypeRequirement.create(CultivationTypes.QI);
        final LearnRequirement spiritual_level_1 = RealmRequirement.create(RealmTypes.QI_REFINING.first(), true);
        final LearnRequirement spiritual_level_2 = RealmRequirement.create(RealmTypes.FOUNDATION.pre(), true);
        final LearnRequirement spiritual_level_3 = RealmRequirement.create(RealmTypes.CORE_SHAPING.pre(), true);

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
            SecretScroll mastery1 = register(context, spell, 1, builder -> {
                builder.require(spiritual_level_2)
//                        .require(new EMPRequirement(1))
                        .require(SpellRequirement.single(SpellTypes.RELEASING, 1));
            });
            SecretScroll mastery2 = register(context, spell, 2, builder -> {
                builder.require(spiritual_level_2)
//                        .require(new EMPRequirement(2))
                        .require(SpellRequirement.single(SpellTypes.DISPERSAL, 1));
            });
            SecretScroll mastery3 = register(context, spell, 3, builder -> {
                builder.require(spiritual_level_3)
//                        .require(new EMPRequirement(3))
                ;
            });
            register(context, Util.prefix(element.name().toLowerCase() + "element_manual"), TipUtil.manual("element_manual", ElementManager.name(element)), List.of(
                    mastery1, mastery2, mastery3)
            );
        }
    }

    /**
     * 注册成套的功法秘籍。
     */
    static void register(BootstrapContext<SecretManual> context, ResourceLocation title, List<SecretScroll> scrolls) {
        context.register(registry().createKey(title), new SecretManual(
                scrolls,
                Util.prefix("item/secret_manual"),
                TipUtil.misc(title.getPath()),
                Optional.of(TipUtil.manual("category.manual")),
                Optional.empty(),
                false
        ));
    }

    /**
     * 注册成套的功法秘籍。
     */
    static void register(BootstrapContext<SecretManual> context, ResourceLocation key, Component title, List<SecretScroll> scrolls) {
        context.register(registry().createKey(key), new SecretManual(
                scrolls,
                Util.prefix("item/secret_manual"),
                title,
                Optional.of(TipUtil.manual("category.manual")),
                Optional.empty(),
                false
        ));
    }

    /**
     * 注册单页的法术卷轴。
     * @return 卷轴。
     */
    static SecretScroll register(BootstrapContext<SecretManual> context, SpellType spell, int level, Consumer<SpellScrollBuilder> consumer) {
        if (level > 0 && level <= spell.getMaxLevel()) {
            final SpellScrollBuilder builder = builder(spell, level);
            if (level > 1) {
                builder.require(new SpellRequirement(List.of(Spell.create(spell, level - 1))));
            }
            consumer.accept(builder);
            SecretScroll scroll = builder.build();
            context.register(createSpellKey(spell, level), new SecretManual(
                    List.of(scroll),
                    Util.prefix("item/secret_scroll"),
                    scroll.getTitle(),
                    Optional.of(TipUtil.manual("category.spell")),
                    Optional.empty(),
                    false
            ));
            return scroll;
        }
        throw new RuntimeException("Secret Manuals Error : Invalid spell level for " + spell.getLocation() + " ! Level must be between 1 and " + spell.getMaxLevel() + ".");
    }

    static SpellScrollBuilder builder(SpellType spell) {
        return builder(spell, 1);
    }

    static SpellScrollBuilder builder(SpellType spell, int level) {
        return builder(new LearnSpellScroll(spell, level));
    }

    static SpellScrollBuilder builder(ScrollContent content) {
        return new SpellScrollBuilder(content);
    }

    static ResourceKey<SecretManual> createSpellKey(SpellType spell) {
        return createSpellKey(spell, 1);
    }

    static ResourceKey<SecretManual> createSpellKey(SpellType spell, int level) {
        return registry().createKey(StringHelper.suffix(spell.getLocation(), String.valueOf(level)));
    }

    static Codec<ResourceKey<SecretManual>> resourceKeyCodec() {
        return ResourceKey.codec(SecretManuals.registry().getRegistryKey());
    }

    static StreamCodec<RegistryFriendlyByteBuf, ResourceKey<SecretManual>> resourceKeyStreamCodec() {
        return ByteBufCodecs.fromCodecWithRegistries(resourceKeyCodec());
    }

    static HTCodecRegistry<SecretManual> registry() {
        return TUTORIALS;
    }


    static ResourceKey<SecretManual> create(String name) {
        return registry().createKey(Util.prefix(name));
    }

    class SpellScrollBuilder {

        private final ScrollContent content;
        private final List<LearnRequirement> requirements = new ArrayList<>();
        private Component title = null;

        public SpellScrollBuilder(ScrollContent content) {
            this.content = content;
        }

        public SpellScrollBuilder require(LearnRequirement requirement) {
            requirements.add(requirement);
            return this;
        }

        public SpellScrollBuilder title(Component title) {
            this.title = title;
            return this;
        }

        public SecretScroll build() {
            return new SecretScroll(requirements, content, Optional.ofNullable(title));
        }
    }

}
