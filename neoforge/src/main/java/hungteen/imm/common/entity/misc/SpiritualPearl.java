package hungteen.imm.common.entity.misc;

import hungteen.htlib.common.entity.HTEntity;
import hungteen.htlib.util.helper.impl.EntityHelper;
import hungteen.imm.common.ElementManager;
import hungteen.imm.common.entity.IMMEntities;
import hungteen.imm.common.item.IMMItems;
import hungteen.imm.util.EntityUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @author PangTeen
 * @program Immortal
 * @create 2023/3/1 10:38
 */
public class SpiritualPearl extends HTEntity implements ItemSupplier {

    private Player owner;
    private Entity target;

    public SpiritualPearl(EntityType<?> type, Level world) {
        super(type, world);
    }

    public SpiritualPearl(Level world, Player player, double x, double y, double z) {
        super(IMMEntities.SPIRITUAL_PEARL.get(), world);
        this.owner = player;
        this.setPos(x, y, z);
    }

    @Override
    public void onFirstSpawn() {
        super.onFirstSpawn();
        if(EntityHelper.isServer(this)){
            this.getCheckTarget().ifPresent(target -> {
                EntityUtil.getElements(target, this.owner).forEach(element -> {
                    ElementManager.addElementAmount(this, element, false, 1000);
                });
            });
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.tickMove(0.98F, 0.8F);
        if(EntityHelper.isServer(this) && this.tickCount > 20){
            if(! ElementManager.hasElement(this, false)){
                this.playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F);
                this.discard();
            }
        }
    }

    public void setCheckTarget(Entity entity){
        this.target = entity;
    }

    public Optional<Entity> getCheckTarget(){
        return Optional.ofNullable(this.target);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double dis) {
        double d0 = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(d0)) {
            d0 = 4.0D;
        }

        d0 *= 64.0D;
        return dis < d0 * d0;
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(IMMItems.SPIRITUAL_PEARL.get());
    }

    @Override
    protected float getGravityVelocity() {
        return 0.00025F;
    }
}
