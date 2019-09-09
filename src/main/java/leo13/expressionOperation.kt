package leo13

sealed class ExpressionOperation {
	override fun toString() = sentenceLine.toString()
}

object LeftExpressionOperation : ExpressionOperation() {
}

object RightExpressionOperation : ExpressionOperation() {
}

data class PlusExpressionOperation(val plus: ExpressionPlus) : ExpressionOperation() {
	override fun toString() = super.toString()
}

data class SwitchExpressionOperation(val switch: ExpressionSwitch) : ExpressionOperation() {
	override fun toString() = super.toString()
}

data class ApplyExpressionOperation(val apply: ExpressionApply) : ExpressionOperation() {
	override fun toString() = super.toString()
}

data class BindExpressionOperation(val bind: ExpressionBind) : ExpressionOperation() {
	override fun toString() = super.toString()
}

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

val ExpressionOperation.sentenceLine: SentenceLine
	get() =
		operationWord lineTo bodySentence

val ExpressionOperation.bodySentence: Sentence
	get() =
		when (this) {
			is LeftExpressionOperation -> sentence(leftWord)
			is RightExpressionOperation -> sentence(rightWord)
			is PlusExpressionOperation -> sentence(plus.sentenceLine)
			is SwitchExpressionOperation -> sentence(switch.sentenceLine)
			is ApplyExpressionOperation -> sentence(apply.sentenceLine)
			is BindExpressionOperation -> sentence(bind.sentenceLine)
		}