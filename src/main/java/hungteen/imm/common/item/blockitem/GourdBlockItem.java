package hungteen.imm.common.item.blockitem;

import hungteen.imm.common.block.plants.GourdGrownBlock;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class GourdBlockItem extends BlockItem {

    private final GourdGrownBlock.GourdTypes gourdType;

    public GourdBlockItem(GourdGrownBlock.GourdTypes gourdType, Block block) {
        super(block, new Item.Properties().food(new FoodProperties.Builder()
                .nutrition(2).saturationMod(0.3F)
                .effect(gourdType.getEffectSupplier(), 1F)
                .build()
        ));
        this.gourdType = gourdType;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        ItemStack itemstack = super.finishUsingItem(itemStack, level, entity);
        if (!level.isClientSide && this.gourdType == GourdGrownBlock.GourdTypes.PURPLE) {
            double d0 = entity.getX();
            double d1 = entity.getY();
            double d2 = entity.getZ();

            for(int i = 0; i < 16; ++i) {
                double d3 = entity.getX() + (entity.getRandom().nextDouble() - 0.5D) * 32.0D;
                double d4 = Mth.clamp(entity.getY() + (double)(entity.getRandom().nextInt(16) - 8), level.getMinBuildHeight(), level.getMinBuildHeight() + ((ServerLevel)level).getLogicalHeight() - 1);
                double d5 = entity.getZ() + (entity.getRandom().nextDouble() - 0.5D) * 32.0D;
                if (entity.isPassenger()) {
                    entity.stopRiding();
                }

                Vec3 vec3 = entity.position();
                level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(entity));
                if (entity.randomTeleport(d3, d4, d5, true)) {
                    SoundEvent soundevent = SoundEvents.CHORUS_FRUIT_TELEPORT;
                    level.playSound(null, d0, d1, d2, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                    entity.playSound(soundevent, 1.0F, 1.0F);
                    break;
                }
            }

            if (entity instanceof Player) {
                ((Player)entity).getCooldowns().addCooldown(this, 20);
            }
        }

        return itemstack;
    }

    public GourdGrownBlock.GourdTypes getGourdType() {
        return gourdType;
    }
}
