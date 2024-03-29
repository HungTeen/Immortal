package hungteen.imm.common.event;

import hungteen.htlib.util.helper.MathHelper;
import hungteen.htlib.util.helper.registry.ParticleHelper;
import hungteen.imm.ImmortalMod;
import hungteen.imm.common.block.IMMBlockPatterns;
import hungteen.imm.common.tag.IMMBlockTags;
import hungteen.imm.util.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
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
public class IMMBlockEvents {

    @SubscribeEvent
    public static void placeBlock(BlockEvent.EntityPlaceEvent event){
        if(! event.getLevel().isClientSide() && event.getLevel() instanceof Level level){
            if(IMMBlockPatterns.PUMPKINS_PREDICATE.test(event.getPlacedBlock())){
                EntityUtil.multiblockSpawn(level, event.getPos(), IMMBlockPatterns.getCreeperPattern().blockPattern(), EntityType.CREEPER);
            }
            if(event.getPlacedBlock().is(IMMBlockTags.FURNACE_BLOCKS)){

            }
        }
    }

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event){
        if(event.getState().is(IMMBlockTags.SPIRITUAL_ORES) && event.getLevel() instanceof Level level){
            ParticleHelper.spawnParticles(level, ParticleTypes.GLOW, MathHelper.toVec3(event.getPos()), 5, 0F, 0.1F);
        }
    }

}
