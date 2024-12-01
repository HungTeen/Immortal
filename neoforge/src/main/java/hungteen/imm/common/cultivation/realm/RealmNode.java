package hungteen.imm.common.cultivation.realm;

import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.cultivation.CultivationType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.RealmTypes;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 修行的境界节点，整个修行体系可以看做一棵树。
 * @author PangTeen.
 */
public class RealmNode {

    private static final RealmNode ROOT = new RealmNode(RealmTypes.MORTALITY);
    private final RealmType realm;
    private final HashSet<RealmNode> nextRealms = new HashSet<>();

    /**
     * 构建整个修行体系的树。
     */
    public static void updateRealmTree() {
        ROOT.add(RealmTypes.QI_REFINING.first());
        List<RealmNode> qiRefiningNodes = addMultiStage(RealmTypes.QI_REFINING.getRealms());
        qiRefiningNodes.forEach(node -> node.add(RealmTypes.FOUNDATION.pre()));
        List<RealmNode> foundationNodes = addMultiStage(RealmTypes.FOUNDATION.getRealms());
        foundationNodes.forEach(node -> node.add(RealmTypes.CORE_SHAPING.pre()));
        List<RealmNode> coreShapingNodes = addMultiStage(RealmTypes.CORE_SHAPING.getRealms());
    }

    /**
     * Add a hierarchy list.
     * @return realm nodes that can break through to next realm.
     */
    public static List<RealmNode> addMultiStage(RealmType[] realmTypes){
        List<RealmNode> nodes = new ArrayList<>();
        RealmNode prev = null;
        for (RealmType realmType : realmTypes) {
            RealmNode now = new RealmNode(realmType);
            if (realmType.canLevelUp()) {
                nodes.add(now);
            }
            if (prev != null) {
                prev.add(now);
            }
            prev = now;
        }
        return nodes;
    }

    @NotNull
    public static RealmNode seekRealm(RealmType type){
        RealmNode node = seekRealm(ROOT, type);
        return node == null ? ROOT : node;
    }

    @Nullable
    public static RealmNode seekRealm(RealmNode root, RealmType type){
        if(root == null) {
            return null;
        } else if(root.realm == type) {
            return root;
        }
        for (RealmNode nextRealm : root.nextRealms) {
            final RealmNode node = seekRealm(nextRealm, type);
            if(node != null) {
                return node;
            }
        }
        return null;
    }

    RealmNode(@NotNull RealmType realm) {
        this.realm = realm;
    }

    @Nullable
    public RealmNode next() {
        return next(realm.getCultivationType());
    }

    @Nullable
    public RealmNode next(CultivationType type) {
        return nextRealms.stream().filter(l -> l.realm.getCultivationType() == type).findAny().orElse(null);
    }

    public void add(RealmType type) {
        this.add(new RealmNode(type));
    }

    public void add(RealmNode node) {
        if (nextRealms.stream().anyMatch(l -> l.realm == node.realm)) {
            IMMAPI.logger().warn("Duplicate realm node : {}", node.realm.getRegistryName());
        } else {
            nextRealms.add(node);
        }
    }

    public RealmType getRealm() {
        return this.realm;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RealmNode && ((RealmNode) obj).realm.equals(realm);
    }

    @Override
    public int hashCode() {
        return realm.hashCode();
    }
}
