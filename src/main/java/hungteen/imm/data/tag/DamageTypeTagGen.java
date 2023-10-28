package hungteen.imm.data.tag;

import hungteen.imm.common.misc.damage.IMMDamageTypes;
import hungteen.imm.common.tag.IMMDamageTypeTags;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/8/22 11:11
 */
public class DamageTypeTagGen extends DamageTypeTagsProvider {

    public DamageTypeTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // IMM Tags.
        this.tag(IMMDamageTypeTags.SPIRITUALS).add(IMMDamageTypes.SPIRITUAL_MANA);
        // Vanilla Tags.
        this.tag(DamageTypeTags.BYPASSES_ARMOR).addTag(IMMDamageTypeTags.SPIRITUALS);
        this.tag(DamageTypeTags.IS_FIRE).add(IMMDamageTypes.FIRE_ELEMENT);
    }
}
