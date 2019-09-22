package leo13.atom

import leo13.ObjectScripting
import leo13.atomName
import leo13.emptyName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

sealed class Atom : ObjectScripting() {
	override val scriptingLine get() = atomName lineTo script(scriptingAtomLine)
	abstract val scriptingAtomLine: ScriptLine
}

object EmptyAtom : Atom() {
	override val scriptingAtomLine get() = emptyName lineTo script()
}

data class PairAtom(val arrow: AtomArrow) : Atom() {
	override fun toString() = super.toString()
	override val scriptingAtomLine get() = arrow.scriptingLine
}

data class FunctionAtom(val function: Function) : Atom() {
	override fun toString() = super.toString()
	override val scriptingAtomLine get() = function.scriptingLine
}

val emptyAtom: Atom = EmptyAtom
fun atom(arrow: AtomArrow): Atom = PairAtom(arrow)
fun atom(function: Function): Atom = FunctionAtom(function)

tailrec fun Atom.evaluate(expression: Expression, given: Atom): Atom {
	val opAtom = evaluate(expression.op, given)
	return if (expression.lhs != null) opAtom.evaluate(expression.lhs, given)
	else opAtom
}

fun Atom.evaluate(op: Op, given: Atom): Atom =
	when (op) {
		is AtomOp -> op.atom
	}