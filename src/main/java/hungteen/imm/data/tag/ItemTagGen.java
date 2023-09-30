package hungteen.imm.data.tag;

import hungteen.htlib.data.tag.HTItemTagGen;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.common.tag.IMMItemTags;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:12
 **/
public class ItemTagGen extends HTItemTagGen {

    public ItemTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> tagLookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, tagLookup, Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        /* Forge Tags */
        this.tag(IMMItemTags.RICE_SEEDS).add(IMMItems.RICE_SEEDS.get());
        this.tag(IMMItemTags.JUTE_SEEDS).add(IMMItems.JUTE_SEEDS.get());
        this.tag(IMMItemTags.JUTE).add(IMMItems.JUTE.get());

        this.tag(IMMItemTags.SPIRITUAL_STONES).addTags(
                IMMItemTags.SPIRITUAL_STONES_LEVEL_ONE,
                IMMItemTags.SPIRITUAL_STONES_LEVEL_TWO
        );
        this.tag(IMMItemTags.SPIRITUAL_STONES_LEVEL_ONE).add(Items.EMERALD);
        this.tag(IMMItemTags.SPIRITUAL_STONES_LEVEL_TWO).add(Items.DIAMOND);

        this.tag(IMMItemTags.COMMON_ARTIFACTS).add(Items.ENDER_PEARL, Items.ENDER_EYE);
//        this.tag(IMMItemTags.MODERATE_ARTIFACTS).add(Items.TRIDENT);
        this.tag(IMMItemTags.ADVANCED_ARTIFACTS).add(Items.TOTEM_OF_UNDYING, Items.ELYTRA);
        this.copy(IMMBlockTags.COMMON_ARTIFACTS, IMMItemTags.COMMON_ARTIFACTS);
        this.copy(IMMBlockTags.MODERATE_ARTIFACTS, IMMItemTags.MODERATE_ARTIFACTS);
        this.copy(IMMBlockTags.ADVANCED_ARTIFACTS, IMMItemTags.ADVANCED_ARTIFACTS);

        this.tag(IMMItemTags.CINNABAR_GEMS).add(IMMItems.CINNABAR.get());
        this.copy(IMMBlockTags.CINNABAR_ORES, IMMItemTags.CINNABAR_ORES);

        /* IMM Tags */
        this.tag(IMMItemTags.MELEE_ATTACK_ITEMS).add(Items.STICK);
    }

}
