package hungteen.imm.common.cultivation;

import hungteen.htlib.api.registry.HTSimpleRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.htlib.util.helper.impl.EffectHelper;
import hungteen.imm.api.cultivation.Element;
import hungteen.imm.api.spell.ElementReaction;
import hungteen.imm.common.cultivation.reaction.*;
import hungteen.imm.common.effect.IMMEffects;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-07-01 19:49
 **/
public interface ElementReactions {

    HTSimpleRegistry<ElementReaction> REACTIONS = HTRegistryManager.simple(Util.prefix("element_reaction"));

    /* 特殊反应 */

    ElementReaction METAL_GILDING = register(new GildingReaction(
            "metal_gilding", ElementManager.ONCE_COST_BASELINE, Element.METAL, 0.8F)
    );

    ElementReaction WOOD_GILDING = register(new GildingReaction(
            "wood_gilding", ElementManager.ONCE_COST_BASELINE, Element.WOOD, 0.8F)
    );

    ElementReaction WATER_GILDING = register(new GildingReaction(
            "water_gilding", ElementManager.ONCE_COST_BASELINE, Element.WATER, 1)
    );

    ElementReaction FIRE_GILDING = register(new GildingReaction(
            "fire_gilding", ElementManager.ONCE_COST_BASELINE, Element.FIRE, 1)
    );

    ElementReaction BURNING = register(new FlamingReaction(
            "burning", false, 120, false, ElementManager.DURATION_COST_BASELINE, 0.5F)
    );

    /* 化灵 */

    ElementReaction METAL_SPIRIT = register(new SummonSpiritReaction<>(
            "metal_spirit", IMMEntities.METAL_SPIRIT, Element.METAL, ElementManager.ONCE_COST_BASELINE
    ));

    ElementReaction WOOD_SPIRIT = register(new SummonSpiritReaction<>(
            "wood_spirit", IMMEntities.WOOD_SPIRIT, Element.WOOD, ElementManager.ONCE_COST_BASELINE
    ));

    ElementReaction WATER_SPIRIT = register(new SummonSpiritReaction<>(
            "water_spirit", IMMEntities.WATER_SPIRIT, Element.WATER, ElementManager.ONCE_COST_BASELINE
    ));

    ElementReaction FIRE_SPIRIT = register(new SummonSpiritReaction<>(
            "fire_spirit", IMMEntities.FIRE_SPIRIT, Element.FIRE, ElementManager.ONCE_COST_BASELINE
    ));

    ElementReaction EARTH_SPIRIT = register(new SummonSpiritReaction<>(
            "earth_spirit", IMMEntities.EARTH_SPIRIT, Element.EARTH, ElementManager.ONCE_COST_BASELINE
    ));

    /* 相生 */

    /**
     * 结晶。
     */
    ElementReaction CRYSTALLIZATION = register(new GenerationReaction(
            "crystallization", true, Element.EARTH, ElementManager.ONCE_COST_BASELINE, Element.METAL, 0.4F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (entity instanceof LivingEntity living) {
                living.addEffect(EffectHelper.viewEffect(MobEffects.ABSORPTION, 1200, (int) (scale * 2)));
            }
        }
    });

    /**
     * 淬刃。
     */
    ElementReaction QUENCH_BLADE = register(new GenerationReaction(
            "quench_blade", false, Element.METAL, ElementManager.DURATION_COST_BASELINE, Element.WATER, 0.5F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
        }
    });

    /**
     * 生长。
     */
    ElementReaction GROWING = register(new GenerationReaction(
            "growing", false, Element.WATER, ElementManager.DURATION_COST_BASELINE, Element.WOOD, 0.4F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (entity instanceof LivingEntity living) {
                living.heal(0.25F * scale);
            }
        }
    });

    /**
     * 燃烧。
     */
    ElementReaction FLAMING = register(new FlamingReaction(
            "flaming", false, 100, true, ElementManager.DURATION_COST_BASELINE, 0.5F)
    );

    /**
     * 化灰。
     */
    ElementReaction ASHING = register(new GenerationReaction(
            "ashing", true, Element.FIRE, ElementManager.ONCE_COST_BASELINE, Element.EARTH, 0.4F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (!QiManager.isQiFull(entity)) {
                QiManager.addQi(entity, scale * 20);
            } else {
                if (entity instanceof LivingEntity living) {
                    living.heal(4F * scale);
                }
            }
        }
    });

    /* 相克 */

    /**
     * 固化。
     */
    ElementReaction SOLIDIFICATION = register(new InhibitionReaction(
            "solidification", true, Element.EARTH, ElementManager.ONCE_COST_BASELINE, Element.WATER, 1.6F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (entity instanceof LivingEntity living) {
                living.addEffect(EffectHelper.viewEffect(IMMEffects.SOLIDIFICATION.holder(), (int) (100 * scale), Mth.ceil(scale)));
            }
        }
    });

    /**
     * 升腾。
     */
    ElementReaction VAPORIZATION = register(new InhibitionReaction(
            "vaporization", true, Element.WATER, ElementManager.ONCE_COST_BASELINE, Element.FIRE, 2F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
            if (entity instanceof LivingEntity living) {
                // TODO 升腾反应 感觉还是不太好
                living.addEffect(EffectHelper.viewEffect(MobEffects.WEAKNESS, (int) (200 * scale), Mth.ceil(scale) + 1));
            }
        }
    });

    /**
     * 熔融。
     */
    ElementReaction MELTING = register(new MeltingReaction(
            "melting", true, 1F, Element.FIRE, ElementManager.ONCE_COST_BASELINE, Element.METAL, 2F)
    );

    /**
     * 切裂。
     */
    ElementReaction CUTTING = register(new CuttingReaction(
            "cutting", false, 1F, Element.METAL, ElementManager.DURATION_COST_BASELINE, Element.WOOD, 2F) {
        @Override
        public void doReaction(Entity entity, float scale) {
            super.doReaction(entity, scale);
        }
    });

    /**
     * 寄生。
     */
    ElementReaction PARASITISM = register(new InhibitionReaction(
            "parasitism", false, Element.WOOD, ElementManager.DURATION_COST_BASELINE, Element.EARTH, 2F) {
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
