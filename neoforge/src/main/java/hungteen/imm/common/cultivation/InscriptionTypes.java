package hungteen.imm.common.cultivation;

import hungteen.htlib.api.registry.HTCustomRegistry;
import hungteen.htlib.common.impl.registry.HTRegistryManager;
import hungteen.imm.api.spell.InscriptionType;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.tag.IMMItemTags;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/16 9:59
 **/
public interface InscriptionTypes {

    HTCustomRegistry<InscriptionType> TYPES = HTRegistryManager.custom(Util.prefix("inscription_type"));

    InscriptionType NONE = registry().register(Util.prefix("none"), new InscriptionTypeImpl("none") {
        @Override
        public boolean compatWith(LivingEntity living, ItemStack item) {
            return false;
        }
    });

    InscriptionType ANY = registry().register(Util.prefix("any"), new InscriptionTypeImpl("any") {
        @Override
        public boolean compatWith(LivingEntity living, ItemStack item) {
            return !item.is(IMMItemTags.EMPTY_TALISMANS);
        }
    });

    InscriptionType JOSS_PAPER = registry().register(Util.prefix("joss_paper"), new InscriptionTypeImpl("joss_paper") {
        @Override
        public boolean compatWith(LivingEntity living, ItemStack item) {
            return item.is(IMMItems.JOSS_PAPER.get());
        }
    });

    static HTCustomRegistry<InscriptionType> registry(){
        return TYPES;
    }

    abstract class InscriptionTypeImpl implements InscriptionType {

        private final String name;

        public InscriptionTypeImpl(String name) {
            this.name = name;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public MutableComponent getComponent() {
            return TipUtil.spell("inscription." + name);
        }

        @Override
        public String getModID() {
            return Util.id();
        }

    }
}
