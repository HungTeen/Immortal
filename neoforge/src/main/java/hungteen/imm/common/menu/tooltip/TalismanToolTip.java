package hungteen.imm.common.menu.tooltip;

import hungteen.imm.api.spell.TalismanSpell;
import hungteen.imm.common.cultivation.ArtifactManager;
import hungteen.imm.common.item.talisman.TalismanItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-10-27 21:17
 **/
public class TalismanToolTip implements TooltipComponent {

    private final ItemStack stack;
    private final TalismanSpell spell;

    public TalismanToolTip(ItemStack stack, TalismanSpell spell) {
        this.stack = stack;
        this.spell = spell;
    }

    public Component getTitle(){
        return ArtifactManager.getArtifactComponent(stack);
    }

    public Optional<Component> getRequirement(){
        if(!getSpell().requireQiRoots().isEmpty()){
            return Optional.of(TalismanItem.requireQiRoots());
        } else if(!getSpell().requireElements().isEmpty()){
            return Optional.of(TalismanItem.requireElements());
        }
        return Optional.empty();
    }

    public TalismanSpell getSpell() {
        return spell;
    }

    public ResourceLocation getTexture(){
        return spell.getSpellTexture();
    }

}
