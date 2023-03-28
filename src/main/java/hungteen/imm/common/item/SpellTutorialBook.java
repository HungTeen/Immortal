package hungteen.imm.common.item;

import hungteen.imm.api.registry.ISpellType;
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

    private final ISpellType spell;

    public SpellTutorialBook(ISpellType spell) {
        super(new Properties().stacksTo(1));
        this.spell = spell;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if(! level.isClientSide){

        }
        return super.use(level, player, interactionHand);
    }

}
