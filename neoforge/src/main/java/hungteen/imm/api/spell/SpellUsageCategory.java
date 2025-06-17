package hungteen.imm.api.spell;

/**
 * 给生物 AI 用的。
 * @program Immortal
 * @author HungTeen
 * @create 2023-09-03 19:26
 **/
public enum SpellUsageCategory {

    /**
     * 只有玩家可用。
     */
    PLAYER_ONLY(0, false),

    /**
     * 被动法术。
     */
    TRIGGERED_PASSIVE(0, false),

    /**
     * 攻击敌人。
     */
    ATTACK_TARGET(SpellType.HIGH, true),

    /**
     * 给敌人加 debuff。
     */
    DEBUFF_TARGET(SpellType.MID_HIGH, true),

    /**
     * 给自己加 buff。
     */
    BUFF_SELF(SpellType.MID_HIGH, false),

    /**
     * 独立考虑的法术。
     */
    CUSTOM_BUFF(0, false),

    /**
     * 独立考虑的法术。
     */
    CUSTOM_TARGET(0, true)
    ;

    private final int defaultPriority;
    private final boolean requireEntityTarget;

    SpellUsageCategory(int defaultPriority, boolean requireEntityTarget){
        this.defaultPriority = defaultPriority;
        this.requireEntityTarget = requireEntityTarget;
    }

    public boolean requireEntityTarget() {
        return requireEntityTarget;
    }

    public int getDefaultPriority() {
        return defaultPriority;
    }
}
