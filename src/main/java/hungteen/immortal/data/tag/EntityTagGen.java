package hungteen.immortal.data.tag;

import hungteen.htlib.data.tag.HTEntityTagGen;
import hungteen.immortal.common.entity.ImmortalEntities;
import hungteen.immortal.common.tag.ImmortalEntityTags;
import hungteen.immortal.utils.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-07 12:13
 **/
public class EntityTagGen extends HTEntityTagGen {

    public EntityTagGen(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ImmortalEntityTags.VILLAGERS).add(EntityType.VILLAGER, EntityType.WANDERING_TRADER);
        this.tag(ImmortalEntityTags.PILLAGERS).add(EntityType.PILLAGER, EntityType.WITCH, EntityType.ILLUSIONER, EntityType.VINDICATOR, EntityType.EVOKER);
        this.tag(ImmortalEntityTags.CULTIVATORS).add(EntityType.PLAYER, ImmortalEntities.EMPTY_CULTIVATOR.get(), ImmortalEntities.SPIRITUAL_CULTIVATOR.get());

        this.tag(ImmortalEntityTags.HUMAN_BEINGS).addTags(ImmortalEntityTags.CULTIVATORS, ImmortalEntityTags.VILLAGERS, ImmortalEntityTags.PILLAGERS);
    }
}
