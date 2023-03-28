package hungteen.imm.mixin;

import com.google.common.collect.ImmutableList;
import hungteen.htlib.HTLib;
import hungteen.htlib.common.world.entity.DummyEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;
import java.lang.annotation.Target;
import java.util.List;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/2 8:50
 */
@Mixin(FoodData.class)
public class MixinFoodData {

    @Shadow private int tickTimer;

    @Inject(method = "tick(Lnet/minecraft/world/entity/player/Player;)V",
            at = @At(
                    value = "INVOKE",
                    target = "net.minecraft.world.entity.player.Player.heal(F)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void onTick(Player player, CallbackInfo result) {
        if(player.getHealth() * 2 > player.getMaxHealth()){
            this.tickTimer = 0;
            result.cancel();
        }
    }
}
