package hungteen.immortal.data.tag;

import hungteen.htlib.data.tag.HTItemTagGen;
import hungteen.htlib.util.helper.ItemHelper;
import hungteen.immortal.common.tag.ImmortalItemTags;
import hungteen.immortal.utils.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TridentItem;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:12
 **/
public class ItemTagGen extends HTItemTagGen {

    public ItemTagGen(DataGenerator generator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ImmortalItemTags.SPIRITUAL_STONES).addTags(
                ImmortalItemTags.SPIRITUAL_STONES_LEVEL_ONE,
                ImmortalItemTags.SPIRITUAL_STONES_LEVEL_TWO
        );
        this.tag(ImmortalItemTags.SPIRITUAL_STONES_LEVEL_ONE).add(Items.EMERALD);
        this.tag(ImmortalItemTags.SPIRITUAL_STONES_LEVEL_TWO).add(Items.DIAMOND);

        this.tag(ImmortalItemTags.MELEE_ATTACK_ITEMS).add(Items.STICK);
    }
}
