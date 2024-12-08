package hungteen.imm.common.entity.human.cultivator;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import hungteen.imm.util.Util;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;

import java.util.Optional;
import java.util.UUID;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2022-12-06 16:45
 **/
public enum CultivatorType implements WeightedEntry, StringRepresentable {

    SLIM_STEVE(Component.literal("Steve"), "player/slim/steve", true, 1000, true),
    WIDE_STEVE(Component.literal("Steve"), "player/wide/steve", false, 1000, true),
    SLIM_ALEX(Component.literal("Alex"), "player/slim/alex", true, 1000, true),
    WIDE_ALEX(Component.literal("Alex"), "player/wide/alex", false, 1000, true),
    SLIM_ARI(Component.literal("Ari"), "player/slim/ari", true, 600, true),
    WIDE_ARI(Component.literal("Ari"), "player/wide/ari", false, 600, true),
    SLIM_EFE(Component.literal("Efe"), "player/slim/efe", true, 600, true),
    WIDE_EFE(Component.literal("Efe"), "player/wide/efe", false, 600, true),
    SLIM_KAI(Component.literal("Kai"), "player/slim/kai", true, 600, true),
    WIDE_KAI(Component.literal("Kai"), "player/wide/kai", false, 600, true),
    SLIM_MAKENA(Component.literal("Makena"), "player/slim/makena", true, 600, true),
    WIDE_MAKENA(Component.literal("Makena"), "player/wide/makena", false, 600, true),
    SLIM_NOOR(Component.literal("Noor"), "player/slim/noor", true, 600, true),
    WIDE_NOOR(Component.literal("Noor"), "player/wide/noor", false, 600, true),
    SLIM_SUNNY(Component.literal("Sunny"), "player/slim/sunny", true, 600, true),
    WIDE_SUNNY(Component.literal("Sunny"), "player/wide/sunny", false, 600, true),
    SLIM_ZURI(Component.literal("Zuri"), "player/slim/zuri", true, 600, true),
    WIDE_ZURI(Component.literal("Zuri"), "player/wide/zuri", false, 600, true),
    HUNG_TEEN("_PangTeen_", "6b78b0dc-ed31-4da6-86fb-8d36092b1023", 10),
    NEW_COMETS("_NewComets_", "f8010ab1-f2ce-45d9-ab78-72b513dc5d52", 100),
    GRASS_CARP("GrassCarp", "9017a74b-ccf4-4896-a3c4-f5ab241ecda1", 25),
    YU("Yu______","314c6ac6-eb95-4994-96eb-85ee2b676a83", 100),
    ;

    public static final Codec<CultivatorType> CODEC = StringRepresentable.fromValues(CultivatorType::values);
    public static final StreamCodec<RegistryFriendlyByteBuf, CultivatorType> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
    private final Component displayName;
    private String profileName;
    private UUID profileUUID;
    /**
     * 访问皮肤的关键，需要异步。
     */
    private volatile GameProfile gameProfile;
    private ResourceLocation skinLocation;
    private boolean isSlim;
    private final boolean common;
    private final int weight;

    /**
     * 离线类型（没有正版号）。
     * @param displayName 玩家头上显示。
     * @param skinLocation 本地贴图。
     */
    CultivatorType(Component displayName, String skinLocation, boolean isSlim, int weight, boolean common) {
        this(displayName, null, null, Util.mc().entityTexture(skinLocation), isSlim, weight, common);
    }

    /**
     * 在线类型。
     * @param displayName 玩家头上显示。
     * @param uuid 玩家UUID。
     */
    CultivatorType(String displayName, String uuid, int weight) {
        this(Component.literal(displayName), displayName, uuid, weight);
    }

    CultivatorType(Component displayName, String profileName, String uuid, int weight) {
        this(displayName, profileName, UUID.fromString(uuid), null, true, weight, false);
    }

    CultivatorType(Component displayName, String profileName, UUID profileUUID, ResourceLocation skinLocation, boolean isSlim, int weight, boolean common) {
        this.displayName = displayName;
        this.profileName = profileName;
        this.profileUUID = profileUUID;
        this.skinLocation = skinLocation;
        this.isSlim = isSlim;
        this.weight = weight;
        this.common = common;
    }

    public Component getDisplayName() {
        return displayName;
    }

    /**
     * 判断有没有正版账户。
     * @return Empty if it's an offline id.
     */
    public Optional<UUID> getProfileUUID() {
        return Optional.ofNullable(profileUUID);
    }

    public Optional<String> getProfileName() {
        return Optional.ofNullable(profileName);
    }

    public Optional<ResourceLocation> getSkinLocation(){
        return Optional.ofNullable(this.skinLocation);
    }

    public void setSkinLocation(ResourceLocation skinLocation) {
        this.skinLocation = skinLocation;
    }

    public boolean isSlim() {
        return isSlim;
    }

    public void setSlim(boolean slim) {
        isSlim = slim;
    }

    /**
     * 随机生成的权重
     * @return Random spawn weight.
     */
    public Weight getWeight() {
        return Weight.of(weight);
    }

    /**
     * 不是定制类型。
     * @return Not specific type.
     */
    public boolean isCommon() {
        return common;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public Optional<GameProfile> getGameProfile() {
        return Optional.ofNullable(gameProfile);
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
