package hungteen.imm.util.enums;

/**
 * @program Immortal
 * @author PangTeen
 * @create 2023/10/30 18:31
 **/
public enum SpellSortCategories {

    BASIC(100),

    COMMON(80),

    METAL(70),

    WOOD(60),

    WATER(50),

    FIRE(40),

    EARTH(30),

    SPIRIT(20),

    MULTI_ELEMENT(10),

    MISC(0),

    ;

    private final int priority;

    SpellSortCategories(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
