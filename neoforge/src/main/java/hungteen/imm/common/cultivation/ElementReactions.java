package hungteen.imm.common.cultivation;

import hungteen.htlib.api.registry.HTSimpleRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.ElementReaction;
import hungteen.imm.common.cultivation.reaction.*;
import hungteen.imm.common.effect.IMMEffects;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-07-01 19:49
 **/
public interface ElementReactions {

    HTSimpleRegistry<ElementReaction> REACTIONS = HTRegistryManager.simple(Util.prefix("element_reaction"));

    /* 特殊反应 */

    ElementReaction METAL_GILDING = register(new GildingReaction(
            "metal_gilding", 10, Element.METAL, 8)
    );

    ElementReaction WOOD_GILDING = register(new GildingReaction(
            "wood_gilding", 10, Element.WOOD, 8)
    );

    ElementReaction WATER_GILDING = register(new GildingReaction(
            "water_gilding", 10, Element.WATER, 10)
    );

    ElementReaction FIRE_GILDING = register(new GildingReaction(
            "fire_gilding", 10, Element.FIRE, 10)
    );

    ElementReaction BURNING = register(new FlamingReaction(
            "burning", false, 120, false, 0.5F, 0.25F)
    );

    /* 化灵 */

    ElementReaction METAL_SPIRIT = register(new SummonSpiritReaction<>(
            "metal_spirit", IMMEntities.METAL_SPIRIT, Element.METAL, 10
    ));

    ElementReaction WOOD_SPIRIT = register(new SummonSpiritReaction<>(
            "wood_spirit", IMMEntities.WOOD_SPIRIT, Element.WOOD, 10
    ));

    ElementReaction WATER_SPIRIT = register(new SummonSpiritReaction<>(
            "water_spirit", IMMEntities.WATER_SPIRIT, Element.WATER, 10
    ));

    ElementReaction FIRE_SPIRIT = register(new SummonSpiritReaction<>(
            "fire_spirit", IMMEntities.FIRE_SPIRIT, Element.FIRE, 10
    ));

    ElementReaction EARTH_SPIRIT = register(new SummonSpiritReaction<>(
            "earth_spirit", IMMEntities.EARTH_SPIRIT, Element.EARTH, 10
    ));

    /* 相生 */

    ElementReaction CRYSTALLIZATION = register(new GenerationReaction(
            "crystallization", true, Element.EARTH, 10, Element.METAL, 4) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (entity instanceof LivingEntity living) {
                living.addEffect(EffectHelper.viewEffect(MobEffects.ABSORPTION, 1200, (int) (scale * 2)));
            }
        }
    });

    ElementReaction QUENCH_BLADE = register(new GenerationReaction(
            "quench_blade", false, Element.METAL, 0.5F, Element.WATER, 0.25F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
        }
    });

    ElementReaction GROWING = register(new GenerationReaction(
            "growing", false, Element.WATER, 0.5F, Element.WOOD, 0.2F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (entity instanceof LivingEntity living) {
                living.heal(0.25F * scale);
            }
        }
    });

    ElementReaction FLAMING = register(new FlamingReaction(
            "flaming", false, 100, true, 0.5F, 0.25F)
    );

    ElementReaction ASHING = register(new GenerationReaction(
            "ashing", true, Element.FIRE, 10, Element.EARTH, 4) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (!EntityUtil.isManaFull(entity)) {
                EntityUtil.addMana(entity, scale * 20);
            } else {
                if (entity instanceof LivingEntity living) {
                    living.heal(4F * scale);
                }
            }
        }
    });

    /* 相克 */

    ElementReaction SOLIDIFICATION = register(new InhibitionReaction(
            "solidification", true, Element.EARTH, 5, Element.WATER, 8) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (entity instanceof LivingEntity living) {
                living.addEffect(EffectHelper.viewEffect(IMMEffects.SOLIDIFICATION.holder(), (int) (100 * scale), Mth.ceil(scale)));
            }
        }
    });

    ElementReaction VAPORIZATION = register(new InhibitionReaction(
            "vaporization", true, Element.WATER, 3, Element.FIRE, 8) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (entity instanceof LivingEntity living) {
                List<MobEffectInstance> effects = living.getActiveEffects().stream().filter(instance -> instance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL).toList();
                if (!effects.isEmpty()) {
                    AreaEffectCloud areaeffectcloud = new AreaEffectCloud(entity.level(), entity.getX(), entity.getY(), entity.getZ());
                    areaeffectcloud.setRadius(2.5F);
                    areaeffectcloud.setRadiusOnUse(-0.5F);
                    areaeffectcloud.setWaitTime(10);
                    areaeffectcloud.setDuration((int) (areaeffectcloud.getDuration() * scale / 2));
                    areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());

                    for (MobEffectInstance mobeffectinstance : effects) {
                        areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
                    }

                    entity.level().addFreshEntity(areaeffectcloud);
                }
            }
        }
    });

    ElementReaction MELTING = register(new MeltingReaction(
            "melting", true, 5, Element.FIRE, 4, Element.METAL, 8)
    );

    ElementReaction CUTTING = register(new CuttingReaction(
            "cutting", false, 1F, Element.METAL, 0.15F, Element.WOOD, 0.4F) {
        /**
         * See {@link hungteen.imm.common.event.IMMLivingEvents#onLivingDamage(LivingDamageEvent)}.
         */
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
        }
    });

    ElementReaction PARASITISM = register(new InhibitionReaction(
            "parasitism", true, Element.WOOD, 0.2F, Element.EARTH, 0.4F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
        }
    });

    static MutableComponent getCategory() {
        return TipUtil.misc("element_reaction");
    }

    static HTSimpleRegistry<ElementReaction> registry() {
        return REACTIONS;
    }

    static ElementReaction register(ElementReaction reaction) {
        return REACTIONS.register(reaction);
    }

}
