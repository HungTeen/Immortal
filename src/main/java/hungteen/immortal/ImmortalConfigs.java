package hungteen.immortal;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-09-24 16:17
 **/
public class ImmortalConfigs {

    private static Common COMMON_CONFIG;
    private static Client CLIENT_CONFIG;

    /**
     * {@link ImmortalMod#ImmortalMod()}
     */
    public static void init(){
        {
            final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
            ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, specPair.getRight());
            COMMON_CONFIG = specPair.getLeft();
        }
        {
            final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
            ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, specPair.getRight());
            CLIENT_CONFIG = specPair.getLeft();
        }
    }

    public static class Common {

        public Common(ForgeConfigSpec.Builder builder) {
            //World Settings.
            builder.comment("Settings about global rules.").push("Rule Settings");
            {

                builder.comment("Settings about spiritual roots.").push("Spiritual Roots Settings");
                {
                    RuleSettings.noRootChance = builder
                            .translation("config.immortal.no_root_chance")
                            .comment("The chance that players have no spiritual root.")
                            .defineInRange("NoRootChance", 0.0, 0, 1);

                    RuleSettings.oneRootChance = builder
                            .translation("config.immortal.one_root_chance")
                            .comment("The chance that players have one spiritual root.")
                            .defineInRange("OneRootChance", 0.1, 0, 1);

                    RuleSettings.twoRootChance = builder
                            .translation("config.immortal.two_root_chance")
                            .comment("The chance that players have two spiritual roots.")
                            .defineInRange("TwoRootChance", 0.2, 0, 1);

                    RuleSettings.threeRootChance = builder
                            .translation("config.immortal.three_root_chance")
                            .comment("The chance that players have three spiritual roots.")
                            .defineInRange("ThreeRootChance", 0.3, 0, 1);

                    RuleSettings.fourRootChance = builder
                            .translation("config.immortal.four_root_chance")
                            .comment("The chance that players have four spiritual roots.")
                            .defineInRange("FourRootChance", 0.3, 0, 1);

                    RuleSettings.metalRootWeight = builder
                            .translation("config.immortal.metal_root_weight")
                            .comment("The weight that players have metal spiritual root.")
                            .defineInRange("MetalRootChance", 100, 0, 1000000);

                    RuleSettings.woodRootWeight = builder
                            .translation("config.immortal.wood_root_weight")
                            .comment("The weight that players have wood spiritual root.")
                            .defineInRange("WoodRootChance", 100, 0, 1000000);

                    RuleSettings.waterRootWeight = builder
                            .translation("config.immortal.water_root_weight")
                            .comment("The weight that players have water spiritual root.")
                            .defineInRange("WaterRootChance", 100, 0, 1000000);

                    RuleSettings.fireRootWeight = builder
                            .translation("config.immortal.fire_root_weight")
                            .comment("The weight that players have fire spiritual root.")
                            .defineInRange("FireRootChance", 100, 0, 1000000);

                    RuleSettings.earthRootWeight = builder
                            .translation("config.immortal.earth_root_weight")
                            .comment("The weight that players have earth spiritual root.")
                            .defineInRange("EarthRootChance", 100, 0, 1000000);

                    RuleSettings.windRootWeight = builder
                            .translation("config.immortal.wind_root_weight")
                            .comment("The weight that players have wind spiritual root (valid only when one spirit root).")
                            .defineInRange("WindRootChance", 30, 0, 1000000);

                    RuleSettings.electricRootWeight = builder
                            .translation("config.immortal.electric_root_weight")
                            .comment("The weight that players have electric spiritual root (valid only when one spirit root).")
                            .defineInRange("ElectricRootChance", 20, 0, 1000000);

                    RuleSettings.drugRootWeight = builder
                            .translation("config.immortal.drug_root_weight")
                            .comment("The weight that players have drug spiritual root (valid only when one spirit root).")
                            .defineInRange("DrugRootChance", 20, 0, 1000000);

                    RuleSettings.iceRootWeight = builder
                            .translation("config.immortal.ice_root_weight")
                            .comment("The weight that players have ice spiritual root (valid only when one spirit root).")
                            .defineInRange("IceRootChance", 30, 0, 1000000);


                }
                builder.pop();
//                RuleSettings.CanSpawnDefaultMonster = builder
//                        .translation("config.pvz.rule.spawn_monster")
//                        .comment("if turn to false, there will have no monster of other monsters spawn in overworld except pvz zombies.")
//                        .define("CanSpawnDefaultMonster", true);

            }
            builder.pop();

            builder.comment("Settings about blocks.").push("Block Settings");
            {
                BlockSettings.furnaceExplodeCD = builder
                        .translation("config.immortal.furnace_explode_cd")
                        .comment("How long will furnace explode when it went wrong.")
                        .defineInRange("FurnaceExplodeCD", 200, 0, 1000000);
            }
            builder.pop();
        }


        public RuleSettings RuleSettings = new RuleSettings();
        public BlockSettings BlockSettings = new BlockSettings();

        public static class RuleSettings {
            public ForgeConfigSpec.DoubleValue noRootChance;
            public ForgeConfigSpec.DoubleValue oneRootChance;
            public ForgeConfigSpec.DoubleValue twoRootChance;
            public ForgeConfigSpec.DoubleValue threeRootChance;
            public ForgeConfigSpec.DoubleValue fourRootChance;
            public ForgeConfigSpec.IntValue metalRootWeight;
            public ForgeConfigSpec.IntValue woodRootWeight;
            public ForgeConfigSpec.IntValue waterRootWeight;
            public ForgeConfigSpec.IntValue fireRootWeight;
            public ForgeConfigSpec.IntValue earthRootWeight;
            public ForgeConfigSpec.IntValue windRootWeight;
            public ForgeConfigSpec.IntValue drugRootWeight;
            public ForgeConfigSpec.IntValue electricRootWeight;
            public ForgeConfigSpec.IntValue iceRootWeight;
        }

        public static class BlockSettings {
            public ForgeConfigSpec.IntValue furnaceExplodeCD;
        }

    }

    public static class Client {

        public Client(ForgeConfigSpec.Builder builder) {
                defaultSpellCircle = builder
                        .translation("config.immortal.default_spell_circle")
                        .comment("Use the default setting to control spell circle.")
                        .define("DefaultSpellCircle", true);
        }

        public ForgeConfigSpec.BooleanValue defaultSpellCircle;

    }

    /* Common */

    public static double getNoRootChance() {
        return COMMON_CONFIG.RuleSettings.noRootChance.get();
    }

    public static double getOneRootChance() {
        return COMMON_CONFIG.RuleSettings.oneRootChance.get();
    }

    public static double getTwoRootChance() {
        return COMMON_CONFIG.RuleSettings.twoRootChance.get();
    }

    public static double getThreeRootChance() {
        return COMMON_CONFIG.RuleSettings.threeRootChance.get();
    }

    public static double getFourRootChance() {
        return COMMON_CONFIG.RuleSettings.fourRootChance.get();
    }

    public static int getMetalWeight() {
        return COMMON_CONFIG.RuleSettings.metalRootWeight.get();
    }

    public static int getWoodWeight() {
        return COMMON_CONFIG.RuleSettings.woodRootWeight.get();
    }

    public static int getWaterWeight() {
        return COMMON_CONFIG.RuleSettings.waterRootWeight.get();
    }

    public static int getFireWeight() {
        return COMMON_CONFIG.RuleSettings.fireRootWeight.get();
    }

    public static int getEarthWeight() {
        return COMMON_CONFIG.RuleSettings.earthRootWeight.get();
    }

    public static int getWindWeight() {
        return COMMON_CONFIG.RuleSettings.windRootWeight.get();
    }

    public static int getElectricWeight() {
        return COMMON_CONFIG.RuleSettings.electricRootWeight.get();
    }

    public static int getDrugWeight() {
        return COMMON_CONFIG.RuleSettings.drugRootWeight.get();
    }

    public static int getIceWeight() {
        return COMMON_CONFIG.RuleSettings.iceRootWeight.get();
    }

    public static int getFurnaceExplodeCD() {
        return COMMON_CONFIG.BlockSettings.furnaceExplodeCD.get();
    }

    /* Client */

    public static boolean defaultSpellCircle() {
        return CLIENT_CONFIG.defaultSpellCircle.get();
    }

}
