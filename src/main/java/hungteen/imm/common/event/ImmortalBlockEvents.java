package hungteen.imm.common.event;

import hungteen.imm.ImmortalMod;
import hungteen.imm.common.block.ImmortalBlockPatterns;
import hungteen.imm.utils.EntityUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author PangTeen
 * @program Immortal
 * @data 2023/3/14 17:17
 */
@Mod.EventBusSubscriber(modid = ImmortalMod.MOD_ID)
public class ImmortalBlockEvents {

    @SubscribeEvent
    public static void placeBlock(BlockEvent.EntityPlaceEvent event){
        if(! event.getLevel().isClientSide() && event.getLevel() instanceof Level level){
            if(ImmortalBlockPatterns.PUMPKINS_PREDICATE.test(event.getPlacedBlock())){
                EntityUtil.multiblockSpawn(level, event.getPos(), ImmortalBlockPatterns.getCreeperPattern(), EntityType.CREEPER);
            }
        }
    }

}
