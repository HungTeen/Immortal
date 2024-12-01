package hungteen.imm.common.cultivation.realm;

import hungteen.imm.api.cultivation.RealmType;

/**
 * 大境界，每个大境界会包含若干个小境界。
 * @author PangTeen
 * @program Immortal
 * @create 2024/12/1 23:04
 **/
public interface MultiRealm {

    RealmType[] getRealms();
}
