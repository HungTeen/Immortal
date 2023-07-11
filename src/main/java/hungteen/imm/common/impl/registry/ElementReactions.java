package hungteen.imm.common.impl.registry;

import hungteen.htlib.api.interfaces.IHTSimpleRegistry;
import hungteen.htlib.common.registry.HTRegistryManager;
import hungteen.htlib.common.registry.HTSimpleRegistry;
import hungteen.imm.api.enums.Elements;
import hungteen.imm.api.registry.IElementReaction;
import hungteen.imm.common.ElementManager;
import hungteen.imm.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-07-01 19:49
 **/
public class ElementReactions {

    private static final HTSimpleRegistry<IElementReaction> REACTIONS = HTRegistryManager.createSimple(Util.prefix("element_reaction"));

    public static IElementReaction ASHING = register(new GenerationReaction(
            "ashing", Elements.FIRE, 10, Elements.EARTH, 10, 5) {
        @Override
        public void doReaction(Entity entity) {
            if(entity instanceof LivingEntity living){
                living.heal(10F);
            }
        }
    });

    public static IHTSimpleRegistry<IElementReaction> registry(){
        return REACTIONS;
    }

    public static IElementReaction register(IElementReaction reaction){
        return REACTIONS.register(reaction);
    }

    private static abstract class ElementReaction implements IElementReaction {

        private final String name;
        private final Map<Elements, Float> elements;
        private final Map<Elements, Boolean> needRobust;
        private final int priority;

        private ElementReaction(String name, Map<Elements, Float> elements, Map<Elements, Boolean> needRobust, int priority) {
            this.name = name;
            this.elements = elements;
            this.needRobust = needRobust;
            this.priority = priority;
        }

        @Override
        public boolean match(Entity entity) {
            return elements.entrySet().stream().allMatch(entry -> {
                final boolean mustRobust = needRobust.get(entry.getKey());
                if(ElementManager.hasElement(entity, entry.getKey(), true)){
                    return true;
                }
                return !mustRobust && ElementManager.hasElement(entity, entry.getKey(), false);
            });
        }

        @Override
        public void consume(Entity entity) {
            elements.forEach((key, value) -> {
                // AI 是懂简化代码的。
                ElementManager.addElementAmount(entity, key, ElementManager.getElementAmount(entity, key, true) >= value, - value);
            });
        }

        @Override
        public int priority() {
            return priority;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getModID() {
            return Util.id();
        }
    }

    private static abstract class GenerationReaction extends ElementReaction {

        private final Elements production;
        private final float productionAmount;

        private GenerationReaction(String name, Elements main, float mainAmount, Elements off, float offAmount, float productionAmount) {
            super(name, Map.of(main, mainAmount, off, offAmount), Map.of(main, true, off, false), 100);
            this.production = off;
            this.productionAmount = productionAmount;
        }

        @Override
        public void doReaction(Entity entity) {
            ElementManager.addElementAmount(entity, this.production, false, this.productionAmount);
        }

    }

    private static abstract class InhibitionReaction extends ElementReaction {

        private InhibitionReaction(String name, Elements main, float mainAmount, Elements off, float offAmount) {
            super(name, Map.of(main, mainAmount, off, offAmount), Map.of(main, false, off, false), 80);
        }

    }

}
