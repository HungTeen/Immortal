package hungteen.imm.data.tag;

import hungteen.htlib.data.tag.HTHolderTagsProvider;
import hungteen.htlib.util.helper.registry.EntityHelper;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.tag.IMMEntityTags;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:13
 **/
public class EntityTagGen extends HTHolderTagsProvider<EntityType<?>> {

    public EntityTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, EntityHelper.get(), Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(IMMEntityTags.VILLAGERS).add(EntityType.VILLAGER, EntityType.WANDERING_TRADER);
        this.tag(IMMEntityTags.PILLAGERS).add(EntityType.PILLAGER, EntityType.WITCH, EntityType.ILLUSIONER, EntityType.VINDICATOR, EntityType.EVOKER);
        this.tag(IMMEntityTags.CULTIVATORS).add(EntityType.PLAYER, IMMEntities.EMPTY_CULTIVATOR.get(), IMMEntities.SPIRITUAL_CULTIVATOR.get());

        this.tag(IMMEntityTags.HUMAN_BEINGS).addTags(IMMEntityTags.CULTIVATORS, IMMEntityTags.VILLAGERS, IMMEntityTags.PILLAGERS);

    }

}
