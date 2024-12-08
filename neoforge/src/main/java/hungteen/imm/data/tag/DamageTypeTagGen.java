package hungteen.imm.data.tag;

import hungteen.imm.common.IMMDamageTypes;
import hungteen.imm.common.tag.IMMDamageTypeTags;
import hungteen.imm.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/8/22 11:11
 */
public class DamageTypeTagGen extends DamageTypeTagsProvider {

    public DamageTypeTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Util.id(), existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        vanilla();
        uniform();

        this.tag(IMMDamageTypeTags.IGNORE_REALM)
                .add(DamageTypes.LIGHTNING_BOLT, DamageTypes.IN_WALL, DamageTypes.CRAMMING, DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.OUTSIDE_BORDER, DamageTypes.GENERIC_KILL, IMMDamageTypes.ELEMENT_REACTION);
        this.tag(IMMDamageTypeTags.IMM_REALM_LEVEL_1)
                .add(DamageTypes.CACTUS, DamageTypes.SWEET_BERRY_BUSH, DamageTypes.FREEZE);
        this.tag(IMMDamageTypeTags.IMM_REALM_LEVEL_2)
                .add(DamageTypes.IN_FIRE, DamageTypes.ON_FIRE, DamageTypes.HOT_FLOOR, DamageTypes.FALL, DamageTypes.FLY_INTO_WALL);
        this.tag(IMMDamageTypeTags.IMM_REALM_LEVEL_3)
                .add(DamageTypes.LAVA, DamageTypes.DROWN, DamageTypes.STARVE, DamageTypes.WITHER);

        this.tag(IMMDamageTypeTags.SPIRITUALS).add(IMMDamageTypes.QI);
        this.tag(IMMDamageTypeTags.ELEMENTS)
                .add(IMMDamageTypes.WOOD_ELEMENT, IMMDamageTypes.WATER_ELEMENT, IMMDamageTypes.FIRE_ELEMENT);

    }

    private void vanilla(){
        this.tag(DamageTypeTags.BYPASSES_ARMOR)
                .addTag(IMMDamageTypeTags.SPIRITUALS)
                .add(IMMDamageTypes.ELEMENT_REACTION)
        ;
        this.tag(DamageTypeTags.BYPASSES_SHIELD).addTag(IMMDamageTypeTags.ELEMENTS);
    }

    private void uniform(){
        this.tag(IMMDamageTypeTags.MELEE_DAMAGES)
                .add(DamageTypes.MOB_ATTACK, DamageTypes.MOB_ATTACK_NO_AGGRO, DamageTypes.PLAYER_ATTACK);
    }
}
