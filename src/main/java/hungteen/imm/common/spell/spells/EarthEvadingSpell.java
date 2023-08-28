package hungteen.imm.common.spell.spells;

import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.api.HTHitResult;
import hungteen.imm.util.BlockUtil;
import hungteen.imm.util.EntityUtil;
import hungteen.imm.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-28 21:11
 **/
public class EarthEvadingSpell extends SpellType{
    public EarthEvadingSpell() {
        super("earth_evading", properties().maxLevel(1).mana(75).cd(300));
    }

    @Override
    public boolean checkActivate(LivingEntity owner, HTHitResult result, int level) {
        if(owner.onGround() && owner.level() instanceof ServerLevel serverLevel){
            final BlockPos.MutableBlockPos pos = owner.blockPosition().mutable();
            pos.move(Direction.DOWN);
            while(pos.getY() >= serverLevel.getMinBuildHeight()){
                // 必须是能破坏的方块，否则无法穿过。
                if(BlockUtil.canDestroy(owner.level(), pos)){
                    // 传送后不会卡墙。
                    boolean found = false;
                    while(serverLevel.noCollision(EntityUtil.getEntityAABB(owner, new Vec3(0, pos.getY() - owner.getY(), 0)))){
                        found = true;
                        pos.move(Direction.DOWN);
                    }
                    if(found){
                        owner.teleportTo(owner.getX(), pos.getY() + 1, owner.getZ());
                        owner.playSound(SoundEvents.GRAVEL_FALL);
                        ParticleHelper.spawnParticles(serverLevel, ParticleUtil.block(Blocks.DIRT.defaultBlockState()), owner.getEyePosition(), 20, 0.3F, 0.1F);
                        return true;
                    }
                    pos.move(Direction.DOWN);
                } else {
                    this.sendTip(owner, "can_not_evade_through");
                    break;
                }
            }
        } else {
            this.sendTip(owner, "not_on_ground");
        }
        return false;
    }
}
