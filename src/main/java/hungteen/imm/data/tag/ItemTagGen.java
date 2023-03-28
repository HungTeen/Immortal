package hungteen.imm.data.tag;

import hungteen.htlib.data.tag.HTBlockTagGen;
import hungteen.htlib.data.tag.HTItemTagGen;
import hungteen.imm.common.tag.ImmortalItemTags;
import hungteen.imm.utils.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:12
 **/
public class ItemTagGen extends HTItemTagGen {

    public ItemTagGen(PackOutput output, HTBlockTagGen generator, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, generator, provider, Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ImmortalItemTags.SPIRITUAL_STONES).addTags(
                ImmortalItemTags.SPIRITUAL_STONES_LEVEL_ONE,
                ImmortalItemTags.SPIRITUAL_STONES_LEVEL_TWO
        );
        this.tag(ImmortalItemTags.SPIRITUAL_STONES_LEVEL_ONE).add(Items.EMERALD);
        this.tag(ImmortalItemTags.SPIRITUAL_STONES_LEVEL_TWO).add(Items.DIAMOND);

        this.tag(ImmortalItemTags.MELEE_ATTACK_ITEMS).add(Items.STICK);
    }

}
