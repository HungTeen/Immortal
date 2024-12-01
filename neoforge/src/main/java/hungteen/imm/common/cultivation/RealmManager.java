package hungteen.imm.common.cultivation;

import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.api.entity.Cultivatable;
import hungteen.imm.api.interfaces.IArtifactBlock;
import hungteen.imm.api.interfaces.IArtifactItem;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.common.tag.IMMItemTags;
import hungteen.imm.compat.minecraft.VanillaCultivationCompat;
import hungteen.imm.util.PlayerUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2024/11/30 15:13
 **/
public class RealmManager {

    /**
     * 有大境界差距并且左边的大。
     */
    public static boolean hasRealmGapAndLarger(Entity entity1, Entity entity2) {
        final RealmType realm1 = getRealm(entity1);
        final RealmType realm2 = getRealm(entity2);
        return hasRealmGap(realm1, realm2) && compare(realm1, realm2);
    }

    public static int getRealmGap(Entity entity1, Entity entity2) {
        return getRealmGap(getRealm(entity1), getRealm(entity2));
    }

    /**
     * @return 正数表示左边境界大。
     */
    public static int getRealmGap(RealmType realm1, RealmType realm2) {
        return realm1.getRealmValue() / 100 - realm2.getRealmValue() / 100;
    }

    /**
     * 有大境界差距。
     */
    public static boolean hasRealmGap(RealmType realm1, RealmType realm2) {
        return getRealmGap(realm1, realm2) != 0;
    }

    /**
     * @return 左边的境界大返回 true。
     */
    public static boolean compare(RealmType realm1, RealmType realm2) {
        return realm1.getRealmValue() > realm2.getRealmValue();
    }

    public static RealmType getRealm(Entity entity) {
        if(entity instanceof Cultivatable realmEntity){
            return realmEntity.getRealm();
        } else if(entity instanceof LivingEntity){
            if(entity instanceof Player player){
                return PlayerUtil.getPlayerRealm(player);
            }
            return VanillaCultivationCompat.getDefaultRealm(entity.getType(), RealmTypes.MORTALITY);
        } else {
            return VanillaCultivationCompat.getDefaultRealm(entity.getType(), RealmTypes.NOT_IN_REALM);
        }
    }

    public static RealmType getRealm(ItemStack stack){
        if(stack.is(IMMItemTags.COMMON_ARTIFACTS)) {
            return RealmTypes.COMMON_ARTIFACT;
        } else if(stack.is(IMMItemTags.MODERATE_ARTIFACTS)) {
            return RealmTypes.MODERATE_ARTIFACT;
        } else if(stack.is(IMMItemTags.ADVANCED_ARTIFACTS)) {
            return RealmTypes.ADVANCED_ARTIFACT;
        } else if(stack.getItem() instanceof IArtifactItem artifactItem) {
            return artifactItem.getArtifactRealm(stack);
        } else if(stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof IArtifactBlock artifactBlock) {
            return artifactBlock.getArtifactRealm(stack);
        }
        return RealmTypes.NOT_IN_REALM;
    }

    public static RealmType getRealm(BlockState state){
        if(state.is(IMMBlockTags.COMMON_ARTIFACTS)) {
            return RealmTypes.COMMON_ARTIFACT;
        } else if(state.is(IMMBlockTags.MODERATE_ARTIFACTS)) {
            return RealmTypes.MODERATE_ARTIFACT;
        } else if(state.is(IMMBlockTags.ADVANCED_ARTIFACTS)) {
            return RealmTypes.ADVANCED_ARTIFACT;
        } else if(state.getBlock() instanceof IArtifactBlock artifactBlock) {
            return artifactBlock.getRealm(state);
        }
        return RealmTypes.NOT_IN_REALM;
    }

    public static boolean notCommon(RealmType type) {
        return type != RealmTypes.NOT_IN_REALM && type != RealmTypes.MORTALITY;
    }
}
