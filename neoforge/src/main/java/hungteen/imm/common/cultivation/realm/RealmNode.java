package hungteen.imm.common.cultivation.realm;

import hungteen.htlib.util.helper.JavaHelper;
import hungteen.imm.api.IMMAPI;
import hungteen.imm.api.cultivation.CultivationType;
import hungteen.imm.api.cultivation.RealmType;
import hungteen.imm.common.cultivation.CultivationTypes;
import hungteen.imm.common.cultivation.RealmTypes;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 修行的境界节点，整个修行体系可以看做一棵树。
 * @author PangTeen.
 */
public class RealmNode {

    private static final RealmNode ROOT = new RealmNode(RealmTypes.MORTALITY);
    private final RealmType realm;
    private final Map<CultivationType, RealmNode> nextRealms = new HashMap<>();

    /**
     * 构建整个修行体系的树。
     */
    public static void updateRealmTree() {
        RealmNode lastQiRefiningNode = addMultiStage(ROOT, RealmTypes.QI_REFINING.getRealms(), CultivationTypes.QI);
        RealmNode lastFoundationNode = addMultiStage(lastQiRefiningNode, RealmTypes.FOUNDATION.getRealms(), CultivationTypes.QI);
        RealmNode lastCoreShapingNode = addMultiStage(lastFoundationNode, RealmTypes.CORE_SHAPING.getRealms(), CultivationTypes.QI);
    }

    /**
     * Add a hierarchy list.
     * @return the last realm node.
     */
    public static RealmNode addMultiStage(RealmNode prevRealm, RealmType[] realmTypes, CultivationType type){
        for (RealmType realmType : realmTypes) {
            RealmNode curRealm = getOrCreate(realmType);
            prevRealm.add(curRealm, type);
            prevRealm = curRealm;
        }
        return prevRealm;
    }

    /**
     * Find the realm node with the same realm type.
     */
    public static Optional<RealmNode> getNodeOpt(RealmType type){
        return Optional.ofNullable(seekRealm(ROOT, type));
    }

    @Nullable
    public static RealmNode seekRealm(RealmNode root, RealmType type){
        if(root == null) {
            return null;
        } else if(root.realm == type) {
            return root;
        }
        for (RealmNode nextRealm : root.nextRealms.values()) {
            final RealmNode node = seekRealm(nextRealm, type);
            if(node != null) {
                return node;
            }
        }
        return null;
    }

    public static RealmNode getOrCreate(RealmType realm) {
        return getNodeOpt(realm).orElseGet(() -> new RealmNode(realm));
    }

    RealmNode(@NotNull RealmType realm) {
        this.realm = realm;
    }

    public List<RealmNode> nextRealms() {
        return nextRealms.values().stream().toList();
    }

    public Optional<RealmNode> next(CultivationType type) {
        return JavaHelper.getOpt(nextRealms, type);
    }

    public void add(RealmType realm, CultivationType type) {
        this.add(new RealmNode(realm), type);
    }

    public void add(RealmNode node, CultivationType type) {
        if(nextRealms.containsKey(type)){
            IMMAPI.logger().warn("Replaced old realm node {} with new one {} for type {} in realm {}",
                    nextRealms.get(type).realm, node.realm, type, this.realm);
        }
        nextRealms.put(type, node);
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
