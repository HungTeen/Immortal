package hungteen.imm.api.spell;

import hungteen.imm.api.HTHitResult;
import hungteen.imm.common.cultivation.SpellManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

/**
 * 施展法术的上下文信息。
 *
 * @author PangTeen
 * @program Immortal
 * @create 2025/6/17 12:25
 **/
public class SpellCastContext {

    private final LivingEntity owner;
    private final int spellLevel;
    private float spellScale;
    private Entity target;
    private BlockState targetState;
    private BlockPos targetPos;
    private Direction direction;
    private ItemStack usingItem;
    private TriggerCondition triggerCondition;

    public SpellCastContext(LivingEntity owner, int spellLevel, TriggerCondition condition, ItemStack usingItem) {
        this(owner, spellLevel, 1F, null, null, null, null, usingItem, condition);
    }

    public SpellCastContext(LivingEntity owner, int spellLevel) {
        this.owner = owner;
        this.spellLevel = spellLevel;
    }

    public SpellCastContext(LivingEntity owner, Spell spell) {
        this(owner, SpellManager.createHitResult(owner, spell), spell.level());
    }

    public SpellCastContext(LivingEntity owner, HTHitResult result) {
        this(owner, result, 1);
    }

    public SpellCastContext(LivingEntity owner, HTHitResult result, int spellLevel) {
        this(owner, result, spellLevel, 1F);
    }

    public SpellCastContext(LivingEntity owner, HTHitResult result, int spellLevel, float spellScale) {
        this(owner, spellLevel, spellScale, result.getEntity(), result.getBlockState(owner.level()), result.getBlockPos(), result.getDirection(), ItemStack.EMPTY, null);
    }

    /**
     * @param owner            施法者。
     * @param spellLevel       法术等级。
     * @param spellScale       射线检测结果。
     * @param target           目标实体。
     * @param state            目标方块状态。
     * @param pos              目标方块位置。
     * @param direction        目标方块的朝向。
     * @param usingItem        使用的物品。
     * @param triggerCondition 触发条件。
     */
    public SpellCastContext(LivingEntity owner, int spellLevel, float spellScale, Entity target, BlockState state, BlockPos pos, Direction direction, ItemStack usingItem, TriggerCondition triggerCondition) {
        this.owner = owner;
        this.spellLevel = spellLevel;
        this.spellScale = spellScale;
        this.target = target;
        this.targetState = state;
        this.targetPos = pos;
        this.direction = direction;
        this.usingItem = usingItem;
        this.triggerCondition = triggerCondition;
    }

    public LivingEntity owner() {
        return owner;
    }

    public Level level() {
        return owner().level();
    }

    public int spellLevel() {
        return spellLevel;
    }

    public void setSpellScale(float spellScale) {
        this.spellScale = spellScale;
    }

    public float scale() {
        return spellScale;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public Optional<Entity> targetOpt() {
        return Optional.ofNullable(target);
    }

    public Entity target() {
        return targetOpt().orElseThrow();
    }

    public Optional<BlockState> targetStateOpt() {
        return Optional.ofNullable(targetState);
    }

    public BlockState targetState() {
        return targetStateOpt().orElseThrow();
    }

    public Optional<BlockPos> targetPosOpt() {
        return Optional.ofNullable(targetPos);
    }

    public BlockPos targetPos() {
        return targetPosOpt().orElseThrow();
    }

    public Optional<Direction> directionOpt() {
        return Optional.ofNullable(direction);
    }

    public Direction direction() {
        return directionOpt().orElseThrow();
    }

    public ItemStack usingItem() {
        return usingItem;
    }

    /**
     * @return 不是玩家法术轮盘触发，而是物品触发。
     */
    public boolean itemTrigger(){
        return triggerCondition != null;
    }

}
