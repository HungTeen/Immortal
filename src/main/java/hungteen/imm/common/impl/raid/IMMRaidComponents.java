package hungteen.imm.common.impl.raid;

import hungteen.htlib.api.interfaces.raid.IPositionComponent;
import hungteen.htlib.api.interfaces.raid.IRaidComponent;
import hungteen.htlib.api.interfaces.raid.IWaveComponent;
import hungteen.htlib.common.impl.position.HTPositionComponents;
import hungteen.htlib.common.impl.raid.HTRaidComponents;
import hungteen.htlib.common.impl.wave.HTWaveComponents;
import hungteen.htlib.util.helper.ColorHelper;
import hungteen.htlib.util.helper.registry.SoundHelper;
import hungteen.imm.api.enums.RealmStages;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.impl.registry.RealmTypes;
import hungteen.imm.common.world.entity.trial.BreakThroughRaid;
import hungteen.imm.util.TipUtil;
import hungteen.imm.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;

import java.util.List;
import java.util.Optional;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-08-01 17:25
 **/
public class IMMRaidComponents {

    public static ResourceKey<IRaidComponent> MORTALITY_TO_SPIRITUAL_1 = create("mortality_to_spiritual_1");

    public static void register(BootstapContext<IRaidComponent> context){
        final HolderGetter<SoundEvent> sounds = SoundHelper.get().lookup(context);
        final HolderGetter<IWaveComponent> waves = context.lookup(HTWaveComponents.registry().getRegistryKey());
        final HolderGetter<IPositionComponent> placements = context.lookup(HTPositionComponents.registry().getRegistryKey());
        final Holder<IPositionComponent> around20 = placements.getOrThrow(IMMPositionComponents.AROUND_20_BLOCKS);
        final Holder<IPositionComponent> around15 = placements.getOrThrow(IMMPositionComponents.AROUND_15_BLOCKS);
        context.register(MORTALITY_TO_SPIRITUAL_1, raid(
                trialBuilder(sounds).place(around20),
                Util.wrap(waves, List.of(IMMWaveComponents.MORTALITY_WAVE_1, IMMWaveComponents.MORTALITY_WAVE_2, IMMWaveComponents.MORTALITY_WAVE_3)),
                RealmTypes.SPIRITUAL_LEVEL_1, RealmStages.PRELIMINARY
        ));
    }

    public static BreakThroughRaid raid(HTRaidComponents.RaidSettingBuilder builder, List<Holder<IWaveComponent>> waves, IRealmType realm, RealmStages stage){
        return new BreakThroughRaid(builder.build(), waves, realm, stage, 100, 0, 100);
    }

    public static HTRaidComponents.RaidSettingBuilder trialBuilder(HolderGetter<SoundEvent> sounds){
        return HTRaidComponents.builder()
                .blockInside(true)
                .blockOutside(true)
                .range(25)
                .renderBorder(true)
                .borderColor(ColorHelper.LIGHT_PURPLE.rgb())
                .color(BossEvent.BossBarColor.PURPLE)
                .title(TipUtil.info("trial.title"))
                .victoryTitle(TipUtil.info("trial.victory_title"))
                .lossTitle(TipUtil.info("trial.loss_title"))
                .raidSound(Optional.of(sounds.getOrThrow(SoundHelper.get().getResourceKey(SoundEvents.ENDERMAN_STARE).get())))
                ;
    }

    public static ResourceKey<IRaidComponent> create(String name){
        return HTRaidComponents.registry().createKey(Util.prefix(name));
    }
}
