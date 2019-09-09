package leo13

sealed class ExpressionOperation

object LeftExpressionOperation : ExpressionOperation()
object RightExpressionOperation : ExpressionOperation()
data class PlusExpressionOperation(val plus: ExpressionPlus) : ExpressionOperation()
data class SwitchExpressionOperation(val switch: ExpressionSwitch) : ExpressionOperation()
data class ApplyExpressionOperation(val apply: ExpressionApply) : ExpressionOperation()
data class BindExpressionOperation(val bind: ExpressionBind) : ExpressionOperation()

val leftExpressionOperation: ExpressionOperation = LeftExpressionOperation
val rightExpressionOperation: ExpressionOperation = RightExpressionOperation
fun operation(plus: ExpressionPlus): ExpressionOperation = PlusExpressionOperation(plus)
fun operation(switch: ExpressionSwitch): ExpressionOperation = SwitchExpressionOperation(switch)
fun operation(apply: ExpressionApply): ExpressionOperation = ApplyExpressionOperation(apply)
fun operation(bind: ExpressionBind): ExpressionOperation = BindExpressionOperation(bind)

fun ExpressionOperation.atom(bindings: AtomBindings, atom: Atom): Atom =
	when (this) {
		is LeftExpressionOperation -> atom.link.leftAtom
		is RightExpressionOperation -> atom.link.rightAtom
		is PlusExpressionOperation -> plus.atom(bindings, atom)
		is SwitchExpressionOperation -> switch.atom(bindings, atom)
		is ApplyExpressionOperation -> apply.atom(bindings, atom)
		is BindExpressionOperation -> bind.atom(bindings, atom)
	}