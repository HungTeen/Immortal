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
        /* Forge */
        this.tag(IMMEntityTags.VILLAGERS).add(EntityType.VILLAGER, EntityType.WANDERING_TRADER);
        this.tag(IMMEntityTags.PILLAGERS).add(EntityType.PILLAGER, EntityType.WITCH, EntityType.ILLUSIONER, EntityType.VINDICATOR, EntityType.EVOKER);
        this.tag(IMMEntityTags.COMMON_ARTIFACTS).add(EntityType.ENDER_PEARL);
        this.tag(IMMEntityTags.REALM_FOLLOW_OWNER_ENTITIES)
                .add(EntityType.LLAMA_SPIT, EntityType.EVOKER_FANGS, EntityType.SHULKER_BULLET)
                .add(IMMEntities.TORNADO.get());

        /* Immortal */
        this.tag(IMMEntityTags.NO_ELEMENT_REACTIONS).add(IMMEntities.SPIRITUAL_PEARL.get(), IMMEntities.ELEMENT_AMETHYST.get());
        this.tag(IMMEntityTags.CULTIVATORS).add(EntityType.PLAYER, IMMEntities.EMPTY_CULTIVATOR.get(), IMMEntities.SPIRITUAL_BEGINNER_CULTIVATOR.get());
        this.tag(IMMEntityTags.HUMAN_BEINGS).addTags(IMMEntityTags.CULTIVATORS, IMMEntityTags.VILLAGERS, IMMEntityTags.PILLAGERS);
    }

}
