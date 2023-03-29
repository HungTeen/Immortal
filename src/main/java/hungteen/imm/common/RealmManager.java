package hungteen.imm.common;

import hungteen.htlib.HTLib;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.registry.ICultivationType;
import hungteen.imm.api.registry.IRealmType;
import hungteen.imm.common.impl.registry.RealmTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2022-10-26 18:36
 **/
public class RealmManager {

    public static final RealmNode ROOT = new RealmNode(RealmTypes.MORTALITY);

    /**
     * {@link hungteen.imm.ImmortalMod#setUp(FMLCommonSetupEvent)}
     */
    public static void updateRealmTree() {
        addRealmLine(ROOT, Arrays.asList(
                RealmTypes.SPIRITUAL_LEVEL_1,
                RealmTypes.SPIRITUAL_LEVEL_2,
                RealmTypes.SPIRITUAL_LEVEL_3
        ));
    }

    /**
     * Find the realm node with the same realm type.
     */
    public static RealmNode findRealmNode(IRealmType realmType){
        return Objects.requireNonNullElse(seekRealm(ROOT, realmType), ROOT);
    }

    public static void getRealm(CompoundTag tag, String name, Consumer<IRealmType> consumer){
        if(tag.contains(name)){
            IMMAPI.get().realmRegistry().flatMap(l -> l.byNameCodec().parse(NbtOps.INSTANCE, tag.get(name))
                    .result()).ifPresent(consumer);
        }
    }

    /**
     * Add a hierarchy list.
     */
    public static void addRealmLine(RealmNode root, List<IRealmType> realmTypes){
        RealmNode tmp = root;
        for(int i = 0; i < realmTypes.size(); ++ i){
            RealmNode now = new RealmNode(realmTypes.get(i), tmp);
            tmp.add(now);
            tmp = now;
        }
    }

    @Nullable
    private static RealmNode seekRealm(RealmNode root, IRealmType type){
        if(root == null) return null;
        if(root.realm == type) return root;
        for (RealmNode nextRealm : root.nextRealms) {
            final RealmNode node = seekRealm(nextRealm, type);
            if(node != null) return node;
        }
        return null;
    }

    /**
     * A tree node.
     */
    public static class RealmNode {

        private final IRealmType realm;
        private RealmNode prevRealm;

        private final HashSet<RealmNode> nextRealms = new HashSet<>();

        private RealmNode(@NotNull IRealmType realm){
            this(realm, null);
        }

        private RealmNode(@NotNull IRealmType realm, @Nullable RealmNode prevRealm) {
            this.realm = realm;
            this.prevRealm = prevRealm;
        }

        @Nullable
        public RealmNode next(ICultivationType type){
            return nextRealms.stream().filter(l -> l.realm.getCultivationType() == type).findAny().orElse(null);
        }

        public void add(RealmNode node){
            if(nextRealms.stream().anyMatch(l -> l.realm == node.realm)){
                HTLib.getLogger().warn("Duplicate realm node : " + node.realm.getRegistryName());
            } else {
                nextRealms.add(node);
            }
        }

        public boolean hasPreviousNode(){
            return this.prevRealm != null;
        }

        public IRealmType getRealm(){
            return this.realm;
        }

        public IRealmType getPreviousRealm(){
            return this.prevRealm.realm;
        }

    }

}
