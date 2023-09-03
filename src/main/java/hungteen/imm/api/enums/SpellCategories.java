package hungteen.imm.api.enums;

/**
 * @program: Immortal
 * @author: HungTeen
 * @create: 2023-09-03 19:26
 **/
public enum SpellCategories {

    /**
     * 只有玩家可用。
     */
    PLAYER_ONLY,

    /**
     * 攻击敌人。
     */
    ATTACK_TARGET,

    /**
     * 给敌人加debuff。
     */
    DEBUFF_TARGET,

    /**
     * 给自己加buff。
     */
    BUFF_SELF,

    /**
     * 独立考虑的法术。
     */
    CUSTOM,
    ;
}
