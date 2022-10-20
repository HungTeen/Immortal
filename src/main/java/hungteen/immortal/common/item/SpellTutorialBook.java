package hungteen.immortal.common.item;

import hungteen.immortal.api.registry.ISpell;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-11 23:01
 **/
public class SpellTutorialBook extends Item {

    private final ISpell spell;

    public SpellTutorialBook(ISpell spell) {
        super(new Properties().stacksTo(0).tab(ItemTabs.MATERIALS));
        this.spell = spell;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if(! level.isClientSide){

        }
        return super.use(level, player, interactionHand);
    }

}
